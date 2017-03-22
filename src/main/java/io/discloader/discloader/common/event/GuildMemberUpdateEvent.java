/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;

/**
 * The object passed to the {@literal "PresenceUpdate"} event
 * 
 * @see DiscLoader
 * @see Guild
 * @see GuildMember
 * @author Perry Berman
 * @since 0.0.1_Alpha
 */
public class GuildMemberUpdateEvent extends DLEvent {

    /**
     * The guild the member belongs to.
     */
    public final Guild guild;

    /**
     * The updated member object
     */
    public final GuildMember member;

    /**
     * A copy of the member object from before the member updated
     */
    public final GuildMember oldMember;

    public GuildMemberUpdateEvent(GuildMember member, GuildMember oldMember, Guild guild) {
        super(guild.loader);
        this.guild = guild;
        this.member = member;
        this.oldMember = oldMember;
    }

}
