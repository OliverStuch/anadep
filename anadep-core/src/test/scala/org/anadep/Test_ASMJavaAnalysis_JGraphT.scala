package org.anadep

import org.anadep.model._
import org.anadep.model.jgrapht._

import  junit.framework._

class Test_ASMJavaAnalysis_JGraphT extends TestCase with AbstractTest_ASMJavaAnalyser {
      override def createGraph : DependencyGraph[Vertex] =  DependencyGraphJGraphTAdaptor[Vertex]
  }