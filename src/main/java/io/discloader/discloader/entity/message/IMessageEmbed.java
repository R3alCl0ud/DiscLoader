package io.discloader.discloader.entity.message;

import java.time.OffsetDateTime;
import java.util.List;

import io.discloader.discloader.entity.message.embed.IEmbedAuthor;
import io.discloader.discloader.entity.message.embed.IEmbedField;
import io.discloader.discloader.entity.message.embed.IEmbedFooter;
import io.discloader.discloader.entity.message.embed.IEmbedImage;
import io.discloader.discloader.entity.message.embed.IEmbedProvider;
import io.discloader.discloader.entity.message.embed.IEmbedThumbnail;
import io.discloader.discloader.entity.message.embed.IEmbedVideo;

public interface IMessageEmbed {

	int getColor();

	String getDescription();

	OffsetDateTime getTimestamp();

	String getTitle();

	String getType();

	String getURL();

	List<IEmbedField> getFeilds();

	IEmbedField getField(int index);

	IEmbedAuthor getAuthor();

	IEmbedFooter getFooter();

	IEmbedImage getImage();

	IEmbedThumbnail getThumbnail();

	IEmbedProvider getProvider();

	IEmbedVideo getVideo();

}
