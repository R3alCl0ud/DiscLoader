package io.discloader.discloader.core.entity.presence;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.TimeZone;

import io.discloader.discloader.entity.presence.IActivityTimestamps;
import io.discloader.discloader.network.json.ActivityTimestampsJSON;

public class ActivityTimestamps implements IActivityTimestamps {

	private long start, end;

	public ActivityTimestamps(ActivityTimestampsJSON data) {
		this.start = data.start;
		this.end = data.end;
	}

	@Override
	public OffsetDateTime getStartTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(start), TimeZone.getDefault().toZoneId());
	}

	@Override
	public OffsetDateTime getEndTime() {
		return OffsetDateTime.ofInstant(Instant.ofEpochMilli(end), TimeZone.getDefault().toZoneId());
	}

}
