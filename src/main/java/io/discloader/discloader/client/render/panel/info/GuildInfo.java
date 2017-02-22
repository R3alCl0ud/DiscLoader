package io.discloader.discloader.client.render.panel.info;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GuildInfo extends JPanel {

	private static final long serialVersionUID = 8299016955635658254L;

	public final JLabel id;
//	public final JLabel avatar;
	
	public GuildInfo() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.add(this.id = new JLabel("ID: "));
		this.id.setForeground(new Color(0xFFFFFF));
		this.add(Box.createHorizontalGlue());
		this.setBackground(new Color(0x2C2F33));
	}

}
