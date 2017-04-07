package io.discloader.discloader.entity.guild;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.Permission;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.IPermission;
import io.discloader.discloader.entity.util.ISnowflake;

/**
 * @author Perry Berman
 *
 */
public interface IRole extends ISnowflake {
	IGuild getGuild();
	
	/**
	 * @return the color
	 */
	int getColor();
	
	/**
	 * Returns the current instance of {@link DiscLoader}.
	 * 
	 * @return the current instance of {@link DiscLoader}, most likely the instance of {@link DiscLoader} that instantiated this
	 *         {@link Role}.
	 */
	DiscLoader getLoader();
	
	/**
	 * Gets the role's name
	 * 
	 * @return the name of the role
	 */
	String getName();
	
	/**
	 * Creates a new {@link Permission} object containing the role's permissions.
	 * 
	 * @return the role's permissions
	 */
	IPermission getPermissions();
	
	/**
	 * @return the position of the role.
	 */
	int getPosition();
	
	/**
	 * Checks if {@link GuildMember members} with this role are shown seperately from online users.
	 * 
	 * @return {@code true} if this role is hoisted, {@code false} otherwise.
	 */
	boolean isHoisted();
	
	boolean isManaged();
	
	boolean isMentionable();
	
	void setColor(int color);
	
	void setHoist(boolean hoist);
	
	void setMentionable(boolean mentionable);
	
	void setName(String name);
	
	void setPermissions(int permissions);
	
	void setPosition(int position);
	
	/**
	 * @return A string that is in the correct format for mentioning this role in a {@link Message}
	 */
	String toMention();
	
	@Override
	String toString();
}
