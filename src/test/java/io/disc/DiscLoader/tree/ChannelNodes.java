/**
 * 
 */
package io.disc.DiscLoader.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Perry Berman
 *
 */
public class ChannelNodes extends DefaultMutableTreeNode {


	private static final long serialVersionUID = 6112668340783473419L;
	private DefaultMutableTreeNode length;
	private HashMap<String, ChannelTree> channels;
	
	
	public ChannelNodes() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param userObject
	 */
	public ChannelNodes(Object userObject) {
		super(userObject);
		this.channels = new HashMap<String, ChannelTree>();
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public ChannelNodes(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

}
