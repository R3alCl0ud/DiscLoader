/**
 * 
 */
package io.disc.DiscLoader.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.GuildMember;

/**
 * @author Perry Berman
 *
 */
public class MemberNodes extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 4204684450462184938L;
	public HashMap<String, MemberTree> members;
	@SuppressWarnings("unused")
	private DefaultMutableTreeNode length;
	

	/**
	 * @param userObject
	 */
	public MemberNodes(Object userObject) {
		super(userObject);
		this.members = new HashMap<String, MemberTree>();
		this.add(this.length = createNode("length: " + this.members.size())); 
	}
	
	public void createMemberNode(GuildMember data) {
		MemberTree member = new MemberTree(data.id, data);
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
