/**
 * 
 */
package io.discloader.discloader.client.renderer.panel;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;

import io.discloader.discloader.client.renderer.nodes.UserNode;
import io.discloader.discloader.client.renderer.trees.ChannelTree;
import io.discloader.discloader.client.renderer.trees.GuildTree;
import io.discloader.discloader.client.renderer.trees.UserTree;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.structures.Guild;
import io.discloader.discloader.common.structures.User;
import io.discloader.discloader.common.structures.channels.Channel;
import io.discloader.discloader.common.structures.channels.PrivateChannel;
import io.discloader.discloader.common.structures.channels.TextChannel;
import io.discloader.discloader.common.structures.channels.VoiceChannel;

import javax.swing.event.*;

/**
 * @author Perry Berman
 *
 */
public class WindowPanel extends JPanel implements TreeSelectionListener {
	private static final long serialVersionUID = 185799926365274718L;

	public final DiscLoader loader;

	public final UserNode user;
	public final UserTree users;
	public final ChannelTree channels;
	public final GuildTree guilds;
	public final DefaultMutableTreeNode rootNode;
	public final JTree root;
	public final JScrollPane treeView;
	public TreePath path;

	/**
	 * @param loader
	 *            The {@link DiscLoader} instance that started the process
	 */
	public WindowPanel(DiscLoader loader) {
		super(new GridLayout(1, 0));
		this.loader = loader;
		this.rootNode = this.createNode("DiscLoader");
		this.rootNode.add(this.user = new UserNode("User", null));
		this.rootNode.add(this.users = new UserTree("Users"));
		this.rootNode.add(this.channels = new ChannelTree("Channels"));
		this.rootNode.add(this.guilds = new GuildTree("Guilds"));
		this.root = new JTree(this.rootNode);
		this.root.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		this.root.addTreeSelectionListener(this);
		this.treeView = new JScrollPane(this.root);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		this.add(splitPane);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		this.path = e.getPath();
	}

	public void load() {
		this.user.updateNode(this.loader.user);
		for (Channel channel : this.loader.channels.values()) {
			switch (channel.type) {
			case "dm":
				this.channels.createPrivateNode((PrivateChannel) channel);
				break;
			case "groupDM":
				this.channels.createChannelNode(channel);
				break;
			case "text":
				this.channels.createTextNode((TextChannel) channel, true);
				break;
			case "voice":
				this.channels.createVoiceNode((VoiceChannel) channel, true);
				break;
			default:
				break;
			}
		}
		for (Guild guild : this.loader.guilds.values()) {
			this.guilds.createGuildNode(guild);
		}
		for (User data : this.loader.users.values()) {
			this.users.createUserNode(data);
		}
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

}
