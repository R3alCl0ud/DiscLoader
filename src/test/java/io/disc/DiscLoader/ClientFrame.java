package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {
	public ClientFrame() {
		this.setSize(1280, 720);
		this.setVisible(true);
		this.setTitle("Re-bug");
		Container content = this.getContentPane();
		content.applyComponentOrientation(ComponentOrientation.getOrientation(content.getLocale()));
	}
}
