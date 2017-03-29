package io.discloader.guimod.gui.embed;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.discloader.discloader.client.render.panel.CenterPanel;
import io.discloader.discloader.common.entity.RichEmbed;

/**
 * @author Perry Berman
 *
 */
public class EmbedBuilder extends JPanel {

	private static final long serialVersionUID = -907310410745925087L;

	public ArrayList<RichEmbed> embeds;

	public JButton addField;
	public FieldList fields;

	public EmbedBuilder() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(new Color(0x2C2F33));
		embeds = new ArrayList<>();
		embeds.add(new RichEmbed());
		this.add(new CenterPanel(new JLabel("RichEmbed Builder", JLabel.CENTER)));
		add(new CenterPanel(fields = new FieldList(embeds.get(0))));
		addField = new JButton("Add new field");
		addField.addActionListener(fields);
		addField.setActionCommand(FieldList.ADD_FIELD);
		add(new CenterPanel(addField));
	}

	public Component add(Component comp) {
		super.add(comp);
		comp.setForeground(new Color(0xFFFFFF));
		return comp;
	}
}
