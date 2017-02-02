/**
 * 
 */
package io.disc.DiscLoader.objects.window.trees.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.GuildMember;
import io.disc.DiscLoader.objects.structures.Presence;

/**
 * @author Perry Berman
 *
 */
public class MemberNode extends DefaultMutableTreeNode {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5081434304255547926L;

	private DefaultMutableTreeNode id; 
	private DefaultMutableTreeNode user;
	private DefaultMutableTreeNode nick;
	private PresenceNode presence;
	private DefaultMutableTreeNode roles;

	
	public MemberNode() {
		
	}

	/**
	 * @param userObject
	 */
	public MemberNode(Object userObject, GuildMember data) {
		super(userObject);
		this.add(this.id = this.createNode("id: " + data.id));
		this.add(this.nick = this.createNode("nick: " + data.nick));
		this.add(this.user = new UserNode("user", data.user));
		Presence status = data.getPresence();
		if (status != null) this.add(this.presence = new PresenceNode("presence", status));
//		this.add(this.roles = this.createNode("bot: " + data.bot));
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public MemberNode(Object userObject, boolean allowsChildren) {
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
		this.add(this.user = new UserNode("user", data.user));
		Presence status = data.getPresence();
		if (status != null) this.add(this.presence = new PresenceNode("presence", status));
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
}
