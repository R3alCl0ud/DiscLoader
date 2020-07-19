package io.discloader.discloader.entity.message;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.message.embed.RichEmbed;
import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.sendable.Attachment;

/**
 * @author Perry Berman
 *
 */
public class MessageBuilder implements Appendable {
	
	public enum Formatting {
		ITALIC("*"),
		BOLD("**"),
		STRIKE("~~"),
		UNDERLINE("__"),
		CODE("`");
		
		private final String tag;
		
		Formatting(String t) {
			tag = t;
		}
		
		public String getTag() {
			return tag;
		}
	}
	
	protected final StringBuilder builder = new StringBuilder();
	private RichEmbed embed = null;
	private Attachment attachment = null;
	
	private ITextChannel channel;
	
	public MessageBuilder(ITextChannel channel) {
		this.channel = channel;
	}
	
	@Override
	public MessageBuilder append(char c) {
		builder.append(c);
		return this;
	}
	
	@Override
	public MessageBuilder append(CharSequence text) {
		builder.append(text);
		return this;
	}
	
	public MessageBuilder append(CharSequence text, Formatting... formats) {
		boolean code = false;
		for (Formatting format : formats) {
			if (format == Formatting.CODE) {
				code = true;
				continue;
			}
			builder.append(format.getTag());
		}
		if (code) builder.append(Formatting.CODE.getTag());
		
		builder.append(text);
		if (code) builder.append(Formatting.CODE.getTag());
		for (int i = formats.length - 1; i >= 0; i--) {
			if (formats[i] == Formatting.CODE) continue;
			builder.append(formats[i].getTag());
		}
		return this;
	}
	
	@Override
	public MessageBuilder append(CharSequence text, int start, int end) {
		builder.append(text, start, end);
		return this;
	}
	
	public MessageBuilder append(Object object) {
		return append(String.valueOf(object));
	}
	
	public MessageBuilder appendFormat(String format, Object... args) {
		return append(String.format(format, args));
	}
	
	public MessageBuilder bold(CharSequence text) {
		return append(text, Formatting.BOLD);
	}
	
	public MessageBuilder code(CharSequence text) {
		return append(text, Formatting.CODE);
	}
	
	public MessageBuilder codeBlock(CharSequence text, CharSequence language) {
		return append("```").append(language).newLine().append(text).newLine().append("```");
	}
	
	public MessageBuilder italics(CharSequence text) {
		return append(text, Formatting.ITALIC);
	}
	
	/**
	 * @return the attachment
	 */
	public Attachment getAttachment() {
		return this.attachment;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return builder.toString();
	}
	
	/**
	 * @return the embed
	 */
	public RichEmbed getEmbed() {
		return this.embed;
	}
	
	public boolean isEmpty() {
		return length() == 0 && embed == null && attachment == null;
	}
	
	public int length() {
		return builder.length();
	}
	
	public MessageBuilder mention(IMentionable mentionable) {
		return append(mentionable.toMention());
	}
	
	public MessageBuilder newLine() {
		return append("\n");
	}
	
	public CompletableFuture<IMessage> sendMessage() {
		String content = builder.toString();
		
		return channel.sendMessage(content, embed, attachment);
	}
	
	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	
	/**
	 * @param embed the embed to set
	 */
	public void setEmbed(RichEmbed embed) {
		this.embed = embed;
	}
}
