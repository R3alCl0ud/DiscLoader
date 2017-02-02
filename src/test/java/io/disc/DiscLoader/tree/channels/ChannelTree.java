/**
 * 
 */
package io.disc.DiscLoader.tree.channels;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.Channel;

/**
 * @author Perry Berman
 *
 */
public class ChannelTree extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -1915867794359095355L;

	/**
	 * @param userObject
	 */
	public ChannelTree(Object userObject) {
		super(userObject);
		
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public ChannelTree(Object userObject, Channel data) {
		super(userObject);

	}
	
	public void updateNode(Channel data) {
		
	}

}
