package io.discloader.discloader.core.entity.message.embed;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import io.discloader.discloader.entity.message.IMessageEmbed;
import io.discloader.discloader.entity.message.embed.IEmbedAuthor;
import io.discloader.discloader.entity.message.embed.IEmbedField;
import io.discloader.discloader.entity.message.embed.IEmbedFooter;
import io.discloader.discloader.entity.message.embed.IEmbedImage;
import io.discloader.discloader.entity.message.embed.IEmbedProvider;
import io.discloader.discloader.entity.message.embed.IEmbedThumbnail;
import io.discloader.discloader.entity.message.embed.IEmbedVideo;
import io.discloader.discloader.network.json.EmbedFieldJSON;
import io.discloader.discloader.network.json.EmbedJSON;

/**
 * @author Perry Berman
 */
public class MessageEmbed implements IMessageEmbed {

	private int color;
	private String title, type, url, description;
	private List<IEmbedField> fields;
	private IEmbedAuthor author = null;
	private IEmbedImage image = null;
	private IEmbedProvider provider = null;
	private IEmbedFooter footer = null;
	private IEmbedThumbnail thumbnail = null;
	private IEmbedVideo video = null;
	private OffsetDateTime timestamp = null;

	public MessageEmbed(EmbedJSON data) {
		fields = new ArrayList<>();
		color = data.color;
		description = data.description;
		title = data.title;
		type = data.type;
		url = data.url;
		if (data.timestamp != null && !data.timestamp.isEmpty()) timestamp = OffsetDateTime.parse(data.timestamp);
		if (data.author != null) author = new MessageEmbedAuthor(data.author);
		if (data.footer != null) footer = new MessageEmbedFooter(data.footer);
		if (data.provider != null) provider = new MessageEmbedProvider(data.provider);
		if (data.image != null) image = new MessageEmbedImage(data.image);
		if (data.video != null) video = new MessageEmbedVideo(data.video);
		if (data.thumbnail != null) thumbnail = new MessageEmbedThumbnail(data.thumbnail);
		if (data.fields != null) {
			for (EmbedFieldJSON fj : data.fields) {
				fields.add(new MessageEmbedField(fj));
			}
		}
	}

	@Override
	public int getColor() {
		return color;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getURL() {
		return url;
	}

	@Override
	public List<IEmbedField> getFeilds() {
		return fields;
	}

	@Override
	public IEmbedField getField(int index) {
		return fields.get(index);
	}

	@Override
	public IEmbedAuthor getAuthor() {
		return author;
	}

	@Override
	public IEmbedFooter getFooter() {
		return footer;
	}

	@Override
	public IEmbedImage getImage() {
		return image;
	}

	@Override
	public IEmbedThumbnail getThumbnail() {
		return thumbnail;
	}

	@Override
	public IEmbedProvider getProvider() {
		return provider;
	}

	@Override
	public IEmbedVideo getVideo() {
		return video;
	}

}
