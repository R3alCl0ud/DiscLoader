package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {
	public JPanel panel;
	public DiscLoader loader;
	
	public ClientFrame(DiscLoader loader) {
		this.loader = loader;
		this.setSize(1280, 720);
		this.setTitle("Re-bug");
		Container content = this.getContentPane();
		content.applyComponentOrientation(ComponentOrientation.getOrientation(content.getLocale()));
		this.panel = new OpenPanel(new FlowLayout(FlowLayout.LEFT));
		this.panel.setLocation(0, 0);
		this.add(this.panel);
		this.panel.validate();
		this.setVisible(true);
	}
}
