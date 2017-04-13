package io.discloader.discloader.common.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import io.discloader.discloader.common.event.channel.ChannelCreateEvent;
import io.discloader.discloader.common.event.channel.ChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.ChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelCreateEvent;
import io.discloader.discloader.common.event.channel.GuildChannelDeleteEvent;
import io.discloader.discloader.common.event.channel.GuildChannelUpdateEvent;
import io.discloader.discloader.common.event.channel.TypingStartEvent;
import io.discloader.discloader.common.event.guild.GuildBanAddEvent;
import io.discloader.discloader.common.event.guild.GuildBanRemoveEvent;
import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.common.event.guild.GuildDeleteEvent;
import io.discloader.discloader.common.event.guild.GuildSyncEvent;
import io.discloader.discloader.common.event.guild.GuildUpdateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleDeleteEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GuildMessageUpdateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageDeleteEvent;
import io.discloader.discloader.common.event.message.PrivateMessageUpdateEvent;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;
import io.discloader.discloader.entity.guild.IGuild;

public class EventManager {

	private final List<IEventListener> handlers = new ArrayList<>();
	private final List<Consumer<DLEvent>> consumers = new ArrayList<>();
	private final Map<Consumer<DLEvent>, Function<IGuild, Boolean>> guildTest = new HashMap<>();

	public void addEventHandler(IEventListener e) {
		handlers.add(e);
	}

	public void removeEventHandler(IEventListener e) {
		handlers.remove(e);
	}

	public void emit(DLEvent event) {
		for (Consumer<DLEvent> consumer : consumers)
			if (event instanceof GuildMembersChunkEvent && guildTest.get(consumer) != null) {
				boolean b = guildTest.get(consumer).apply(((GuildMembersChunkEvent) event).guild);
				System.out.println(b);
				if (b) {
					consumer.accept(event);
					guildTest.remove(consumer);
					// consumers.remove(consumer);
				}
			} else {
				consumer.accept(event);
			}

		for (IEventListener handler : handlers) {
			if (event instanceof DLPreInitEvent) {
				handler.PreInit((DLPreInitEvent) event);
			} else if (event instanceof RawEvent) {
				handler.RawPacket((RawEvent) event);
			} else if (event instanceof ReadyEvent) {
				handler.Ready((ReadyEvent) event);
			} else if (event instanceof GuildChannelCreateEvent) {
				handler.GuildChannelCreate((GuildChannelCreateEvent) event);
			} else if (event instanceof ChannelCreateEvent) {
				handler.ChannelCreate((ChannelCreateEvent) event);
			} else if (event instanceof GuildChannelDeleteEvent) {
				handler.GuildChannelDelete((GuildChannelDeleteEvent) event);
			} else if (event instanceof ChannelDeleteEvent) {
				handler.ChannelDelete((ChannelDeleteEvent) event);
			} else if (event instanceof GuildChannelUpdateEvent) {
				handler.GuildChannelUpdate((GuildChannelUpdateEvent) event);
			} else if (event instanceof ChannelUpdateEvent) {
				handler.ChannelUpdate((ChannelUpdateEvent) event);
			} else if (event instanceof GuildCreateEvent) {
				handler.GuildCreate((GuildCreateEvent) event);
			} else if (event instanceof GuildDeleteEvent) {
				handler.GuildDelete((GuildDeleteEvent) event);
			} else if (event instanceof GuildUpdateEvent) {
				handler.GuildUpdate((GuildUpdateEvent) event);
			} else if (event instanceof GuildBanAddEvent) {
				handler.GuildBanAdd((GuildBanAddEvent) event);
			} else if (event instanceof GuildBanRemoveEvent) {
				handler.GuildBanRemove((GuildBanRemoveEvent) event);
			} else if (event instanceof GuildMemberAddEvent) {
				handler.GuildMemberAdd((GuildMemberAddEvent) event);
			} else if (event instanceof GuildEmojiCreateEvent) {
				handler.GuildEmojiCreate((GuildEmojiCreateEvent) event);
			} else if (event instanceof GuildEmojiDeleteEvent) {
				handler.GuildEmojiDelete((GuildEmojiDeleteEvent) event);
			} else if (event instanceof GuildEmojiUpdateEvent) {
				handler.GuildEmojiUpdate((GuildEmojiUpdateEvent) event);
			} else if (event instanceof GuildMemberRemoveEvent) {
				handler.GuildMemberRemove((GuildMemberRemoveEvent) event);
			} else if (event instanceof GuildMemberUpdateEvent) {
				handler.GuildMemberUpdate((GuildMemberUpdateEvent) event);
			} else if (event instanceof GuildMembersChunkEvent) {
				handler.GuildMembersChunk((GuildMembersChunkEvent) event);
			} else if (event instanceof GuildRoleCreateEvent) {
				handler.GuildRoleCreate((GuildRoleCreateEvent) event);
			} else if (event instanceof GuildRoleDeleteEvent) {
				handler.GuildRoleDelete((GuildRoleDeleteEvent) event);
			} else if (event instanceof GuildRoleUpdateEvent) {
				handler.GuildRoleUpdate((GuildRoleUpdateEvent) event);
			} else if (event instanceof GuildSyncEvent) {
				handler.GuildSync((GuildSyncEvent) event);
			} else if (event instanceof GuildMessageCreateEvent) {
				handler.GuildMessageCreate((GuildMessageCreateEvent) event);
			} else if (event instanceof GuildMessageDeleteEvent) {
				handler.GuildMessageDelete((GuildMessageDeleteEvent) event);
			} else if (event instanceof GuildMessageUpdateEvent) {
				handler.GuildMessageUpdate((GuildMessageUpdateEvent) event);
			} else if (event instanceof PrivateMessageCreateEvent) {
				handler.PrivateMessageCreate((PrivateMessageCreateEvent) event);
			} else if (event instanceof GuildMessageDeleteEvent) {
				handler.PrivateMessageDelete((PrivateMessageDeleteEvent) event);
			} else if (event instanceof PrivateMessageUpdateEvent) {
				handler.PrivateMessageUpdate((PrivateMessageUpdateEvent) event);
			} else if (event instanceof MessageCreateEvent) {
				handler.MessageCreate((MessageCreateEvent) event);
			} else if (event instanceof MessageDeleteEvent) {
				handler.MessageDelete((MessageDeleteEvent) event);
			} else if (event instanceof MessageUpdateEvent) {
				handler.MessageUpdate((MessageUpdateEvent) event);
			} else if (event instanceof TypingStartEvent) {
				handler.TypingStart((TypingStartEvent) event);
			} else if (event instanceof UserUpdateEvent) {
				handler.UserUpdate((UserUpdateEvent) event);
			} else if (event instanceof VoiceStateUpdateEvent) {
				handler.VoiceStateUpdate((VoiceStateUpdateEvent) event);
			}
		}
	}

	public void onEvent(Consumer<DLEvent> consumer) {
		consumers.add(consumer);
	}

	public void onceEvent(Consumer<DLEvent> consumer) {
		onEvent(consumer);
		consumer.andThen(after -> {
			System.out.println(consumers.indexOf(consumer));
			consumers.remove(consumer);
		});
	}

	public void onceEvent(Consumer<DLEvent> consumer, Function<IGuild, Boolean> checker) {
		// consumers.add(consumer);
		onceEvent(consumer);
		guildTest.put(consumer, checker);
	}

	public List<IEventListener> getHandlers() {
		return handlers;
	}

}
