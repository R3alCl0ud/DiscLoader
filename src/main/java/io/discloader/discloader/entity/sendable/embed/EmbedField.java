package io.discloader.discloader.entity.sendable.embed;

/**
 * Prototype for embed fields
 * @author Perry Berman
 * @since 0.0.1
 */
public class EmbedField {

	/**
	 * Is the field displayed inline
	 */
	public boolean inline;
	/**
	 * The name of the field
	 */
	public String name;
	/**
	 * The field's content
	 */
	public String value;

	/**
	 * @param name The name of the field
	 * @param value The content of the field
	 * @param inline Should the field be displayed inline
	 */
	public EmbedField(String name, String value, boolean inline) {
		this.setName(name);
		this.setValue(value);
		this.setInline(inline);
	}

	/**
	 * @return value, The field's content
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Changes the field's content
	 * @param value The new content of the field
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the name of the field
	 */
	public String getName() {
		return name;
	}

	/**
	 * Changes the field's name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return inline. Is {@literal true} if the field is displayed inline. Is {@literal false} otherwise. 
	 */
	public boolean isInline() {
		return inline;
	}

	/**
	 * Changes the field's inline value
	 * @param inline Should the field be displayed inline
	 */
	public void setInline(boolean inline) {
		this.inline = inline;
	}
	
	

}
