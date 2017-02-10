package io.disc.discloader;

import io.disc.discloader.objects.annotations.Mod;
import io.disc.discloader.objects.annotations.eventHandler;
import io.disc.discloader.objects.structures.Message;

/**
 * @author Perry Berman
 *
 */
@Mod(desc = HelpMod.DESC, modid = HelpMod.MODID, version = HelpMod.VERSION)
public class HelpMod {

	public static final String DESC = "Help Mod";
	public static final String MODID = "HelpMod";
	public static final String VERSION = "0.0.0";

	@eventHandler
	public void MessageCreate(Message message) {

	}

	@eventHandler
	public void raw(String raw) {
		System.out.println(raw);
	}

}
