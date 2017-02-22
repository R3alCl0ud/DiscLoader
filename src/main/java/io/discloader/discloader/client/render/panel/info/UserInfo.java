package io.discloader.discloader.client.render.panel.info;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.discloader.discloader.entity.User;

public class UserInfo extends JPanel {

	private static final long serialVersionUID = 8299016955635658254L;

	public final JLabel username;
	public final JLabel id;
	public final JLabel avatar;
	public final JLabel avatarURL;
	public final JLabel avatarHash;
	
	public UserInfo() {
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(this.avatar = new JLabel());
		this.add(this.username = new JLabel("Username: "));
		this.add(this.id = new JLabel("ID: "));
		this.add(this.avatarHash = new JLabel("Avatar: "));
		this.add(this.avatarURL = new JLabel("AvatarURL: "));
		this.add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, 500)));
		this.username.setForeground(new Color(0xFFFFFF));
		this.id.setForeground(new Color(0xFFFFFF));
		this.avatar.setForeground(new Color(0xFFFFFF));
		this.avatarHash.setForeground(new Color(0xFFFFFF));
		this.avatarURL.setForeground(new Color(0xFFFFFF));
	}
	
	public void update(User user) {
		this.username.setText(String.format("Username: %s#%s", user.username, user.discriminator));
		this.id.setText(String.format("ID: %s", user.id));
		this.avatar.setIcon(user.loader.clientRegistry.textureRegistry.getUserIcons().get(user.avatar != null ? user.avatar : user.id).getImageIcon(128, 128));
		this.avatarHash.setText(String.format("Avatar: %s", user.avatar));
		this.avatarURL.setText(String.format("AvatarURL: %s", user.avatarURL));
	}

}
