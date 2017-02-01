package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import io.disc.DiscLoader.events.eventHandler;
import io.disc.DiscLoader.objects.structures.Channel;
import io.disc.DiscLoader.objects.structures.Guild;
import io.disc.DiscLoader.objects.structures.GuildChannel;
import io.disc.DiscLoader.objects.structures.GuildMember;
import io.disc.DiscLoader.objects.structures.Presence;
import io.disc.DiscLoader.objects.structures.TextChannel;
import io.disc.DiscLoader.objects.structures.User;
import io.disc.DiscLoader.objects.structures.VoiceChannel;

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

	public HashMap<String, HashMap<String, DefaultMutableTreeNode>> members;
	
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
		this.user.removeAllChildren();
		this.channels.removeAllChildren();
		this.guilds.removeAllChildren();
		DefaultMutableTreeNode channel = null;
		for (Channel chan : this.loader.channels.values()) {
			channel = createChannelNode(chan, true);
			this.channels.add(channel);
		}
		this.user.add(createNode("id: " + this.loader.user.id));
		this.user.add(createNode("username: " + this.loader.user.username));
		this.user.add(createNode("discriminator: " + this.loader.user.discriminator));
		this.user.add(createNode("avatar: " + this.loader.user.avatar));
		this.user.add(createNode("bot: " + this.loader.user.bot));
		for (Guild guild : this.loader.guilds.values()) {
			this.guilds.add(createGuildNode(guild, false));
		}
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
			TextChannel text = (TextChannel) channel;
			prop = createNode("topic: " + text.topic);
			node.add(prop);
			if (guild) {				
				prop = createGuildNode(text.guild, true);
				node.add(prop);
			}
			prop = createNode("members");
			prop.add(createNode("length: " + text.getMembers().size()));
			for (GuildMember member : text.getMembers().values()) {
				prop.add(createMember(member));
			}
			node.add(prop);
		} else if (channel.type.equalsIgnoreCase("voice")) {
			VoiceChannel voice = (VoiceChannel) channel;
			if (guild) {				
				prop = createGuildNode(voice.guild, true);
				node.add(prop);
			}
		}
		return node;
	}
	
	public DefaultMutableTreeNode createGuildChannelNode(GuildChannel channel, boolean guild) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(channel.id);
		DefaultMutableTreeNode prop = null;
		prop = createNode("id: " + channel.id);
		node.add(prop);
		prop = createNode("name: " + channel.name);
		node.add(prop);
		prop = createNode("type: " + channel.type);
		node.add(prop);
		if (channel.type.equalsIgnoreCase("text")) {
			TextChannel text = (TextChannel) channel;
			prop = createNode("topic: " + text.topic);
		}
		node.add(prop);
		prop = createNode("members");
		prop.add(createNode("length: " + channel.getMembers().size()));
		for (GuildMember member : channel.getMembers().values()) {
			prop.add(createMember(member));
		}
		node.add(prop);
		return node;
	}
	
	public DefaultMutableTreeNode createGuildNode(Guild guild, boolean nested) {
		DefaultMutableTreeNode node = nested ? createNode("guild") : createNode(guild.id);
		node.add(createNode("id: " + guild.id));
		node.add(createNode("name: " + guild.name));
		node.add(createNode("icon: " + guild.icon));
		DefaultMutableTreeNode prop = createNode("members");
		prop.add(createNode("length: " + guild.members.size()));
		for (GuildMember member : guild.members.values()) {
			prop.add(createMember(member));
		}
		node.add(prop);
		prop = createNode("channels");
		prop.add(createNode("length: " + guild.channels.size()));
		for (GuildChannel chan : guild.channels.values()) {
			prop.add(createChannelNode(chan, false));
		}
		node.add(prop);
		return node;
	}
	
	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
	
	public DefaultMutableTreeNode createMember(GuildMember member) {
		DefaultMutableTreeNode node = createNode(member.id);
		DefaultMutableTreeNode prop = null;
		prop = createNode("id: " + member.id);
		node.add(prop);
		prop = createNode("user");
		prop.add(createNode("id: " + member.user.id));
		prop.add(createNode("username: " + member.user.username));
		prop.add(createNode("discriminator: " + member.user.discriminator));
		prop.add(createNode("avatar: " + member.user.avatar));
		prop.add(createNode("bot: " + member.user.bot));
		node.add(prop);
		if (member.nick != null) {
			prop = createNode("nick: " + member.nick);
		}
		Presence status = member.getPresence();
		if (status != null) {
			prop = createNode("status: " + status.status);
			node.add(prop);
			if (status.game != null) {
				prop = createNode("game");
				prop.add(createNode("name: " + status.game.name));
				prop.add(createNode("type: " + status.game.type));
				prop.add(createNode("streaming: " + status.game.streaming));
				prop.add(createNode("url: " + status.game.url));
				node.add(prop);
			}
		}
		return node;
	}
	
	public DefaultMutableTreeNode createUser(User user) {
		DefaultMutableTreeNode node = createNode(user.id);
		node.add(createNode("id: " + user.id));
		node.add(createNode("username: " + user.username));
		node.add(createNode("discriminator: " + user.discriminator));
		node.add(createNode("avatar: " + user.avatar));
		node.add(createNode("bot: " + user.bot));
		return node;
	}
}
