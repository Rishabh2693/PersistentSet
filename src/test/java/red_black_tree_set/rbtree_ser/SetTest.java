package red_black_tree_set.rbtree_ser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.junit.Test;

import junit.framework.TestCase;

public class SetTest extends TestCase {
	
	public static final Comparator<Integer> INTEGER_COMP = new Comparator<Integer>() {
		 
		public int compare(Integer o1, Integer o2) {
			return o1 - o2;
		}
	};
 
	@Test
	public void testInsert() {
		Set<Integer> tree = new RedBlackTree<Integer>(INTEGER_COMP);
		ArrayList<Integer> shuffledIntegers = getShuffledIntegers(100);
		for (Integer integer : shuffledIntegers) {
			tree = tree.add(integer);
		}
		assertTrue(tree.size() == 100);
		int count = 0;
		Iterator<Integer> i = tree.iterator();
		while(i.hasNext()){
			Integer in =  i.next();
			assertEquals(in.intValue(), count++);
		}
	}
 
	@Test
	public void testDelete() {
		Set<Integer> tree = new RedBlackTree<Integer>(INTEGER_COMP);
		ArrayList<Integer> shuffledIntegers = getShuffledIntegers(100);
		for (Integer integer : shuffledIntegers) {
			tree = tree.add(integer);
		}
		Collections.shuffle(shuffledIntegers);
		int size = 100;
		assertTrue(tree.size() == size);
 
		for (Integer integer : shuffledIntegers) {
			assertTrue(tree.contains(integer));
			tree = tree.remove(integer);
			assertEquals(tree.size(), --size);
			assertFalse(tree.contains(integer));
			tree = tree.remove(integer);
			assertEquals(size, tree.size());
			tree = tree.remove(101);
			assertEquals(size, tree.size());
			tree = tree.remove(-1);
			assertEquals(size, tree.size());
		}
 
		assertFalse(tree.iterator().hasNext());
	}
 
	private static ArrayList<Integer> getShuffledIntegers(int number) {
		ArrayList<Integer> shuffledIntegers = new ArrayList<Integer>(number);
		for (int i = 0; i < 100; i++) {
			shuffledIntegers.add(i);
		}
		Collections.shuffle(shuffledIntegers);
		return shuffledIntegers;
	}
 
	public void testDelete2() {
		Set<Integer> tree = new RedBlackTree<Integer>(INTEGER_COMP);
 
		for (int i = 0; i < 100000; i++) {
 
			int el = (int) (Math.random() * 10);
			Set<Integer> del = tree.remove(el);
			Set<Integer> ins = tree.add(el);
			assertTrue(tree.equals(ins) || tree.equals(del));
 
			double choice = Math.random();
 
			if(choice < .33){
				tree = del;
			}else if(choice < .66){
				tree = ins;
			}
		}
	}
 
	@Test
	public void testIterator(){
		Set<Integer> tree1 = new RedBlackTree<Integer>(INTEGER_COMP);
		Set<Integer> tree2 = new RedBlackTree<Integer>(INTEGER_COMP);
		ArrayList<Integer> tree1Contents = new ArrayList<Integer>();
		ArrayList<Integer> tree2Contents = new ArrayList<Integer>();
 
		for(int i=0;i<=1000;i++){
				tree1 = tree1.add(i);
				tree1Contents.add(i);
 
 
		}
 
		for(int i=1001;i<=2000;i++){
			tree2 = tree2.add(i);
			tree2Contents.add(i);
		}
 
 
		for(int i=2001;i<=3000;i++){
			tree1 = tree1.add(i);
			tree1Contents.add(i);
		}
		Collections.sort(tree1Contents, INTEGER_COMP);
		Collections.sort(tree2Contents, INTEGER_COMP);
 
		assertEquals(tree1.getElements(), tree1Contents);
		assertEquals(tree2.getElements(), tree2Contents);
 
		Iterator<Integer> tree1Iter = tree1.iterator();
		Iterator<Integer> tree2Iter = tree2.iterator();
 
		int cursur = 0;
		while(tree1Iter.hasNext()){
			int el1 = tree1Iter.next();
			assertEquals(el1, (int)tree1Contents.get(cursur));
			cursur++;
		}
 
		cursur = 0;
		while(tree2Iter.hasNext()){
			int el2 = tree2Iter.next();
 
			assertEquals(el2, (int)tree2Contents.get(cursur));
			cursur++;
		}
	}
 
}
