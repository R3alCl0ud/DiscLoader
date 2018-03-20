package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.util.Methods;

/**
 * @author Perry Berman
 *
 * @param <T>
 *            Completion type
 */
public abstract class RestAction<T> {

	public static final Logger LOG = DLLogger.getLogger(RestAction.class);

	protected final DiscLoader loader;

	private final String endpoint;
	public static Consumer<?> DEFAULT_SUCCESS = o -> {};
	public static Consumer<Throwable> DEFAULT_FAILURE = t -> {
		LOG.throwing(t.getStackTrace()[0].getClassName(), t.getStackTrace()[0].getMethodName(), t);
	};
	// private Consumer<? super T> success = (obj -> {});
	// private Consumer<Throwable> failure = (ex -> {});
	private RESTOptions options;

	public RestAction(DiscLoader loader, String endpoint, RESTOptions options) {
		this.loader = loader;
		this.endpoint = endpoint;
	}

	public RestAction<T> onSuccess(Consumer<? super T> action) {
		// this.success = action;
		return this;
	}

	public <U> RestAction<T> execute(Class<U> cls) {
		CompletableFuture<U> future = loader.rest.request(Methods.GET, endpoint, options, cls);
		return this;
	}
}
