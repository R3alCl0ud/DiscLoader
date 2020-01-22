package io.discloader.discloader.entity.guild;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IGuildEmoji extends IEmoji {
	
	CompletableFuture<IGuildEmoji> delete();
	
	IGuild getGuild();
	
	Map<Long, IRole> getRoles();
	
	/**
	 * @return The {@link IUser} who created the emoji.
	 */
	IUser getUser();
	
	boolean isAnimated();
	
	boolean isManaged();
	
	boolean requiresColons();
	
	CompletableFuture<IGuildEmoji> setName(String name) throws PermissionsException;
	
	CompletableFuture<IGuildEmoji> setRoles(IRole... roles);
	
}
