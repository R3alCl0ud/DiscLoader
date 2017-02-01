/**
 * 
 */
package io.disc.DiscLoader.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.User;

/**
 * @author Perry Berman
 *
 */
public class UserNodes extends DefaultMutableTreeNode {

	public HashMap<String, UserTree> users;
	private DefaultMutableTreeNode length;
	/**
	 * 
	 * 
	 */
	public UserNodes() {
		
	}

	/**
	 * @param userObject
	 */
	public UserNodes(Object userObject) {
		super(userObject);
		this.users = new HashMap<String, UserTree>();
		this.add(this.length = this.createNode("" + this.users.size()));
		this.length.setUserObject("" + this.users.size());
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public UserNodes(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}
	
	public void createUserNode(User data) {
		UserTree user = new UserTree(data.id, data);
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
