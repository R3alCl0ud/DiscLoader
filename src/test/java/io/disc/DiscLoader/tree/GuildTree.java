package io.disc.DiscLoader.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildChannel;
import io.disc.DiscLoader.objects.structures.GuildMember;

/**
 * @author Perry Berman
 *
 */

public class GuildTree extends DefaultMutableTreeNode {

	private DefaultMutableTreeNode id; 
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode owner;
	private DefaultMutableTreeNode icon;
	private MemberNodes members;
	private ChannelNodes channels;
	
	private static final long serialVersionUID = -5501035825628371530L;

	
	public GuildTree(String content, Guild data) {
		super(content);
		this.add(this.id = createNode("id: " + data.id));
		this.add(this.name = createNode("name: " + data.name));
		if (data.icon != null) this.add(this.icon = createNode("icon: " + data.icon));
		else this.add(this.icon = createNode("icon: null"));
		this.add(this.members = new MemberNodes("members"));
		for (GuildMember member : data.members.values()) {
//			this.members.
		}
		
		this.channels = new ChannelNodes("channels");
	}
	
	public void updateNode(Guild data) {
		
	}
	
	public void updateMemberNode(GuildMember member) {
		
	}
	
	public void updateChannelNode(GuildChannel channel) {
		
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
}
