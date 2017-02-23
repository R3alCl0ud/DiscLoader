package io.discloader.discloader.entity.sendable;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import io.discloader.discloader.entity.embed.EmbedAuthor;
import io.discloader.discloader.entity.embed.EmbedField;
import io.discloader.discloader.entity.embed.EmbedFooter;
import io.discloader.discloader.entity.embed.EmbedThumbnail;

/**
 * @author Perry Berman
 *
 */
public class RichEmbed {

	public String title;
	public String description;
	public String url;
	public String timestamp;
	public int color;

	
	public ArrayList<EmbedField> fields;
	public EmbedFooter footer;
	public EmbedThumbnail thumbnail;
	public EmbedAuthor author;

	public RichEmbed() {
		this(null);
	}

	public RichEmbed(String title) {
		this.title = title;
		this.fields = new ArrayList<EmbedField>();
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
	public RichEmbed addField(String name, String value) {
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
	public RichEmbed addField(String name, String value, boolean inline) {
		if (this.fields.size() < 26) {
			this.fields.add(new EmbedField(name, value, inline));
		}
		return this;
	}

	public RichEmbed setAuthor(String name, String url, String icon) {
		this.author = new EmbedAuthor(name, url, icon);
		return this;
	}
	
	public RichEmbed setAuthor(String name, String url) {
		return this.setAuthor(name, url, null);
	}
	
	public RichEmbed setAuthor(String name) {
		return this.setAuthor(name, null);
	}
	
	public RichEmbed setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public RichEmbed setFooter(String text, String icon_url) {
		this.footer = new EmbedFooter(text, icon_url);
		return this;
	}
	
	public RichEmbed setThumbnail(String url) {
		this.thumbnail = new EmbedThumbnail(url);
		return this;
	}

	public RichEmbed setThumbnail(File file) {
		this.thumbnail = new EmbedThumbnail(file);
		return this;
	}
	
	public RichEmbed setColor(int color) {
		this.color = color;
		return this;
	}
}
