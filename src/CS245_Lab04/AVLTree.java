package CS245_Lab04;

//WORKED WITH MITCHEL EWAN!
import java.util.Iterator;
import java.util.LinkedList;
import java.util.*;

public class AVLTree<E extends Comparable<E>> {
    private AVLNode<E> root; //root of the tree
    private int size; //size of the tree
    private static final int ALLOWED_IMBALANCE = 1;

    /*Class state information maintained in helper method pre- and post-conditions.*/
    private AVLNode<E> parentNode;
    private LinkedList<AVLNode<E>> pathToRootFromCurrentNode; //May use as stack
    private AVLNode<E> currentNode;
    private int leftRightChild; // negative if currentNode is left child of non-null parent
    // positive if currentNode is right child of non-null parent
    // zero when parentNode is null (i.e., currentNode is root)


    //Constructor
    public AVLTree() {
        pathToRootFromCurrentNode = new LinkedList<AVLNode<E>>();
        root = null;
        size = 0;
    }

    //Constructor
    public AVLTree(AVLNode<E> node) {
        pathToRootFromCurrentNode = new LinkedList<AVLNode<E>>();
        root = node;
        size = 1;
    }


    /**
     * determines if the tree is empty
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * searches for a node that contains it.
     * if it finds it, it returns that node
     * else it returns null
     *
     * @param it - the element to look for
     * @return the node reference that contains it (null if not present in the tree)
     * Postcondition:
     * 1) Sets currentNode to the return value (null if a node with value it does not exist)
     * 2) parentNode is set to refer to the parent of the return value,
     * or what would be the parent of a node with element it.
     * 3) sets leftRightChild as currentNode is/would be left or right child
     * 4) pathToRootFromCurrentNode fills a linked list from root (at the tail) to the last visited (at head)
     * We say that currentNode, parentNode, and leftRightChild are *oriented* if these postconditions are met.
     */
    public AVLNode<E> search(E it) {

        currentNode = root;
        parentNode = null;
        leftRightChild = 0;
        pathToRootFromCurrentNode.clear();
        if (currentNode == null) {
            return null;
        }

        //Else AVL Tree is not empty, so continue...

        while (currentNode != null) {
            pathToRootFromCurrentNode.addFirst(currentNode);
            leftRightChild = it.compareTo(currentNode.getElement());
            if (leftRightChild < 0) {
                // it is less than the current subtree root node, go left
                parentNode = currentNode;
                currentNode = currentNode.getLeft();
            } else if (leftRightChild > 0) {
                // it is greater than the current subtree root node, go right
                parentNode = currentNode;
                currentNode = currentNode.getRight();
            } else {
                if (parentNode != null) {
                    leftRightChild = it.compareTo(parentNode.getElement());
                }
                return currentNode; //Done! found the node with element value it.
            }

        }
        return null; //This is returned if item is not found.

    }

    /**
     * determines is the tree contains the element
     *
     * @return true if it is in the tree
     */
    public boolean contains(E it) {
        return search(it) != null;
    }

    /**
     * Add the element to the correct location
     * all elements to the left are less than the parent
     * all elements to the rights are greater than the parent
     * Do not allow duplicates
     *
     * @param it the element to insert
     * @return a reference to the newly created node if created, null otherwise
     * Precondition:
     * 1) The tree is currently AVL balanced
     * Postconditions: If a new node is added
     * 1) Tree is AVL balanced
     * 2) Node heights are correctly updated
     * 3) currentNode is set to refer to the newly added node,
     * which is then added to the front of the pathToRootFromCurrentNode
     */
    public AVLNode<E> insert(E it) {

        AVLNode<E> addedNode = null;  //stays null if no new node is created/added.
        search(it); //currentNode, parentNode, leftRightChild is oriented either to the location to insert,
        //	or otherwise to an already existing node with element E it.
        if (currentNode == null) {
            // There is not a node with element E it yet, so add it!
            addedNode = new AVLNode<E>(it);
            if (leftRightChild < 0) {//add as left child
                parentNode.setLeft(addedNode);
                currentNode = addedNode;
            } else if (leftRightChild > 0) {
                parentNode.setRight(addedNode);
                currentNode = addedNode;
            } else { //parentNode is null, so set currentNode as root
                currentNode = addedNode;
                root = currentNode;
            }

            pathToRootFromCurrentNode.addFirst(currentNode); // currentNode -> parentNode -> ... -> root
            size++;

            adjustHeightsAndRebalance();

        } else {
            //Nothing happens.
        }
        return addedNode;
    }

    /* Helper method:
     * Preconditions:
     * 		1) currentNode is a newly added node, parentNode is its (possibly null) parent,
     * 			leftRightChild indicates relationship between currentNode and parentNode
     * 		2) pathToRootFromCurrentNode has been oriented so that currentNode -> parentNode -> ... -> root
     * Postcondition: height adjustments and rebalancing precipitate up the pathToRootFromCurrentNode as needed.
     */
    private void adjustHeightsAndRebalance() {
        // height of currentNode correctly starts at 0.
        Iterator<AVLNode<E>> pathIter = pathToRootFromCurrentNode.iterator();

        int oldCurrentNodeHeight = -1;
        currentNode = pathIter.next(); // will always be assumed to be to be 0. starts off the iteration at the beginning of the iteration


        while (pathIter.hasNext()) {
            parentNode = pathIter.next(); //sets parent to one above current/or equal to root node in iteration to allow for rotations
            leftRightChild = currentNode.getElement().compareTo(parentNode.getElement()); // checks for which side of the current node is at and what side of tree will need to be rotated
            boolean rotate = adjustHeightsAndRebalanceFromCurrentNode(); // checks whether the method is returning true or false
            if (rotate && currentNode.getHeight() == oldCurrentNodeHeight) { // when rotation has completed along the height of the height of the current node
                //and has checked all nodes along the way, it'll return to the loop and continue on
                return;
            }
            currentNode = parentNode; // progresses the iterator
            oldCurrentNodeHeight = currentNode.getHeight();
        }

        //look for and address special cases
        leftRightChild = 0;
        parentNode = null;
        adjustHeightsAndRebalanceFromCurrentNode();


    }

    /*
       Precondition: currentNode refers to the intened node to center adjustment of heights and rebalancing
       Postcondition:
      @return true if a rotation occurred, false otherwise
       */
    private boolean adjustHeightsAndRebalanceFromCurrentNode() {
        boolean rotationOccurred = false;
        int leftSTHeight = -1; // left subtree height is -1 if it's empty
        int rightSTHeight = -1; // right subtree height is -1 if it's empty
        if (currentNode.hasLeft()) {
            leftSTHeight = currentNode.getLeft().getHeight();
        }
        if (currentNode.hasRight()) {
            rightSTHeight = currentNode.getRight().getHeight();
        }

        int balanceFactor = rightSTHeight - leftSTHeight;
        if (balanceFactor > ALLOWED_IMBALANCE) {// right heavy tree

            rebalanceRightHeavyTreeAtCurrentNode(); //heights adjusted
            rotationOccurred = true;
        } else if (balanceFactor < -ALLOWED_IMBALANCE) { //left heavy tree

            rebalanceLeftHeavyTreeAtCurrentNode(); //heights adjusted
            rotationOccurred = true;
        } else {
            currentNode.setHeight(Math.max(leftSTHeight, rightSTHeight) + 1);
        }
        return rotationOccurred;
    }

    /*
       Precondition: balanceFactor at currentNode is 2
      Postcondition: Carry out required single or double rotation about the currentNode
     */
    private void rebalanceRightHeavyTreeAtCurrentNode() {
        //Either a single left rotation at parentNode
        //		or a double right left rotation
        AVLNode<E> oldParentNode = parentNode;
        int oldLeftRightChild = leftRightChild;

        AVLNode<E> rightSTRoot = currentNode.getRight(); //Precondition implies rightSTRoot is non-null

        //The below RHS expression of form   booleanCondition ? A : B
        //	returns A  if booleanCondition is true, otherwise returns B
        int rlHeight = rightSTRoot.hasLeft() ? rightSTRoot.getLeft().getHeight() : -1;
        int rrHeight = rightSTRoot.hasRight() ? rightSTRoot.getRight().getHeight() : -1;
        int rightBalanceFactor = rrHeight - rlHeight;
        if (rightBalanceFactor >= 0) {
            singleLeftRotation();
        } else { //Double right-left rotation

            parentNode = currentNode; //parentNode is set to current so this way the "A" values will be checked properly
            currentNode = rightSTRoot; //current node is then set to the right subtree root, because this is where rotation will need to take palce
            leftRightChild = -1;
            singleRightRotation();
            currentNode = parentNode; //start it over again
            parentNode = oldParentNode; //set back to old parameters to allow rotation to happen correctly in tree order
            leftRightChild = oldLeftRightChild;
            singleLeftRotation();

        }
    }

    /*
       Precondition: balanceFactor at currentNode is -2
      Postcondition: Carry out required single or double rotation about the currentNode
     */
    private void rebalanceLeftHeavyTreeAtCurrentNode() {
        // Either a single right rotation at parentNode
        //		or double left right rotation
        AVLNode<E> oldParentNode = parentNode;
        int oldLeftRightChild = leftRightChild;

        AVLNode<E> leftSTRoot = currentNode.getLeft(); //Precondition implies rightSTRoot is non-null

        //The below RHS expression of form   booleanCondition ? A : B
        //	returns A  if booleanCondition is true, otherwise returns B
        int llHeight = leftSTRoot.hasLeft() ? leftSTRoot.getLeft().getHeight() : -1;
        int lrHeight = leftSTRoot.hasRight() ? leftSTRoot.getRight().getHeight() : -1;
        int leftBalanceFactor = lrHeight - llHeight;

        if (leftBalanceFactor <= 0) {
            singleRightRotation();
        } else { //Double left-right rotation OPPOSITE of right heavy

            parentNode = currentNode;
            currentNode = leftSTRoot;
            leftRightChild = -1;
            singleLeftRotation();
            currentNode = parentNode;
            parentNode = oldParentNode;
            leftRightChild = oldLeftRightChild;
            singleRightRotation();

        }


    }


    //Precondition:
    //	1) Rotation occurs at currentNode, with parentNode oriented as parent of currentNode.
    //	2) currentNode has a non-null left child
    //Postconditions:
    //	1) currentNode refers to B and replaces A as child of parentNode
    //		or otherwise B becomes root if A was root
    //
	/*			    |		        |
  				    A		        B
  			      /   \		      /   \
  			     B     Z   ----> X     A
  			    / \			          /  \
  			   X   Y		          Y   Z
  		2) Nodes X,Y,Z keep same height, 
  		3) Only nodes A and B need to have height updated.
   	*/
    private void singleRightRotation() {
        AVLNode<E> A = currentNode;
        AVLNode<E> B = currentNode.getLeft();    //Assumed non-null in precondition
        AVLNode<E> X = B.getLeft(); //may be null;
        AVLNode<E> Y = B.getRight(); //may be null;
        AVLNode<E> Z = A.getRight(); //may be null;

        //if any of the x,y,z heights are null; set to -1, if they're not, grab the height

        int xHeight = (X == null) ? -1 : X.getHeight();
        int yHeight = (Y == null) ? -1 : Y.getHeight();
        int zHeight = (Z == null) ? -1 : Z.getHeight();


        if (leftRightChild < 0) {
            parentNode.setLeft(B);
        } else if (leftRightChild > 0) {
            parentNode.setRight(B);
        } else { // leftRightChild == 0
            root = B;
        }

        A.setLeft(Y); //Y needs to be adjusted to be child of A, Z remains to the right
        B.setRight(A); //A becomes child of B, this connection brings A,Z,Y


        A.setHeight(Math.max(yHeight, zHeight) + 1);
        B.setHeight(Math.max(xHeight, A.getHeight()) + 1);
        //set current node to B
        currentNode = B;


    }

    //Precondition:
    //	1) Rotation occurs at currentNode, with parentNode oriented as parent of currentNode.
    //	2) currentNode has a non-null right child
    //Postconditions:
    //	1) currentNode refers to B and replaces A as child of parentNode
    //		or otherwise B becomes root if A was root
	/*			|		     |
  				A		     B
  			   / \		    / \
  			  Z   B   ---->A   Y
  			     / \	  / \
  			     X  Y	 Z  X
  		2) Nodes X,Y,Z keep same height, 
  		3) Only nodes A and B need to have height updated.
   	*/
    private void singleLeftRotation() {
        AVLNode<E> A = currentNode;
        AVLNode<E> B = currentNode.getRight();    //Assumed non-null in precondition
        AVLNode<E> X = B.getLeft(); //may be null;
        AVLNode<E> Y = B.getRight(); //may be null;
        AVLNode<E> Z = A.getLeft(); //may be null;


        //same as single right rotation!
        int xHeight = (X == null) ? -1 : X.getHeight();
        int yHeight = (Y == null) ? -1 : Y.getHeight();
        int zHeight = (Z == null) ? -1 : Z.getHeight();

        if (leftRightChild < 0) {
            parentNode.setLeft(B);
        } else if (leftRightChild > 0) {
            parentNode.setRight(B);
        } else {
            root = B;
        }

        A.setRight(X);
        B.setLeft(A);


        A.setHeight(Math.max(zHeight, xHeight) + 1);
        B.setHeight(Math.max(yHeight, A.getHeight()) + 1);

        currentNode = B;
    }


    /**
     * Removes the node that contains it.
     * If the tree does not contain it, it prints that to
     * the user and does nothing else.
     * Otherwise it removes the node and maintains the
     * BST properties
     * if removing a node with two children, replace it
     * with its inorder predecessor.
     * <p>
     * element of the node you want to remove.
     */

    public void remove(E it) {
        search(it); //currentNode, parentNode, leftRightChild is set

        if (currentNode != null) {

            if (currentNode.hasLeft() && currentNode.hasRight()) {
                //currentNode has both a (non-null) left and right child.

                //currentNode will be set to the in-order predecessor, need to save
                // the old reference to currentNode as the return value oldCurrentNode
                AVLNode<E> oldCurrentNode = findInOrderPredecessor(currentNode);
                swapElements(oldCurrentNode, currentNode);
                //Task of remove passed to in-order predecessor as currentNode,
                //	which is guaranteed to be addressable with....
                removeWithOneOrNoChildren();
            } else { //Handled directly as one of the two easier cases where currentNode has one or no children.
                removeWithOneOrNoChildren();
            }
            size--;
        }
    }
    /**Returns the height of the tree
     * if tree is empty, height is -1
     * if tree only has one node, height is 0
     * @return the integer height of the tree
     *
     */

    /**
     * Helper method: removeWithOneOrNoChildren()
     * Precondition:
     * 0) currentNode has at most one (non-null) child
     * 1) currentNode is set to the node to be removed
     * 2) parentNode is the parent of currentNode
     * (may be null if the currentNode is the root node, also evidenced by leftRightChild == 0)
     * 3) leftRightChild is set according to whether currentNode is left or right child of parentNode;
     * it may be zero (i.e., currentNode is the root node)
     * Postcondition:
     * 1) relink references between the parent and any child node
     * to accomplish removal of current node
     * does NOT update size (this is the responsibility of remove())
     * 2) Precipitate adjustments of heights up path from currentNode to root and employ
     * AVL rebalancing as required.
     */
    private void removeWithOneOrNoChildren() {
        //First identify any relevant child of the currentNode that may be
        //	relinked with parentNode
        AVLNode<E> childNode = null; //child of currentNode
        if (currentNode.hasLeft()) {
            childNode = currentNode.getLeft();
        } else if (currentNode.hasRight()) {
            childNode = currentNode.getRight();
        }


        pathToRootFromCurrentNode.removeFirst();
        if (leftRightChild < 0) {
            parentNode.setLeft(childNode);
            currentNode = parentNode;
            //adjustHeightsAndRebalanceFromCurrentNode();
            adjustHeightsAndRebalance();
        } else if (leftRightChild > 0) {
            parentNode.setRight(childNode);
            currentNode = parentNode;
            //adjustHeightsAndRebalanceFromCurrentNode();
            adjustHeightsAndRebalance();
        } else { //currentNode is root node
            root = childNode;
        }
    }

    /**
     * Helper method
     * For removal of node with two chidren, you need to swap elements of two specific nodes
     *
     * @param node1 , node2 the nodes whose contents you are swapping
     */
    private void swapElements(AVLNode<E> node1, AVLNode<E> node2) {
        E tempValue = node1.getElement();
        node1.setElement(node2.getElement());
        node2.setElement(tempValue);

    }

    /**
     * Helper method
     * Precondition: subtreeRootNode has two non-null children
     *
     * @return a reference to the original subtreeRootNode that was passed by reference
     * Postconditions:
     * 1) currentNode is the inorder predecessor of subtreeRootNode
     * 2) parentNode is the parent of currentNode
     * 3) leftRightChild is updated to indicate whether current node is left or right child
     * 4) pathToRootFromCurrentNode is prepended from the front to have form as linked list
     * currentNode -> parentNode -> ... -> subtreeRootNode -> ... -> root
     */
    private AVLNode<E> findInOrderPredecessor(AVLNode<E> subtreeRootNode) {
        parentNode = subtreeRootNode;
        currentNode = subtreeRootNode.getLeft();
        pathToRootFromCurrentNode.addFirst(currentNode);
        leftRightChild = -1; //currentNode starts out as left child
        while (currentNode.hasRight()) {
            parentNode = currentNode;
            currentNode = currentNode.getRight();
            pathToRootFromCurrentNode.addFirst(currentNode);
            leftRightChild = 1;
        }
        return subtreeRootNode;
    }


    public int getHeight() {
        int height = -1;
        if (root != null) {
            height = root.getHeight();
        }
        return height;
    }

    /**
     * prints each level of the tree on its own line
     * use your Queue class
     */
    public void printLevelOrder() {
        Queue<AVLNode<E>> Q = new LinkedList<AVLNode<E>>();
        Queue<Integer> depths = new LinkedList<Integer>();
        AVLNode<E> node = root;
        int currentHeight = 0, oldHeight = 0;
        if (root == null) {
            System.out.println("Depth-first: Empty tree");
            return;
        }
        Q.add(node);
        depths.add(0);
        while (!Q.isEmpty()) {
            node = Q.remove();
            currentHeight = depths.remove();
            if (currentHeight > oldHeight) {
                System.out.println();
            }
            System.out.print(node.getElement() + " ");
            if (node.hasLeft()) {
                Q.add(node.getLeft());
                depths.add(currentHeight + 1);
            }
            if (node.hasRight()) {
                Q.add(node.getRight());
                depths.add(currentHeight + 1);
            }
            oldHeight = currentHeight;
        }
        System.out.println();

    }
}
