/**
 * 
 */
package io.discloader.discloader.client.render.panel.folders;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.client.render.list.AbstractList;
import io.discloader.discloader.client.render.list.GuildList;
import io.discloader.discloader.client.render.list.UserList;
import io.discloader.discloader.client.render.panel.info.AbstractInfo;
import io.discloader.discloader.client.render.panel.info.GuildInfo;
import io.discloader.discloader.client.render.panel.info.UserInfo;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildFolders<T extends Guild> extends AbstractFolder<T> {

	private static final long serialVersionUID = -1589736360078814907L;

	public GuildFolders(DiscLoader loader) {
		super(loader);
	}
	
	@Override
	public AbstractList<T> createList() {
		return new GuildList<T>(this.loader);
	}

	@Override
	public AbstractInfo<T> createInfo() {
		return new GuildInfo<T>();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (list.folders.getSelectedIndex() != -1) {
			info.update(list.items.get(list.folders.getSelectedIndex()));
		}
	}
}
