package red_black_tree_set.rbtree_ser;

import java.util.Iterator;
import java.util.List;

public interface Set<T extends Comparable<T>> {
	public RedBlackTree<T> add(T Key);
	public Iterator<T> iterator();
	public RedBlackTree<T> remove(T element);
	public int size();
	public boolean contains(Object e);
	public List<T> getElements();
}
