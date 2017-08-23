package io.discloader.guimod.gui;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.guimod.gui.embed.EmbedBuilder;
import io.discloader.guimod.gui.tab.ChannelTab;
import io.discloader.guimod.gui.tab.CommandsTab;
import io.discloader.guimod.gui.tab.GuildFolders;
import io.discloader.guimod.gui.tab.ModsFolder;
import io.discloader.guimod.gui.tab.PacketsTab;
import io.discloader.guimod.gui.tab.UserFolders;

public class TabbedPanel extends JPanel {

	private static final long serialVersionUID = -6974630780146823721L;
	public final DiscLoader loader;
	private static JTabbedPane tabbedPane;

	public TabbedPanel(DiscLoader loader) {
		super(new GridLayout(1, 1));
		this.loader = loader;
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Mods", new ModsFolder<ModContainer>(this.loader));
		tabbedPane.addTab(LanguageRegistry.getLocalized("gui.tabcommands.name"), new CommandsTab<Command>(this.loader));
		tabbedPane.addTab("Users", resizeImageIcon(createImageIcon("texture.gui.icons.missing-icon", "Missing Icon"), 16, 16), new UserFolders<User>(this.loader), "Users");
		tabbedPane.addTab("Channels", new ChannelTab<Channel>(loader));
		tabbedPane.addTab("Guilds", resizeImageIcon(createImageIcon("texture.gui.icons.missing-icon", "Missing Icon"), 16, 16), new GuildFolders<Guild>(this.loader), "Guilds");
		tabbedPane.addTab("Embed Builder", new EmbedBuilder());
		tabbedPane.addTab("Packet Counts", new PacketsTab());
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		this.add(tabbedPane);
		this.validate();
	}

	public static void addTab(String name, Component comp) {
		tabbedPane.addTab(name, comp);
	}

	public static void removeTab(Component tab) {
		tabbedPane.remove(tab);
	}

	protected ImageIcon createImageIcon(String path, String description) {
		URL imgURL = ClassLoader.getSystemResource(String.format("assets/discloader/%s.png", path.replace('.', '/')));
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	protected ImageIcon resizeImageIcon(ImageIcon imageIcon, int width, int height) {
		return new ImageIcon(imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}

}
