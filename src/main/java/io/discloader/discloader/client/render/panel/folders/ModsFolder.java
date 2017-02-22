package io.discloader.discloader.client.render.panel.folders;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.discloader.discloader.client.render.list.ModList;
import io.discloader.discloader.client.render.panel.info.ModInfo;
import io.discloader.discloader.common.DiscLoader;

public class ModsFolder extends JPanel {

	private static final long serialVersionUID = -920545644404939296L;
	public final DiscLoader loader;
	private final ModInfo info;
	private final ModList list;

	public ModsFolder(DiscLoader loader) {
		this.loader = loader;
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.list = new ModList(this.loader);
		this.list.folders.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (list.folders.getSelectedIndex() != -1) {
					info.update(list.mods.get(list.folders.getSelectedIndex()));
				}
			}

		});
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.list);
		this.add(Box.createRigidArea(new Dimension(50, 0)));
		this.add(this.info = new ModInfo());
	}

}
