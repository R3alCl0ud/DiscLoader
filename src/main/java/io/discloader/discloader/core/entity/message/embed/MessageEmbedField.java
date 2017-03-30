package io.discloader.discloader.core.entity.message.embed;

/**
 * Represents a field 
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class MessageEmbedField {

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
	public MessageEmbedField(String name, String value, boolean inline) {
		this.name = name;
		this.value = value;
		this.inline = inline;
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
