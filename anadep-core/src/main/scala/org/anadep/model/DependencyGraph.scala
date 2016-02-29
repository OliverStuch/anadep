package org.anadep.model

trait DependencyGraph[V] {
    def numberVertices : Int
    def numberEdges : Int
    /**
     * Adds <code>vertex</code> to this graph.
     * Fails if <code>vertex</code> is null. 
     * Nothing is done if vertsx is already in the graph.
     * 
     * @param vertex    the vertex to add
     * @return <code>true</code> if the add is successful, and <code>false</code> otherwise
     * @throws IllegalArgumentException if <code>vertex</code> is <code>null</code>
       */
    def addVertex [W <: V] ( v:W )  : W
    def addVertices ( vs:V* )  = {
	       for { v <- vs 
		   v1 = addVertex (v)
	       } yield v
       }
    
    def connect (from:V,  to:V, dependency:Dependency[V]) : Boolean
    def containsVertex (v:V) : Boolean
    def findVertex (v:V) : Option[V]
    
    def isConnected (from:V,  to:V) : Boolean
    def findEdge (from:V,  to:V) : Dependency[V]
    def containsEdge (edge:Dependency[V]) : Boolean
    def filterVertices (filter : V => Boolean) : Iterable[V]
    def foreach (f : V => Unit) 
    def foreachEdge (f : Dependency[V] => Unit) 
        
        
//    
//    def connectedComponents:Set[DependencyGraph[V,E]]    
//    def stronglyConnectedComponents:Set[DependencyGraph[V,E]]
//    def cylces:Set[Cycle[V,E]]

    // Topologie...
    
//    def shortestPath(from:V, to:V):GraphPath[V,E] 
//    def allPaths(from:V, to:V):Set[GraphPath[V,E]] 
    
    // speichern und einlesen
    
    // gui filtern
    
}

trait GraphPath[V]

trait Cycle[V] extends GraphPath[V]