package io.discloader.discloader.core.entity;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.entity.embed.EmbedAuthor;
import io.discloader.discloader.entity.embed.EmbedField;
import io.discloader.discloader.entity.embed.EmbedFooter;
import io.discloader.discloader.entity.embed.EmbedImage;
import io.discloader.discloader.entity.embed.EmbedThumbnail;
import io.discloader.discloader.entity.message.IMessageEmbed;
import io.discloader.discloader.entity.message.embed.IEmbedField;

/**
 * @author Perry Berman
 */
public class RichEmbed {

	public static RichEmbed from(IMessageEmbed embed) {
		RichEmbed rich = new RichEmbed();
		rich.setColor(embed.getColor());
		if (embed.getTitle() != null)
			rich.setTitle(embed.getTitle());
		if (embed.getAuthor() != null)
			rich.setAuthor(embed.getAuthor().getName(), embed.getAuthor().getURL(), embed.getAuthor().getIconURL());
		if (embed.getURL() != null)
			rich.setURL(embed.getURL());
		if (embed.getDescription() != null)
			rich.setDescription(embed.getDescription());
		if (embed.getFooter() != null)
			rich.setFooter(embed.getFooter().getContent(), embed.getFooter().getIconURL());
		if (embed.getThumbnail() != null)
			rich.setThumbnail(embed.getThumbnail().getURL());
		if (embed.getImage() != null)
			rich.setImage(embed.getImage().getURL());
		if (embed.getTimestamp() != null)
			rich.setTimestamp(embed.getTimestamp());
		for (IEmbedField field : embed.getFeilds()) {
			rich.addField(field.getName(), field.getContent(), field.isInline());
		}
		return rich;
	}

	/**
	 * title of embed
	 */
	public String title;

	/**
	 * description of RichEmbed
	 */
	public String description;

	/**
	 * url of embed
	 */
	public String url;

	/**
	 * timestamp of embed content
	 */
	public String timestamp;

	/**
	 * The color of the RichEmbed color bar
	 */
	private int color;

	private EmbedImage image;

	/**
	 * An {@link ArrayList} of {@link EmbedField EmbedFields}.
	 * 
	 * @author Perry Berman
	 */
	private List<EmbedField> fields;
	private EmbedFooter footer;
	private EmbedThumbnail thumbnail;

	private EmbedAuthor author;

	/**
	 * Creates a new RichEmbed
	 */
	public RichEmbed() {
		this(null);
	}

	/**
	 * Creates a new RichEmbed
	 * 
	 * @param title
	 *            The title of the embed
	 */
	public RichEmbed(String title) {
		this.title = title;
		fields = new ArrayList<EmbedField>();
	}

	/**
	 * Adds a new field to the embed
	 * 
	 * @param name
	 *            The name of the field
	 * @param value
	 *            The content of the field
	 * @return this
	 */
	public RichEmbed addField(String name, Object value) {
		return this.addField(name, value, false);
	}

	/**
	 * Adds a new field to the embed
	 * 
	 * @param name
	 *            The name of the field
	 * @param value
	 *            The content of the field
	 * @param inline
	 *            Should the field be displayed inline
	 * @return this
	 */
	public RichEmbed addField(String name, Object value, boolean inline) {
		if (this.fields.size() < 26) {
			this.fields.add(new EmbedField(name, value.toString(), inline));
		}
		return this;
	}

	/**
	 * @return the author
	 */
	public EmbedAuthor getAuthor() {
		return author;
	}

	/**
	 * @return the color
	 */
	public int getColor() {
		return color;
	}

	public List<EmbedField> getFields() {
		return fields;
	}

	/**
	 * @return the footer
	 */
	public EmbedFooter getFooter() {
		return footer;
	}

	/**
	 * @return the image
	 */
	public EmbedImage getImage() {
		return image;
	}

	public EmbedThumbnail getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param author
	 *            the author to set
	 */
	public void setAuthor(EmbedAuthor author) {
		this.author = author;
	}

	/**
	 * Sets the author of the RichEmbed
	 * 
	 * @param name
	 *            The name of the author
	 * @return this
	 */
	public RichEmbed setAuthor(String name) {
		return this.setAuthor(name, null);
	}

	/**
	 * Set's the author of the RichEmbed
	 * 
	 * @param name
	 *            The name of the author
	 * @param url
	 *            The author's website
	 * @return this
	 */
	public RichEmbed setAuthor(String name, String url) {
		return this.setAuthor(name, url, null);
	}

	/**
	 * Set's the author of the RichEmbed
	 * 
	 * @param name
	 *            The name of the author
	 * @param url
	 *            The author's website
	 * @param icon
	 *            The url to the author's icon
	 * @return this
	 */
	public RichEmbed setAuthor(String name, String url, String icon) {
		this.author = new EmbedAuthor(name, url, icon);
		return this;
	}

	/**
	 * Sets the RichEmbed's color bar's color
	 * 
	 * @param color
	 *            The integer representation of the color bars color. Ex:
	 *            {@code 0xFFFFFF} is the integer value for white
	 * @return this
	 */
	public RichEmbed setColor(int color) {
		this.color = color;
		return this;
	}

	/**
	 * Sets the RichEmbed's {@link #description}
	 * 
	 * @param description
	 *            The new {@link #description}
	 * @return this
	 */
	public RichEmbed setDescription(String description) {
		this.description = description;
		return this;
	}

	/**
	 * @param footer
	 *            the footer to set
	 */
	public void setFooter(EmbedFooter footer) {
		this.footer = footer;
	}

	public RichEmbed setFooter(String text) {
		return setFooter(text, null);
	}

	/**
	 * Sets the RichEmbed's footer
	 * 
	 * @param text
	 *            The footer's content
	 * @param iconURL
	 *            The footer's icon
	 * @return this
	 */
	public RichEmbed setFooter(String text, String iconURL) {
		this.footer = new EmbedFooter(text, iconURL);
		return this;
	}

	public RichEmbed setImage(File img) {
		image = new EmbedImage(img);
		return this;
	}

	public RichEmbed setImage(Resource resource) throws IOException {
		image = new EmbedImage(resource);
		return this;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public RichEmbed setImage(String url) {
		image = new EmbedImage(url);
		return this;
	}

	/**
	 * Sets the RichEmbed's {@link #thumbnail}
	 * 
	 * @param file
	 *            The image file to use as the {@link #thumbnail}
	 * @return this
	 */
	public RichEmbed setThumbnail(File file) {
		this.thumbnail = new EmbedThumbnail(file);
		return this;
	}

	public RichEmbed setThumbnail(Resource resource) {
		if (resource != null)
			thumbnail = new EmbedThumbnail(resource);
		return this;
	}

	/**
	 * Sets the RichEmbed's {@link #thumbnail}
	 * 
	 * @param URL
	 *            The URL of the image to use as the thumbnail
	 * @return this
	 */
	public RichEmbed setThumbnail(String URL) {
		this.thumbnail = new EmbedThumbnail(URL);
		return this;
	}

	/**
	 * Shortcut method for setting the embed's Timestamp to the current time
	 * 
	 * @return {@code this}
	 */
	public RichEmbed setTimestamp() {
		return setTimestamp(OffsetDateTime.now());
	}

	public RichEmbed setTimestamp(TemporalAccessor time) {
		// LocalDateTime ldt = LocalDateTime.from(time).plusHours(7l);
		timestamp = time.toString();
		return this;
	}

	/**
	 * Sets the RichEmbed's title
	 * 
	 * @param title
	 *            The new title
	 * @return this
	 */
	public RichEmbed setTitle(String title) {
		this.title = title;
		return this;
	}

	public RichEmbed setURL(String url) {
		this.url = url;
		return this;
	}

}
