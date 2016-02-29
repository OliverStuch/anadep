package org.anadep.util

object JavaIteratorForeachAdaptor { def apply[A] (javaIter:java.util.Iterator[A])  = new JavaIteratorForeachAdaptor[A](javaIter) }
class JavaIteratorForeachAdaptor[A](javaIter:java.util.Iterator[A]) {
    def foreach(f: A => Unit)  {
	while (javaIter.hasNext) {
	  f (javaIter.next)
	}
    }
}
