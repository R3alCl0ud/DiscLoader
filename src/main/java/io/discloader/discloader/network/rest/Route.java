package io.discloader.discloader.network.rest;

import static io.discloader.discloader.util.DLUtil.gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.exceptions.DiscordException;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.network.json.ExceptionJSON;
import io.discloader.discloader.network.util.Methods;
import io.discloader.discloader.util.DLUtil;

/**
 * 
 * @param <T>
 * 
 * @author Perry Berman
 */
public class Route<T> {

	private final RateLimiter rateLimiter;
	private final List<Request<T>> queue;
	private final Class<T> cls;
	private final RESTManager rest;

	private final String endpoint;
	private final Methods method;

	private final boolean auth;
	private boolean waiting;

	public Route(RESTManager rest, String endpoint, Methods method, boolean auth, Class<T> cls) {
		this.queue = new ArrayList<>();
		this.method = method;
		this.endpoint = endpoint;
		this.auth = auth;
		this.rateLimiter = new RateLimiter(this);
		this.waiting = false;
		this.rest = rest;
		this.cls = cls;
	}

	public CompletableFuture<T> push(Request<T> apiRequest) {
		queue.add(apiRequest);
		handle();
		return apiRequest.getFuture();
	}

	public void handle() {
		if (waiting || rateLimiter.isLimited() || queue.isEmpty())
			return;
		waiting = true;
		Request<T> apiRequest = queue.remove(0);
		BaseRequest request = createRequest(apiRequest);
		request.asStringAsync(new Callback<String>() {

			@Override
			public void completed(HttpResponse<String> response) {
				try {
					rateLimiter.readHeaders(response.getHeaders());
					RawEvent event = new RawEvent(rest.loader, response);
					rest.loader.emit(event);
					int code = response.getStatus();
					waiting = false;
					if (code == 429) {
						queue.add(0, apiRequest);
						rateLimiter.limitRoute(rest.isGlobally(), rateLimiter.retryIn());
						return; // return early so handle() at the end of handle() doesn't get called
					} else if (code >= 500 && code < 600) {
						queue.add(0, apiRequest);
					} else if (code >= 400 && code < 500) {
						apiRequest.getFuture().completeExceptionally(new DiscordException(gson.fromJson(response.getBody(), ExceptionJSON.class)));
					} else if (code == 204) {
						apiRequest.getFuture().complete(null);
					} else {
						T res = gson.fromJson(response.getBody(), cls);
						apiRequest.getFuture().complete(res);
					}
					handle();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void failed(UnirestException ex) {
				ex.printStackTrace();
				queue.add(0, apiRequest);
				handle();
			}

			@Override
			public void cancelled() {
				queue.add(0, apiRequest);
				handle();
			}

		});
	}

	public BaseRequest createRequest(Request<T> request) {
		BaseRequest base = null;
		String endpoint = this.endpoint;
		if (request.getOptions() != null && request.getOptions().getParameters() != null) {
			for (int i = 0; i < request.getOptions().getParameters().length; i++) {
				endpoint += String.format(i == 0 ? "?%s=%s" : "&%s=%s", request.getOptions().getParameters()[i].getName(), request.getOptions().getParameters()[i].getValue());
			}
		}
		switch (method) {
		case GET:
			base = Unirest.get(endpoint);
			break;
		case POST:
			base = Unirest.post(endpoint);
			if (request.getData() instanceof SendableMessage) {
				MultipartBody body = ((HttpRequestWithBody) base).fields(null);
				body.field("Content-Type", "multipart/form-data");
				SendableMessage sdata = (SendableMessage) request.getData();
				if (sdata.file != null || sdata.resource != null) {
					try {
						File file = sdata.file;
						Resource resource = sdata.resource;
						byte[] bytes = new byte[0];
						if (file != null)
							bytes = DLUtil.readAllBytes(file);
						if (resource != null && resource.getResourceAsStream() != null)
							bytes = DLUtil.readAllBytes(resource);
						String loc = "";
						if (file != null)
							loc = file.getName();
						if (resource != null)
							loc = resource.getFileName();
						body.field("file", bytes, loc);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				try {
					if (request.getData() instanceof JSONObject) {
						body.field("payload_json", request.getData().toString());
					} else if (request.getData() instanceof String) {
						body.field("payload_json", request.getData());
					} else {
						body.field("payload_json", gson.toJson(request.getData()));
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				base.getHttpRequest().header("Content-Type", "application/json");
				if (request.getData() instanceof JSONObject) {
					((HttpRequestWithBody) base).body(request.getData().toString());
				} else if (request.getData() instanceof String) {
					((HttpRequestWithBody) base).body(request.getData());
				} else {
					((HttpRequestWithBody) base).body(gson.toJson(request.getData()));
				}
			}
			break;
		case DELETE:
			base = Unirest.delete(endpoint);
			break;
		case PATCH:
			base = Unirest.patch(endpoint);
			base.getHttpRequest().header("Content-Type", "application/json");
			if (request.getData() instanceof JSONObject) {
				((HttpRequestWithBody) base).body(request.getData().toString());
			} else if (request.getData() instanceof String) {
				((HttpRequestWithBody) base).body(request.getData().toString());
			} else {
				((HttpRequestWithBody) base).body(gson.toJson(request.getData()));
			}
			break;
		case PUT:
			base = Unirest.put(endpoint);
			base.getHttpRequest().header("Content-Type", "application/json");
			if (request.getData() instanceof JSONObject) {
				((HttpRequestWithBody) base).body(request.getData().toString());
			} else if (request.getData() instanceof String) {
				((HttpRequestWithBody) base).body(request.getData());
			} else {
				((HttpRequestWithBody) base).body(gson.toJson(request.getData()));
			}
			break;
		default:
			base = Unirest.get(endpoint);
			break;
		}

		// body.
		HttpRequest httprequest = base.getHttpRequest();
		if (auth && rest.loader.token != null)
			httprequest.header("Authorization", rest.loader.token);
		if (request.getOptions() != null && request.getOptions().getReason() != null) {
			httprequest.header("X-Audit-Log-Reason", request.getOptions().getReason());
			System.out.println("is this thing working?");
		}
		httprequest.header("user-agent", "DiscordBot (http://discloader.io, v0.2.0)");
		httprequest.header("Accept-Encoding", "gzip");
		return base;
	}

	public String toString() {
		return endpoint;
	}

	/**
	 * @return the rest
	 */
	public RESTManager getREST() {
		return this.rest;
	}

	// public void setWaiting
}
