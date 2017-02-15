package io.discloader.discloader.objects.window.trees.nodes.channels;

import javax.swing.tree.DefaultMutableTreeNode;

import io.discloader.discloader.objects.structures.GuildMember;
import io.discloader.discloader.objects.structures.channels.TextChannel;
import io.discloader.discloader.objects.structures.channels.VoiceChannel;
import io.discloader.discloader.objects.window.trees.MemberTree;
import io.discloader.discloader.objects.window.trees.nodes.GuildNode;

/**
 * @author Perry Berman
 *
 */

@SuppressWarnings("unused")
public class TextNode extends ChannelNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1028902587689097554L;
	private DefaultMutableTreeNode id;
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode bitrate;
	private MemberTree members;
	private GuildNode guild;

	/**
	 * @param userObject
	 * @param data
	 * @param displayGuild
	 */
	public TextNode(String userObject, TextChannel data, boolean displayGuild) {
		super(userObject);
		this.add(this.id = createNode("id: " + data.id));
		this.add(this.name = createNode("name: " + data.name));
		this.add(this.bitrate = createNode("topic: " + data.topic));
		this.add(this.members = new MemberTree("members"));
		for (GuildMember member : data.getMembers().values()) {
			this.members.createMemberNode(member);
		}
		if (displayGuild)
			this.add(this.guild = new GuildNode("guild", data.guild));
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

	public void updateNode(VoiceChannel data) {

	}
}
