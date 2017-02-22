package io.discloader.discloader.entity;

import java.io.File;

/**
 * @author Perry Berman
 *
 */
public class SendableMessage {

	public final String content;
	
	public final RichEmbed embed;
	
	public final Attachment attachment;
	
	public File file;
	
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
