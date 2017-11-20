package io.discloader.discloader.entity.guild;

import io.discloader.discloader.network.json.VoiceRegionJSON;

public class VoiceRegion {

	public final String id;
	private String name;
	private boolean vip;
	private boolean custom;
	private boolean deprecated;
	private boolean optimal;

	public VoiceRegion(String id) {
		this.id = id;
	}

	public VoiceRegion(VoiceRegionJSON data) {
		this(data.id);
		this.name = data.name;
		this.custom = data.custom;
		this.deprecated = data.deprecated;
		this.optimal = data.optimal;
		this.vip = data.vip;
	}

	public String getID() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	public boolean isCustom() {
		return custom;
	}

	public boolean isVIP() {
		return vip;
	}

	public boolean isOptimal() {
		return optimal;
	}

}
