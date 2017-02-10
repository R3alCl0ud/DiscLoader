/**
 * 
 */
package io.disc.discloader.objects.window.trees.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import io.disc.discloader.objects.structures.Presence;

/**
 * @author Perry Berman
 *
 */
public class PresenceNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4759765872586081712L;
	public GameNode game;
	public DefaultMutableTreeNode status;

	/**
	 * @param userObject
	 */
	public PresenceNode(Object userObject, Presence data) {
		super(userObject);
		this.add(this.status = createNode("status: " + data.status));
		this.add(this.game = new GameNode("game", data.game));
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

	public void updateNode(Presence data) {
		this.status.setUserObject(data.status);
		this.game.setup(data.game);
	}

}
