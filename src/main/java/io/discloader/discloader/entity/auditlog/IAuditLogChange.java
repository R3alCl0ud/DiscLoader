package io.discloader.discloader.entity.auditlog;

public interface IAuditLogChange {

	/**
	 * The key which defines the field that was updated by this change
	 *
	 * @return The key
	 */
	AuditLogChangeKeys getKey();

	/**
	 * The updated value for the field specified by {@link #getKey()}.
	 *
	 * @param <T>
	 *            The expected generic type for this value. <br>
	 *            This will be used to cast the value.
	 *
	 * @throws java.lang.ClassCastException
	 *             If the type cast to the generic type fails
	 *
	 * @return The new value
	 */
	<T> T getNewValue();

	/**
	 * The previous value for the field specified by {@link #getKey()}.
	 *
	 * @param <T>
	 *            The expected generic type for this value. <br>
	 *            This will be used to cast the value.
	 *
	 * @throws java.lang.ClassCastException
	 *             If the type cast to the generic type fails
	 *
	 * @return The old value
	 */
	<T> T getOldValue();

}
