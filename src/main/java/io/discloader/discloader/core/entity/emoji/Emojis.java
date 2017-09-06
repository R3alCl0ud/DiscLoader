package io.discloader.discloader.core.entity.emoji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import io.discloader.discloader.network.util.Endpoints;

public class Emojis {

	private static final List<Emoji> emojis = new ArrayList<>();
	private static final Map<EmojiCategory, List<Emoji>> emojiCategories = new HashMap<>();
	private static final Gson gson = new Gson();

	static {
		try {
			HttpResponse<String> response = Unirest.get(Endpoints.EmojiJSON).asString();
			JSONTokener tokener = new JSONTokener(response.getBody());
			JSONObject json = new JSONObject(tokener);
			for (String key : json.keySet()) {
				JSONObject obj = json.getJSONObject(key);
				Emoji.CodePoints codePoints = gson.fromJson(obj.getJSONObject("code_points").toString(), Emoji.CodePoints.class);
				String name = obj.getString("name"), unicode = "";
				EmojiCategory category = EmojiCategory.getByName(obj.getString("category"));
				for (String uni : codePoints.getBase().split("-")) {
					unicode += String.valueOf(Character.toChars(Integer.parseInt(uni, 16)));
				}
				List<String> keywords = new ArrayList<>(), shortnames = new ArrayList<>();
				obj.getJSONArray("keywords").forEach(keyword -> keywords.add(keyword.toString()));
				obj.getJSONArray("shortname_alternates").forEach(alt -> shortnames.add(alt.toString().replaceAll(":", "")));
				emojis.add(new Emoji(name, unicode, obj.getDouble("unicode_version"), obj.getInt("order"), obj.getInt("display"), keywords, shortnames, EmojiGender.getByCode(obj.get("gender").toString()), category, codePoints));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Emoji> getEmojiFromGender(EmojiGender emojiGender) {
		List<Emoji> emotes = new ArrayList<>();
		for (Emoji emoji : emojis) {
			if (emoji.getGender() == emojiGender) emotes.add(emoji);
		}
		return emotes;
	}

	public static Emoji getEmoji(String name) {
		for (Emoji emoji : emojis) {
			if (emoji.getName().equals(name) || emoji.getAliases().contains(name)) return emoji;
		}
		return null;
	}

	public static Emoji getEmojiFromUnicode(String unicode) {
		for (Emoji emoji : emojis) {
			if (emoji.getUnicode().equals(unicode)) return emoji;
		}
		return null;
	}

	public static List<Emoji> getEmojisByCategory(EmojiCategory category) {
		if (!emojiCategories.containsKey(category)) {
			List<Emoji> emotes = new ArrayList<>();
			for (Emoji emoji : emojis) {
				if (emoji.getCategory().equals(category)) emotes.add(emoji);
			}
			emojiCategories.put(category, emotes);
		}
		return emojiCategories.get(category);
	}

	public static List<Emoji> getEmojisByKeyword(String... keywords) {
		List<Emoji> emotes = new ArrayList<>();
		for (Emoji emoji : emojis) {
			boolean add = true;
			for (String keyword : keywords) {
				if (!emoji.getKeywords().contains(keyword)) {
					add = false;
					break;
				}
			}
			if (add) emotes.add(emoji);
		}
		return emotes;
	}

	public static List<Emoji> getEmojisOnPage(int page) {
		List<Emoji> emotes = new ArrayList<>();
		for (Emoji emoji : emojis) {
			if (emoji.getDisplay() == page) emotes.add(emoji);
		}
		emotes.sort((a, b) -> {
			if (a.getOrder() < b.getOrder()) return -1;
			if (a.getOrder() > b.getOrder()) return 1;
			return 0;
		});
		return emotes;
	}

	public static List<Emoji> getEmojisOfVersion(double version) {
		List<Emoji> emotes = new ArrayList<>();
		for (Emoji emoji : emojis) {
			if (emoji.getUnicodeVersion() == version) emotes.add(emoji);
		}
		return emotes;
	}
}
