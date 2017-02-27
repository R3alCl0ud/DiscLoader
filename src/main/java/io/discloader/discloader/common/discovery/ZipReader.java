package io.discloader.discloader.common.discovery;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.discloader.discloader.common.language.Language;
import io.discloader.discloader.common.language.LanguageRegistry;

public class ZipReader {

	public static void readZip(File zip) {
		ZipFile zipFile = null;
		try {
			try {
				zipFile = new ZipFile(zip);
			} catch (ZipException e) {
				e.printStackTrace();
			}
			if (zipFile != null) {
				// read zip file
				for (ZipEntry e : readEntries(zipFile.entries())) {
					if (e.getName().endsWith(".lang")) {
						InputStream is = zipFile.getInputStream(e);
						Language lang = new Language(is, getLocale(e.getName()));
						LanguageRegistry.registerLanguage(lang);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static ArrayList<ZipEntry> readEntries(Enumeration<? extends ZipEntry> enumeration) {
		ArrayList<ZipEntry> entries = new ArrayList<ZipEntry>();
		ZipEntry entry = null;
		while (enumeration.hasMoreElements()) {
			entry = enumeration.nextElement();
			if (entry.isDirectory() || entry.getName().endsWith(".class")) {
				continue;
			}
			entries.add(entry);
		}

		return entries;
	}

	protected static Locale getLocale(String s) {
		s = s.substring(s.lastIndexOf('/') + 1, s.indexOf('.'));
		s = s.replace('_', '-');
		if (s.equals("en-US"))
			return Locale.US;
		else if(s.equals("en-UK"))
			return Locale.UK;
		return Locale.US;
	}

}
