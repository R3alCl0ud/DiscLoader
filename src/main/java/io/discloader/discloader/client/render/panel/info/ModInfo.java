package io.discloader.discloader.client.render.panel.info;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.discloader.discloader.common.discovery.ModContainer;

public class ModInfo extends JPanel {

	private static final long serialVersionUID = -7096114819622461589L;
	public JLabel modId;
	public JLabel modName;
	public JLabel modIcon;
	public JLabel modVersion;
	public JLabel modDesc;
	public JLabel modAuthor;
	
	public ModInfo() {
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(this.modIcon = new JLabel());
		this.add(this.modName = new JLabel("Name: "));
		this.add(this.modId = new JLabel("Id: "));
		this.add(this.modVersion = new JLabel("Version: "));
		this.add(this.modDesc = new JLabel("Description: "));
		this.add(this.modAuthor = new JLabel("Author(s): "));
		this.add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, 500)));
		this.modId.setForeground(new Color(0xFFFFFF));
		this.modIcon.setForeground(new Color(0xFFFFFF));
		this.modVersion.setForeground(new Color(0xFFFFFF));
		this.modName.setForeground(new Color(0xFFFFFF));
		this.modDesc.setForeground(new Color(0xFFFFFF));
		this.modAuthor.setForeground(new Color(0xFFFFFF));
		this.validate();
	}

	protected void createRigid() {
		this.add(Box.createRigidArea(new Dimension(400,0)));
	}
	
	public void update(ModContainer mc) {
		this.modName.setText(String.format("Name: %s", mc.modInfo.name()));
		this.modId.setText(String.format("ID: %s", mc.modInfo.modid()));
		this.modVersion.setText(String.format("Version: %s", mc.modInfo.version()));
		this.modDesc.setText(String.format("Description: %s", mc.modInfo.desc()));
		this.modAuthor.setText(String.format("Author(s): %s", mc.modInfo.author()));
	}
	
	
}
