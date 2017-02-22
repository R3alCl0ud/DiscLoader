package io.discloader.discloader.client.render.panel.info;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class AbstractInfo extends JPanel {

	private static final long serialVersionUID = -7208230161857207623L;

	public AbstractInfo() {
	}

	public AbstractInfo(LayoutManager layout) {
		super(layout);
	}

	public AbstractInfo(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public AbstractInfo(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

}
