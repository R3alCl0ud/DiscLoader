package io.discloader.discloader.common.language;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

/**
 * @author Perry Berman
 * @since 0.0.3
 */
public class Language {

	/**
	 * The locale the language applies to
	 */
	private Locale locale;

	/**
	 * mapping of language entries
	 */
	public final HashMap<String, String> types;

	/**
	 * Creates a new language object
	 * 
	 * @param langStream A File Input Stream of the .lang file for this language
	 * @param locale The locale the language applies to
	 */
	public Language(InputStream langStream, Locale locale) {
		types = LanguageParser.parseLang(langStream);
		setLocale(locale);
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
