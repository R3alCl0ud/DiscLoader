package io.discloader.guimod;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.Mod.EventHandler;
import io.discloader.discloader.common.event.EventAdapter;

@Mod(desc = GUIMod.DESC, modid = GUIMod.MODID, name = GUIMod.NAME, version = GUIMod.VERSION, author = GUIMod.AUTHOR)
public class GUIMod {

	public static final String DESC = "A custom GUI for DiscLoader";
	public static final String MODID = "guimod";
	public static final String NAME = "GUI Mod";
	public static final String VERSION = "1.0.0";
	public static final String AUTHOR = "R3alCl0ud";
	
//	public static final WindowFrame

	public GUIMod() {
		DiscLoader.addEventHandler(new EventAdapter() {
			@Override
			public void raw(String text) {
				System.out.println(text);
			}
			
			@Override
			public void Ready(DiscLoader loader) {
				
			}
		});
	}

	@EventHandler
	public void PreInit() {
		
	}
	
}
