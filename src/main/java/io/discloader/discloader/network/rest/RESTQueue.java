package io.discloader.discloader.network.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.exceptions.AccountTypeException;
import io.discloader.discloader.common.exceptions.UnauthorizedException;
import io.discloader.discloader.common.exceptions.UnknownException;
import io.discloader.discloader.network.json.ExceptionJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * @author Perry Berman
 */
public class RESTQueue {

	public List<APIRequest> queue;

	public RESTManager rest;

	public DiscLoader loader;

	public Timer timer;

	private boolean waiting;

	private boolean globalLimit;

	public long timeDifference;

	public long resetTime;

	public int rateLimit;

	public int remaining;

	public RESTQueue(RESTManager restManager) {
		rest = restManager;
		loader = rest.loader;
		waiting = false;
		timeDifference = 0;
		queue = new ArrayList<>();
	}

	public void handle() {
		try {
			if (waiting || queue.size() == 0 || globalLimit) {
				return;
			}

			waiting = true;

			final APIRequest apiRequest = queue.get(0);

			BaseRequest request = apiRequest.createRequest();

			request = addHeaders(request, apiRequest.auth, apiRequest.multi);

			request.asStringAsync(new Callback<String>() {

				@Override
				public void cancelled() {
					apiRequest.future.completeExceptionally(new Throwable());
				}

				@Override
				public void completed(HttpResponse<String> response) {
					Map<String, List<String>> headers = response.getHeaders();
					headers.forEach((name, value) -> {
						switch (name) {
						case "X-RateLimit-Limit":
							rateLimit = Integer.parseInt(value.get(0), 10);
							break;
						case "X-RateLimit-Remaining":
							remaining = Integer.parseInt(value.get(0), 10);
							break;
						case "x-ratelimit-reset":
						case "X-RateLimit-Reset":
							resetTime = (Long.parseLong(value.get(0), 10) * 1000L);
							break;
						case "X-RateLimit-Global":
							globalLimit = Boolean.parseBoolean(value.get(0));
							break;
						}
					});
					DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
					try {
						timeDifference = Date.from(Instant.now()).getTime() - df.parse(headers.get("Date").get(0)).getTime();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					RawEvent event = new RawEvent(loader, response);
					int code = response.getStatus();
					if (code == 429) {
						Thread wait = new Thread("Ratelimit resetting - " + apiRequest.url) {

							@Override
							public void run() {
								try {
									Thread.sleep(Integer.parseInt(headers.get("retry-after").get(0), 10) + 500);
								} catch (NumberFormatException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								waiting = false;
								globalLimit = false;
								handle();
							}
						};
						wait.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
						wait.setDaemon(true);
						wait.start();
						return;
					} else if (code != 200 && code != 201 && code != 204 && code != 304) {
						queue.remove(apiRequest);
						loader.emit(event);
						loader.emit("RawPacket", event);
						ExceptionJSON data = DLUtil.gson.fromJson(response.getBody(), ExceptionJSON.class);
						switch (code) {
						case 401:
							apiRequest.future.completeExceptionally(new UnauthorizedException(response.getBody()));
							break;
						case 403:
							switch (data.code) {
							case 20002:
								apiRequest.future.completeExceptionally(new AccountTypeException(data));
								break;
							}
							break;
						default:
							apiRequest.future.completeExceptionally(new UnknownException(data));
							break;
						}
					} else {
						queue.remove(apiRequest);
						loader.emit(event);
						loader.emit("RawPacket", event);
						apiRequest.future.complete(response.getBody());
					}
					globalLimit = false;
					long waitTime = ((resetTime - System.currentTimeMillis()) + timeDifference + 500);
					if (remaining == 0 && waitTime > 0) {
						Thread wait = new Thread("REST Waiting - " + apiRequest.url) {

							@Override
							public void run() {
								try {
									Thread.sleep(waitTime);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								waiting = false;
								handle();
							}
						};
						wait.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
						wait.setDaemon(true);
						wait.start();
					} else {
						waiting = false;
						handle();
					}
				}

				@Override
				public void failed(UnirestException e) {
					apiRequest.future.completeExceptionally(e);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addToQueue(APIRequest request) {
		this.queue.add(request);
		this.handle();
	}

	public <T extends BaseRequest> T addHeaders(T baseRequest, boolean auth, boolean multi) {
		HttpRequest request = baseRequest.getHttpRequest();

		if (auth && loader.token != null) request.header("authorization", loader.token);
		if (!(request instanceof GetRequest) && !(baseRequest instanceof MultipartBody) && !multi) {
			request.header("Content-Type", "application/json");
		}
		request.header("user-agent", "DiscordBot (http://discloader.io, v0.1.1)");
		request.header("Accept-Encoding", "gzip");
		return baseRequest;
	}

}
