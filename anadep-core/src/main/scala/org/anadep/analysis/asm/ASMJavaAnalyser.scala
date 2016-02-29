package org.anadep.analysis.asm

import org.anadep.model._

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import scala.collection.mutable._
class ASMJavaAnalyser {
    def addAnalysis (classFile:java.io.InputStream, dependencyGraph:DependencyGraph[Vertex], element2jarComponent:Map[Vertex, JarComponent], jarComponent:JarComponent) = {
	val classVisitor = new ClassVisitor(dependencyGraph, element2jarComponent, jarComponent)
	new ClassReader(classFile).accept( classVisitor, 0) // 0 means skip nothing TODO maybe performance issue
	classVisitor.classVertex
    }	
}
