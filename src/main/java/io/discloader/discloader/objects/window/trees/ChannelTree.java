/**
 * 
 */
package io.discloader.discloader.objects.window.trees;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.discloader.discloader.objects.structures.Channel;
import io.discloader.discloader.objects.structures.PrivateChannel;
import io.discloader.discloader.objects.structures.TextChannel;
import io.discloader.discloader.objects.structures.VoiceChannel;
import io.discloader.discloader.objects.window.trees.nodes.channels.ChannelNode;
import io.discloader.discloader.objects.window.trees.nodes.channels.TextNode;
import io.discloader.discloader.objects.window.trees.nodes.channels.VoiceNode;

/**
 * @author Perry Berman
 *
 */
public class ChannelTree extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 6112668340783473419L;
	private DefaultMutableTreeNode length;
	private HashMap<String, ChannelNode> channels;

	/**
	 * @param userObject
	 */
	public ChannelTree(Object userObject) {
		super(userObject);
		this.channels = new HashMap<String, ChannelNode>();
		this.add(this.length = createNode("length: " + this.channels.size()));
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

	/**
	 * @param data
	 */
	public void createChannelNode(Channel data) {
		ChannelNode channel = new ChannelNode(data.id, data);
		this.channels.put(data.id, channel);
		this.add(channel);
		this.length.setUserObject("length: " + this.channels.size());
	}


	public void createVoiceNode(VoiceChannel data, boolean guild) {
		VoiceNode voice = new VoiceNode(data.id, data, guild);
	}

	public void createTextNode(TextChannel data, boolean guild) {
		TextNode text = new TextNode(data.id, data, guild);
		this.channels.put(data.id, text);
		this.add(text);
		this.length.setUserObject("length: " + this.channels.size());
	}

	/**
	 * @param channel
	 */
	public void createPrivateNode(PrivateChannel channel) {

	}
}
