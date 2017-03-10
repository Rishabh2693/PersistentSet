package red_black_tree_set.rbtree_ser;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;



public class RedBlackTree<T extends Comparable<T>> implements Iterable<T>,Set<T>{
 
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	Comparator<? super T> comparator;
	RedBlackTreeNode<T> root = new RedBlackTreeNode<T>((RedBlackTreeNode<T>)null);
	RedBlackTreeNode<T> newRoot = null;
	int size = 0;
	private long longHashcode=0; 
	int hashcode;  
	private ArrayList<T> elements = null;
 
	public RedBlackTree(Comparator<? super T> comparator) {
		this.comparator = comparator;
	}
 
	private RedBlackTree(Comparator<? super T> comparator, RedBlackTreeNode<T> root, int size, int hashcode, long longHashcode) {
		this.size =size;
    	this.comparator = comparator;
		this.root = root;
		this.hashcode = hashcode;
		this.longHashcode = longHashcode;
	}
 
	private RedBlackTreeNode<T> rotate_right(LinkedList<RedBlackTreeNode<T>> parents, RedBlackTreeNode<T> n) {
		RedBlackTreeNode<T> left = n.left ;
		RedBlackTreeNode<T> right = n.right;
		RedBlackTreeNode<T> left_right = n.left.right;
		if(n.parent(parents)!= null){
			if(n.parent(parents).left == n) n.parent(parents).left = left;
			else n.parent(parents).right = left;
		}else{
			newRoot = left;
		}
		left.right = n;
		n.left = left_right;
		n.right = right;
		return left;
	}
 
	private RedBlackTreeNode<T> rotate_left(LinkedList<RedBlackTreeNode<T>> parents, RedBlackTreeNode<T> n) {
		RedBlackTreeNode<T> right = n.right;
		RedBlackTreeNode<T> left = n.left;
		RedBlackTreeNode<T> right_left = n.right.left;
		if(n.parent(parents)!= null){
			if(n.parent(parents).left == n) n.parent(parents).left = right;
			else n.parent(parents).right = right;
		}else{
			newRoot = right;
		}
		right.left = n;
		n.right = right_left;
		n.left = left;
		return right;
	}
 
	private void replace_node(RedBlackTreeNode<T> node,RedBlackTreeNode<T> nodeParent, RedBlackTreeNode<T> replacement) {
		if(nodeParent != null){
			if(nodeParent.left == node) nodeParent.left = replacement;
			else {
				nodeParent.right = replacement;
			}
		}else{
			newRoot = replacement;
		}
	}
 
	public RedBlackTree<T> add(T key) {
		RedBlackTreeNode<T> newNode = new RedBlackTreeNode<T>(key);
		return addNode(newNode);
	}
 
	private RedBlackTree<T> addNode(RedBlackTreeNode<T> newNode) {
		newRoot = root.clone();
		RedBlackTreeNode<T> current = newRoot;
		LinkedList<RedBlackTreeNode<T>> parents = new LinkedList<RedBlackTreeNode<T>>();
		parents.add(null);
		while (current.key != null) {
			parents.add(current);
			int dir = comparator.compare(newNode.key, current.key);
			if(dir == 0) {
				newRoot = null;  
				return this;
			} else if (dir < 0) {
				current = current.left = current.left.clone();
			} else {
				current = current.right = current.right.clone();
			}
		}
		if(parents.getLast() != null){
			if (parents.getLast().left == current) {
				parents.getLast().left = newNode;
			} else {
				assert parents.getLast().right == current;
				parents.getLast().right = newNode;
			}
		}
		insertCase_1(parents, newNode);
		RedBlackTree<T> result =  new RedBlackTree<T>(comparator, newRoot, size+1, hashcode ^ newNode.key.hashCode(), longHashcode + newNode.key.hashCode());
		newRoot = null;
		return result;
	}
 
	private void insertCase_1(LinkedList<RedBlackTreeNode<T>> parents, RedBlackTreeNode<T> n) {
		if (parents.getLast() == null){
			newRoot = n;
			n.black = BLACK;
		}
		else
			{
			if (parents.getLast().black){
				 
			}else{
				if (!n.uncle(parents).black) {
					parents.getLast().black = BLACK;
					n.cloneUncle(parents);
					n.uncle(parents).black = BLACK;
					n.grandparent(parents).black = RED;
					RedBlackTreeNode<T> grandparent = n.grandparent(parents);
					parents.removeLast();
					parents.removeLast();
					insertCase_1(parents, grandparent);
				} else
					{
					if (n == n.parent(parents).right && n.parent(parents) == n.grandparent(parents).left) {
						RedBlackTreeNode<T> nParent = n.parent(parents);
						parents.removeLast();
						RedBlackTreeNode<T> replacement = rotate_left(parents, nParent);
						parents.addLast(replacement);
						n = replacement.left = replacement.left.clone();
					} else if (n == n.parent(parents).left && n.parent(parents) == n.grandparent(parents).right) {
						RedBlackTreeNode<T> nParent = n.parent(parents);
						parents.removeLast();
						RedBlackTreeNode<T> replacement = rotate_right(parents, nParent);
						parents.addLast(replacement);
						n = replacement.right = replacement.right.clone();
					}
					n.parent(parents).black = BLACK;
					n.grandparent(parents).black = RED;
					if (n == n.parent(parents).left && n.parent(parents) == n.grandparent(parents).left) {
						RedBlackTreeNode<T> gp = n.grandparent(parents);
						parents.removeLast();
						parents.removeLast();
						rotate_right(parents, gp);
					} else {
						assert n == n.parent(parents).right && n.parent(parents) == n.grandparent(parents).right;
						RedBlackTreeNode<T> gp = n.grandparent(parents);
						parents.removeLast();
						parents.removeLast();
						rotate_left(parents, gp);
					}
					}
			}
			}
	}
 
	private void remove_helper(LinkedList<RedBlackTreeNode<T>> parents, RedBlackTreeNode<T> n) {
 
		assert n.parent(parents) == null || n.parent(parents).left == n || n.parent(parents).right == n;
		if(n.left.key != null && n.right.key != null){
			parents.addLast(n);
			n.left = n.left.clone();
			RedBlackTreeNode<T> current = n.right = n.right.clone();
			while(current.key != null){
				parents.addLast(current);
				current = current.left = current.left.clone();
			}
 
			n.key = current.parent(parents).key;
			RedBlackTreeNode<T> currentParent = current.parent(parents);
			parents.removeLast();
			delete_one_child(parents, currentParent);
		}else{
			delete_one_child(parents, n);
		}
	}
 
	private void delete_one_child(LinkedList<RedBlackTreeNode<T>> parents, RedBlackTreeNode<T> n) {
		RedBlackTreeNode<T> child;
		if(n.right.key == null){
			child = n.left = n.left.clone();
		}else{
			assert n.left.key == null;
			child = n.right = n.right.clone();
		}
 
		replace_node(n, n.parent(parents), child);
		if (n.black) {
			if (!child.black)
				child.black = BLACK;
			else
				delete(parents, child);
		}
	}
 
	private void delete(LinkedList<RedBlackTreeNode<T>> parents, RedBlackTreeNode<T> n) {
		if (n.parent(parents) == null)
		{
			newRoot = n;
		}else{
			if (!n.sibling(parents).black) {
				n.cloneSibling(parents);
				n.parent(parents).black = RED;
				n.sibling(parents).black = BLACK;
				if (n == n.parent(parents).left)
				{
					RedBlackTreeNode<T> parent = n.parent(parents);
					parents.removeLast();
					RedBlackTreeNode<T> replacement = rotate_left(parents, parent);
					parents.add(replacement);
					parents.add(replacement.left);
					n = replacement.left.left = replacement.left.left.clone();
				}
				else
				{
					RedBlackTreeNode<T> parent = n.parent(parents);
					parents.removeLast();
					RedBlackTreeNode<T> replacement =rotate_right(parents, parent);
					parents.add(replacement);
					parents.add(replacement.right);
					n = replacement.right.right = replacement.right.right.clone();
				}
			}
			if (n.parent(parents).black &&
					n.sibling(parents).black &&
					n.sibling(parents).left.black &&
					n.sibling(parents).right.black) {
				n.cloneSibling(parents);
				n.sibling(parents).black = RED;
				RedBlackTreeNode<T> parent = parents.removeLast();
				delete(parents, parent);
			} else {if (!n.parent(parents).black &&
						n.sibling(parents).black &&
						n.sibling(parents).left.black &&
						n.sibling(parents).right.black) {
					n.cloneSibling(parents);
					n.sibling(parents).black = RED;
					n.parent(parents).black = BLACK;
				} else
					{
					if (n == n.parent(parents).left &&
							n.sibling(parents).black &&
							!n.sibling(parents).left.black &&
							n.sibling(parents).right.black) {
						n.cloneSibling(parents);
						n.sibling(parents).black = RED;
						n.sibling(parents).left = n.sibling(parents).left.clone();
						n.sibling(parents).left.black = BLACK;
						rotate_right(parents, n.sibling(parents));
					} else if (n == n.parent(parents).right &&
							n.sibling(parents).black &&
							!n.sibling(parents).right.black &&
							n.sibling(parents).left.black) {
						n.cloneSibling(parents);
						n.sibling(parents).black = RED;
						n.sibling(parents).right = n.sibling(parents).right.clone();
						n.sibling(parents).right.black = BLACK;
						rotate_left(parents, n.sibling(parents));
					}
					n.cloneSibling(parents);
					n.sibling(parents).black = n.parent(parents).black;
					n.parent(parents).black = BLACK;
					if (n == n.parent(parents).left) {
						n.sibling(parents).right = n.sibling(parents).right.clone();
						n.sibling(parents).right.black = BLACK;
						RedBlackTreeNode<T> parent = parents.removeLast();
						rotate_left(parents, parent);
					} else {
						n.sibling(parents).left = n.sibling(parents).left.clone();
						n.sibling(parents).left.black = BLACK;
						RedBlackTreeNode<T> parent = parents.removeLast();
						rotate_right(parents, parent);
					}
					}
			}
		}
	}
 
 
	private ArrayList<T> fillArray(ArrayList<T> array, RedBlackTreeNode<T> current){
		if(current.key == null) return array;//null element
		fillArray(array, current.left);
		array.add(current.key);
		fillArray(array, current.right);
		return array;
	}
 
	public List<T> getElements(){
		if(elements == null){
			elements = new ArrayList<T>(size);
			fillArray(elements, root);
		}
		return elements;
	}
 
	public Iterator<T> iterator() {
		return new NullIteratorAdapter<T>(new InOrderTraverser());
	}
 
	public int size() {
		return size;
	}
 
	public T getRandomLeaf(){
		RedBlackTreeNode<T> current = root;
		while (true) {
			int dir = Math.random()>.5f?1:-1;
			if(dir < 0) {
				if(current.left.key == null) return current.key;
				current = current.left;
			} else {
				if(current.right.key == null) return current.key;
				current = current.right;
			}
		}
	}

	public boolean contains(Object e) {
		T element = (T) e;
		RedBlackTreeNode<T> current = root;
		while (current.key != null) {
			int dir = comparator.compare(element, current.key);
			if (dir == 0 ){
				return true;
			}else if(dir < 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		}
		return false;
	}
 
	public T get(T element) {
		RedBlackTreeNode<T> current = root;
		while (current.key != null) {
			int dir = comparator.compare(element, current.key);
			if (dir == 0 ){
				return current.key;
			}else if(dir < 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		}
		return null;
	}
 
	public int hashCode() {
		return hashcode;
	}
 
	private class InOrderTraverser implements Iterator<T>{
		Stack<TraversalVariable> stack = new Stack<TraversalVariable>();
 
		public InOrderTraverser() {
			if(root.key!=null){
				stack.push(new TraversalVariable(TraversalSymbol.RIGHT, root));
				stack.push(new TraversalVariable(TraversalSymbol.VISIT, root));
				stack.push(new TraversalVariable(TraversalSymbol.LEFT, root));
			}
		}
 
		public boolean hasNext() {
			return true;
		}
 
		public T next(){
			if(stack.isEmpty()) return null;
			TraversalVariable curr = stack.pop();
 
			while(curr.s != TraversalSymbol.VISIT){
				RedBlackTreeNode<T> child;
				if(curr.s == TraversalSymbol.LEFT){
					child = curr.node.left;
				}else{
					assert curr.s == TraversalSymbol.RIGHT;
					child = curr.node.right;
				}
				if(child.key != null){
					stack.push(new TraversalVariable(TraversalSymbol.RIGHT, child));
					stack.push(new TraversalVariable(TraversalSymbol.VISIT, child));
					stack.push(new TraversalVariable(TraversalSymbol.LEFT, child));
				}
				if(stack.isEmpty()) return null;
				curr = stack.pop();
			}
			return curr.node.key;
		}
 
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
 
	private class TraversalVariable{
		TraversalSymbol s;
		public TraversalVariable(TraversalSymbol s, RedBlackTreeNode<T> node) {
			this.s = s;
			this.node = node;
		}
		RedBlackTreeNode<T> node;
	}

	private static enum TraversalSymbol{LEFT, RIGHT, VISIT}
 
	public boolean equals(Object obj) {
		RedBlackTree<T> other = (RedBlackTree<T>) obj;
		if(other.hashcode != hashcode) return false;
		if(other.longHashcode != longHashcode)return false;
		List<T> l1 = other.getElements();
		List<T> l2 = getElements();
		return l1.equals(l2);
	}
 
	private class NullIteratorAdapter<T> implements Iterator<T> {
		T next;
		Iterator<T> wrapper;
		public NullIteratorAdapter(Iterator<T> nullTerminatedIterator) {
			this.wrapper = nullTerminatedIterator;
			next = wrapper.next();
		}
 
		public boolean hasNext() {
			return next != null;
		}
 
		public T next() {
			T ret = next;
			next = wrapper.next();
			return ret;
		}
 
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public RedBlackTree<T> remove(T Key) {
			RedBlackTreeNode<T> current = newRoot = root.clone();
			LinkedList<RedBlackTreeNode<T>> parents = new LinkedList<RedBlackTreeNode<T>>();
			parents.add(null);
			while (current.key != null) {
				parents.add(current);
				int dir = comparator.compare(Key, current.key);
				if (dir < 0) {
					current = current.left = current.left.clone();
				} else if(dir > 0){
					current = current.right= current.right.clone();
				}else{
					parents.removeLast();
					remove_helper(parents, current);
					RedBlackTree<T> tree = new RedBlackTree<T>(comparator, newRoot, size-1, hashcode ^ Key.hashCode(), longHashcode - Key.hashCode());
					newRoot = null;
					return tree;
				}
			}
			newRoot = null;
			return this;
	}
}
