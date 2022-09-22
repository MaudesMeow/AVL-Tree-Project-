package CS245_Lab04;

import java.util.*;


public class MakeAnAVL{
	public static void main(String[] args){
		
		integerTree();
		
	}



	public static void integerTree(){





		AVLTree<Integer> bt = new AVLTree<Integer>();
		System.out.println("inserting: 37");
		bt.insert(37);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 24");
		bt.insert(24);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 32");
		bt.insert(32);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 25");
		bt.insert(25);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 7");
		bt.insert(7);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 42");
		bt.insert(42);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 120");
		bt.insert(120);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();
		System.out.println("inserting: 40");
		bt.insert(40);
		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();

		System.out.println("Height: " + bt.getHeight());
		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();

		AVLNode<Integer> node = bt.search(24);
		if(node.isLeaf()){
			System.out.println("The node with " + node.getElement()+ "has no children.");
		}
		else{
			System.out.println("The node with " + node.getElement()+ " has at least one child.");
			if(node.hasLeft()){
				System.out.println("It's left child has: "+ node.getLeft().getElement());
			}
			if(node.hasRight()){
				System.out.println("It's right child has: "+ node.getRight().getElement());
			}
		}
		System.out.println("removing: 24");
		bt.remove(24);


		System.out.println("Printing by breadth first:");
		bt.printLevelOrder();

		System.out.println("Same numbers, but a different order:");
		AVLTree<Integer> bt2 = new AVLTree<Integer>();
		bt2.insert(120);
		bt2.insert(42);
		bt2.insert(7);
		bt2.insert(32);
		bt2.insert(37);
		bt2.insert(24);
		bt2.insert(25);
		bt2.insert(40);
		System.out.println("Height: " + bt2.getHeight());
		System.out.println("Printing by breadth first:");
		bt2.printLevelOrder();

		node = bt2.search(32);
		if(node.isLeaf()){
			System.out.println("The node with " + node.getElement()+ "has no children.");
		}
		else{
			System.out.println("The node with " + node.getElement()+ " has at least one child.");
			if(node.hasLeft()){
				System.out.println("Its left child has: "+ node.getLeft().getElement());
			}
			if(node.hasRight()){
				System.out.println("Its right child has: "+ node.getRight().getElement());
			}
		}
		System.out.println("removing: 32");
		bt2.remove(32);
		node = bt2.search(25);
		if(node.isLeaf()){
			System.out.println("The node with " + node.getElement()+ " has no children.");
		}
		else{
			System.out.println("The node with " + node.getElement()+ " has at least one child.");
			if(node.hasLeft()){
				System.out.println("Its left child has: "+ node.getLeft().getElement());
			}
			if(node.hasRight()){
				System.out.println("Its right child has: "+ node.getRight().getElement());
			}
		}
		System.out.println("Height: " + bt2.getHeight());
		System.out.println("Printing by breadth first:");
		bt2.printLevelOrder();



	
	}


		
		
}
