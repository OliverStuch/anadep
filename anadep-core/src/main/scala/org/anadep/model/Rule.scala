package org.anadep.model

trait Rule[Elem] {
    def ok (dependencyGraph:DependencyGraph[Elem]):Boolean
}

object Forbid { def apply[Elem] ( observation:Observation[Elem] ) = new Forbid ( observation ) }
class Forbid[Elem]  ( observation:Observation[Elem] ) extends Rule[Elem] { 
    override def ok (dependencyGraph:DependencyGraph[Elem]) : Boolean =  if ( observation trueIn dependencyGraph ) false  else  true }


object Require { def apply[Elem] (observation:Observation[Elem] ) = new Require (observation ) }
class Require[Elem] ( observation:Observation[Elem] ) extends Rule[Elem] { 
    override def ok (dependencyGraph:DependencyGraph[Elem]) : Boolean = observation trueIn dependencyGraph }




class Rules[Elem] {
    var rules:Set[Rule[Elem]] = Set()
    
    def forbid ( observation:Observation[Elem] ) { rules +=  Forbid ( observation ) }
    def require ( observation:Observation[Elem] ) { rules += Require ( observation ) }

    def findViolatedRules ( dependencyGraph:DependencyGraph[Elem] )  =  for ( rule <- rules if !( rule ok  dependencyGraph) ) yield rule
        
    def allRulesAreOk ( dependencyGraph:DependencyGraph[Elem] )  =  true
    
    private def recursiveFindFirstViolatedRule ( ruleIterator : Iterator[Rule[Elem]], dependencyGraph:DependencyGraph[Elem] )  : Rule[Elem] = {
      if (!ruleIterator.hasNext)  null else {
      val rule = ruleIterator.next()
      if (!rule.ok(dependencyGraph)) rule else recursiveFindFirstViolatedRule (ruleIterator,dependencyGraph)
      }
    }
    
    def findFirstViolatedRule ( dependencyGraph:DependencyGraph[Elem] )  : Rule[Elem] =  recursiveFindFirstViolatedRule(rules.elements, dependencyGraph)

}
