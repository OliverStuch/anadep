package org.anadep

import org.anadep.analysis.asm.ASMJavaAnalyser
import org.anadep.model._
import scala.collection.mutable._

import org.objectweb.asm.Opcodes
import org.apache.log4j.Logger

trait AbstractTest_ASMJavaAnalyser {

    def createGraph: DependencyGraph[ Vertex ]

    def test_innerClass() {
        val testClassName = "org/anadep/analyse/OuterClass"
        val asmAnalyser = new ASMJavaAnalyser()
        val classAsInputstream = Util.getResourceAsStream( "/" + testClassName + ".class" )
        assert( classAsInputstream != null )
        var element2jarComponent: Map[ Vertex, JarComponent ] = new HashMap[ Vertex, JarComponent ]()
        val dependencyGraph: DependencyGraph[ Vertex ] = createGraph
        asmAnalyser.addAnalysis( classAsInputstream, dependencyGraph, element2jarComponent, new JarComponent( "dummy" ) )

        assert( dependencyGraph.numberVertices == 6 ) // 4 ClassVertices + 2 MethodVertices, insbesondere kein void-Vertex durch eine Methode ohne Rückgabewert
        assert( dependencyGraph.numberEdges == 6 )

        assert_testClassPresent_visited_extendsObject( dependencyGraph, ClassVertex( testClassName ) ) // ClassVertex #1+2, Edge #1
        val defaultConstructor = MethodVertex( testClassName, "<init>", "()V" )
        assert( dependencyGraph isConnected ( defaultConstructor, ClassVertex( testClassName ) ) )
        assert( dependencyGraph.findEdge( defaultConstructor, ClassVertex( testClassName ) ).isInstanceOf[ LocalVariableDependency[ Vertex ] ] )

        val defaultConstructorOfJavaLangObject = MethodVertex( "java/lang/Object", "<init>", "()V" )
        assert( dependencyGraph isConnected ( defaultConstructor, defaultConstructorOfJavaLangObject ) )
        assert( dependencyGraph.findEdge( defaultConstructor, defaultConstructorOfJavaLangObject ).isInstanceOf[ MethodCallDependency[ Vertex ] ] )
    }

    def assertGraphVertices( dependencyGraph: DependencyGraph[ Vertex ], mustContainClassVertices: List[ ClassVertex ], mustContainMethodVertices: List[ MethodVertex ] ) {
        val mustContainVertices = mustContainClassVertices ::: mustContainMethodVertices
        assert( dependencyGraph.numberVertices == mustContainVertices.size )
        for ( vertex <- mustContainVertices ) {
            assert( dependencyGraph containsVertex vertex )
        }
    }

    def assertGraphEdges( dependencyGraph: DependencyGraph[ Vertex ], mustContainEdges: List[ Dependency[ Vertex ] ] ) {
        assert( dependencyGraph.numberEdges == mustContainEdges.size )
        for ( edge <- mustContainEdges ) {
            assert( dependencyGraph containsEdge edge )
        }
    }

    def test_classAnalysis_DefaultConstructorThrowsExceptionAndMore() {
        val testClassName = "org/anadep/analyse/DefaultConstructorAndMore"
        val asmAnalyser = new ASMJavaAnalyser()
        val classAsInputstream = Util.getResourceAsStream( "/" + testClassName + ".class" )
        assert( classAsInputstream != null )
        val dependencyGraph: DependencyGraph[ Vertex ] = createGraph
        var element2jarComponent: Map[ Vertex, JarComponent ] = new HashMap[ Vertex, JarComponent ]()
        asmAnalyser.addAnalysis( classAsInputstream, dependencyGraph, element2jarComponent, new JarComponent( "dummy" ) )

        val mustContainClassVertices = List(
            ClassVertex( testClassName ),
            ClassVertex( "java/lang/Exception" ),
            ClassVertex( "java/lang/Object" ) )
        val mustContainMethodVertices = List(
            MethodVertex( "java/lang/Object", "<init>", "()V" ),
            MethodVertex( testClassName, "throwsException", "()V" ),
            MethodVertex( "java/lang/Exception", "<init>", "()V" ),
            MethodVertex( testClassName, "<init>", "()V" ) )
        assertGraphVertices( dependencyGraph, mustContainClassVertices, mustContainMethodVertices )
        assert_testClassPresent_visited_extendsObject( dependencyGraph, ClassVertex( testClassName ) ) // ClassVertex #1+2, Edge #1
        val mustBeConnected: List[ Dependency[ Vertex ] ] = List(
            SuperclassDependency( ClassVertex( testClassName ) -> ClassVertex( "java/lang/Object" ) ),
            MethodMemberDependency( ClassVertex( testClassName ) -> MethodVertex( testClassName, "<init>", "()V" ), Opcodes.ACC_PUBLIC ),
            MethodCallDependency( MethodVertex( testClassName, "<init>", "()V" ) -> MethodVertex( "java/lang/Object", "<init>", "()V" ) ),
            LocalVariableDependency( MethodVertex( testClassName, "<init>", "()V" ) -> ClassVertex( testClassName ) ),
            MethodMemberDependency( ClassVertex( testClassName ) -> MethodVertex( testClassName, "throwsException", "()V" ), Opcodes.ACC_PRIVATE ),
            ThrowsExceptionDependency( MethodVertex( testClassName, "throwsException", "()V" ), ClassVertex( "java/lang/Exception" ) ),
            MethodCallDependency( MethodVertex( testClassName, "throwsException", "()V" ) -> MethodVertex( "java/lang/Exception", "<init>", "()V" ) ),
            LocalVariableDependency( MethodVertex( testClassName, "throwsException", "()V" ) -> ClassVertex( testClassName ) ) )
        assertGraphEdges( dependencyGraph, mustBeConnected )
    }

    def test_classAnalysis_NoDefaultConstructorAndMore() {
        val testClassName = "org/anadep/analyse/NoDefaultConstructorAndMore"
        val testClass = ClassVertex( testClassName )
        val asmAnalyser = new ASMJavaAnalyser()
        val classAsInputstream = Util.getResourceAsStream( "/" + testClassName + ".class" )
        assert( classAsInputstream != null )
        val dependencyGraph: DependencyGraph[ Vertex ] = createGraph
        var element2jarComponent: Map[ Vertex, JarComponent ] = new HashMap[ Vertex, JarComponent ]()
        asmAnalyser.addAnalysis( classAsInputstream, dependencyGraph, element2jarComponent, new JarComponent( "dummy" ) )

        assert( dependencyGraph.numberVertices == 5 ) // 3 ClassVertices + 2  MethodVertices, insbesondere kein void-Vertex durch eine Methode ohne Rückgabewert
        assert( dependencyGraph.numberEdges == 5 )

        assert_testClassPresent_visited_extendsObject( dependencyGraph, ClassVertex( testClassName ) ) // ClassVertex #1+2, Edge #1

        val nonDefaultConstructor = MethodVertex( testClassName, "<init>", "(Ljava/lang/String;)V" )
        assert( dependencyGraph isConnected ( testClass, nonDefaultConstructor ) ) // Edge #2
        assert( dependencyGraph.findEdge( testClass, nonDefaultConstructor ).isInstanceOf[ MethodMemberDependency[ Vertex ] ] )

        assert( dependencyGraph isConnected ( nonDefaultConstructor, ClassVertex( "java/lang/String" ) ) )
        assert( dependencyGraph.findEdge( nonDefaultConstructor, ClassVertex( "java/lang/String" ) ).isInstanceOf[ ArgumentTypeDependency[ Vertex ] ] )

        assert( dependencyGraph isConnected ( nonDefaultConstructor, ClassVertex( testClassName ) ) )
        assert( dependencyGraph.findEdge( nonDefaultConstructor, ClassVertex( testClassName ) ).isInstanceOf[ LocalVariableDependency[ Vertex ] ] )

        val defaultConstructorOfJavaLangObject = MethodVertex( "java/lang/Object", "<init>", "()V" )
        assert( dependencyGraph isConnected ( nonDefaultConstructor, defaultConstructorOfJavaLangObject ) )
        assert( dependencyGraph.findEdge( nonDefaultConstructor, defaultConstructorOfJavaLangObject ).isInstanceOf[ MethodCallDependency[ Vertex ] ] )
    }

    def assert_testClassPresent_visited_extendsObject( dependencyGraph: DependencyGraph[ Vertex ], testClass: ClassVertex ) {
        // vertices
        assert( dependencyGraph containsVertex testClass ) // ClassVertex #1 testClass must be present
        assert( dependencyGraph containsVertex ClassVertex( "java/lang/Object" ) ) // ClassVertex #2 Object always present
        assert( dependencyGraph containsVertex MethodVertex( "java/lang/Object", "<init>", "()V" ) ) // Default constructor of java.lang.Object is always called
        assert( !( dependencyGraph containsVertex ClassVertex( "java/lang/SomeBullshitClassNotExistent" ) ) ) // do not find something not present
        // edges
        assert( dependencyGraph isConnected ( testClass, ClassVertex( "java/lang/Object" ) ) ) // Edge #1
        assert( dependencyGraph.findEdge( testClass, ClassVertex( "java/lang/Object" ) ).isInstanceOf[ SuperclassDependency[ Vertex ] ] )
        val foundTestClass = dependencyGraph findVertex testClass
        assert( foundTestClass.get visited, "testClass must be visited" )
        val javaLangException = dependencyGraph findVertex ClassVertex( "java/lang/Object" )
        assert( !( javaLangException.get visited ), "java.lang.Exception must not be visited" )

    }
    val logger = Logger.getLogger( getClass )
}
