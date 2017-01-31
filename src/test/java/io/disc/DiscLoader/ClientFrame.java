package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.*;

@SuppressWarnings("serial")
public class ClientFrame extends JFrame {
	public DiscLoader loader;
	public List<OpenPanel> panels;
	public ClientTree tree;
	
	public ClientFrame(DiscLoader loader) {
		this.loader = loader;
		this.panels = new ArrayList<OpenPanel>();
		this.setSize(1280, 720);
		this.setTitle("Re-bug");
		this.add(this.tree = new ClientTree(loader));
		this.setVisible(true);
	}
	
}
