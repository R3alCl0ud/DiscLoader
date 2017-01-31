package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import io.disc.DiscLoader.events.eventHandler;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildChannel;
import io.disc.DiscLoader.objects.structures.GuildMember;
import io.disc.DiscLoader.objects.structures.User;

/**
 * @author Perry Berman
 *
 */
public class ClientTree extends JPanel implements TreeSelectionListener {

	private static final long serialVersionUID = -1699355010437939560L;

	private DiscLoader loader;
	public DefaultMutableTreeNode user;
	public DefaultMutableTreeNode users;
	public DefaultMutableTreeNode channels;
	public DefaultMutableTreeNode guilds;

	public ClientTree(DiscLoader loader) {
		super(new GridLayout(1, 0));
		this.loader = loader;
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("DiscLoader");
		createNodes(top);
		JTree tree = new JTree(top);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// Listen for when the selection changes.
		tree.addTreeSelectionListener(this);
		JScrollPane treeView = new JScrollPane(tree);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(treeView);
		add(splitPane);
	}

	public void createNodes(DefaultMutableTreeNode top) {
		this.user = null;
		this.users = null;
		this.channels = null;
		this.guilds = null;
		this.user = new DefaultMutableTreeNode("User");
		this.users = new DefaultMutableTreeNode("Users");
		this.channels = new DefaultMutableTreeNode("Channels");
		this.guilds = new DefaultMutableTreeNode("Guilds");
		top.add(user);
		top.add(users);
		top.add(channels);
		top.add(guilds);
	}

	
	public void ready() {
		DefaultMutableTreeNode channel = null;
		for (Channel chan : this.loader.channels.values()) {
			channel = createChannelNode(chan, true);
			this.channels.add(channel);
		}
		this.user.add(createNode("id: " + this.loader.user.id));
		this.user.add(createNode("username: " + this.loader.user.username));
		this.user.add(createNode("discriminator: " + this.loader.user.discriminator));
		System.out.println(this.loader.user.username);
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {

	}

	public DefaultMutableTreeNode createChannelNode(Channel channel, boolean guild) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(channel.id);
		DefaultMutableTreeNode prop = null;
		prop = createNode("id: " + channel.id);
		node.add(prop);
		prop = createNode("name: " + channel.name);
		node.add(prop);
		prop = createNode("type: " + channel.type);
		node.add(prop);
		if (channel.type.equalsIgnoreCase("text")) {
			GuildChannel chan = (GuildChannel) channel;
			prop = createNode("topic: " + chan.topic);
			node.add(prop);
			prop = createNode("Members");
			for (GuildMember mem : chan.getMembers().values()) {
				prop.add(createMember(mem));
			}
			node.add(prop);
			if (guild) {
				node.add(createGuildNode(chan.guild, true));
			}
		} 

		return node;
	}
	
	public DefaultMutableTreeNode createGuildNode(Guild guild, boolean nested) {
		DefaultMutableTreeNode node = nested ? createNode("guild: " + guild.id) : createNode(guild.id);
		node.add(createNode("id: " + guild.id));
		node.add(createNode("name: " + guild.name));
		node.add(createNode("icon: " + guild.icon));
		DefaultMutableTreeNode prop = createNode("members");
		for (GuildMember member : guild.members.values()) {
			prop.add(createMember(member));
		}
		node.add(prop);
		prop = createNode("channels");
		for (Channel chan : guild.channels.values()) {
			prop.add(createChannelNode(chan, false));
		}
		return node;
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
	
	public DefaultMutableTreeNode createMember(GuildMember member) {
		DefaultMutableTreeNode mem = createNode(member.id);

		return mem;
	}
	
	public DefaultMutableTreeNode createUser(User user) {
		DefaultMutableTreeNode node = createNode(user.id);
		node.add(createNode("id: " + user.id));
		node.add(createNode("username: " + user.username));
		node.add(createNode("discriminator: " + user.discriminator));
		return node;
	}
	
	
	
}
