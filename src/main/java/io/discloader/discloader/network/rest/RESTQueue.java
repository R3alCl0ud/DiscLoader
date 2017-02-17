package io.discloader.discloader.network.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.body.MultipartBody;

import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class RESTQueue {
	public List<APIRequest> queue;

	public RESTManager rest;

	public DiscLoader loader;

	public Timer timer;

	private boolean waiting;
	private boolean globalLimit;

	public long timeDifference;
	public int rateLimit;
	public long resetTime;
	public int remaining;

	public RESTQueue(RESTManager rESTManager) {
		this.rest = rESTManager;

		this.loader = this.rest.loader;

		this.waiting = false;

		this.timeDifference = 0;

		this.queue = new ArrayList<APIRequest>();
	}

	public void handle() {
		if (this.waiting || this.queue.size() == 0 || this.globalLimit) {
			return;
		}

		this.waiting = true;

		final APIRequest apiRequest = this.queue.get(0);

		BaseRequest request = apiRequest.createRequest();

		request = this.addHeaders(request, apiRequest.auth, apiRequest.multi);
		
		
		final RESTQueue this_arg = this;
		request.asStringAsync(new Callback<String>() {
			public void cancelled() {
				apiRequest.future.completeExceptionally(new Throwable());
			}

			public void completed(HttpResponse<String> response) {
				Map<String, List<String>> headers = response.getHeaders();
				headers.forEach((name, value) -> {
					switch (name) {
					case "X-RateLimit-Limit":
						this_arg.rateLimit = Integer.parseInt(value.get(0), 10);
						break;
					case "X-RateLimit-Remaining":
						this_arg.remaining = Integer.parseInt(value.get(0), 10);
						break;
					case "X-RateLimit-Reset":
						this_arg.resetTime = (Long.parseLong(value.get(0), 10) * 1000L);
						break;
					case "X-RateLimit-Global":
						this_arg.globalLimit = Boolean.parseBoolean(value.get(0));
						break;
					}
				});
				DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
				try {
					this_arg.timeDifference = Date.from(Instant.now()).getTime()
							- df.parse(headers.get("Date").get(0)).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int code = response.getStatus();
				if (code == 429) {
					TimerTask wait = new TimerTask() {
						@Override
						public void run() {
							this_arg.waiting = false;
							this_arg.globalLimit = false;
							this_arg.handle();
						}
					};
					this_arg.loader.timer.schedule(wait, Integer.parseInt(headers.get("retry-after").get(0), 10) + 500);
					return;
				}
				this_arg.queue.remove(0);
				this_arg.loader.emit("raw", response.getBody());
				apiRequest.future.complete(response.getBody());
				this_arg.globalLimit = false;
				if (this_arg.remaining == 0) {
					TimerTask wait = new TimerTask() {
						@Override
						public void run() {
							this_arg.waiting = false;
							this_arg.handle();
						}
					};
					this_arg.loader.timer.schedule(wait,
							this_arg.resetTime - System.currentTimeMillis() + this_arg.timeDifference + 500);
				} else {
					this_arg.waiting = false;
					this_arg.handle();
				}
			}

			public void failed(UnirestException e) {
				apiRequest.future.completeExceptionally(e);
			}
		});
	}

	public void addToQueue(APIRequest request) {
		this.queue.add(request);
		this.handle();
	}

	public <T extends BaseRequest> T addHeaders(T baseRequest, boolean auth, boolean multi) {
		HttpRequest request = baseRequest.getHttpRequest();
		if (auth && this.loader.token != null)
			request.header("authorization", this.loader.token);
		if (!(request instanceof GetRequest) && !(baseRequest instanceof MultipartBody) && !multi) {
			System.out.println("hello");
			request.header("Content-Type", "application/json");
		} else {
			System.out.println("Multipart");
			request.header("content-type", "multipart/form-data");
		}
		request.header("user-agent", "DiscordBot (https://gitlab.com/R3alCl0ud/DiscLoader, v0.0.1)");
		request.header("Accept-Encoding", "gzip");
		return baseRequest;
	}

}
