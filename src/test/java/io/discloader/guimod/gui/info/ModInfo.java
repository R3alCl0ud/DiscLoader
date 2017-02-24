package io.discloader.guimod.gui.info;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;

import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.util.Constants;

public class ModInfo<T extends ModContainer> extends AbstractInfo<T> {

	private static final long serialVersionUID = -7096114819622461589L;
	public JLabel modId;
	public JLabel modName;
	public JLabel modIcon;
	public JLabel modVersion;
	public JLabel modDesc;
	public JLabel modAuthor;

	public ModInfo() {
		super();
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(this.modIcon = new JLabel());
		this.add(this.modName = new JLabel("Name: "));
		this.add(this.modId = new JLabel("Id: "));
		this.add(this.modVersion = new JLabel("Version: "));
		this.add(this.modDesc = new JLabel("Description: "));
		this.add(this.modAuthor = new JLabel("Author(s): "));
		this.add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, (int) Constants.screenSize.getHeight())));
		this.modId.setForeground(new Color(0xFFFFFF));
		this.modIcon.setForeground(new Color(0xFFFFFF));
		this.modVersion.setForeground(new Color(0xFFFFFF));
		this.modName.setForeground(new Color(0xFFFFFF));
		this.modDesc.setForeground(new Color(0xFFFFFF));
		this.modAuthor.setForeground(new Color(0xFFFFFF));
		this.validate();
	}

	protected void createRigid() {
		this.add(Box.createRigidArea(new Dimension(400, 0)));
	}

	@Override
	public void update(ModContainer object) {
		ModContainer mc = (ModContainer) object;
		this.modName.setText(String.format("Name: %s", mc.modInfo.name()));
		this.modId.setText(String.format("ID: %s", mc.modInfo.modid()));
		this.modVersion.setText(String.format("Version: %s", mc.modInfo.version()));
		this.modDesc.setText(String.format("Description: %s", mc.modInfo.desc()));
		this.modAuthor.setText(String.format("Author(s): %s", mc.modInfo.author()));
	}

}
