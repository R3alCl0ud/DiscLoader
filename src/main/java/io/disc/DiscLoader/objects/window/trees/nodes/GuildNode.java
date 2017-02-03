package io.disc.DiscLoader.objects.window.trees.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildChannel;
import io.disc.DiscLoader.objects.structures.GuildMember;
import io.disc.DiscLoader.objects.window.trees.ChannelTree;
import io.disc.DiscLoader.objects.window.trees.MemberTree;

/**
 * @author Perry Berman
 *
 */

public class GuildNode extends DefaultMutableTreeNode {

	private DefaultMutableTreeNode id; 
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode icon;
	private MemberNode owner;
	private MemberTree members;
	private ChannelTree channels;
	
	private static final long serialVersionUID = -5501035825628371530L;

	
	public GuildNode(String content, Guild data) {
		super(content);
		this.add(this.id = createNode("id: " + data.id));
		this.add(this.name = createNode("name: " + data.name));
		if (data.icon != null) this.add(this.icon = createNode("icon: " + data.icon));
		else this.add(this.icon = createNode("icon: null"));
		this.add(this.owner = new MemberNode("owner", data.owner));
		this.add(this.members = new MemberTree("members"));
		for (GuildMember member : data.members.values()) {
			this.members.createMemberNode(member);
		}
		
		this.channels = new ChannelTree("channels");
	}
	
	public void updateNode(Guild data) {
		
	}
	
	public void updateMemberNode(GuildMember member) {
		this.members.updateMemberNode(member);
	}
	
	public void updateChannelNode(GuildChannel channel) {
		
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
}
