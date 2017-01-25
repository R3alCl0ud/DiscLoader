package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {
	public JPanel panel;
	
	public ClientFrame() {
		this.setSize(1280, 720);
		this.setTitle("Re-bug");
		Container content = this.getContentPane();
		content.applyComponentOrientation(ComponentOrientation.getOrientation(content.getLocale()));
		this.panel = new JPanel();
		this.add(this.panel);
		this.panel.add(new JLabel("Test"));
		this.panel.validate();
		this.setVisible(true);
	}
}
