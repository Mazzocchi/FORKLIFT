package engine;

import java.util.Iterator;


class EmptyIterator<T>
implements Iterator<T> {
	EmptyIterator () {}
	public boolean hasNext() {return false;}
	public T next() {return null;}
}