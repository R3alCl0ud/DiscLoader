package io.discloader.discloader.core.entity.message;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.json.UserJSON;

/**
 * Contains all things mention in a message
 * 
 * @author Perry Berman
 *
 */
public class Mentions {

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;

	/**
	 * The message these mentions apply to
	 */
	public final Message message;

	/**
	 * The channel the {@link #message} was sent in.
	 */
	public final ITextChannel channel;

	/**
	 * The guild the message was sent in. null if message was sent in a private
	 * channel.
	 */
	public final IGuild guild;

	private boolean everyone;

	/**
	 * A HashMap of mentioned Users. Indexed by {@link User#id}.
	 */
	public final HashMap<String, IUser> users;

	/**
	 * A HashMap of mentioned Roles. Indexed by {@link Role#id}.
	 */
	public final HashMap<String, Role> roles;

	public Mentions(Message message, UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
		this.message = message;
		loader = message.loader;
		everyone = mention_everyone;
		guild = message.guild != null ? message.guild : null;
		channel = message.channel;
		users = new HashMap<>();
		roles = new HashMap<>();
		for (UserJSON data : mentions) {
			IUser user = loader.users.get(data.id);
			if (user == null)
				user = loader.addUser(data);
			users.put(data.id, user);
		}
		if (guild != null) {
			for (RoleJSON data : mention_roles) {
				roles.put(data.id, guild.getRoles().get(data.id));
			}
		}
	}

	public void patch(UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
		everyone = mention_everyone;
		users.clear();
		roles.clear();
		for (UserJSON data : mentions) {
			IUser user = loader.users.get(data.id);
			if (user == null)
				user = loader.addUser(data);
			users.put(data.id, user);
		}
		if (guild != null) {
			for (RoleJSON data : mention_roles) {
				roles.put(data.id, guild.getRoles().get(data.id));
			}
		}
	}

	/**
	 * Whether or not you were mentioned
	 * 
	 * @return true if you were mentioned
	 */
	public boolean isMentioned() {
		return isMentioned(loader.user);
	}

	public boolean isMentioned(User user) {
		boolean mentioned = everyone ? true : users.containsKey(user.getID());
		return mentioned;
	}

	public boolean isMentioned(Role role) {
		return roles.containsKey(role.id);
	}

	/**
	 * Whether or not {@literal @everyone} was mentioned
	 * 
	 * @return true if the {@link #message}'s content contains
	 *         {@literal @everyone}, false otherwise
	 */
	public boolean mentionedEveryone() {
		return everyone;
	}

}
