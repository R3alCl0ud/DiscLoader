package io.discloader.discloader.objects.window.trees;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Perry Berman
 *
 */
public class ModTree extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -6448653048807528753L;

	
	
	/**
	 * @param userObject
	 */
	public ModTree(Object userObject) {
		super(userObject);
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public ModTree(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

}
