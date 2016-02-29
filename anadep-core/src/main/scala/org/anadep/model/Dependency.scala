package org.anadep.model

abstract class Dependency[Elem](val from:Elem, val to:Elem) extends Observation[Elem] {
      override def trueIn (dependencyGraph:DependencyGraph[Elem]) = {
	if (! dependencyGraph.isConnected (from, to)) false else {
	    dependencyGraph.findEdge(from, to) == this
	}	
    }	
      override def toString = from + " -> " + getClass().getSimpleName() + " -> " + to
}

object SomeDependency {  def apply[Elem](elements:Pair[Elem,Elem]) = new SomeDependency(elements._1, elements._2) }
case class SomeDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) {
    override def trueIn (dependencyGraph:DependencyGraph[Elem]) =  dependencyGraph isConnected (from, to)
}

object FieldDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new FieldDependency(elements._1, elements._2) }
case class FieldDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object SuperclassDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new SuperclassDependency(elements._1, elements._2) }
case class SuperclassDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object InterfaceDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new InterfaceDependency(elements._1, elements._2) }
case class InterfaceDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object MethodMemberDependency { def apply[Elem](elements:Pair[Elem,Elem], access: Int) = new MethodMemberDependency(elements._1, elements._2, access) }
case class MethodMemberDependency[Elem](override val from:Elem, override val to: Elem, accesss: Int) extends Dependency(from, to) 

object MethodCallDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new MethodCallDependency(elements._1, elements._2) }
case class MethodCallDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object ReturnTypeDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new ReturnTypeDependency(elements._1, elements._2) }
case class ReturnTypeDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object ArgumentTypeDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new ArgumentTypeDependency(elements._1, elements._2) }
case class ArgumentTypeDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object ThrowsExceptionDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new ThrowsExceptionDependency(elements._1, elements._2) }
case class ThrowsExceptionDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object InnerClassDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new InnerClassDependency(elements._1, elements._2) }
case class InnerClassDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object AnnotationDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new AnnotationDependency(elements._1, elements._2) }
case class AnnotationDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 

object LocalVariableDependency { def apply[Elem](elements:Pair[Elem,Elem]) = new LocalVariableDependency(elements._1, elements._2) }
case class LocalVariableDependency[Elem](override val from:Elem, override val to: Elem) extends Dependency(from, to) 






