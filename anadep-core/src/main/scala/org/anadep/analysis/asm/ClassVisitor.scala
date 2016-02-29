package org.anadep.analysis.asm

import org.anadep.NameConverter
import org.anadep.model._
import org.objectweb.asm._
import org.apache.log4j.Logger
import java.lang.UnsupportedOperationException

import scala.collection.mutable._
// top level visitor (for java)
class ClassVisitor (dependencyGraph:DependencyGraph[Vertex], var element2jarComponent:Map[Vertex, JarComponent], jarComponent:JarComponent) extends org.objectweb.asm.ClassVisitor {
     var classVertex:ClassVertex = null

    override def visit (version:Int,access:Int, name:String, signature:String, superName:String, interfaces:Array[String]) {
	classVertex = dependencyGraph addVertex ClassVertex (name)  
	element2jarComponent += (classVertex -> jarComponent) 
	val superClassVertex = dependencyGraph addVertex ClassVertex (superName)
	dependencyGraph connect (classVertex, superClassVertex, SuperclassDependency(classVertex, superClassVertex))
	for (interfaze <- interfaces) {
	  val interfaceVertex = dependencyGraph addVertex ClassVertex (interfaze) 
	  dependencyGraph connect (classVertex, interfaceVertex, InterfaceDependency(classVertex, interfaceVertex))
	}
    }
    
    override def visitSource(source:String,  debug:String) { /* do nothing */ }
    override def visitOuterClass(owner:String,  name:String,  desc:String)  {  /* do nothing, everything happened in visitInnerClass */ }
    override def visitAnnotation(desc:String, visible:Boolean): org.objectweb.asm.AnnotationVisitor = { 
	    val annotationVertex = dependencyGraph addVertex AnnotationVertex(NameConverter.pureAsmType (desc))
     	    dependencyGraph connect (classVertex, annotationVertex, AnnotationDependency (classVertex, annotationVertex))
	    new AnnotationVisitor(dependencyGraph,desc)
    }
    override def visitAttribute(attr:org.objectweb.asm.Attribute) { logger.info("Attribute in " + classVertex + " type: " + attr.`type`) }
    override def visitInnerClass(name:String, outerName:String, innerName:String, access:Int) {  // innerName == null, if 
      val innerClassVertex = dependencyGraph addVertex ClassVertex (name)
      val outerClassVertex = dependencyGraph addVertex ClassVertex (outerName)
      dependencyGraph connect ( innerClassVertex, outerClassVertex, InnerClassDependency(innerClassVertex, outerClassVertex))
    }
    
    override def visitField(access:Int, name:String, desc:String, signature:String, value:Object):org.objectweb.asm.FieldVisitor = {
	    val fieldVertex = FieldVertex (access, classVertex.name, name, desc) 
	    classVertex add fieldVertex
	    new FieldVisitor (dependencyGraph)
    }

    override def visitMethod(access:Int, name:String, desc:String, signature:String, exceptions:Array[String]):org.objectweb.asm.MethodVisitor = {
	    var methodVertex = MethodVertex (classVertex.name, name, desc)
  	    element2jarComponent += (methodVertex -> jarComponent) 
	    dependencyGraph addVertex methodVertex
	    dependencyGraph connect (classVertex, methodVertex, MethodMemberDependency (classVertex, methodVertex, access))
     
	    for ( argumentType <- Type.getArgumentTypes (desc)) {
	      val argumentTypeVertex = dependencyGraph addVertex ClassVertex (NameConverter.pureAsmType (argumentType.getDescriptor))
	      dependencyGraph connect (methodVertex, argumentTypeVertex, ArgumentTypeDependency (methodVertex, argumentTypeVertex))
	    }
     
	    val returnType = Type.getReturnType(desc)
	    if (!returnType.equals(Type.VOID_TYPE)) {
		val returnTypeVertex = dependencyGraph addVertex ClassVertex ( NameConverter.pureAsmType (returnType.getDescriptor));
		dependencyGraph connect (methodVertex, returnTypeVertex, ReturnTypeDependency (methodVertex, returnTypeVertex))
	    }

	    if (exceptions != null) {
		for ( exceptionType <- exceptions ) {
		    val throwsExceptionVertex = dependencyGraph addVertex ClassVertex (exceptionType)
		    dependencyGraph connect (methodVertex, throwsExceptionVertex, ThrowsExceptionDependency (methodVertex, throwsExceptionVertex))
		}	
	    }
	    new MethodVisitor (dependencyGraph, methodVertex)
      }
    override def visitEnd() {	
      classVertex.markAsVisited }
        val logger = Logger.getLogger (getClass)
}
