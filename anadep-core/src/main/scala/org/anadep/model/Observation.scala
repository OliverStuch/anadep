package org.anadep.model

trait Observation[Elem]  {
   def trueIn (dependencyGraph:DependencyGraph[Elem]):Boolean
}

