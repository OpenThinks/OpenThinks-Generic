package org.openthinks.generic.util.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * A recursion template for a recursive tree class
 * 
 * @author Zhang Junlong
 * 
 */
public class TreeRecursion {

	private boolean isDone = false;

	// protected abstract void init();

	// protected abstract void idle();

	// protected abstract void cleanup();

	protected void setDone() {
		isDone = true;
	}

	protected boolean done() {
		return isDone;
	}

	public void run() {

	}

	public void recursion(TreeNode node) {
		System.out.println(node.getName());
		if (node.getChildren() != null) {
			for (TreeNode childNode : node.getChildren())
				recursion(childNode);
		}
	}

	public static void main(String[] args) {
		TreeNode parent = new TreeNode();
		TreeNode child = new TreeNode();
		TreeNode child2 = new TreeNode();

		parent.setName("Parent");
		child.setName("child1");
		child2.setName("Child2");

		List<TreeNode> children = new ArrayList<TreeNode>();
		parent.setChildren(children);

		new TreeRecursion().recursion(parent);

	}
}