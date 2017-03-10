package red_black_tree_set.rbtree_ser;

import java.util.LinkedList;

public class RedBlackTreeNode<T> {
//Values are not final, but each new Tree is created by cloning old trees. Old set is never mutated	
 RedBlackTreeNode<T> left;
 RedBlackTreeNode<T> right;
 boolean black;
 T key;


public RedBlackTreeNode() {
}
private static final boolean RED = false;
private static final boolean BLACK = true;

public RedBlackTreeNode(T key) {
	this.key = key;
	black = RED;
	this.left = new RedBlackTreeNode<T>(this);  
	this.right = new RedBlackTreeNode<T>(this);
}

public RedBlackTreeNode(RedBlackTreeNode<T> parent) {
	this.black = BLACK;
}

RedBlackTreeNode<T> grandparent(LinkedList<RedBlackTreeNode<T>> parents) {
	return parents.get(parents.size() - 2);
}

RedBlackTreeNode<T> parent(LinkedList<RedBlackTreeNode<T>> parents) {
	return parents.getLast();
}

RedBlackTreeNode<T> uncle(LinkedList<RedBlackTreeNode<T>> parents) {
	if (grandparent(parents).left == parents.getLast()){
		return grandparent(parents).right;
	}
	else{
		assert grandparent(parents).right == parents.getLast();
		return grandparent(parents).left;
	}
}

public void cloneUncle(LinkedList<RedBlackTreeNode<T>> parents){
	if (grandparent(parents).left == parents.getLast()){
		grandparent(parents).right = grandparent(parents).right.clone();
	}
	else{
		assert grandparent(parents).right == parents.getLast();
		grandparent(parents).left = grandparent(parents).left.clone();
	}
}


public RedBlackTreeNode<T> sibling(LinkedList<RedBlackTreeNode<T>> parents) {
	if (parent(parents).left == this){
		return parent(parents).right;
	}
	else{
		return parent(parents).left;
	}
}


public void cloneSibling(LinkedList<RedBlackTreeNode<T>> parents) {
	if (parent(parents).left == this){
		parent(parents).right = parent(parents).right.clone();
	}
	else{
		assert parent(parents).right == this;
		parent(parents).left = parent(parents).left.clone();
	}
}

public RedBlackTreeNode<T> clone(){
	RedBlackTreeNode<T> node = new RedBlackTreeNode<T>();
	node.key = key;
	node.left = left;
	node.right = right;
	node.black = black;
	return node;
}

}
