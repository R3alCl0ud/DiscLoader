package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedField;
import io.discloader.discloader.network.json.EmbedFieldJSON;

/**
 * Represents a field
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class MessageEmbedField implements IEmbedField {

	/**
	 * Is the field displayed inline
	 */
	private boolean inline;
	/**
	 * The name of the field
	 */
	private String name;
	/**
	 * The field's content
	 */
	private String value;

	/**
	 * @param name The name of the field
	 * @param value The content of the field
	 * @param inline Should the field be displayed inline
	 */
	public MessageEmbedField(EmbedFieldJSON data) {
		name = data.name;
		value = data.value;
		inline = data.inline;
	}

	/**
	 * @return The field's content
	 */
	public String getContent() {
		return value;
	}

	/**
	 * @return The name of the field
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return inline. Is {@literal true} if the field is displayed inline. Is
	 *         {@literal false} otherwise.
	 */
	public boolean isInline() {
		return this.inline;
	}

}
