package io.discloader.discloader.common.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Perry Berman
 * @since 0.0.3
 */
public class LanguageParser {

	private static String regex = "(.*)(?<!\\\\)=\\s*(.*)";
	private static Pattern pattern = Pattern.compile(regex);

	public static HashMap<String, String> parseLang(InputStream is) {
		HashMap<String, String> lang = new HashMap<>();
		if (is != null) {
			Scanner sc = new Scanner(is);
			while (sc.hasNextLine()) {
				String l = sc.nextLine();
				Matcher matcher = pattern.matcher(l);
				if (!matcher.find()) continue;
				lang.put(matcher.group(1), matcher.group(2));
			}
			sc.close();
		}
		return lang;
	}

}
