package org.anadep.analysis.asm

import org.anadep.model._
import org.objectweb.asm.Label
import org.anadep.NameConverter

// throws new UncheckedException

class MethodVisitor(dependencyGraph:DependencyGraph[Vertex], methodVertex:MethodVertex)  extends org.objectweb.asm.MethodVisitor{
    override def visitAnnotationDefault():AnnotationVisitor = null
    override def visitAnnotation(desc:String, visible:Boolean):AnnotationVisitor = {new AnnotationVisitor(dependencyGraph, desc)}
    override def visitParameterAnnotation(parameter:Int, desc:String, visible:Boolean)= {new AnnotationVisitor(dependencyGraph, desc)}
    override def visitAttribute(attr:org.objectweb.asm.Attribute) {}
    override def visitCode() {}
    override def visitFrame(`type`:Int, nLocal:Int, local:Array[Object], nStack:Int, stack:Array[Object]) {}
    override def visitInsn(opcode:Int) {}
    override def visitIntInsn(opcode:Int, operand:Int) {}
    override def visitVarInsn(opcode:Int, `var`:Int) {}
    override def visitTypeInsn(opcode:Int, `type`:String) {  /* ??? */ }
    override def visitFieldInsn(opcode:Int, owner:String, name:String, desc:String) {  /* ??? */ }
    override def visitMethodInsn(opcode:Int, owner:String, name:String, desc:String) {
      var calledMethodVertex =   dependencyGraph addVertex MethodVertex (owner, name, desc)
      dependencyGraph connect (methodVertex, calledMethodVertex, MethodCallDependency (methodVertex, calledMethodVertex))
    }
    override def visitJumpInsn(opcode:Int, label:org.objectweb.asm.Label) {}
    override def visitLabel(label:Label) {}
    override def visitLdcInsn(cst:Object) {}
    override def visitIincInsn(`var`:Int, increment:Int) {}
    override def visitTableSwitchInsn(min:Int, max:Int, dflt:Label, labels:Array[Label]) {}
    override def visitLookupSwitchInsn(dflt: Label, keys:Array[Int], labels:Array[Label]) {}
    override def visitMultiANewArrayInsn(desc:String, dims:Int) {}
    override def visitTryCatchBlock(start:Label, end:Label, handler:Label, `type`:String ) {}
    override def visitLocalVariable(name:String, desc:String, signature:String, start:Label, end:Label, index:Int) {
	val localVariableVertex = dependencyGraph addVertex (ClassVertex (NameConverter.pureAsmType (desc)))
	dependencyGraph connect (methodVertex, localVariableVertex, LocalVariableDependency (methodVertex, localVariableVertex))
    }      
    override def visitLineNumber(line:Int, start:Label) { /* do nothing */ }
    override def visitMaxs(maxStack:Int, maxLocals:Int) { /* do nothing */ }
    override def visitEnd() { 
      methodVertex.markAsVisited
    }
}
