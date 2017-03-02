package io.discloader.discloader.common.language;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author Perry Berman
 *
 */
public class LanguageRegistry {

	private static LanguageRegistry langs = new LanguageRegistry();
	public final HashMap<Locale, HashMap<String, HashMap<String, HashMap<String, String>>>> localizedNames;

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

	public static HashMap<String, HashMap<String, HashMap<String, String>>> getLocale(Locale locale) {
		if (getLocales().localizedNames.containsKey(locale))
			return getLocales().localizedNames.get(locale);
		return null;
	}

	public static HashMap<String, HashMap<String, String>> getLocalized(Locale locale, String type) {
		if (getLocale(locale) != null)
			return getLocale(locale).get(type);
		return null;
	}

	public static HashMap<String, String> getLocalized(Locale locale, String type, String field) {
		if (getLocalized(locale, type) != null)
			return getLocalized(locale, type).get(field);
		return null;
	}

	public static String getLocalized(Locale locale, String type, String field, String prop) {
		if (getLocalized(locale, type, field) != null)
			return getLocalized(locale, type, field).get(prop);
		return null;
	}

	public static String getLocalized(String TypeFieldProp) {
		String[] l = TypeFieldProp.split("[.]");
		if (l.length < 1 || l.length > 3)
			return null;
		String t = l[0], f = l[1], p = l[2];
		return langs.localizedNames.get(Locale.US).get(t).get(f).get(p);
	}

	/**
	 * Adds a language to the registry
	 * @param lang The language to register
	 */
	public static void registerLanguage(Language lang) {
		System.out.print(lang.getLocale().toLanguageTag());
		if (!langs.localizedNames.containsKey(lang.getLocale())) {
			langs.localizedNames.put(lang.getLocale(), lang.types);
			return;
		}
		HashMap<String, HashMap<String, HashMap<String, String>>> Names = langs.localizedNames.get(lang.getLocale());
		for (String t : lang.types.keySet()) {
			if (!Names.containsKey(t)) {
				Names.put(t, lang.types.get(t));
				continue;
			}
			for (String f : lang.types.get(t).keySet()) {
				if (!Names.get(t).containsKey(f)) {
					Names.get(t).put(f, lang.types.get(t).get(f));
					continue;
				}
				for (String p : lang.types.get(t).get(f).keySet()) {
					Names.get(t).get(f).put(p, lang.types.get(t).get(f).get(p));
				}
			}
		}
	}

}
