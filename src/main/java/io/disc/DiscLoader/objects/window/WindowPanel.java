/**
 * 
 */
package io.disc.DiscLoader.objects.window;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.window.trees.UserTree;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.PrivateChannel;
import io.disc.DiscLoader.objects.structures.TextChannel;
import io.disc.DiscLoader.objects.structures.User;
import io.disc.DiscLoader.objects.structures.VoiceChannel;
import io.disc.DiscLoader.objects.window.trees.ChannelTree;
import io.disc.DiscLoader.objects.window.trees.GuildTree;
import io.disc.DiscLoader.objects.window.trees.nodes.UserNode;

/**
 * @author Perry Berman
 *
 */
public class WindowPanel extends JPanel implements TreeSelectionListener {
	private static final long serialVersionUID = 185799926365274718L;

	public DiscLoader loader;
	
	public UserNode user;
	public UserTree users;
	public ChannelTree channels;
	public GuildTree guilds;
	public DefaultMutableTreeNode rootNode;
	public JTree root;
	
	/**
	 * @param loader
	 *            The {@link DiscLoader} instance that started the process
	 */
	public WindowPanel(DiscLoader loader) {
		super(new GridLayout(1, 0));
		this.loader = loader;
		this.rootNode = new DefaultMutableTreeNode("DiscLoader");
		this.root = new JTree(this.rootNode);
		this.root.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.rootNode.add(this.user = new UserNode("User", null));
		this.rootNode.add(this.users = new UserTree("Users"));
		this.rootNode.add(this.channels = new ChannelTree("Channels"));
		this.rootNode.add(this.guilds = new GuildTree("Guilds"));
		this.root.addTreeSelectionListener(this);
		JScrollPane treeView = new JScrollPane(this.root);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		this.add(splitPane);
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {

	}
	
	public void load() {
		this.channels.removeAllChildren();
		this.guilds.removeAllChildren();
		for (Channel channel : this.loader.channels.values()) {
			System.out.println(channel.type);
			switch(channel.type) {
			case "dm":
				this.channels.createPrivateNode((PrivateChannel)channel);
				break;
			case "groupDM":
				this.channels.createChannelNode(channel);
				break;
			case "text":
				System.out.println(channel.name);
				this.channels.createTextNode((TextChannel)channel, true);
				break;
			case "voice":

				this.channels.createVoiceNode((VoiceChannel)channel, true);
				break;
			default:
				break;
			}
		}
		this.user.updateNode(this.loader.user);
		for (Guild guild : this.loader.guilds.values()) {
			this.guilds.createGuildNode(guild);
		}
		for (User data : this.loader.users.values()) {
			this.users.createUserNode(data);
		}
		System.out.println(this.loader.user.username);
	}
	
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

}