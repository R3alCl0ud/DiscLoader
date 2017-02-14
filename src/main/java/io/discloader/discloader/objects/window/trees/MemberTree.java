/**
 * 
 */
package io.discloader.discloader.objects.window.trees;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.discloader.discloader.objects.structures.GuildMember;
import io.discloader.discloader.objects.window.trees.nodes.MemberNode;

/**
 * @author Perry Berman
 *
 */
public class MemberTree extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 4204684450462184938L;
	public HashMap<String, MemberNode> members;
	@SuppressWarnings("unused")
	private DefaultMutableTreeNode length;

	/**
	 * @param userObject
	 */
	public MemberTree(Object userObject) {
		super(userObject);
		this.members = new HashMap<String, MemberNode>();
		this.add(this.length = createNode("length: " + this.members.size()));
	}

	public void createMemberNode(GuildMember data) {
		MemberNode member = new MemberNode(data.id, data);
		this.members.put(data.id, member);
		this.add(member);
	}

	public void removeMemberNode(GuildMember data) {
		this.remove(this.members.get(data.id));
		this.members.remove(data.id);
	}

	public void updateMemberNode(GuildMember data) {

	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

}
