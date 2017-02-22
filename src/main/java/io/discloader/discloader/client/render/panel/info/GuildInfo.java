package io.discloader.discloader.client.render.panel.info;

import java.awt.Color;

import javax.swing.JLabel;

import io.discloader.discloader.entity.Guild;

public class GuildInfo<T> extends AbstractInfo<T> {

	private static final long serialVersionUID = 8299016955635658254L;

	public final JLabel id;
	public final JLabel name;
	public final JLabel icon;
	
	public GuildInfo() {
		super();
		this.add(this.icon = new JLabel());
		this.add(this.name = new JLabel("Name: "));
		this.add(this.id = new JLabel("ID: "));
		
		this.id.setForeground(new Color(0xFFFFFF));
		this.name.setForeground(new Color(0xFFFFFF));

	}
	
	@Override
	public void update(Object object) {
		Guild guild = (Guild) object;
		this.id.setText(String.format("ID: %s", guild.id));
		this.name.setText(String.format("Name: %s", guild.name));
	}

}
