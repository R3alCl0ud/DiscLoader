package io.discloader.discloader.entity.guild;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.IEmoji;

/**
 * @author Perry Berman
 */
public interface IGuildEmoji extends IEmoji {

	CompletableFuture<IGuildEmoji> delete();

	IGuild getGuild();

	Map<String, IRole> getRoles();

	boolean requiresColons();

	CompletableFuture<IGuildEmoji> setName(String name);

	CompletableFuture<IGuildEmoji> setRoles(IRole... roles);

}
