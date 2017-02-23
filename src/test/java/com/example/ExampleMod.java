package com.example;

import io.discloader.discloader.common.discovery.Mod.EventHandler;

import com.example.common.ObjectHandler;

import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.Mod.Instance;
import io.discloader.discloader.common.event.DLPreInitEvent;

/**
 * @author Perry Berman
 *
 */
@Mod(desc = ExampleMod.DESC, modid = ExampleMod.MODID, name = ExampleMod.NAME, version = ExampleMod.VERSION, author = ExampleMod.AUTHORS)
public class ExampleMod {

	public static final String DESC = "Example Mod for testing";
	public static final String MODID = "examplemod";
	public static final String NAME = "ExampleMod";
	public static final String VERSION = "1.0.0";
	public static final String AUTHORS = "R3alCl0ud";
	
	@Instance(MODID)
	public static ExampleMod instance;
	
	public ExampleMod() {
		
	}
	
	@EventHandler
	public void preInit(DLPreInitEvent e) {
		ObjectHandler.register();
	}

}