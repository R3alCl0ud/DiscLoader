package io.discloader.guimod.gui.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.start.Main;
import io.discloader.discloader.network.gateway.packets.SocketPacket;
import io.discloader.guimod.gui.TabbedPanel;
import io.discloader.guimod.gui.tab.PacketsTab;

public class GUIEvents extends EventListenerAdapter {
	
	private static WindowFrame window = Main.window;
	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	private TabbedPanel tabs;
	
	public void RawPacket(RawEvent e) {
		if (e.isGateway() && e.getFrame().isTextFrame()) {
			SocketPacket packet = gson.fromJson(e.getFrame().getPayloadText(), SocketPacket.class);
			PacketsTab.update(packet);
		}
	}
	
	public void PreInit(DLPreInitEvent e) {
		
	}
	
	@Override
	public void Ready(ReadyEvent e) {
		window = Main.window;
		if (window != null) {
			window.remove(WindowFrame.loading);
			window.revalidate();
			window.add(this.setTabs(new TabbedPanel(e.loader)));
			window.revalidate();
			window.repaint();
		}
	}
	
	/**
	 * @return the tabs
	 */
	public TabbedPanel getTabs() {
		return tabs;
	}
	
	/**
	 * @param tabs the tabs to set
	 */
	public TabbedPanel setTabs(TabbedPanel tabs) {
		this.tabs = tabs;
		return tabs;
	}
	
}
