package io.discloader.guimod.gui.info;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import io.discloader.discloader.core.entity.user.User;
import io.discloader.guimod.GUIMod;

public class UserInfo<T extends User> extends AbstractInfo<T> {

	private static final long serialVersionUID = 8299016955635658254L;

	public final JLabel username;
	public final JLabel id;
	public final JLabel avatar;
	public final JLabel avatarURL;
	public final JLabel avatarHash;

	public UserInfo() {
		super();
		this.add(Box.createRigidArea(new Dimension(0, 40)));
		this.add(this.avatar = new JLabel());
		this.add(this.username = new JLabel("Username: "));
		this.add(this.id = new JLabel("ID: "));
		this.add(this.avatarHash = new JLabel("Avatar: "));
		this.add(this.avatarURL = new JLabel("AvatarURL: "));
		this.add(new Box.Filler(new Dimension(0, 300), new Dimension(0, 400), new Dimension(0, (int) GUIMod.screenSize.getHeight())));
		this.username.setForeground(new Color(0xFFFFFF));
		this.id.setForeground(new Color(0xFFFFFF));
		this.avatar.setForeground(new Color(0xFFFFFF));
		this.avatarHash.setForeground(new Color(0xFFFFFF));
		this.avatarURL.setForeground(new Color(0xFFFFFF));
	}

	@Override
	public void update(User user) {
		username.setText(String.format("Username: %s#%s", user.getUsername(), user.getDiscriminator()));
		id.setText(String.format("ID: %s", user.getID()));
		avatarHash.setText(String.format("Avatar: %s", user.getAvatar().getHash()));
		try {
			URL avatarUrl = user.getAvatar().toURL();
			ImageIcon icon = new ImageIcon(avatarUrl);
			avatar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(128, 128, Image.SCALE_SMOOTH)));
			avatarURL.setText(String.format("AvatarURL: %s", avatarUrl.toString()));
		} catch (MalformedURLException e) {
			avatar.setIcon(null);
		}
	}

}
