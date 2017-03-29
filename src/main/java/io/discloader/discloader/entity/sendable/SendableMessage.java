package io.discloader.discloader.entity.sendable;

import java.io.File;

import io.discloader.discloader.common.entity.RichEmbed;

/**
 * @author Perry Berman
 */
public class SendableMessage {

	public final String content;

	public final RichEmbed embed;

	public final Attachment attachment;

	public transient File file;

	public SendableMessage(String content, RichEmbed embed, Attachment attachment, File file) {
		this.content = content;
		this.embed = embed;
		this.attachment = attachment;
		this.file = file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
