package io.discloader.guimod.gui.embed;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.embed.EmbedField;

public class FieldList extends JPanel implements ActionListener {

	private static final long serialVersionUID = 7481073360553976933L;

	public ArrayList<FieldBuilder> fields;
	public RichEmbed embed;

	public static String ADD_FIELD = "ADD_FIELD";

	public FieldList(RichEmbed embed) {
		// super(new GridLayout(0, 1));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(new Color(0x2C2F33));
		this.embed = embed;
		fields = new ArrayList<>();
		for (EmbedField f : embed.fields) {
			FieldBuilder fb = new FieldBuilder(f);
			fields.add(fb);
			add(fb);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (ADD_FIELD.equals(e.getActionCommand())) {
			int i = embed.fields.size();
			embed.addField("", "", false);
			FieldBuilder fb = new FieldBuilder(embed.fields.get(i));
			fields.add(fb);
			add(fb);
		}
		revalidate();
		repaint();
	}
}
