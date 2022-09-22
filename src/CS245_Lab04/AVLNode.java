package CS245_Lab04;

public class AVLNode<E> {
	private E element;        // Value for this node
	private AVLNode<E> left;     // reference to left child
	private AVLNode<E> right;	//reference to right child
	private int height;
	//height is the height of the subtree rooted at 'This' AVLNode
	//aside from an initial height of 0 (assumes AVLNode is constructed as a leaf node)
	// the height is otherwise maintained within the ambient tree of the node
	// height is determined recursively
	
	  // Constructor
	public AVLNode(E it){
	  	element = it;
	  	left = null;
		right = null;
		height = 0;	  
	}
	
	public boolean isLeaf(){
		return( left == null && right == null);
	}
	 
	 //get and set
	 public AVLNode<E> getLeft() { 
	 	return left; 
	 }  
	 
	 public void setLeft(AVLNode<E> l){
	 	 left = l; 
	 }     
	 
	 public AVLNode<E> getRight() { 
	 	return right; 
	 }  
	 
	 public void setRight(AVLNode<E> r){
	 	 right = r; 
	 }     
	 
	 public E getElement() { 
	 	return element; 
	 } 
	  
	 public E setElement(E it) {
	 	return element = it; 
	 }
	 
	public String toString(){
		return (element.toString());
	}
	public boolean hasLeft(){
		return left != null;
	}
	public boolean hasRight(){
		return right !=null;
	}
	
	public int getHeight(){
		return height;
	}
	public void setHeight(int h){
		height = h;
	}

	public int getLeftSTHeight(){
		int leftSTHeight = -1;
		if( hasLeft() ){
			height = getLeft().getHeight();
		}
		return height;
	}
	//@return the height of the subtree rooted at the right child of this node,
	//    with -1 returned if there is no right child.
	public int getRightSTHeight(){
		int rightSTHeight = -1;
		if( hasRight() ){
			height = getRight().getHeight();
		}
		return height;
	}
	
}
