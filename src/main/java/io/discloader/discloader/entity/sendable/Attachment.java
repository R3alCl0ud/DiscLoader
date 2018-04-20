package io.discloader.discloader.entity.sendable;

import java.io.File;

/**
 * @author Perry Berman
 *
 */
public class Attachment {

	public final String filename;

	public Attachment(String filename) {
		this.filename = filename;
	}

	public File getFile() {
		return new File(filename);
	}

}
