package org.anadep;

import java.io.File

import org.anadep.model._
import org.anadep.analysis._
import org.anadep.model.{SomeDependency => AnyDependency}

import  junit.framework._
import org.apache.log4j.Logger

class Test_Rule extends TestCase {
    def testComponentSpecificationByPattern (){
	val compA =  Component ( "org.anadep.compA.*" )
	val compB =  Component ( "org.anadep.compB.*" )
 	val compC =  Component ( "org.anadep.compC.*" )

     	var rules = new Rules[Component]
	rules forbid FieldDependency (compA -> compB ) 
 	rules forbid AnyDependency (compB -> compC ) 
   	rules require SomeDependency (compA -> compC )

	val analyser = Analyser (new File("/home/os/.m2/repository/org/anadep"))
// 	val analyser = Analyser (new File("src/test/testres/auslieferung"))
 
	var classCounter = 0
	var methodCounter = 0
	analyser.detailDependencyGraph foreach ( (v:Vertex) =>  if (v.isInstanceOf[ClassVertex]) { classCounter += 1} )
 	analyser.detailDependencyGraph foreach ( (v:Vertex) =>  if (v.isInstanceOf[MethodVertex]) { methodCounter += 1} )
// 	analyser.detailDependencyGraph foreach ( v =>  if (v.isInstanceOf[ClassVertex]) logger.info (v) )
	logger.info (classCounter)
 	logger.info (methodCounter)
//	val componentDependencyGraph = analyser.createComponentDependencyGraph(SetComponentSpecification(compA, compB, compC))
 	val componentDependencyGraph = analyser.createComponentDependencyGraph()
  	
  	logger.info (componentDependencyGraph.numberVertices)
     	logger.info (componentDependencyGraph.numberEdges)

 	logger.info (sun.misc.VM.maxDirectMemory)
	assert ( rules.allRulesAreOk(componentDependencyGraph) )
    }
    
//    def testComponentSpecificationByJar () {
//       	val analyser =  Analyser (  new File(".") ) 
//       	analyser.buildComponentDependencyGraph(JarComponentSpecification())
//        
//    }
                                                                           
	val logger = Logger.getLogger (getClass)                                         
}
