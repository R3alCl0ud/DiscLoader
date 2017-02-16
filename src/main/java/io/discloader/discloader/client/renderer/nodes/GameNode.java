/**
 * 
 */
package io.discloader.discloader.client.renderer.nodes;

import javax.swing.tree.DefaultMutableTreeNode;

import io.discloader.discloader.common.structures.Game;

/**
 * @author Perry Berman
 *
 */
public class GameNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 8216615118013800271L;
	public DefaultMutableTreeNode name;
	public DefaultMutableTreeNode type;
	public DefaultMutableTreeNode streaming;
	public DefaultMutableTreeNode url;

	/**
	 * @param userObject
	 */
	public GameNode(Object userObject, Game data) {
		super(userObject);
		this.add(this.name = createNode("name: null"));
		this.add(this.type = createNode("type: null"));
		this.add(this.streaming = createNode("streaming: false"));
		this.add(this.url = createNode("url: null"));
		if (data != null)
			this.setup(data);
	}

	public void setup(Game data) {
		if (data == null)
			return;
		if (data.name != null)
			this.name.setUserObject("name: " + data.name);
		this.type.setUserObject("type: " + data.type);
		if (data.streaming == true || data.streaming == false)
			this.streaming.setUserObject("streaming: " + data.streaming);
		if (data.url != null)
			this.url.setUserObject("url: " + data.url);
	}

	public DefaultMutableTreeNode createNode(String content) {
		return new DefaultMutableTreeNode(content);
	}

}
