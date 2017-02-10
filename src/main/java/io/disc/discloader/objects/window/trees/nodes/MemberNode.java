/**
 * 
 */
package io.disc.discloader.objects.window.trees.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.discloader.objects.structures.GuildMember;
import io.disc.discloader.objects.structures.Presence;

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
	private UserNode user;
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
		this.add(this.presence = new PresenceNode("presence", status != null ? status : new Presence()));
		// this.add(this.roles = this.createNode("bot: " + data.bot));
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public MemberNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	/**
	 * @param data
	 */
	public void updateMember(GuildMember data) {
		this.id.setUserObject("id: " + data.id);
		this.nick.setUserObject("nick: " + data.nick);
		this.user.updateNode(data.user);
		Presence status = data.getPresence();
		this.presence.updateNode(status != null ? status : new Presence());
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
}
