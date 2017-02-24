package io.discloader.guimod.gui.list;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

/**
 * @author Perry Berman
 *
 */
public class CellRenderer extends JLabel implements ListCellRenderer<Object> {

	private static final long serialVersionUID = -7445408041582446652L;

	public CellRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.
	 * JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index,
			boolean isSelected, boolean cellHasFocus) {

		setText(value.toString());

		Color background, foreground;
		Border border;
		JList.DropLocation dropLocation = list.getDropLocation();
		if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index) {
			border = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0x2C2F33));
			background = Color.BLUE;
			foreground = Color.WHITE;
			// check if this cell is selected
		} else if (isSelected) {
			border = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0x99AAB5));
			background = new Color(0x2C2F33);
			foreground = new Color(0x99AAB5);
			// unselected, and not the DnD drop location
		} else {
			border = BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0x2C2F33));
			background = new Color(0x2C2F33);
			foreground = new Color(0x99AAB5);
		}

		setBorder(border);
		setBackground(background);
		setForeground(foreground);

		return this;
	}

}
