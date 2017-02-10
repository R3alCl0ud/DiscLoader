/**
 * 
 */
package io.disc.discloader.objects.window.trees;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.discloader.objects.structures.Guild;
import io.disc.discloader.objects.window.trees.nodes.GuildNode;

/**
 * @author Perry Berman
 *
 */
public class GuildTree extends DefaultMutableTreeNode {

	public HashMap<String, GuildNode> guilds;
	private DefaultMutableTreeNode length;

	private static final long serialVersionUID = 8662315363268991611L;

	public GuildTree(Object userObject) {
		super(userObject);
		this.guilds = new HashMap<String, GuildNode>();
		this.add(this.length = this.createNode("length: " + this.guilds.size()));
	}

	public void createGuildNode(Guild data) {
		GuildNode user = new GuildNode(data.id, data);
		this.guilds.put(data.id, user);
		this.length.setUserObject("length: " + this.guilds.size());
		this.add(user);
	}

	public void removeGuildNode(Guild data) {
		this.remove(this.guilds.get(data.id));
		this.guilds.remove(data.id);
		this.length.setUserObject("length: " + this.guilds.size());
	}

	public void updateGuildNode(Guild data) {
		this.guilds.get(data.id).updateNode(data);
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}
}
