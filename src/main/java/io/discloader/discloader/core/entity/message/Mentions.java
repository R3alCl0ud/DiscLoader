package io.discloader.discloader.core.entity.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.message.IMentions;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.RoleJSON;
import io.discloader.discloader.network.json.UserJSON;

/**
 * Contains all things mention in a message
 * 
 * @author Perry Berman
 */
public class Mentions implements IMentions {

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;

	/**
	 * The message these mentions apply to
	 */
	public final IMessage message;

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
	public final Map<Long, IUser> users;

	/**
	 * A HashMap of mentioned Roles. Indexed by {@link Role#id}.
	 */
	public final Map<Long, IRole> roles;

	public Mentions(Message<?> message, UserJSON[] mentions, String[] mention_roles, boolean mention_everyone) {
		this.message = message;
		loader = message.loader;
		everyone = mention_everyone;
		guild = message.guild != null ? message.guild : null;
		channel = message.channel;
		users = new HashMap<>();
		roles = new HashMap<>();
		if (mentions != null) {
			for (UserJSON data : mentions) {
				IUser user = EntityRegistry.addUser(data);
				user.setup(data);
				users.put(user.getID(), user);
			}
		}
		if (guild != null && mention_roles != null) {
			for (String id : mention_roles) {
				long idl = SnowflakeUtil.parse(id);
				roles.put(idl, guild.getRoles().get(idl));
			}
		}
	}

	@Override
	public IMessage getMessage() {
		return message;
	}

	public boolean isMentioned() {
		return isMentioned(loader.getSelfUser());
	}

	@Override
	public boolean isMentioned(IGuildMember member) {
		return isMentioned(member.getUser());
	}

	public boolean isMentioned(IRole role) {
		return roles.containsKey(role.getID()) || everyone;
	}

	public boolean isMentioned(IUser user) {
		return users.containsKey(user.getID()) || everyone;
	}

	public boolean mentionedEveryone() {
		return everyone;
	}

	public void patch(UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
		everyone = mention_everyone;
		users.clear();
		roles.clear();
		for (UserJSON data : mentions) {
			IUser user = EntityRegistry.getUserByID(data.id);
			if (user == null)
				user = EntityRegistry.addUser(data);
			users.put(SnowflakeUtil.parse(data.id), user);
		}
		if (guild != null) {
			for (RoleJSON data : mention_roles) {
				long id = SnowflakeUtil.parse(data.id);
				roles.put(id, guild.getRoles().get(id));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.discloader.discloader.entity.message.IMentions#getUsers()
	 */
	@Override
	public List<IUser> getUsers() {
		return new ArrayList<>(users.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.discloader.discloader.entity.message.IMentions#getRoles()
	 */
	@Override
	public List<IRole> getRoles() {
		return new ArrayList<>(roles.values());
	}

	@Override
	public int size() {
		return roles.size() + users.size();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Mentions))
			return false;
		Mentions mentions = (Mentions) object;

		return (this == mentions) || getMessage().equals(mentions.getMessage());
	}

	@Override
	public int hashCode() {
		return getMessage().hashCode();
	}

	@Override
	public boolean isMentioned(IMentionable mentionable) {
		if (mentionable instanceof IUser) {
			return isMentioned((IUser) mentionable);
		} else if (mentionable instanceof IRole) {
			return isMentioned((IRole) mentionable);
		} else if (mentionable instanceof IChannel) {
			return isMentioned((IChannel) mentionable);
		}
		return everyone;
	}

	@Override
	public List<IMentionable> toList() {
		List<IMentionable> mentioned = new ArrayList<>();
		users.forEach((id, user) -> {
			mentioned.add(user);
		});
		roles.forEach((id, role) -> {
			mentioned.add(role);
		});
		return mentioned;
	}

	@Override
	public boolean isMentioned(IChannel channel) {
		return message.getContent().toLowerCase().contains("<#" + channel.getID() + ">");
	}

}
