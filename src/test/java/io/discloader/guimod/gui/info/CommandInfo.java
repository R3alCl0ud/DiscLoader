package io.discloader.guimod.gui.info;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.util.Constants;

public class CommandInfo<T extends Command> extends AbstractInfo<T> {

	private static final long serialVersionUID = -7477439343021525783L;

	private JLabel icon;
	private JLabel unlocalizedName;
	private JLabel id;
	private JLabel usage;
	private JLabel desc;

	public CommandInfo() {
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(this.icon = new JLabel());
		this.add(this.unlocalizedName = new JLabel("Unlocalized Name: "));
		this.add(this.id = new JLabel("id: "));
		this.add(this.usage = new JLabel("Usage: "));
		this.add(this.desc = new JLabel("Description: "));
		this.add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, (int) Constants.screenSize.getHeight())));
	}

	public void update(Command command) {
		this.icon.setIcon(new ImageIcon(command.getIcon().getImage()));
		this.unlocalizedName.setText(String.format("UnlocalizedName: %s", command.getUnlocalizedName()));
		this.id.setText(String.format("id: %s", command.getId()));
		this.usage.setText(String.format("Usage: %s", command.getUsage()));
		this.desc.setText(String.format("Description: %s", command.getDescription()));
	}

}
