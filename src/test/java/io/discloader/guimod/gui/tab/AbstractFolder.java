package io.discloader.guimod.gui.tab;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.discloader.guimod.gui.info.AbstractInfo;
import io.discloader.guimod.gui.list.AbstractList;
import io.discloader.discloader.common.DiscLoader;

public abstract class AbstractFolder<T, V extends AbstractList<T>, S extends AbstractInfo<T>> extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = -335604613355001641L;
	public final DiscLoader loader;
	public S info;
	public V list;

	public AbstractFolder(DiscLoader loader) {
		this.loader = loader;
		this.list = this.createList();
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.list.folders.addListSelectionListener(this);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.list);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.info = this.createInfo());
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		return;
	}
	
	public V createList() {
		return null;
	}
	
	public S createInfo() {
		return null;
	}
}
