package pers.zuhonm.he2;

import java.util.ArrayList;

class TreeNode{
	String attr_gate;
	TreeNode left;
	TreeNode right;
	boolean isLeaf;
	ArrayList<Integer> vector; //the matrix element
	
	public TreeNode (String attr_gate) {
        // union of attribute and bool_gate
		this.attr_gate = attr_gate;
		this.left = null;
		this.right = null;
		vector = new ArrayList<Integer>();
	}
}