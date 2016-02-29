package org.anadep.model

import org.anadep._

abstract class Vertex {
    val name : String
    var visited = false
    def markAsVisited () { visited = true }
}

case class ClassVertex (name:String) extends Vertex {
    var fields : List[FieldVertex] = Nil
    def add (field : FieldVertex) { field :: fields }
}
case class MethodVertex (owner:String, name:String, desc:String) extends Vertex 
case class FieldVertex (access:Int, owner:String, name:String, desc:String) extends Vertex 
case class AnnotationVertex (name:String) extends Vertex 

