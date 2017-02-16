package io.discloader.discloader.client.renderer.channels;

import javax.swing.tree.DefaultMutableTreeNode;

import io.discloader.discloader.client.renderer.nodes.GuildNode;
import io.discloader.discloader.common.structures.channels.Channel;
import io.discloader.discloader.common.structures.channels.VoiceChannel;

/**
 * @author Perry Berman
 *
 */

@SuppressWarnings("unused")
public class VoiceNode extends ChannelNode {

	private static final long serialVersionUID = -1915867794359095355L;

	private DefaultMutableTreeNode id;
	private DefaultMutableTreeNode name;
	private DefaultMutableTreeNode bitrate;
	private DefaultMutableTreeNode userLimit;
	private DefaultMutableTreeNode guild;

	/**
	 * @param userObject
	 * @param displayGuild
	 */
	public VoiceNode(Object userObject, VoiceChannel data, boolean displayGuild) {
		super(userObject);
		this.id = null;
		this.name = null;
		this.add(this.id = createNode("id: " + data.id));
		this.add(this.name = createNode("name: " + data.name));
		this.add(this.bitrate = createNode("bitrate: " + data.bitrate));
		this.add(this.userLimit = createNode("userLimit: " + data.userLimit));
		if (displayGuild)
			this.add(this.guild = new GuildNode("guild", data.guild));
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

	public void updateNode(VoiceChannel data) {

	}

}
