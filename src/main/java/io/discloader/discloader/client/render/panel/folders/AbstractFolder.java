package io.discloader.discloader.client.render.panel.folders;

import java.awt.LayoutManager;

import javax.swing.JPanel;

public abstract class AbstractFolder extends JPanel {

	public AbstractFolder() {
	}

	public AbstractFolder(LayoutManager layout) {
		super(layout);
	}

	public AbstractFolder(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	public AbstractFolder(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

}
