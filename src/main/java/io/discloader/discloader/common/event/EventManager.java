package io.discloader.discloader.common.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
import io.discloader.discloader.common.event.guild.GuildEvent;
import io.discloader.discloader.common.event.guild.GuildSyncEvent;
import io.discloader.discloader.common.event.guild.GuildUpdateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiCreateEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiDeleteEvent;
import io.discloader.discloader.common.event.guild.emoji.GuildEmojiUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberAddEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.NicknameUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceJoinEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceLeaveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberEvent.VoiceSwitchEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberRemoveEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.event.guild.member.GuildMembersChunkEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleCreateEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleDeleteEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleEvent;
import io.discloader.discloader.common.event.guild.role.GuildRoleUpdateEvent;
import io.discloader.discloader.common.event.message.GroupMessageCreateEvent;
import io.discloader.discloader.common.event.message.GroupMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GroupMessageUpdateEvent;
import io.discloader.discloader.common.event.message.GuildMessageCreateEvent;
import io.discloader.discloader.common.event.message.GuildMessageDeleteEvent;
import io.discloader.discloader.common.event.message.GuildMessageUpdateEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.message.MessageDeleteEvent;
import io.discloader.discloader.common.event.message.MessageReactionAddEvent;
import io.discloader.discloader.common.event.message.MessageReactionRemoveEvent;
import io.discloader.discloader.common.event.message.MessageUpdateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageCreateEvent;
import io.discloader.discloader.common.event.message.PrivateMessageDeleteEvent;
import io.discloader.discloader.common.event.message.PrivateMessageUpdateEvent;
import io.discloader.discloader.common.event.voice.VoiceStateUpdateEvent;

public class EventManager {

	private final List<IEventListener> handlers = new ArrayList<>();
	@SuppressWarnings("rawtypes")
	private final Map<String, List<Consumer>> _listeners = new HashMap<>();
	@SuppressWarnings("rawtypes")
	private final Map<String, List<Consumer>> _onceListeners = new HashMap<>();

	public void addEventHandler(IEventListener e) {
		handlers.add(e);
	}

	public void removeEventHandler(IEventListener e) {
		handlers.remove(e);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void emit(DLEvent event) {
		if (_listeners.containsKey(event.getClass().getSimpleName())) {
			List<Consumer> cons = _listeners.get(event.getClass().getSimpleName());
			for (int i = cons.size() - 1; i >= 0; i--) {
				cons.get(i).accept(event);
			}
		}
		if (_onceListeners.containsKey(event.getClass().getSimpleName())) {
			List<Consumer> cons = _onceListeners.get(event.getClass().getSimpleName());
			for (int i = cons.size() - 1; i >= 0; i--) {
				cons.remove(i).accept(event);
			}
		}
		for (int i = handlers.size() - 1; i >= 0; i--) {
			try {
				IEventListener handler = handlers.get(i);
				if (event instanceof MessageUpdateEvent) {
					if (event instanceof GuildMessageUpdateEvent) {
						handler.GuildMessageUpdate((GuildMessageUpdateEvent) event);
					} else if (event instanceof GroupMessageUpdateEvent) {
						handler.GroupMessageUpdate((GroupMessageUpdateEvent) event);
					} else if (event instanceof PrivateMessageUpdateEvent) {
						handler.PrivateMessageUpdate((PrivateMessageUpdateEvent) event);
					} else {
						handler.MessageUpdate((MessageUpdateEvent) event);
					}
				} else if (event instanceof MessageCreateEvent) {
					if (event instanceof GuildMessageCreateEvent) {
						handler.GuildMessageCreate((GuildMessageCreateEvent) event);
					} else if (event instanceof GroupMessageCreateEvent) {
						handler.GroupMessageCreate((GroupMessageCreateEvent) event);
					} else if (event instanceof PrivateMessageCreateEvent) {
						handler.PrivateMessageCreate((PrivateMessageCreateEvent) event);
					} else {
						handler.MessageCreate((MessageCreateEvent) event);
					}
				} else if (event instanceof MessageDeleteEvent) {
					if (event instanceof GuildMessageDeleteEvent) {
						handler.GuildMessageDelete((GuildMessageDeleteEvent) event);
					} else if (event instanceof PrivateMessageDeleteEvent) {
						handler.PrivateMessageDelete((PrivateMessageDeleteEvent) event);
					} else if (event instanceof GroupMessageDeleteEvent) {
						handler.GroupMessageDelete((GroupMessageDeleteEvent) event);
					} else {
						handler.MessageDelete((MessageDeleteEvent) event);
					}
				} else if (event instanceof GuildEvent) {
					handler.GuildEvent((GuildEvent) event);
					if (event instanceof GuildCreateEvent) {
						handler.GuildCreate((GuildCreateEvent) event);
					} else if (event instanceof GuildDeleteEvent) {
						handler.GuildDelete((GuildDeleteEvent) event);
					} else if (event instanceof GuildUpdateEvent) {
						handler.GuildUpdate((GuildUpdateEvent) event);
					} else if (event instanceof GuildRoleEvent) {
						handler.GuildRoleEvent((GuildRoleEvent) event);
						if (event instanceof GuildRoleCreateEvent) {
							handler.GuildRoleCreate((GuildRoleCreateEvent) event);
						} else if (event instanceof GuildRoleDeleteEvent) {
							handler.GuildRoleDelete((GuildRoleDeleteEvent) event);
						} else if (event instanceof GuildRoleUpdateEvent) {
							handler.GuildRoleUpdate((GuildRoleUpdateEvent) event);
						}
					} else if (event instanceof GuildMemberEvent) {
						GuildMemberEvent gme = (GuildMemberEvent) event;
						handler.GuildMemberEvent(gme);
						if (gme instanceof VoiceJoinEvent) {
							handler.GuildMemberVoiceJoin((VoiceJoinEvent) gme);
						} else if (gme instanceof VoiceLeaveEvent) {
							handler.GuildMemberVoiceLeave((VoiceLeaveEvent) gme);
						} else if (gme instanceof VoiceSwitchEvent) {
							handler.GuildMemberVoiceSwitch((VoiceSwitchEvent) gme);
						} else if (gme instanceof NicknameUpdateEvent) {
							handler.GuildMemberNicknameUpdated((NicknameUpdateEvent) gme);
						}
					}
				} else if (event instanceof MessageReactionAddEvent) {
					handler.MessageReactionAdd((MessageReactionAddEvent) event);
				} else if (event instanceof MessageReactionRemoveEvent) {
					handler.MessageReactionRemove((MessageReactionRemoveEvent) event);
				} else if (event instanceof DLPreInitEvent) {
					handler.PreInit((DLPreInitEvent) event);
				} else if (event instanceof RawEvent) {
					handler.RawPacket((RawEvent) event);
				} else if (event instanceof ReadyEvent) {
					handler.Ready((ReadyEvent) event);
				} else if (event instanceof ReconnectEvent) {
					handler.Reconnect((ReconnectEvent) event);
				} else if (event instanceof DisconnectEvent) {
					handler.Disconnected((DisconnectEvent) event);
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
				} else if (event instanceof GuildSyncEvent) {
					handler.GuildSync((GuildSyncEvent) event);
				} else if (event instanceof TypingStartEvent) {
					handler.TypingStart((TypingStartEvent) event);
				} else if (event instanceof UserUpdateEvent) {
					handler.UserUpdate((UserUpdateEvent) event);
				} else if (event instanceof VoiceStateUpdateEvent) {
					handler.VoiceStateUpdate((VoiceStateUpdateEvent) event);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public <T extends DLEvent> void onEvent(Class<T> cls, Consumer<T> consumer) {
		if (_listeners.get(cls.getSimpleName()) == null) {
			_listeners.put(cls.getSimpleName(), new ArrayList<>());
		}
		_listeners.get(cls.getSimpleName()).add(consumer);
	}

	public <T extends DLEvent> void onceEvent(Class<T> cls, Consumer<T> consumer) {
		if (_onceListeners.get(cls.getSimpleName()) == null) {
			_onceListeners.put(cls.getSimpleName(), new ArrayList<>());
		}
		_onceListeners.get(cls.getSimpleName()).add(consumer);
	}

	public List<IEventListener> getHandlers() {
		return handlers;
	}

	@SuppressWarnings("unchecked")
	public <T> List<Consumer<DLEvent>> getConsumers(Class<T> cls) {
		List<Consumer<DLEvent>> listeners = new ArrayList<>();
		for (Consumer<DLEvent> consumer : _listeners.get(cls.getSimpleName()))
			listeners.add(consumer);
		for (Consumer<DLEvent> consumer : _onceListeners.get(cls.getSimpleName()))
			listeners.add(consumer);
		return listeners;
	}

}
