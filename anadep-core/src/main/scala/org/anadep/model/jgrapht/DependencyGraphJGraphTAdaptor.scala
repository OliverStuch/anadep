package org.anadep.model.jgrapht

import org.anadep.model.Dependency
import org.anadep.model.DependencyGraph
import org.jgrapht._
import org.jgrapht.graph._

object DependencyGraphJGraphTAdaptor {
    def apply[ V ] = new DependencyGraphJGraphTAdaptor[ V ](
        new DefaultDirectedGraph[ V, Dependency[ V ] ](
            new EdgeFactory[ V, Dependency[ V ] ]() {
                def createEdge( s: V, t: V ): Dependency[ V ] = throw new UnsupportedOperationException()
            } ) )
}

class DependencyGraphJGraphTAdaptor[ V ]( val dependencyGraph: DirectedGraph[ V, Dependency[ V ] ] ) extends DependencyGraph[ V ] {

    override def numberVertices() = dependencyGraph.vertexSet.size

    override def numberEdges() = dependencyGraph.edgeSet.size

    override def addVertex[ W <: V ]( v: W ) = { dependencyGraph.addVertex( v ); v } // TODO exception

    override def containsVertex( v: V ) = dependencyGraph.containsVertex( v )

    override def findVertex( v: V ) = { // TODO slow and ugly
        val vertices = scala.collection.JavaConversions.asSet( dependencyGraph.vertexSet )
        vertices.find( vertex => vertex equals v )
    }

    override def filterVertices( filter: V => Boolean ) = { // TODO slow and ugly
        val vertices = scala.collection.JavaConversions.asSet( dependencyGraph.vertexSet )
        vertices filter filter
    }

    override def foreach( doSomething: V => Unit ) {
        val vertices = scala.collection.JavaConversions.asSet( dependencyGraph.vertexSet )
        vertices foreach doSomething
    }

    override def foreachEdge( doSomething: Dependency[ V ] => Unit ) {
        val edges = scala.collection.JavaConversions.asSet( dependencyGraph.edgeSet )
        edges foreach doSomething
    }

    override def connect( from: V, to: V, dependency: Dependency[ V ] ) = dependencyGraph.addEdge( from, to, dependency )

    override def isConnected( from: V, to: V ) = dependencyGraph.containsEdge( from, to )

    def findEdge( from: V, to: V ): Dependency[ V ] = dependencyGraph getEdge ( from, to )

    def findEdgesTo( to: V ): Iterable[ Dependency[ V ] ] = scala.collection.JavaConversions.asSet( dependencyGraph incomingEdgesOf ( to ) )

    override def containsEdge( edge: Dependency[ V ] ) = dependencyGraph containsEdge edge

    @deprecated
    def underlying = dependencyGraph
}
