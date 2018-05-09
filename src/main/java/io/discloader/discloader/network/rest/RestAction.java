package io.discloader.discloader.network.rest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.logger.DLLogger;
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

	protected final String endpoint;
	public static Consumer<?> DEFAULT_SUCCESS = o -> {};
	public static Consumer<Throwable> DEFAULT_FAILURE = t -> {
		LOG.throwing(t.getStackTrace()[0].getClassName(), t.getStackTrace()[0].getMethodName(), t);
	};
	protected RESTOptions options;
	protected Methods method;
	protected Gson gson;
	protected final CompletableFuture<T> future = new CompletableFuture<>();
	protected boolean executed = false;

	public RestAction(DiscLoader loader, String endpoint, Methods method, RESTOptions options) {
		this.loader = loader;
		this.endpoint = endpoint;
		this.method = method;
		this.options = options;
		this.gson = new Gson();
	}

	public RestAction<T> onSuccess(Consumer<? super T> success) {
		future.thenAcceptAsync(success);
		return this;
	}

	public RestAction<T> onFailure(Consumer<Throwable> failure) {
		future.exceptionally(ex -> {
			failure.accept(ex);
			return null;
		});
		return this;
	}

	public abstract RestAction<T> execute();

	public RestAction<T> execute(Consumer<? super T> success, Consumer<Throwable> failure) {
		return onSuccess(success).onFailure(failure).execute();
	}

	public boolean isDone() {
		return future.isDone();
	}

	public boolean isCanceled() {
		return future.isCancelled();
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return future.cancel(mayInterruptIfRunning);
	}

	public T get() throws InterruptedException, ExecutionException {
		return future.get();
	}

	public T join() {
		return future.join();
	}

	protected void autoExecute() {
		if (loader.getOptions().autoExecRestActions()) {
			execute();
		}
	}
}
