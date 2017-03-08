package io.discloader.discloader.entity.message;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.Role;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.user.User;
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
     * The guild the message was sent in. null if message was sent in a private channel.
     */
    public final Guild guild;

    /**
     * Whether or not {@literal @everyone} was mentioned
     */
    public boolean everyone;

    /**
     * Whether or not you were mentioned
     */
    public boolean isMentioned;

    /**
     * A HashMap of mentioned Users. Indexed by {@link User#id}.
     */
    public HashMap<String, User> users;

    /**
     * A HashMap of mentioned Roles. Indexed by {@link Role#id}.
     */
    public HashMap<String, Role> roles;

    public Mentions(Message message, UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
        this.message = message;
        this.loader = this.message.loader;
        this.everyone = mention_everyone;
        this.isMentioned = this.everyone ? true : false;
        this.guild = this.message.guild != null ? this.message.guild : null;
        this.channel = message.channel;
        this.users = new HashMap<String, User>();
        this.roles = new HashMap<String, Role>();
        for (UserJSON data : mentions) {
            this.users.put(data.id, this.loader.addUser(data));
        }
        if (this.guild != null) {
            for (RoleJSON data : mention_roles) {
                this.roles.put(data.id, this.guild.roles.get(data.id));
            }
        }
    }

    public void patch(UserJSON[] mentions, RoleJSON[] mention_roles, boolean mention_everyone) {
        users.clear();
        roles.clear();
        for (UserJSON data : mentions) {
            this.users.put(data.id, this.loader.addUser(data));
        }
        if (this.guild != null) {
            for (RoleJSON data : mention_roles) {
                this.roles.put(data.id, this.guild.roles.get(data.id));
            }
        }
    }

}
