package pers.zuhonm.he2;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser_GenerateAccessTree {
		
	private static Map<String, Integer> precedenceMap = new HashMap<String, Integer>();
	/*
	 * define precedence when parsing
	 */
	static {
		precedenceMap.put("(", 4);
		precedenceMap.put("OR", 1);
		precedenceMap.put("AND", 2);
		precedenceMap.put(")", 3);
	}
	
	public TreeNode ParseFormula2Tree(String formula) {
		formula = preprocess(formula);
		String[] formula_token = formula.split("\\s+");
		Stack<TreeNode> opts = new Stack<TreeNode>();
		Stack<TreeNode> nodes = new Stack<TreeNode>();
		for(int i = 0; i < formula_token.length; i++)
		{
			TreeNode node = new TreeNode(formula_token[i]);
			if( isOpt(formula_token[i]) )
			{
				node.isLeaf = false;
				/*
				 * meet ")" we should pop all and make kids until "("
				 */
				if(formula_token[i].equals(")"))
				{
					while(!opts.empty() && opts.peek().attr_gate != "(" && !nodes.empty())
					{
						node = opts.pop();
						if(!nodes.empty()) node.right = nodes.pop();
						if(!nodes.empty())	node.left = nodes.pop();
						nodes.push(node);
						if(opts.peek().attr_gate.equals("("))
						{
							opts.pop();
							break;
						}
					}
				}
				/*
				 * when opts are empty or the fresh opt is higher precedence we push it into opts
				 */
				else if(opts.empty() || precedenceMap.get(opts.peek().attr_gate) < precedenceMap.get(formula_token[i]))
				{
					opts.push(node);
				}
				/*
				 * the fresh opt is in lower or equal precedence we pop and make kids until things change
				 */
				else
				{
					while(!opts.peek().attr_gate.equals("(") && !opts.empty() && !nodes.empty() && precedenceMap.get(formula_token[i]) <= precedenceMap.get(opts.peek().attr_gate))
					{
						TreeNode temp = opts.pop();
						temp.right = nodes.pop();
						if(!nodes.empty())	temp.left = nodes.pop();
						nodes.push(temp);
					}
					opts.push(node);
				}
			}
			/*
			 * not an operator means it's an attribute which presented as leaf node
			 */
			else 
			{
				node.isLeaf = true;
				nodes.push(node);
			}
		}
		/*
		 * eliminate all opts remained in opts
		 */
		while(!opts.empty() && !nodes.empty())
		{
			TreeNode node = opts.pop();
			node.right = nodes.pop();
			if(!nodes.empty()) node.left = nodes.pop();
			nodes.push(node);
		}
		/*
		 * return
		 */
		if(!nodes.empty())
			return nodes.pop();
		else
			return null;
	}
	
	private String preprocess(String formula)
	{
		/*
		 * add blanket on "(" and ")" for splitting correctly into token
		 * in case blankets exceed we will perform formula.split("\\s+") later;
		 */
		Pattern pattern = Pattern.compile("\\(");
		Matcher matcher = pattern.matcher(formula);
		formula = matcher.replaceAll(" $0 ");
		pattern = Pattern.compile("\\)");
		matcher = pattern.matcher(formula);
		formula = matcher.replaceAll(" $0 ");
		return formula;
	}
	
	private boolean isOpt(String a) {
		return (a.equals("AND") || a.equals("OR") || a.equals("(") || a.equals(")"));
	}
	
}
