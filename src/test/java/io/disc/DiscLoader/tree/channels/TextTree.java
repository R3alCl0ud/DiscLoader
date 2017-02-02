/**
 * 
 */
package io.disc.DiscLoader.tree.channels;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.GuildMember;
import io.disc.DiscLoader.objects.structures.TextChannel;
import io.disc.DiscLoader.objects.structures.VoiceChannel;
import io.disc.DiscLoader.tree.GuildTree;
import io.disc.DiscLoader.tree.MemberNodes;

/**
 * @author Perry Berman
 *
 */

@SuppressWarnings("unused")
public class TextTree extends ChannelTree {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1028902587689097554L;
	private DefaultMutableTreeNode id;
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode bitrate;
	private MemberNodes members;
	private GuildTree guild;
	
	

	/**
	 * @param id
	 * @param data
	 * @param guild
	 */
	public TextTree(String userObject, TextChannel data, boolean displayGuild) {
		super(userObject);
		this.add(this.id = createNode("id: " + data.id));
		this.add(this.name = createNode("name: " + data.name));
		this.add(this.bitrate = createNode("topic: " + data.topic));
		this.add(this.members = new MemberNodes("members"));
		for (GuildMember member : data.getMembers().values()) {
			this.members.createMemberNode(member);
		}
		if (displayGuild) this.add(this.guild = new GuildTree("guild", data.guild));
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

	public void updateNode(VoiceChannel data) {
		
	}
}
