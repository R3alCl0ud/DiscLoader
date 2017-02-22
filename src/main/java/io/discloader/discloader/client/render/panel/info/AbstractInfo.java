package io.discloader.discloader.client.render.panel.info;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public abstract class AbstractInfo<T extends Object> extends JPanel {

	private static final long serialVersionUID = -7208230161857207623L;

	public AbstractInfo() {
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.validate();
	}
	
	public void update(Object object) {
		return;
	}
}
