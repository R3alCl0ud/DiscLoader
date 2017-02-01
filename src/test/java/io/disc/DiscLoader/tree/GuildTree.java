package io.disc.DiscLoader.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildTree extends DefaultMutableTreeNode {

	private DefaultMutableTreeNode id; 
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode owner;
	private DefaultMutableTreeNode icon;
	private DefaultMutableTreeNode members;
	private DefaultMutableTreeNode channels;
	
	private static final long serialVersionUID = -5501035825628371530L;

	public HashMap<String, MemberTree> memberNodes;
	public HashMap<String, MemberTree> channelNodes;
	
	public GuildTree(String content, Guild data) {
		super(content);
		
		this.memberNodes = new HashMap<String, MemberTree>();
	}
	
	public void updateNode(Guild data) {
		
	}
}
