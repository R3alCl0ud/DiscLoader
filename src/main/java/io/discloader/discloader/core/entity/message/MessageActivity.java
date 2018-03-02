package io.discloader.discloader.core.entity.message;

import io.discloader.discloader.entity.message.IMessageActivity;
import io.discloader.discloader.entity.message.MessageActivityType;
import io.discloader.discloader.network.json.MessageActivityJSON;

public class MessageActivity implements IMessageActivity {

	private final int type;
	private String party_id;

	public MessageActivity(MessageActivityJSON data) {
		type = data.type;
		party_id = data.party_id == null ? "" : data.party_id;
	}

	@Override
	public MessageActivityType getActivityType() {
		return MessageActivityType.getTypeFromInt(type);
	}

	@Override
	public String getPartyID() {
		return party_id;
	}

	@Override
	public String toString() {
		return getActivityType().name();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof MessageActivity))
			return false;
		MessageActivity activity = (MessageActivity) object;
		return this == activity || (activity.getActivityType() == getActivityType() && activity.getPartyID().equals(getPartyID()));
	}

	@Override
	public int hashCode() {
		return (toString() + getPartyID()).hashCode();
	}
}
