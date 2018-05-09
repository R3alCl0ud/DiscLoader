package io.discloader.discloader.common.language;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;

import io.discloader.discloader.client.render.util.Resource;

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
	public final Map<String, String> types;

	/**
	 * Creates a new language object
	 * 
	 * @param langStream
	 *            A File Input Stream of the .lang file for this language.
	 * @param locale
	 *            The locale the language applies to.
	 */
	public Language(InputStream langStream, Locale locale) {
		types = LanguageParser.parseLang(langStream);
		setLocale(locale);
	}

	/**
	 * Creates a new language object
	 * 
	 * @param langResourcePack
	 *            A Resource object pointing to the .lang for this language.
	 * @param locale
	 *            The locale the language applies to.
	 */
	public Language(Resource langResourcePack, Locale locale) {
		this(langResourcePack.getResourceAsStream(), locale);
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

}
