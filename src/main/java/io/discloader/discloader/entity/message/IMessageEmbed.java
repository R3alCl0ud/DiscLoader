package io.discloader.discloader.entity.message;

import java.time.OffsetDateTime;

public interface IMessageEmbed {

	int getColor();

	String getDescription();

	OffsetDateTime getTimestamp();

	String getTitle();

	String getType();

	String getURL();
}
