package org.anadep.model

import scala.util.matching.Regex
import scala.collection.mutable._

import java.io.File

trait Component extends DependencyFactory[Component]  {
    override def dependsOn (component:Component): Pair[Component,Component] = new Pair(this, component)
 }

class PatternComponent(patternNameRegex:Regex)  extends Component

object Component {
  def apply(patternNameRegex:String) = new PatternComponent(new Regex (patternNameRegex))
}

class JarComponent(name:String) extends Component { override def toString = name}

trait ComponentSpecification

class JarComponentSpecification extends ComponentSpecification
object JarComponentSpecification {
    def apply() = new JarComponentSpecification()
}

class PackageComponentSpecification extends ComponentSpecification

class SetComponentSpecification extends HashSet[Component] with ComponentSpecification {
}

object SetComponentSpecification{
    def apply (elements:Component*):SetComponentSpecification=  (new SetComponentSpecification() ++ elements).asInstanceOf[ SetComponentSpecification]
}

