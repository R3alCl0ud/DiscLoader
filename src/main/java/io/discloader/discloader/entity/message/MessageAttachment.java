package io.discloader.discloader.entity.message;

import io.discloader.discloader.network.json.AttachmentJSON;

/**
 * Represents an attachment on a message
 * 
 * @author Perry Berman
 *
 */
public class MessageAttachment {
    public final String id;
    public final String filename;
    public final int size;
    public final String url;
    public final String proxyURL;
    private int height;
    private int width;

    public MessageAttachment(AttachmentJSON data) {
        this.id = data.id;
        this.filename = data.filename;
        this.size = data.size;
        this.url = data.url;
        this.proxyURL = data.proxy_url;
        this.height = data.height;
        this.width = data.width;
    }

    /**
     * @return the height of the image. returns {@code 0} if {@link #isImage()} is false
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @return the width of the image. returns {@code 0} if {@link #isImage()} is false
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Checks if the attachment is an image.
     * 
     * @return true if {@link #getWidth()} &amp; {@link #getHeight()} don't return 0, false otherwise.
     */
    public boolean isImage() {
        return width != 0 && height != 0;
    }

}
