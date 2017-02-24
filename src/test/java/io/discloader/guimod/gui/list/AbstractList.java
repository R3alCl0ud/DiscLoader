package io.discloader.guimod.gui.list;

import io.discloader.discloader.common.DiscLoader;

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

public abstract class AbstractList<T> extends JPanel {

	private static final long serialVersionUID = 1057817663097474031L;
	public final DiscLoader loader;
	public final JList<Object> folders;
	public final ArrayList<T> items;

	public AbstractList(DiscLoader loader) {
		this.loader = loader;
		this.items = new ArrayList<T>();
		JScrollPane pane = new JScrollPane();
		JScrollBar bar = new JScrollBar();
		bar.setUI(new ScrollBar());
		bar.setBackground(new Color(0x23272A));
		bar.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0, 0), 2));
		bar.setForeground(new Color(0xFFFFFF));
		bar.putClientProperty("JScrollBar.fastWheelScrolling", true);
		pane.setVerticalScrollBar(bar);
		pane.setBorder(BorderFactory.createLineBorder(new Color(0x99AAB5), 5));
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.folders = new JList<Object>(this.createListModel());
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

	public DefaultListModel<Object> createListModel() {
		return new DefaultListModel<Object>();
	}

	public ArrayList<T> createItems() {
		return new ArrayList<T>();
	}
	
}
