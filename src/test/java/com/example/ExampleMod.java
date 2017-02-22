package com.example;

import io.discloader.discloader.common.discovery.Mod.EventHandler;

import com.example.common.ObjectHandler;

import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.Mod.Instance;
import io.discloader.discloader.common.events.DiscPreInitEvent;

/**
 * @author Perry Berman
 *
 */
@Mod(desc = ExampleMod.DESC, modid = ExampleMod.MODID, name = ExampleMod.NAME, version = ExampleMod.VERSION)
public class ExampleMod {

	public static final String DESC = "Example Mod for testing";
	public static final String MODID = "examplemod";
	public static final String NAME = "ExampleMod";
	public static final String VERSION = "1.0.0";

	@Instance(MODID)
	public static ExampleMod instance;
	
	public ExampleMod() {
		
	}
	
	@EventHandler
	public void preInit(DiscPreInitEvent e) {
		ObjectHandler.register();
	}

}