package io.discloader.discloader.entity.sendable;

import java.io.File;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.core.entity.RichEmbed;

/**
 * @author Perry Berman
 */
public class SendableMessage {

	public final String content;

	public final RichEmbed embed;

	public final Attachment attachment;

	public transient File file;

	public transient Resource resource;

	public SendableMessage(String content, RichEmbed embed, Attachment attachment, File file) {
		this.content = content;
		this.embed = embed;
		this.attachment = attachment;
		this.file = file;
	}

	public SendableMessage(String content, RichEmbed embed, Attachment attachment, Resource resource) {
		this.content = content;
		this.embed = embed;
		this.attachment = attachment;
		this.resource = resource;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
