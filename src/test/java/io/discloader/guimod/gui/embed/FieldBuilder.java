package io.discloader.guimod.gui.embed;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.discloader.discloader.client.render.panel.CenterPanel;
import io.discloader.discloader.entity.embed.EmbedField;

public class FieldBuilder extends JPanel {

	private static final long serialVersionUID = 8066068746626620444L;

	private EmbedField field;
	private JLabel name;
	private JLabel value;
	private JCheckBox inline;

	public FieldBuilder(EmbedField field) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(new Color(0x2C2F33));
		this.field = field;
		this.name = new JLabel(String.format("Name: ", field.getName()));
		this.value = new JLabel(String.format("Content: ", field.getValue()));
		this.inline = new JCheckBox("Inline");
		inline.setSelected(field.isInline());
		this.add(new CenterPanel(name));
		this.add(new CenterPanel(value));
		this.add(new CenterPanel(inline));
	}

	public Component add(Component comp) {
		super.add(comp);
		comp.setForeground(new Color(0xFFFFFF));
		return comp;
	}

	public void setInline(boolean inline) {
		field.setInline(inline);
		this.inline.setSelected(inline);
	}

}
