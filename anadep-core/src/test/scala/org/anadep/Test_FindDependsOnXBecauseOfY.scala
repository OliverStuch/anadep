package org.anadep

import java.io.File
import org.anadep.model._
import org.anadep.analysis._
import org.anadep.model.{ SomeDependency => AnyDependency }
import junit.framework._
import org.apache.log4j.Logger
import scala.collection.generic.MutableSetFactory
class Test_FindDependsOnXBecauseOfY extends TestCase {
    def testFindSubclassesOf() {
        val analyser = Analyser( new File( "../componentDImpl/" ) )

        var classCounter = 0
        var methodCounter = 0
        analyser.detailDependencyGraph foreach ( ( v: Vertex ) => if ( v.isInstanceOf[ ClassVertex ] ) { classCounter += 1 } )
        analyser.detailDependencyGraph foreach ( ( v: Vertex ) => if ( v.isInstanceOf[ MethodVertex ] ) { methodCounter += 1 } )

        logger.info( "classes found: " + classCounter )
        logger.info( "methods found: " + methodCounter )

        var foundDimpl = analyser.detailDependencyGraph.findVertex( ClassVertex( "testutil/dimpl/DImpl" ) )
        assert( foundDimpl != None )
        logger.info( "Dimpl resides in: " + ( analyser.element2jarComponent get foundDimpl.get ) toString )

        var foundDimpl2 = analyser.detailDependencyGraph.findVertex( ClassVertex( "testutil/dimpl/DImpl2" ) )
        assert( foundDimpl2 != None )
        logger.info( "Dimpl2 resides in: " + ( analyser.element2jarComponent get foundDimpl2.get ) toString )

        val dependentObjects = analyser.detailDependencyGraph.findEdgesTo( foundDimpl.get )
        
        logger.info( "# dependent objects:" + dependentObjects.size )
        val superclassDependencies = dependentObjects.filter( x => x.toString().contains( "SuperclassDependency" ) )
        var subclasses: scala.collection.mutable.Set[ Vertex ] = new scala.collection.mutable.HashSet()
        for ( x <- superclassDependencies ) subclasses += x.from
        logger.info( "# subclasses:" + subclasses.size )

    }

    def testFindSubclassesOfCrefoteam() {
        val analyser = Analyser( new File( "../../orm_analyse" ) )
        val componentDependencyGraph = analyser.createComponentDependencyGraph()

        var classCounter = 0
        var methodCounter = 0
        analyser.detailDependencyGraph foreach ( ( v: Vertex ) => if ( v.isInstanceOf[ ClassVertex ] ) { classCounter += 1 } )
        analyser.detailDependencyGraph foreach ( ( v: Vertex ) => if ( v.isInstanceOf[ MethodVertex ] ) { methodCounter += 1 } )

        logger.info( "classes found: " + classCounter )
        logger.info( "methods found: " + methodCounter )

        var foundBaseDO = analyser.detailDependencyGraph.findVertex( ClassVertex( "de/creditreform/tech/hibernatewkoconnector/dataentities/BaseDO" ) )
        assert( foundBaseDO != None )
        logger.info( "BaseDO resides in: " + ( analyser.element2jarComponent get foundBaseDO.get ) toString )

        var TextTokenGruppenDO = analyser.detailDependencyGraph.findVertex( ClassVertex( "de/creditreform/crefoteam/beteiligtenverwaltung/shared/TextTokenGruppenDO" ) )
        assert( TextTokenGruppenDO != None )
        logger.info( "TextTokenGruppenDO resides in: " + ( analyser.element2jarComponent get TextTokenGruppenDO.get ) toString )

        val dependentObjects = analyser.detailDependencyGraph.findEdgesTo( foundBaseDO.get )
        logger.info( "# dependent objects:" + dependentObjects.size )
        val superclassDependencies = dependentObjects.filter( x => x.toString().contains( "SuperclassDependency" ) )
        var subclasses: scala.collection.mutable.Set[ Vertex ] = new scala.collection.mutable.HashSet()
        for ( x <- superclassDependencies ) subclasses += x.from
        logger.info( "# subclasses:" + subclasses.size )

        logger.info( componentDependencyGraph.numberVertices )
        logger.info( componentDependencyGraph.numberEdges )
    }
    val logger = Logger.getLogger( getClass )
}