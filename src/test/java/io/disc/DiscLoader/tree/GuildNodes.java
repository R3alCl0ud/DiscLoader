/**
 * 
 */
package io.disc.DiscLoader.tree;

import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.DiscLoader.objects.structures.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildNodes extends DefaultMutableTreeNode {

	public HashMap<String, GuildTree> guilds;
	private DefaultMutableTreeNode length;

	private static final long serialVersionUID = 8662315363268991611L;

	public GuildNodes(Object userObject) {
		super(userObject);
		this.guilds = new HashMap<String, GuildTree>();
		this.add(this.length = this.createNode("length: " + this.guilds.size()));
	}

	public void createGuildNode(Guild data) {
		GuildTree user = new GuildTree(data.id, data);
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
