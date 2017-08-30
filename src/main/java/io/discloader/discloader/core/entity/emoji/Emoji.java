package io.discloader.discloader.core.entity.emoji;

import java.util.List;

import com.google.common.collect.Lists;

public class Emoji {

	public static class CodePoints {

		private String base;
		private String fully_qualified;
		private String non_fully_qualified;
		private String output;
		private String decimal;
		private String[] default_matches;
		private String[] greedy_matches;

		/**
		 * @return the base
		 */
		public String getBase() {
			return base;
		}

		/**
		 * @return the decimal
		 */
		public String getDecimal() {
			return decimal;
		}

		/**
		 * @return the default_matches
		 */
		public List<String> getDefaultMatches() {
			return Lists.newArrayList(default_matches);
		}

		/**
		 * @return the fully_qualified
		 */
		public String getFullyQualified() {
			return fully_qualified;
		}

		/**
		 * @return the greedy_matches
		 */
		public List<String> getGreedyMatches() {
			return Lists.newArrayList(greedy_matches);
		}

		/**
		 * @return the non_fully_qualified
		 */
		public String getNonFullyQualified() {
			return non_fully_qualified;
		}

		/**
		 * @return the output
		 */
		public String getOutput() {
			return output;
		}

	}
	private String name, shortname, unicode;
	private int order, display;
	private double unicode_version;
	
	private List<String> keywords, shortnames;
	
	private EmojiGender gender;
	private EmojiCategory category;
	
	private CodePoints code_points;

	public Emoji(String name, String unicode, double unicode_version, int order, int display, List<String> keywords, List<String> shortnames, EmojiGender gender, EmojiCategory category, CodePoints code_points) {
		this.name = name;
		this.unicode = unicode;
		this.unicode_version = unicode_version;
		this.order = order;
		this.display = display;
		this.keywords = keywords;
		this.shortnames = shortnames;
		this.gender = gender;
		this.category = category;
		this.code_points = code_points;
	}

	/**
	 * @return the aliases of the emoji
	 */
	public List<String> getAliases() {
		return shortnames;
	}

	/**
	 * @return the category
	 */
	public EmojiCategory getCategory() {
		return category;
	}

	/**
	 * @return the code_points
	 */
	public CodePoints getCodePoints() {
		return code_points;
	}

	/**
	 * @return the display
	 */
	public int getDisplay() {
		return display;
	}

	/**
	 * @return the gender
	 */
	public EmojiGender getGender() {
		return gender;
	}

	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @return the shortname
	 */
	public String getShortname() {
		return shortname;
	}

	/**
	 * @return the unicode
	 */
	public String getUnicode() {
		return unicode;
	}

	/**
	 * @return the unicode_version
	 */
	public double getUnicodeVersion() {
		return unicode_version;
	}

	@Override
	public String toString() {
		return getUnicode();
	}

}
