package xyz.r3alb0t.help;

import io.discloader.discloader.common.discovery.Mod.EventHandler;
import io.discloader.discloader.common.discovery.Mod;
import io.discloader.discloader.common.discovery.Mod.Instance;
import io.discloader.discloader.common.events.DiscPreInitEvent;
import xyz.r3alb0t.help.common.ObjectHandler;

/**
 * @author Perry Berman
 *
 */
@Mod(desc = HelpMod.DESC, modid = HelpMod.MODID, name = HelpMod.NAME, version = HelpMod.VERSION)
public class HelpMod {

	public static final String DESC = "Help Mod";
	public static final String MODID = "helpmod";
	public static final String NAME = "HelpMod";
	public static final String VERSION = "0.0.0";

	@Instance(MODID)
	public static HelpMod instance;
	
	public HelpMod() {
		
	}
	
	@EventHandler
	public void preInit(DiscPreInitEvent e) {
		ObjectHandler.register();
	}

	@EventHandler
	public void raw(String raw) {
		System.out.println(raw);
	}

}