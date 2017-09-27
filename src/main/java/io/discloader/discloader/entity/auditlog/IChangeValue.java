package io.discloader.discloader.entity.auditlog;

public interface IChangeValue {

	boolean isBoolean();

	boolean isDouble();

	boolean isFloat();

	boolean isInt();

	boolean isLong();

	boolean isShort();

	boolean isSnowflake();

	boolean isString();

	/**
	 * Converts this value to a boolean.
	 * 
	 * @return a boolean representation of this value. if {@link #isSnowflake()}
	 *         returns {@code true} then this method will return {@code true}.
	 *         <br>
	 *         if the type of this value is a {@code String} this method will
	 *         check if the {@link #toString()} representation has more than
	 *         {@code 0} characters
	 *         <br>
	 *         all other types are checked as a {@code falsy} value. ei, if this
	 *         value's type is a number type then we check if the value is not
	 *         equal to {@code 0}.
	 */
	boolean toBoolean();

	double toDouble();

	float toFloat();

	int toInt();

	long toLong();

	short toShort();

	long toSnowflake();

	String toString();
}
