package io.discloader.discloader.common.entity;

import java.io.File;
import java.util.ArrayList;

import io.discloader.discloader.entity.embed.EmbedAuthor;
import io.discloader.discloader.entity.embed.EmbedField;
import io.discloader.discloader.entity.embed.EmbedFooter;
import io.discloader.discloader.entity.embed.EmbedThumbnail;

/**
 * @author Perry Berman
 *
 */
public class RichEmbed {

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
    public int color;

    /**
     * An {@link ArrayList} of {@link EmbedField EmbedFields}.
     * 
     * @author Perry Berman
     */
    public ArrayList<EmbedField> fields;

    public EmbedFooter footer;
    public EmbedThumbnail thumbnail;
    public EmbedAuthor author;

    /**
     * Creates a new RichEmbed
     */
    public RichEmbed() {
        this(null);
    }

    /**
     * Creates a new RichEmbed
     * 
     * @param title The title of the embed
     */
    public RichEmbed(String title) {
        this.title = title;
        this.fields = new ArrayList<EmbedField>();
    }

    /**
     * Adds a new field to the embed
     * 
     * @param name The name of the field
     * @param value The content of the field
     * @return this
     */
    public RichEmbed addField(String name, String value) {
        return this.addField(name, value, false);
    }

    /**
     * Adds a new field to the embed
     * 
     * @param name The name of the field
     * @param value The content of the field
     * @param inline Should the field be displayed inline
     * @return this
     */
    public RichEmbed addField(String name, String value, boolean inline) {
        if (this.fields.size() < 26) {
            this.fields.add(new EmbedField(name, value, inline));
        }
        return this;
    }

    /**
     * Sets the author of the RichEmbed
     * 
     * @param name The name of the author
     * @return this
     */
    public RichEmbed setAuthor(String name) {
        return this.setAuthor(name, null);
    }

    /**
     * Set's the author of the RichEmbed
     * 
     * @param name The name of the author
     * @param url The author's website
     * @return this
     */
    public RichEmbed setAuthor(String name, String url) {
        return this.setAuthor(name, url, null);
    }

    /**
     * Set's the author of the RichEmbed
     * 
     * @param name The name of the author
     * @param url The author's website
     * @param icon The url to the author's icon
     * @return this
     */
    public RichEmbed setAuthor(String name, String url, String icon) {
        this.author = new EmbedAuthor(name, url, icon);
        return this;
    }

    /**
     * Sets the RichEmbed's color bar's color
     * 
     * @param color The integer representation of the color bars color. Ex: {@code 0xFFFFFF} is the integer value for white
     * @return this
     */
    public RichEmbed setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     * Sets the RichEmbed's {@link #description}
     * 
     * @param description The new {@link #description}
     * @return this
     */
    public RichEmbed setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Sets the RichEmbed's footer
     * 
     * @param text The footer's content
     * @param iconURL The footer's icon
     * @return this
     */
    public RichEmbed setFooter(String text, String iconURL) {
        this.footer = new EmbedFooter(text, iconURL);
        return this;
    }

    /**
     * Sets the RichEmbed's {@link #thumbnail}
     * 
     * @param file The image file to use as the {@link #thumbnail}
     * @return this
     */
    public RichEmbed setThumbnail(File file) {
        this.thumbnail = new EmbedThumbnail(file);
        return this;
    }

    /**
     * Sets the RichEmbed's {@link #thumbnail}
     * 
     * @param URL The URL of the image to use as the thumbnail
     * @return this
     */
    public RichEmbed setThumbnail(String URL) {
        this.thumbnail = new EmbedThumbnail(URL);
        return this;
    }

    /**
     * Sets the RichEmbed's title
     * 
     * @param title The new title
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
