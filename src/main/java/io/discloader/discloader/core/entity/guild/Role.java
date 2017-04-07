package io.discloader.discloader.core.entity.guild;

import java.math.BigDecimal;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.RoleJSON;

/**
 * Represents a role in a guild on Discord
 * 
 * @author Perry Berman
 */
public class Role implements IRole {
	
	/**
	 * The role's Snowflake ID.
	 */
	public final String id;
	/**
	 * The role's name
	 */
	private String name;
	
	/**
	 * The 53bit permissions integer for the role
	 */
	private int permissions;
	
	/**
	 * The color users with this role should display as
	 */
	private int color;
	
	/**
	 * The role's position in the role hiarchy
	 */
	private int position;
	
	/**
	 * Whether or not online users are displayed seperately
	 */
	private boolean hoist;
	
	/**
	 * Something to do with bots
	 */
	private final boolean managed;
	
	/**
	 * Can everyone mention this role
	 */
	private boolean mentionable;
	
	/**
	 * The {@link Guild} the role belongs to.
	 */
	private final IGuild guild;
	
	/**
	 * The current instance of DiscLoader
	 */
	private final DiscLoader loader;
	
	/**
	 * Creates a new role object
	 * 
	 * @param guild The guild the role belongs to
	 * @param role The role's data
	 */
	public Role(IGuild guild, RoleJSON role) {
		this.guild = guild;
		loader = guild.getLoader();
		id = role.id;
		name = role.name;
		color = new BigDecimal(role.color).intValue();
		permissions = new BigDecimal(role.permissions).intValue();
		position = new BigDecimal(role.position).intValue();
		hoist = role.hoist;
		mentionable = role.mentionable;
		managed = role.managed;
	}
	
	@Override
	public int getColor() {
		return color;
	}
	
	@Override
	public IGuild getGuild() {
		return guild;
	}
	
	@Override
	public long getID() {
		return SnowflakeUtil.parse(id);
	}
	
	@Override
	public DiscLoader getLoader() {
		return loader;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public Permission getPermissions() {
		return new Permission(this, permissions);
	}
	
	@Override
	public int getPosition() {
		return position;
	}
	
	@Override
	public boolean isHoisted() {
		return hoist;
	}
	
	/**
	 * @return the managed
	 */
	@Override
	public boolean isManaged() {
		return managed;
	}
	
	/**
	 * @return the mentionable
	 */
	@Override
	public boolean isMentionable() {
		return mentionable;
	}
	
	/**
	 * @param color the color to set
	 */
	@Override
	public void setColor(int color) {
		this.color = color;
	}
	
	/**
	 * @param hoist the hoist to set
	 */
	@Override
	public void setHoist(boolean hoist) {
		this.hoist = hoist;
	}
	
	/**
	 * @param mentionable the mentionable to set
	 */
	@Override
	public void setMentionable(boolean mentionable) {
		this.mentionable = mentionable;
	}
	
	/**
	 * @param name the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @param permissions the permissions to set
	 */
	@Override
	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}
	
	/**
	 * @param position the position to set
	 */
	@Override
	public void setPosition(int position) {
		this.position = position;
	}
	
	@Override
	public String toMention() {
		return String.format("<@&%s>", id);
	}
	
}
