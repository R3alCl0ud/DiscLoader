package io.discloader.discloader.network.json;

public class MessageJSON {

	public String id;
	public String content;
	public String channel_id;
	public String timestamp;
	public String edited_timestamp;
	public String nonce;
	public String webhook_id;

	public int type;

	public boolean tts;
	public boolean mention_everyone;
	public boolean pinned;

	public UserJSON author;
	public MessageActivityJSON activity;
	public MessageApplicationJSON application;

	public UserJSON[] mentions;
	public String[] mention_roles;
	public EmbedJSON[] embeds;
	public AttachmentJSON[] attachments;
	public ReactionJSON[] reactions;

}
