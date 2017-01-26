package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.loader.ModHandler;;

public class Mod {
	public String modid;
	public String version;
	public String desc;
	
	public Mod(ModHandler mh) {
		mh.beginLoader(null);
	}
}
