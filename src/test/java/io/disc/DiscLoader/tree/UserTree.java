/**
 * 
 */
package io.disc.DiscLoader.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.User;

/**
 * @author Perry Berman
 *
 */
public class UserTree extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5066506489252832420L;

	/**
	 * 
	 */
	public UserTree() {
		// TODO Auto-generated constructor stub
	}

	private DefaultMutableTreeNode id; 
	private DefaultMutableTreeNode username;
	private DefaultMutableTreeNode discriminator;
	private DefaultMutableTreeNode avatar;
	private DefaultMutableTreeNode bot;
	
	
	/**
	 * @param userObject
	 */
	public UserTree(Object userObject, User data) {
		super(userObject);
		if (data != null) this.setup(data);
		else {
			this.add(this.id = this.createNode("id: null"));
			this.add(this.username = this.createNode("username: null"));
			this.add(this.discriminator = this.createNode("discriminator: " + data.discriminator));
			this.add(this.avatar = this.createNode("avatar: " + data.avatar));
			this.add(this.bot = this.createNode("bot: " + data.bot));
		}
	}

	public void setup(User data) {
		this.add(this.id = this.createNode("id: " + data.id));
		this.add(this.username = this.createNode("username: " + data.username));
		this.add(this.discriminator = this.createNode("discriminator: " + data.discriminator));
		this.add(this.avatar = this.createNode("avatar: " + data.avatar));
		this.add(this.bot = this.createNode("bot: " + data.bot));
	}
	
	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public UserTree(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public void updateNode(User data) {
		if (data.id != null) this.id.setUserObject("id: " + data.id);
		if (data.username != null) this.username.setUserObject("username: " + data.username);
		if (data.discriminator != null) this.discriminator.setUserObject("discriminator: " + data.discriminator);
		if (data.avatar != null) this.avatar.setUserObject("avatar: " + data.avatar);
		if (data.bot || !data.bot) this.bot.setUserObject("bot: " + data.bot);
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
	
}
