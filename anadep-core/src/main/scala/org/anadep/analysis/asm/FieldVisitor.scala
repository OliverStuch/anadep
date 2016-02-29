package org.anadep.analysis.asm
import org.anadep.model._

class FieldVisitor (dependencyGraph:DependencyGraph[Vertex])  extends org.objectweb.asm.FieldVisitor{
    override def visitAnnotation(desc:String, visible:Boolean) = new AnnotationVisitor (dependencyGraph, desc) 
    override def visitAttribute(attr:org.objectweb.asm.Attribute) { throw new UnsupportedOperationException () }
    override def visitEnd() {}
}
