/**
 * 
 */
package io.disc.DiscLoader.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.GuildMember;

/**
 * @author Perry Berman
 *
 */
public class MemberTree extends DefaultMutableTreeNode {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5081434304255547926L;

	private DefaultMutableTreeNode id; 
	private DefaultMutableTreeNode user;
	private DefaultMutableTreeNode nick;
	private DefaultMutableTreeNode presence;
	private DefaultMutableTreeNode roles;

	
	public MemberTree() {
		
	}

	/**
	 * @param userObject
	 */
	public MemberTree(Object userObject, GuildMember data) {
		super(userObject);
		this.add(this.id = this.createNode("id: " + data.id));
		this.add(this.nick = this.createNode("username: " + data.nick));
		this.add(this.user = new UserTree("user", data.user));
//		this.add(this.presence = this.createNode("avatar: " + data.avatar));
//		this.add(this.roles = this.createNode("bot: " + data.bot));
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public MemberTree(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * @param data
	 */
	public void updateMember(GuildMember data) {
		this.removeAllChildren();
		this.add(this.id = this.createNode("id: " + data.id));
		this.add(this.nick = this.createNode("username: " + data.nick));
		this.add(this.user = new UserTree("user", data.user));
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
}
