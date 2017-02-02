/**
 * 
 */
package io.disc.DiscLoader.objects.window.trees;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.User;
import io.disc.DiscLoader.objects.window.trees.nodes.UserNode;

/**
 * @author Perry Berman
 *
 */
public class UserTree extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6710691349739462130L;
	public HashMap<String, UserNode> users;
	private DefaultMutableTreeNode length;
	/**
	 * 
	 * 
	 */
	public UserTree() {
		
	}

	/**
	 * @param userObject
	 */
	public UserTree(Object userObject) {
		super(userObject);
		this.users = new HashMap<String, UserNode>();
		this.add(this.length = this.createNode("length: " + this.users.size()));
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public UserTree(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
	
	public void createUserNode(User data) {
		UserNode user = new UserNode(data.id, data);
		this.users.put(data.id, user);
		this.length.setUserObject("length: " + this.users.size());
		this.add(user);
	}
	
	public void removeUserNode(User data) {
		this.remove(this.users.get(data.id));
		this.users.remove(data.id);
		this.length.setUserObject("length: " + this.users.size());
	}
	
	public void updateUserNode(User data) {
		this.users.get(data.id).updateNode(data);
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

}
