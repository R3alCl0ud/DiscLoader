package io.discloader.discloader.common.language;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

public class Language {

	private Locale locale;
	
	public final HashMap<String, HashMap<String, HashMap<String, String>>> types;
	
	/**
	 * Creates a new language object
	 * @param langStream
	 * @param locale
	 */
	public Language(InputStream langStream, Locale locale) {
		this.types = LanguageParser.parseLang(langStream);
		this.setLocale(locale);
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
