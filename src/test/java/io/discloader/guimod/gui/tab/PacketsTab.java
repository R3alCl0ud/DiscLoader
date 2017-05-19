package io.discloader.guimod.gui.tab;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.discloader.discloader.network.gateway.packets.SocketPacket;
import io.discloader.discloader.util.DLUtil;

public class PacketsTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7800123056493331419L;
	private static HashMap<String, Integer> counts;
	private static HashMap<String, JLabel> labels;
	private static PacketsTab instance;

	public PacketsTab() {
		super();
		instance = this;
		counts = new HashMap<>();
		labels = new HashMap<>();
		this.setBackground(new Color(0x2C2F33));
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}

	public static void update(SocketPacket packet) {
		String t = packet.t;
		if (!counts.containsKey(t) && !labels.containsKey(t)) {
			counts.put(t, 0);
			JLabel l = new JLabel();
			l.setForeground(new Color(0xFFFFFF));
			labels.put(t, l);
			instance.add(l);
		}
		if (t.equals("MESSAGE_REACTION_ADD") ||t.equals("MESSAGE_REACTION_REMOVE")) {
			System.out.println(DLUtil.gson.toJson(packet));
		}
		counts.put(t, counts.get(t) + 1);
		labels.get(t).setText(String.format("%s: %d", t, counts.get(t)));
	}

}
