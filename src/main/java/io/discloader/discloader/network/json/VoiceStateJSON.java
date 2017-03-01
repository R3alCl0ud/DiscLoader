/**
 * 
 */
package io.discloader.discloader.network.json;

/**
 * @author Perry Berman
 *
 */
public class VoiceStateJSON {
	public String guild_id;
	public String channel_id;
	public String user_id;
	public String session_id;
	public boolean deaf;
	public boolean mute;
	public boolean self_deaf;
	public boolean self_mute;
	public boolean suppress;
}
