package io.discloader.guimod.gui.info;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import io.discloader.discloader.entity.message.IMentions;

public class MentionInfo extends AbstractInfo<IMentions> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7744796516259542389L;
	private JLabel everyone;
	private JLabel roles;
	private JLabel users;

	public MentionInfo() {
		super();
		setBackground(new Color(0x2C2F33));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		validate();
		add(Box.createRigidArea(new Dimension(0, 40)));
		add(everyone = new JLabel("@everyone: "));
		add(roles = new JLabel("Roles: "));
		add(users = new JLabel("Users: "));
		validate();
	}

	@Override
	public void update(IMentions mentions) {
		everyone.setText(format("@everyone", mentions.mentionedEveryone() ? "true" : "false"));
	}

}
