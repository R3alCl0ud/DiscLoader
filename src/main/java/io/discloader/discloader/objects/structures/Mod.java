package io.discloader.discloader.objects.structures;

import io.discloader.discloader.objects.loader.ModHandler;;

public class Mod {
	public String modid;
	public String version;
	public String desc;

	public Mod(ModHandler mh) {
		mh.beginLoader(null);
	}
}
