package io.discloader.discloader.core.entity.message.embed;

import java.time.OffsetDateTime;
import java.util.List;

import io.discloader.discloader.entity.message.IMessageEmbed;
import io.discloader.discloader.entity.message.embed.IEmbedAuthor;
import io.discloader.discloader.entity.message.embed.IEmbedField;
import io.discloader.discloader.entity.message.embed.IEmbedFooter;
import io.discloader.discloader.entity.message.embed.IEmbedImage;
import io.discloader.discloader.entity.message.embed.IEmbedProvider;
import io.discloader.discloader.entity.message.embed.IEmbedThumbnail;
import io.discloader.discloader.entity.message.embed.IEmbedVideo;

/**
 * @author Perry Berman
 */
public class MessageEmbed implements IMessageEmbed {

	@Override
	public int getColor() {
		return 0;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public OffsetDateTime getTimestamp() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String getURL() {
		return null;
	}

	@Override
	public List<IEmbedField> getFeilds() {
		return null;
	}

	@Override
	public IEmbedField getField(int index) {
		return null;
	}

	@Override
	public IEmbedAuthor getAuthor() {
		return null;
	}

	@Override
	public IEmbedFooter getFooter() {
		return null;
	}

	@Override
	public IEmbedImage getImage() {
		return null;
	}

	@Override
	public IEmbedThumbnail getThumbnail() {
		return null;
	}

	@Override
	public IEmbedProvider getProvider() {
		return null;
	}

	@Override
	public IEmbedVideo getVideo() {
		return null;
	}

}
