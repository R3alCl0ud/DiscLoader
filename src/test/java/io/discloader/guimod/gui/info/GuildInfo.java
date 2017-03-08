package io.discloader.guimod.gui.info;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;

import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.util.DLUtil;

public class GuildInfo<T> extends AbstractInfo<T> {

	private static final long serialVersionUID = 8299016955635658254L;

	public final JLabel id;
	public final JLabel name;
	public final JLabel icon;
	public final JLabel region;
	
	public GuildInfo() {
		super();
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(this.icon = new JLabel());
		this.add(this.name = new JLabel("Name: "));
		this.add(this.id = new JLabel("ID: "));
		this.add(this.region = new JLabel("Region: "));
		this.add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, (int) DLUtil.screenSize.getHeight())));
		this.id.setForeground(new Color(0xFFFFFF));
		this.name.setForeground(new Color(0xFFFFFF));

	}
	
	@Override
	public void update(Object object) {
		Guild guild = (Guild) object;
		this.id.setText(String.format("ID: %s", guild.id));
		this.name.setText(String.format("Name: %s", guild.name));
		this.region.setText(String.format("Region: %s", guild.voiceRegion.id));
	}

}
