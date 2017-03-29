package io.discloader.discloader.entity.guild;

import java.math.BigDecimal;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Permission;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.network.json.RoleJSON;

/**
 * Represents a role in a guild on Discord
 * 
 * @author Perry Berman
 */
public class Role {

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
	public final Guild guild;

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
	public Role(Guild guild, RoleJSON role) {
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

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Returns the current instance of {@link DiscLoader}.
	 * 
	 * @return the current instance of {@link DiscLoader}, most likely the
	 *         instance of {@link DiscLoader} that instantiated this
	 *         {@link Role}.
	 */
	public DiscLoader getLoader() {
		return loader;
	}

	/**
	 * Gets the role's name
	 * 
	 * @return the name of the role
	 */
	public String getName() {
		return name;
	}

	/**
	 * Creates a new {@link Permission} object containing the role's
	 * permissions.
	 * 
	 * @return the role's permissions
	 */
	public Permission getPermissions() {
		return new Permission(this, permissions);
	}

	/**
	 * @return the position of the role.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Checks if {@link GuildMember members} with this role are shown seperately
	 * from online users.
	 * 
	 * @return {@code true} if this role is hoisted, {@code false} otherwise.
	 */
	public boolean isHoisted() {
		return hoist;
	}

	/**
	 * @return the managed
	 */
	public boolean isManaged() {
		return managed;
	}

	/**
	 * @return the mentionable
	 */
	public boolean isMentionable() {
		return mentionable;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @param hoist the hoist to set
	 */
	public void setHoist(boolean hoist) {
		this.hoist = hoist;
	}

	/**
	 * @param mentionable the mentionable to set
	 */
	public void setMentionable(boolean mentionable) {
		this.mentionable = mentionable;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param permissions the permissions to set
	 */
	public void setPermissions(int permissions) {
		this.permissions = permissions;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return A string that is in the correct format for mentioning this role
	 *         in a {@link Message}
	 */
	public String toMention() {
		return String.format("<@&%s>", id);
	}

	public String toString() {
		return name;
	}

}
