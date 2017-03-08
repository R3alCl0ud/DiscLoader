package io.discloader.discloader.entity.guild;

import java.math.BigDecimal;

import io.discloader.discloader.common.DiscLoader;
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
    public String name;
    /**
     * The 53bit permissions integer for the role
     */
    public int permissions;
    /**
     * The color users with this role should display as
     */
    public int color;
    /**
     * The role's position in the role hiarchy
     */
    public int position;
    /**
     * Whether or not online users are displayed seperately
     */
    public boolean hoist;
    /**
     * Something to do with bots
     */
    public final boolean managed;
    /**
     * Can everyone mention this role
     */
    public boolean mentionable;

    /**
     * The {@link Guild} the role belongs to.
     */
    public final Guild guild;

    /**
     * The current instance of DiscLoader
     */
    public final DiscLoader loader;

    /**
     * Creates a new role object
     * 
     * @param guild The guild the role belongs to
     * @param role The role's data
     */
    public Role(Guild guild, RoleJSON role) {
        this.guild = guild;
        this.loader = this.guild.loader;
        this.id = role.id;
        this.name = role.name;
        this.color = new BigDecimal(role.color).intValue();
        this.permissions = new BigDecimal(role.permissions).intValue();
        this.position = new BigDecimal(role.position).intValue();
        this.hoist = role.hoist;
        this.mentionable = role.mentionable;
        this.managed = role.managed;
    }

    private Role(Guild guild, String id, String name, int color, int permissions, int position, boolean hoist, boolean mentionable, boolean managed) {
        this.guild = guild;
        this.loader = this.guild.loader;
        this.id = id;
        this.name = name;
        this.color = color;
        this.permissions = permissions;
        this.position = position;
        this.hoist = hoist;
        this.mentionable = mentionable;
        this.managed = managed;
    }

    public Role clone() {
        return new Role(this.guild, this.id, this.name, this.color, this.permissions, this.position, this.hoist, this.mentionable, this.managed);
    }

    public Role update(RoleJSON role) {
        this.name = role.name;
        this.color = new BigDecimal(role.color).intValue();
        this.permissions = new BigDecimal(role.permissions).intValue();
        this.position = new BigDecimal(role.position).intValue();
        this.hoist = role.hoist;
        this.mentionable = role.mentionable;
        return this;
    }

    public Role update(String name, int color, int permissions, int position, boolean hoist, boolean mentionable) {
        this.name = name;

        this.color = color;

        this.permissions = permissions;

        this.position = position;

        this.hoist = hoist;

        this.mentionable = mentionable;

        return this;
    }

    /**
     * @return A string that is in the correct format for mentioning this role in a {@link Message}
     */
    public String toMention() {
        return String.format("<@&%s>", id);
    }

}
