/**
 * 
 */
package io.disc.DiscLoader.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.PrivateChannel;
import io.disc.DiscLoader.objects.structures.TextChannel;
import io.disc.DiscLoader.objects.structures.VoiceChannel;
import io.disc.DiscLoader.tree.channels.ChannelTree;
import io.disc.DiscLoader.tree.channels.TextTree;
import io.disc.DiscLoader.tree.channels.VoiceTree;

/**
 * @author Perry Berman
 *
 */
public class ChannelNodes extends DefaultMutableTreeNode {


	private static final long serialVersionUID = 6112668340783473419L;
	private DefaultMutableTreeNode length;
	private HashMap<String, ChannelTree> channels;
	
	/**
	 * @param userObject
	 */
	public ChannelNodes(Object userObject) {
		super(userObject);
		this.channels = new HashMap<String, ChannelTree>();
		this.add(this.length = createNode("length: " + this.channels.size()));
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

	/**
	 * @param channel
	 */
	public void createChannelNode(Channel data) {
		ChannelTree channel = new ChannelTree(data.id, data);
		this.channels.put(data.id, channel);
		this.add(channel);
		this.length.setUserObject("length: " + this.channels.size());
	}

	/**
	 * @param channel
	 */
	public void createVoiceNode(VoiceChannel data, boolean guild) {
		VoiceTree voice = new VoiceTree(data.id, data, guild);
	}
	
	/**
	 * @param channel
	 */
	public void createTextNode(TextChannel data, boolean guild) {
		TextTree text = new TextTree(data.id, data, guild);
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
