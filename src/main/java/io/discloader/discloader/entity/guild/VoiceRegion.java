package io.discloader.discloader.entity.guild;

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
