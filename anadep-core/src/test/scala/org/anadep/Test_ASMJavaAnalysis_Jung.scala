package org.anadep

import org.anadep.model._
import org.anadep.model.jung._

import junit.framework._

class Test_ASMJavaAnalysis_Jung extends TestCase with AbstractTest_ASMJavaAnalyser {
    override def createGraph: DependencyGraph[ Vertex ] = DependencyGraphJungAdaptor[ Vertex ]
}
