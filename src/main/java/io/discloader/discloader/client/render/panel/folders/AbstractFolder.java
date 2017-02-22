package io.discloader.discloader.client.render.panel.folders;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionListener;

import io.discloader.discloader.client.render.panel.info.AbstractInfo;
import io.discloader.discloader.common.DiscLoader;

public abstract class AbstractFolder extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = -335604613355001641L;
	public final DiscLoader loader;
	public AbstractInfo info;

	public AbstractFolder(DiscLoader loader) {
		this.loader = loader;
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
	}
	
}
