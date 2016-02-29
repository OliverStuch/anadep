package org.anadep.analysis.asm
import org.anadep.model._
import org.anadep._
import org.apache.log4j.Logger

// top level visitor (for java)
class AnnotationVisitor (dependencyGraph:DependencyGraph[Vertex], desc:String) extends org.objectweb.asm.AnnotationVisitor {
    var annotationVertex = dependencyGraph addVertex AnnotationVertex(NameConverter.pureAsmType (desc))
    override def visit (name:String, value:Object) { logger info name + " " + value  /* mark as visited*/ }
    override def visitEnum (name:String,desc:String,value:String) { logger info name + " " + value + " " + desc}
    override def visitAnnotation (name:String,  desc:String) : AnnotationVisitor = new AnnotationVisitor (dependencyGraph, desc)
    override def visitArray (name:String) : AnnotationVisitor = new AnnotationVisitor (dependencyGraph, desc)
    override def visitEnd () {  annotationVertex.markAsVisited }
    val logger = Logger.getLogger (getClass)
}
