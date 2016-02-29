package org.anadep.analysis

import scala.collection.mutable._

import java.io.File

import org.anadep.model.jung._
import org.anadep.model.jgrapht._
import org.anadep.model._
import org.anadep.analysis.asm._
import org.anadep.util._

import org.jgrapht.traverse._

class Analyser (root : File){
  var element2jarComponent = new HashMap[Vertex, JarComponent]()
  var detailDependencyGraph = DependencyGraphJGraphTAdaptor[Vertex]

  addAnalysis(root)

  private def addAnalysis (file:File) {
    if ( file.isFile) {
      if (file.getName().endsWith(".jar")) {
        val zip = new Zip(file);
        val jarComponent = new JarComponent(file.getName)
        val asmAnalyser = new ASMJavaAnalyser()
        zip.foreach ( _ .endsWith(".class"), asmAnalyser.addAnalysis (_, detailDependencyGraph, element2jarComponent, jarComponent) )
      }
    } else {
      for (fileOfDir <- file.listFiles) {
        addAnalysis (fileOfDir)
      }
    }
  }
//        def createComponentDependencyGraph ( componentSpecification:ComponentSpecification ) : DependencyGraph[Component]  = {
  def createComponentDependencyGraph () : DependencyGraph[Component]  = {
    val graphIterator =new JarComponentBuilder (element2jarComponent, detailDependencyGraph)
    while (graphIterator.hasNext){
      graphIterator.next
    }
    graphIterator.componentGraph
  }

}

class JarComponentBuilder (element2jarComponent:Map[Vertex, JarComponent ], graph:DependencyGraphJGraphTAdaptor[Vertex]) extends BreadthFirstIterator[Vertex, Dependency[Vertex]] (graph.dependencyGraph) {
  var componentGraph =   DependencyGraphJungAdaptor[Component]

  override def  encounterVertex(vertex:Vertex, edge:Dependency[Vertex]) {
    if (edge != null){
      var fromJarComponent : JarComponent = null
      if (element2jarComponent.contains (edge.from)) {
        fromJarComponent = element2jarComponent(edge.from)
        componentGraph.addVertex(fromJarComponent)
      }

      var toJarComponent : JarComponent = null
      if (element2jarComponent.contains (edge.to)) {
        toJarComponent = element2jarComponent(edge.to)
        componentGraph.addVertex (toJarComponent)
      }
      if (fromJarComponent != null && toJarComponent != null && fromJarComponent != toJarComponent) {
        componentGraph connect (fromJarComponent, toJarComponent, SomeDependency(fromJarComponent, toJarComponent))}
    }
    super.encounterVertex(vertex, edge)
  }
}

object Analyser {
  def apply(root : File) = new Analyser(root)
}


