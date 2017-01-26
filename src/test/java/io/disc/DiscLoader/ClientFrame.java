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
	
	public ClientFrame(DiscLoader loader) {
		this.loader = loader;
		this.panels = new ArrayList<OpenPanel>();
		this.setSize(1280, 720);
		this.setTitle("Re-bug");
		Container content = this.getContentPane();
		content.applyComponentOrientation(ComponentOrientation.getOrientation(content.getLocale()));
		this.setLayout(new FlowLayout());
		this.panels.add(new OpenPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)));
		this.panels.get(0).setLocation(0, 0);
		this.panels.get(0).setLayout(new BoxLayout(this.panels.get(0), BoxLayout.X_AXIS));
		this.add(this.panels.get(0));
		this.panels.get(0).validate();
		this.setVisible(true);
		this.updateContents();
	}
	
	public void updateContents() {
		OpenPanel panel = this.panels.get(0);
		ClientButton client = new ClientButton("DiscLoader", panel, this.loader);
		client.setLocation(0, panel.getHeight() + 220);
		panel.setSize(400, 720);
		panel.add(client);
		panel.revalidate();
		panel.repaint();
	}
}
