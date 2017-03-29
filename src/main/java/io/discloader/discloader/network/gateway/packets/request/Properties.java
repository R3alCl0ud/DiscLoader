package io.discloader.discloader.network.gateway.packets.request;

public class Properties {

	public String $os;
	public String $device;
	public String $browser;

	public Properties(String os, String device, String browser) {
		$os = os;
		$device = device;
		$browser = browser;
	}
}
