package io.discloader.discloader.core.entity.message.embed;

import io.discloader.discloader.entity.message.embed.IEmbedProvider;
import io.discloader.discloader.network.json.EmbedProviderJSON;

/**
 * @author Perry Berman
 */
public class MessageEmbedProvider implements IEmbedProvider {

	private String name, url;

	public MessageEmbedProvider(EmbedProviderJSON data) {
		url = data.url;
		name = data.name;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getURL() {
		return url;
	}

}
