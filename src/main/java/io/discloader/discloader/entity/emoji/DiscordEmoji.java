package io.discloader.discloader.entity.emoji;

public class DiscordEmoji {

	public class CodePoints {

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
		public String[] getDefaultMatches() {
			return default_matches;
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
		public String[] getGreedyMatches() {
			return greedy_matches;
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
	private String name, category, shortname, diversity, gender;
	private String[] shortname_alternatives;
	private String[] ascii;
	private String[] diversities;
	private String[] genders;
	private String[] keywords;
	private int order;
	private int display;

	private float unicode_version;

	private CodePoints code_points;

	/**
	 * @return the ascii
	 */
	public String[] getAscii() {
		return ascii;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
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
	 * @return the diversities
	 */
	public String[] getDiversities() {
		return diversities;
	}

	/**
	 * @return the diversity
	 */
	public String getDiversity() {
		return diversity;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the genders
	 */
	public String[] getGenders() {
		return genders;
	}

	/**
	 * @return the keywords
	 */
	public String[] getKeywords() {
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
	 * @return the shortname_alternatives
	 */
	public String[] getShortnameAlternatives() {
		return shortname_alternatives;
	}

	/**
	 * @return the unicode_version
	 */
	public float getUnicodeVersion() {
		return unicode_version;
	}

	/**
	 * @return the unicode
	 */
	public String getUnicode() {
		return code_points.base;
	}

}
