package io.discloader.discloader.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import io.discloader.discloader.common.DLOptions;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.common.logger.DLLogger;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.common.start.Options;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.entity.voice.VoiceEventAdapter;

public class Main {

	public static final Gson gson = new Gson();

	public static DiscLoader loader;
	public static Options options = new Options();
	public static String token;

	public static IGuild guild = null;
	public static IUser appOwner = null;
	public static String guildName = "" + System.currentTimeMillis();

	public static final Logger logger = DLLogger.getLogger("Test - Main");

	public static void AvoidRateLimits() {
		AvoidRateLimits(250l);
	}

	public static void AvoidRateLimits(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String... args) throws IOException, InterruptedException, ExecutionException {
		System.setProperty("http.agent", "DiscLoader");
		logger.info("Attempting to read config");
		String content = "";
		if (new File("options.json").exists()) {
			Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
			for (Object line : lines)
				content += line;
			options = gson.fromJson(content, Options.class);
			token = options.auth.token;
			logger.info("Finished reading config");
		}
		DLOptions opt = new DLOptions();
		opt.loadMods(options.loadMods);
		opt.setToken(token);
		opt.setPrefix(options.prefix);
		opt.useDefaultCommands(options.defaultCommands);
		opt.setDebug(true);
		loader = new DiscLoader(opt);
		loader.addEventListener(new Listener());
		loader.login().exceptionally(ex -> {
			logger.severe("Test Failed");
			ex.printStackTrace();
			System.exit(1);
			return null;
		});
		CommandRegistry.registerCommand(new CommandRunTest(), "runtest");
		CommandRegistry.registerCommand(new CommandCreateGuild(), "createguild");
		CommandRegistry.registerCommand(new CommandDeleteGuild(), "deleteguild");
	}

	public static void runTest(int testNumber) throws Exception {
		if (guild == null)
			throw new NullPointerException("Test Guild Not Found.");
		switch (testNumber) {
		case 4:
			runVoiceChannelTest();
			break;
		case 3:
			runTextChannelTest();
			break;
		case 2:
			runRestActionTest();
			break;
		case 1:
		default:
			runNormalTest();
			break;
		}
	}

	public static void runNormalTest() throws Exception {
		try {
			AvoidRateLimits();
			runTextChannelTest();
			AvoidRateLimits();
			runVoiceChannelTest();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}

		// loader.addEventListener(new EventListenerAdapter() {
		// @Override
		// public void RawPacket(RawEvent data) {
		// WebSocketFrame frame = data.getFrame();
		// if (data.isGateway() && frame.isTextFrame() &&
		// !frame.getPayloadText().contains("PRESENCE_UPDATE") &&
		// !frame.getPayloadText().contains("GUILD_CREATE") &&
		// !frame.getPayloadText().contains("READY")) {
		// logger.info("[Gateway Frame] " + frame.getPayloadText());
		// } else if (data.isREST() && data.getHttpResponse() != null &&
		// data.getHttpResponse().getBody() != null) {
		// logger.info("[REST Response] " + data.getHttpResponse().getBody());
		// }
		// }
		//
		// @Override
		// public void Ready(ReadyEvent e) {
		// logger.info("Ready");
		// logger.info("In " + e.getLoader().getGuilds().size() + " Guild(s).");
		// CompletableFuture<List<VoiceRegion>> f2 = loader.getVoiceRegions();
		// f2.thenAcceptAsync(regions -> {
		// for (VoiceRegion region : regions) {
		// if (region.isOptimal()) {
		// AvoidRateLimits();
		// CompletableFuture<IGuild> f3 = loader.createGuild("Test Guild", region);
		// f3.exceptionally(ex -> {
		// logger.severe("Test Failed");
		// ex.printStackTrace();
		// System.exit(3);
		// return null;
		// });
		// f3.thenAcceptAsync(guild -> {
		// AvoidRateLimits();
		// CompletableFuture<IChannelCategory> f4 = guild.createCategory("category");
		// f4.exceptionally(ex -> {
		// logger.severe("Test Failed");
		// ex.printStackTrace();
		// System.exit(4);
		// return null;
		// });
		// f4.thenAcceptAsync(category -> {
		// CompletableFuture<Void> tfuture = testTextChannelThings(guild, category);
		// tfuture.thenAcceptAsync(n -> {
		// logger.config("Text Future Completed");
		// });
		// CompletableFuture<Void> vfuture = testVoiceThings(guild, category);
		// vfuture.thenAcceptAsync(n -> {
		// logger.config("Voice Future Completed");
		// });
		// CompletableFuture<Void> future = CompletableFuture.allOf(tfuture, vfuture);
		// future.thenAcceptAsync(v -> {
		// logger.config("Going to attempt to delete the guild in 5 seconds");
		// AvoidRateLimits(5000l);
		// CompletableFuture<IGuild> f17 = guild.delete();
		// f17.exceptionally(ex -> {
		// logger.severe("Test Failed");
		// ex.printStackTrace();
		// System.exit(17);
		// return null;
		// });
		// f17.thenAcceptAsync(g2 -> {
		// logger.info("Testing Completed Successfully!");
		// });
		// });
		// });
		// });
		// break;
		// }
		// }
		// });
		// f2.exceptionally(ex -> {
		// logger.severe("Test Failed");
		// ex.printStackTrace();
		// System.exit(2);
		// return null;
		// });
		// }
		// });
		//
	}

	public static void runRestActionTest() {
		try {
			IGuildTextChannel channel = Main.guild.getTextChannelByName("general");
			if (channel != null) {
				channel.testNewMessageAction("this is some test content").get();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		// loader.addEventListener(new EventListenerAdapter() {
		// public String guildName = "" + System.currentTimeMillis();
		// public IGuild guild = null;
		// public IUser appOwner = null;
		//
		// @Override
		// public void GuildChannelCreate(GuildChannelCreateEvent e) {
		// if (e.getGuild().getID() == guild.getID()) {
		// if (e.getChannel() instanceof IGuildTextChannel) {
		// IGuildTextChannel channel = (IGuildTextChannel) e.getChannel();
		// channel.createInvite().onSuccess(invite -> {
		// logger.warning("Invite code for the test guild: " + invite.getCode());
		// }).onException(ex -> {
		// ex.printStackTrace();
		// }).execute();
		// channel.testNewMessageAction("this is some test content").onSuccess(message
		// -> {
		// logger.info(message.getDisplayedContent());
		// }).onException(ex -> {
		// ex.printStackTrace();
		// }).execute();
		// }
		// }
		// }
		//
		// @Override
		// public void GuildCreate(GuildCreateEvent e) {
		// if (e.getGuild().getName().equals(guildName)) {
		// guild = e.getGuild();
		// IGuildTextChannel channel = guild.getTextChannelByName("general");
		// if (channel != null) {
		// channel.createInvite().onSuccess(invite -> {
		// logger.warning("Invite code for the test guild: " + invite.getCode());
		// try {
		// if (appOwner != null) {
		// appOwner.sendMessage("Testing Area: " +
		// Endpoints.inviteLink(invite.getCode())).get();
		// }
		// } catch (InterruptedException | ExecutionException e1) {
		// e1.printStackTrace();
		// }
		// }).onException(ex -> {
		// ex.printStackTrace();
		// }).execute();
		// channel.testNewMessageAction("this is some test content").onSuccess(message
		// -> {
		// logger.info(message.getDisplayedContent());
		// }).onException(ex -> {
		// ex.printStackTrace();
		// }).execute();
		// }
		// }
		// }
		//
		// @Override
		// public void GuildMessageCreate(GuildMessageCreateEvent e) {
		// if (e.getGuild().getID() == guild.getID() &&
		// e.getMessage().getContent().toLowerCase().startsWith(options.prefix)) {
		// String content =
		// e.getMessage().getContent().substring(options.prefix.length());
		// if (content.toLowerCase().startsWith("done")) {
		// try {
		// IUser author = e.getMessage().getAuthor();
		// guild.delete().get();
		// guild = null;
		// author.sendMessage("Testing complete! Guild successfully deleted.");
		// } catch (InterruptedException | ExecutionException e1) {
		// e.getMessage().getAuthor().sendMessage("Testing failed. :(");
		// e1.printStackTrace();
		// }
		// } else if (content.toLowerCase().startsWith("rename")) {
		// String name = content.toLowerCase().substring(6).trim().replace(" ",
		// "-").replace("#", "");
		// logger.config("New Name: " + name);
		// try {
		// e.getChannel().setName(name).get();
		// } catch (PermissionsException | DiscordException | InterruptedException |
		// ExecutionException e1) {
		// e1.printStackTrace();
		// }
		// }
		// }
		// }
		//
		// @Override
		// public void PrivateMessageCreate(PrivateMessageCreateEvent e) {
		// String content = e.getMessage().getContent();
		// if (appOwner != null && e.getChannel().getRecipient().getID() ==
		// appOwner.getID() && content.toLowerCase().startsWith(options.prefix)) {
		// content = content.substring(options.prefix.length());
		// if (content.toLowerCase().startsWith("runtest") && guild == null) {
		// guildName = "" + System.currentTimeMillis();
		// loader.createGuild(guildName);
		// }
		// }
		// }
		//
		// @Override
		// public void RawPacket(RawEvent data) {
		// WebSocketFrame frame = data.getFrame();
		// if (data.isGateway() && frame.isTextFrame() &&
		// !frame.getPayloadText().contains("PRESENCE_UPDATE") &&
		// !frame.getPayloadText().contains("GUILD_CREATE") &&
		// !frame.getPayloadText().contains("READY")) {
		// // logger.config(frame.getPayloadText());
		// } else if (data.isREST() && data.getHttpResponse() != null &&
		// data.getHttpResponse().getBody() != null) {
		// // logger.config(data.geFtHttpResponse().getBody());
		// }
		// }
		//
		// /**
		// * @param e
		// */
		// @Override
		// public void Ready(ReadyEvent e) {
		// try {
		// appOwner = loader.getSelfUser().getOAuth2Application().get().getOwner();
		// } catch (InterruptedException | ExecutionException e1) {
		// e1.printStackTrace();
		// }
		// loader.createGuild(guildName);
		// }
		// });
		//
		//
	}

	public static boolean createGuild() throws UnauthorizedException, InterruptedException, ExecutionException {
		if (guild == null) {
			guild = loader.createGuild(guildName = "" + System.currentTimeMillis()).get();
			return true;
		}
		return false;
	}

	public static boolean deleteGuild() throws UnauthorizedException, InterruptedException, ExecutionException {
		if (guild != null) {
			guild.delete().get();
			guild = null;
			return true;
		}
		return false;
	}

	public static void runTextChannelTest() throws Exception {
		IGuildTextChannel channel = guild.getDefaultChannel();
		IMessage msg = channel.sendMessage("content", new RichEmbed("embed").addField().setTimestamp(), new File("README.md")).get();
		AvoidRateLimits();
		msg.edit("New Content").get();
	}

	public static void runVoiceChannelTest() throws Exception {
		CompletableFuture<VoiceConnection> future = new CompletableFuture<>();
		IGuildVoiceChannel channel = guild.getVoiceChannelByName("General");
		VoiceConnection voiceconnection = channel.join().get();
		voiceconnection.addListener(new VoiceEventAdapter() {
			@Override
			public void end(AudioTrack track, AudioTrackEndReason endReason) {
				if (endReason == AudioTrackEndReason.LOAD_FAILED) {
					logger.severe("Test Failed\nFailed to load the audio track.");
				} else {
					logger.info("Track Finished Playing");
				}
				CompletableFuture<VoiceConnection> f16 = voiceconnection.disconnect();
				f16.exceptionally(ex -> {
					future.completeExceptionally(ex);
					return null;
				});
				f16.thenAcceptAsync(vc -> {
					logger.config("Disconnected From VC");
					future.complete(vc);
				});
			}
		});
		voiceconnection.play("https://soundcloud.com/r3alcl0ud/guardians-of-animus");
		future.get();
	}

	public static CompletableFuture<Void> testTextChannelThings(IGuild guild, IChannelCategory category) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		AvoidRateLimits();
		CompletableFuture<IGuildTextChannel> f5 = category.createTextChannel("text-channel");
		f5.exceptionally(ex -> {
			logger.severe("Test Failed");
			ex.printStackTrace();
			System.exit(5);
			return null;
		});
		f5.thenAcceptAsync(textchannel -> {
			logger.config("Text Channel Created");
			AvoidRateLimits();
			CompletableFuture<IMessage> f6 = textchannel.sendMessage("content", new RichEmbed("embed").addField().setTimestamp(), new File("README.md"));
			f6.exceptionally(ex -> {
				logger.severe("Test Failed");
				ex.printStackTrace();
				System.exit(6);
				return null;
			});
			f6.thenAcceptAsync(m1 -> {
				logger.config("Message Created");
				AvoidRateLimits();
				CompletableFuture<IMessage> f7 = m1.edit("editted content");
				f7.exceptionally(ex -> {
					logger.severe("Test Failed");
					ex.printStackTrace();
					System.exit(7);
					return null;
				});
				f7.thenAcceptAsync(m2 -> {
					logger.config("Message Edited");
					AvoidRateLimits();
					CompletableFuture<IMessage> f8 = m2.pin();
					f8.exceptionally(ex -> {
						logger.severe("Test Failed");
						ex.printStackTrace();
						System.exit(8);
						return null;
					});
					f8.thenAcceptAsync(m3 -> {
						logger.config("Message Pinned");
						AvoidRateLimits();
						CompletableFuture<IMessage> f9 = m3.unpin();
						f9.exceptionally(ex -> {
							logger.severe("Test Failed");
							ex.printStackTrace();
							System.exit(9);
							return null;
						});
						f9.thenAcceptAsync(m4 -> {
							logger.config("Message Unpinned");
							AvoidRateLimits();
							CompletableFuture<Void> f10 = m4.addReaction("🍠");
							f10.exceptionally(ex -> {
								logger.severe("Test Failed");
								ex.printStackTrace();
								System.exit(10);
								return null;
							});
							f10.thenAcceptAsync(n -> {
								logger.config("Reaction added");
								AvoidRateLimits();
								CompletableFuture<IMessage> f11 = m4.deleteAllReactions();
								f11.exceptionally(ex -> {
									logger.severe("Test Failed");
									ex.printStackTrace();
									System.exit(11);
									return null;
								});
								f11.thenAcceptAsync(m5 -> {
									logger.config("Reaction removed");
									AvoidRateLimits();
									future.complete(null);
								});
							});
						});
					});
				});
			});
		});
		return future;
	}

	public static CompletableFuture<Void> testVoiceThings(IGuild guild, IChannelCategory category) {
		CompletableFuture<Void> future = new CompletableFuture<>();
		CompletableFuture<IGuildVoiceChannel> f13 = category.createVoiceChannel("voice-channel");
		f13.exceptionally(ex -> {
			logger.severe("Test Failed");
			ex.printStackTrace();
			System.exit(13);
			return null;
		});
		f13.thenAcceptAsync(voicechannel -> {
			AvoidRateLimits();
			CompletableFuture<VoiceConnection> f14 = voicechannel.join();
			f14.exceptionally(ex -> {
				logger.severe("Test Failed");
				ex.printStackTrace();
				System.exit(14);
				return null;
			});
			f14.thenAcceptAsync(voiceconnection -> {
				voiceconnection.addListener(new VoiceEventAdapter() {
					@Override
					public void end(AudioTrack track, AudioTrackEndReason endReason) {
						logger.info("Track Finished Playing");
						if (endReason == AudioTrackEndReason.LOAD_FAILED) {
							logger.severe("Test Failed\nFailed to load the audio track.");
							System.exit(15);
							return;
						}
						CompletableFuture<VoiceConnection> f16 = voiceconnection.disconnect();
						f16.exceptionally(ex -> {
							logger.severe("Test Failed");
							ex.printStackTrace();
							System.exit(16);
							return null;
						});
						f16.thenAcceptAsync(vc -> {
							logger.config("Disconnected From VC");
							future.complete(null);
						});
					}
				});
				voiceconnection.play("./audio_test.wav");
			});
		});
		return future;
	}

}
