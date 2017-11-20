package io.discloader.guimod;

import java.awt.Dimension;
import java.awt.Toolkit;

import io.discloader.discloader.common.DLOptions;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.Mod.Instance;
import io.discloader.guimod.gui.event.GUIEvents;

@Mod(desc = GUIMod.DESC, modid = GUIMod.MODID, name = GUIMod.NAME, version = GUIMod.VERSION, author = GUIMod.AUTHOR)
public class GUIMod {

	public static final String DESC = "A custom GUI for DiscLoader";
	public static final String MODID = "guimod";
	public static final String NAME = "GUI Mod";
	public static final String VERSION = "1.0.0";
	public static final String AUTHOR = "R3alCl0ud";
	public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	@Instance(MODID)
	public static GUIMod instance;

	public GUIMod() {
		DiscLoader.getDiscLoader().addEventListener(new GUIEvents());
	}

	public static void main(String... args) {
		DLOptions options = new DLOptions("mfa.0-m9z_HyPdDdACfLKwC1YX_X1E55EJaWrSa5-fYQzbVklhAya5LHCx98EtcfLpYewamwhpz3NaIGfKH--F7g", "c!");
		DiscLoader loader = new DiscLoader(options);
		loader.addEventListener(new GUIEvents());
		loader.login();
	}

}
