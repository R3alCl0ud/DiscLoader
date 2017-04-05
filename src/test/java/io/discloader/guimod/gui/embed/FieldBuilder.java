package io.discloader.guimod.gui.embed;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import io.discloader.discloader.client.render.panel.CenterPanel;
import io.discloader.discloader.entity.embed.EmbedField;

public class FieldBuilder extends JPanel {

	private static final long serialVersionUID = 8066068746626620444L;

	private EmbedField field;
	private JTextField name;
	private JTextField value;
	private JLabel nameL;
	private JLabel valueL;
	private JCheckBox inline;
	private Dimension td = new Dimension(100, 20);

	public FieldBuilder(EmbedField field) {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.setBackground(new Color(0x2C2F33));
		this.field = field;
		name = new JTextField();
		value = new JTextField();
		name.setPreferredSize(td);
		name.setMaximumSize(td);
		value.setPreferredSize(td);
		value.setMaximumSize(td);
		nameL = new JLabel("Name");
		nameL.setLabelFor(name);
		valueL = new JLabel("Content");
		valueL.setLabelFor(value);
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
