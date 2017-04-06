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

	public static HashMap<String, String> parseLang(InputStream fis) {
		HashMap<String, String> lang = new HashMap<>();
		Scanner sc = new Scanner(fis);
		while (sc.hasNextLine()) {
			String l = sc.nextLine();
			Matcher matcher = pattern.matcher(l);
			if (!matcher.find() || matcher.groupCount() < 3) continue;
			System.out.println(matcher.group(1) + matcher.group(2));
		}
		sc.close();
		return lang;
	}

	public static HashMap<String, HashMap<String, HashMap<String, String>>> parseLang(File lang) {
		HashMap<String, HashMap<String, HashMap<String, String>>> types = new HashMap<>();
		try {
			Stream<String> stream = Files.lines(lang.toPath());
			for (Object o : stream.toArray()) {
				String line = o.toString();
				String[] l = line.split("[.]");
				if (l.length < 1 || l.length > 3) continue;
				String type = l[0], field = l[1], propVal = l[2];
				if (propVal.indexOf('=') == -1) {
					continue;
				}
				String prop = propVal.split("=")[0], value = propVal.split("=")[1];
				if (!types.containsKey(type)) {
					types.put(type, new HashMap<>());
				}
				HashMap<String, HashMap<String, String>> fields = types.get(type);
				if (!fields.containsKey(field)) {
					fields.put(field, new HashMap<>());
				}
				HashMap<String, String> props = fields.get(field);
				props.put(prop, value);
			}
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return types;
	}

	public static HashMap<String, HashMap<String, HashMap<String, String>>> parseLang(FileInputStream langStream) {
		HashMap<String, HashMap<String, HashMap<String, String>>> types = new HashMap<>();
		Scanner sc = new Scanner(langStream);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.isEmpty()) continue;
			String[] l = line.split("[.]");
			if (l.length < 1 || l.length > 3) continue;
			String type = l[0], field = l[1], propVal = l[2];
			if (propVal.indexOf('=') == -1) {
				continue;
			}
			String prop = propVal.split("=")[0], value = propVal.split("=")[1];
			if (!types.containsKey(type)) {
				types.put(type, new HashMap<>());
			}
			HashMap<String, HashMap<String, String>> fields = types.get(type);
			if (!fields.containsKey(field)) {
				fields.put(field, new HashMap<>());
			}
			HashMap<String, String> props = fields.get(field);
			props.put(prop, value);
		}
		sc.close();
		return types;
	}

}
