package io.discloader.discloader.common.structures;

import java.util.ArrayList;

import io.discloader.discloader.common.structures.embed.EmbedField;

/**
 * @author Perry Berman
 *
 */
public class RichEmbed {
	
	public String title;
	public String type;
	public String description;
	public String url;
	public String timestamp;
	public int color;

	public ArrayList<EmbedField> fields;
	
	public RichEmbed() {
		this.fields = new ArrayList<EmbedField>();
	}

	/**
	 * Adds a new field to the embed
	 * @param name The name of the field
	 * @param value The content of the field
	 * @param inline Should the field be displayed inline
	 * @return this
	 */
	public RichEmbed addField(String name, String value, boolean inline) {
		this.fields.add(new EmbedField(name, value, inline));
		return this;
	}
	
	/**
	 * Adds a new field to the embed
	 * @param name The name of the field
	 * @param value The content of the field
	 * @return this
	 */
	public RichEmbed addField(String name, String value) {
		return this.addField(name, value, false);
	}
}
