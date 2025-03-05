package pers.zuhonm.he2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class LSSS_GenerateMatrixFromTree {
	/*
	 * LSSS Generator will generate access policy matrix, and map function
	 * according to Parser Generator's output, access policy tree
     * 
     * Matrix represents the LSSS,
     * l2attrMap will map the row index -> attribute, while attr2lMap does the vice
	 */	
	public double[][] Matrix;
	public Map<Integer, String> l2attrMap;
	public Map<String, Integer> attr2lMap;

	public LSSS_GenerateMatrixFromTree(TreeNode root) {
		LSSS_matrix(root);
	}
	
	private void LSSS_matrix(TreeNode root) {
		if(root == null)
			return ;
		int c = 1;
		int tree_nodes_count = countTreeNode(root);
		/* 
		 * TreeNode has an attribute names (ArrayList)vector ,
		 * which means its vector in LSSS matrix stored in itself,
		 * we will pick leafs' vectors and store them in (ArrayList<ArrayList<Integer>>)recorder 
		 * 
		 * we will transform recorder into (int[][])matrix before return
		 * 
		 * we assert the tree is full,
		 * which means only n0 and n2 exist, and n2 = n0 + 1
		 * so that the size of leaf_vector = (tree_nodes_count + 1) / 2
		 */
		l2attrMap = new HashMap<Integer, String>();
		attr2lMap = new HashMap<String, Integer>();
		int indexVLeaf = 0;
		root.vector.add(1);
		ArrayList<ArrayList<Integer>> recorder = new ArrayList<ArrayList<Integer>>();
		/*
		 * we will use BFS
		 * to execute the tree
		 * nodes are numbered in BFS order
		 */
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while( !queue.isEmpty() )
		{
			TreeNode current = queue.poll();
			if(current.left != null)	queue.offer(current.left);
			if(current.right != null)	queue.offer(current.right);
			if(current.attr_gate.equals("OR"))
			{
				/*
				 * if gate = OR we should copy its vector to its children
				 * and do not change c,
				 * 
				 * the code below shows shallow copy,
				 * we assert it is right in the result
				 */
				for(int i = 0; i < current.vector.size(); i++)
				{
					int j = current.vector.get(i);
					current.left.vector.add(j);
					current.right.vector.add(j);
				}
			}
			else if(current.attr_gate.equals("AND"))
			{
				/*
				 * if gate = AND we should make sure length = c,
				 * and we perform algorithm by order:
				 * 1. self padding 0
				 * 2. copy self to right kid and pad 1
				 * 3. force 0,0,...,-1 to left kid
				 * 4. c+=1
				 */
				for(int i = 0; i < c - current.vector.size(); i++)
					current.vector.add(0);
				
				for(int i = 0; i < current.vector.size(); i++)
				{
					int j = current.vector.get(i);
					current.right.vector.add(j);
				}
				current.right.vector.add(1);
				
				for(int i = 0; i < c; i++)
					current.left.vector.add(0);
				current.left.vector.add(-1);
				
				c += 1;
			}
			/*
			 * leaf (or we called attributes) will be recorded in recorder
			 * and the element is numbered in BFS-order,
			 * assert that vector of matrix is also numbered in the same order
			 * 
			 * and according to the basement above
			 * we make the function ρ:[l]->Ω and ρ':Ω->[l] here,
			 * names l2attrMap and attr2lMap respectively
			 */
			else
			{
				recorder.add(current.vector);
				l2attrMap.put(indexVLeaf, current.attr_gate);
				attr2lMap.put(current.attr_gate, indexVLeaf);
				indexVLeaf++;
			}
		}
		
		Matrix = new double [(tree_nodes_count + 1) / 2][c];
		int row = 0;
		for(ArrayList<Integer> i : recorder)
		{
			int comlunm = 0;
			for(; comlunm < i.size(); comlunm++)
				Matrix[row][comlunm] = (double)i.get(comlunm);
			for(; comlunm < c; comlunm++)
				Matrix[row][comlunm] = 0.0;
			row++;
		}
	}

	private int countTreeNode(TreeNode root)
	{
		if(root == null)
			return 0;
		return 1 + countTreeNode(root.left) + countTreeNode(root.right);
	}
}
