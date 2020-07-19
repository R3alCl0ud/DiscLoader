package io.discloader.discloader.entity.sendable;

import java.io.File;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.core.entity.message.embed.RichEmbed;

/**
 * @author Perry Berman
 */
public class SendableMessage {

	public final String content;

	public final RichEmbed embed;

	public final Attachment attachment;

	public transient File file;

	public transient Resource resource;

	public final boolean tts;

	public SendableMessage(String content, boolean tts, RichEmbed embed, Attachment attachment, File file) {
		this.content = content;
		this.embed = embed;
		this.attachment = attachment;
		this.file = file;
		this.tts = tts;
	}

	public SendableMessage(String content, boolean tts, RichEmbed embed, Attachment attachment, Resource resource) {
		this.content = content;
		this.embed = embed;
		this.attachment = attachment;
		this.resource = resource;
		this.tts = tts;
	}

}
