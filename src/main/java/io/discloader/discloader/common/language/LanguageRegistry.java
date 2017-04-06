package io.discloader.discloader.common.language;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author Perry Berman
 */
public class LanguageRegistry {

	private static LanguageRegistry langs = new LanguageRegistry();
	public final HashMap<Locale, HashMap<String, String>> localizedNames;

	/**
	 * Creates a new LanguageRegistry instance
	 */
	public LanguageRegistry() {
		this.localizedNames = new HashMap<>();
		this.createLocales();
	}

	/**
	 * Creates the default locales
	 */
	private void createLocales() {
		this.localizedNames.put(Locale.US, new HashMap<>());
	}

	public static LanguageRegistry getLocales() {
		return langs;
	}

	public static HashMap<String, String> getLocale(Locale locale) {
		if (getLocales().localizedNames.containsKey(locale)) return getLocales().localizedNames.get(locale);
		return null;
	}

	public static String getLocalized(Locale locale, String holder) {
		if (getLocale(locale) != null) return getLocale(locale).get(holder);
		return null;
	}

	public static String getLocalized(String holder) {
		return getLocalized(Locale.US, holder);
	}

	/**
	 * Adds a language to the registry
	 * 
	 * @param lang The language to register
	 */
	public static void registerLanguage(Language lang) {
		if (!langs.localizedNames.containsKey(lang.getLocale())) {
			langs.localizedNames.put(lang.getLocale(), lang.types);
			return;
		}

	}

}
