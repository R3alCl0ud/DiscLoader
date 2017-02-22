package io.discloader.discloader.common.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;

/**
 * @author Perry Berman
 *
 */
public class FileLogger extends AbstractLogger {

	private File logFile;
	private FileOutputStream logStream;

	public FileLogger() throws IOException {
		super("","");
		this.logFile = new File(String.format("logs/%s.log", Date.from(Instant.now()).toString().replace(" ", "-").replace(":", "_")));
		this.logFile.createNewFile();
		this.logStream = new FileOutputStream(this.logFile);
	}
	
	@Override
	public String log(String text) throws IOException {
		text = super.log(text);
		if (!this.logFile.exists())
			return text;
		byte[] bytes = text.getBytes();
		this.logStream.write(bytes);
		return text;
	}
	
	@Override
	public void shutdown() throws IOException {
		this.logStream.close();
	}
	
	/**
	 * Gets the log file for the session
	 * 
	 * @return the logFile
	 */
	public File getLogFile() {
		return logFile;
	}


}
