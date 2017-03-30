package io.discloader.discloader.entity.guild;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.IEmoji;

/**
 * @author Perry Berman
 *
 */
public interface IGuildEmoji extends IEmoji {
	IGuild getGuild();
	
	Map<String, Role> getRoles();
	
	CompletableFuture<IGuildEmoji> delete();
	
}
