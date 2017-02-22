package io.discloader.discloader.client.render.list;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.registry.ModRegistry;

public class ModList extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1567206350246898955L;
	public final DiscLoader loader;
	public final JList<Object> folders;
	public final ArrayList<ModContainer> mods;

	public ModList(DiscLoader loader) {
		this.mods = new ArrayList<ModContainer>();
		JScrollPane pane = new JScrollPane();
		JScrollBar bar = new JScrollBar();
		bar.setUI(new ScrollBar());
		bar.setBackground(new Color(0x23272A));
		bar.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		bar.setForeground(new Color(0xFFFFFF));
		pane.setVerticalScrollBar(bar);
		pane.setBorder(BorderFactory.createLineBorder(new Color(0x99AAB5), 5));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.loader = loader;
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (ModContainer mc : ModRegistry.mods.values()) {
			listModel.addElement(String.format("<html>%s<br>%s<br>%s</html>", mc.modInfo.name(), mc.modInfo.version(), mc.modInfo.desc()));
			this.mods.add(mc);
		}
		this.folders = new JList<Object>(listModel);
		this.folders.setBackground(new Color(0x23272A));
		this.folders.setCellRenderer(new CellRenderer());
		this.folders.setFixedCellHeight(60);
		this.folders.setFixedCellWidth(220);
		this.folders.setBorder(BorderFactory.createEmptyBorder());
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(pane);
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		pane.setViewportView(this.folders);
		this.folders.setMinimumSize(new Dimension(300, 400));
		this.setMaximumSize(new Dimension(300, 1000));
	}

}
