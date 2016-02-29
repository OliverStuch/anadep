package org.anadep.model.jung

import org.anadep._
import org.anadep.model._
import edu.uci.ics.jung.graph.DirectedGraph
import edu.uci.ics.jung.graph.DelegateForest
import edu.uci.ics.jung.graph._
import scala.collection.mutable._
import scala.collection.JavaConversions.JCollectionWrapper

object DependencyGraphJungAdaptor { def apply[ V ] = new DependencyGraphJungAdaptor[ V ]( new DirectedSparseGraph() ) }

class DependencyGraphJungAdaptor[ V ]( val dependencyGraph: DirectedGraph[ V, Dependency[ V ] ] ) extends DependencyGraph[ V ] {

    override def numberVertices() = dependencyGraph.getVertexCount

    override def numberEdges() = dependencyGraph.getEdgeCount

    override def addVertex[ W <: V ]( v: W ) = { dependencyGraph.addVertex( v ); v } // TODO exception 

    override def containsVertex( v: V ) = dependencyGraph.containsVertex( v )

    override def findVertex( v: V ) = { // TODO slow
        val vv: java.util.Map[ V, AnyRef ] = PrivilegedReflectionUtil.getValue( dependencyGraph, "vertices" ).asInstanceOf[ java.util.Map[ V, AnyRef ] ] // TODO  ugly
        val vertices = scala.collection.JavaConversions.asSet( vv.keySet() )
        vertices.find( vertex => vertex equals v )
    }

    override def filterVertices( filter: V => Boolean ) = { // TODO slow 
        val vv: java.util.Map[ V, AnyRef ] = PrivilegedReflectionUtil.getValue( dependencyGraph, "vertices" ).asInstanceOf[ java.util.Map[ V, AnyRef ] ] // TODO  ugly
        val vertices = scala.collection.JavaConversions.asScalaSet( vv.keySet() )
        vertices filter filter
    }

    override def foreach( doSomething: V => Unit ) { // TODO slow 
        val vv: java.util.Map[ V, AnyRef ] = PrivilegedReflectionUtil.getValue( dependencyGraph, "vertices" ).asInstanceOf[ java.util.Map[ V, AnyRef ] ] // TODO  ugly
        val vertices = scala.collection.JavaConversions.asScalaSet( vv.keySet() )
        vertices foreach doSomething
    }

    override def foreachEdge( doSomething: Dependency[ V ] => Unit ) { // TODO slow 
        val vv: java.util.Map[ Dependency[ V ], AnyRef ] = PrivilegedReflectionUtil.getValue( dependencyGraph, "edges" ).asInstanceOf[ java.util.Map[ Dependency[ V ], AnyRef ] ] // TODO  ugly
        val edges = scala.collection.JavaConversions.asScalaSet( vv.keySet() )
        edges foreach doSomething
    }

    override def connect( from: V, to: V, dependency: Dependency[ V ] ) = dependencyGraph.addEdge( dependency, from, to )

    override def isConnected( from: V, to: V ) = dependencyGraph.isNeighbor( from, to )

    def findEdge( from: V, to: V ): Dependency[ V ] = dependencyGraph.findEdge( from, to )

    def findEdgesTo( to: V ): Iterable[ Dependency[ V ] ] = { // TODO ugly
        var result = new java.util.HashSet[ Dependency[ V ] ]();
        result.addAll( dependencyGraph getInEdges ( to ) )
        scala.collection.JavaConversions.asScalaSet( result )
    }

    override def containsEdge( edge: Dependency[ V ] ) = dependencyGraph containsEdge edge
}
