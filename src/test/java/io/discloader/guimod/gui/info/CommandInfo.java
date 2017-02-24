package io.discloader.guimod.gui.info;

import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.util.Constants;

public class CommandInfo<T extends Command> extends AbstractInfo<T> {

	private static final long serialVersionUID = -7477439343021525783L;
	
	private JLabel icon;
	private final JLabel unlocalizedName;
	private final JLabel id;
	private final JLabel usage;
	private final JLabel desc;
	
	
	public CommandInfo() {
		try {
			this.add(this.icon = new JLabel(new ImageIcon(Constants.MissingTexture.toURL())));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.add(this.unlocalizedName = new JLabel("Unlocalized Name: "));
		this.add(this.id = new JLabel("id: "));
		this.add(this.usage = new JLabel("Usage: "));
		this.add(this.desc = new JLabel("Description: "));
	}

	public void update(Command command) {
		
	}
	
}
