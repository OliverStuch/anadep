package org.anadep.model


trait DependencyFactory[Elem] {
    def dependsOn (element:Elem): Pair[Elem,Elem] 
    def ->(element:Elem): Pair[Elem,Elem]  = dependsOn (element)
}

