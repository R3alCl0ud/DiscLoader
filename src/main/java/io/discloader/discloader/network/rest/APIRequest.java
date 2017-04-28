package io.discloader.discloader.network.rest;

import static io.discloader.discloader.util.DLUtil.gson;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.util.DLUtil;
import io.discloader.discloader.util.DLUtil.Methods;

public class APIRequest {

	public String url;

	public String route;

	public int method;

	public boolean auth;

	public final boolean multi;

	public Object data;

	public CompletableFuture<String> future;

	/**
	 * Creates a new APIRequest
	 * 
	 * @param url The endpoint's url
	 * @param method The request method to use
	 * @param auth Does the endpoint require authorization
	 * @param data The payload to attach to the request
	 */
	public APIRequest(String url, int method, boolean auth, Object data) {
		this.url = url;
		this.method = method;
		this.auth = auth;
		this.data = data;
		this.route = this.getRoute(this.url);
		if (data != null && data instanceof SendableMessage && (((SendableMessage) data).file != null || ((SendableMessage) data).resource != null)) {
			this.multi = true;
		} else {
			this.multi = false;
		}
	}

	/**
	 * Converts the {@link APIRequest} URL to the corresponding API Endpoint
	 * 
	 * @param url The url of the endpoint
	 * @return API Endpoint {@link DLUtil.Endpoints}
	 */
	public String getRoute(String url) {
		String route = url.split("[?]")[0];
		if (route.contains("/channels/") || route.contains("/guilds/")) {
			int startInd = route.contains("/channels/") ? route.indexOf("/channels/") : route.indexOf("/guilds/");
			String majorID = route.substring(startInd).split("/")[2];
			route = route.replaceAll("(\\d{8,})/g", ":id").replaceAll(":id", majorID);
		}
		return route;
	}

	public CompletableFuture<?> setFuture(CompletableFuture<String> future) {
		this.future = future;
		return future;
	}

	public BaseRequest createRequest() {
		BaseRequest request = null;
		switch (this.method) {
		case Methods.GET:
			request = Unirest.get(this.route);
			break;
		case Methods.POST:
			request = Unirest.post(this.route);
			if (this.multi && data instanceof SendableMessage) {
				SendableMessage sdata = (SendableMessage) this.data;
				File file = sdata.file;
				Resource resource = sdata.resource;
				try {
					byte[] bytes = new byte[0];
					if (file != null) bytes = DLUtil.readAllBytes(file);
					else if (resource != null) bytes = DLUtil.readAllBytes(resource);
					MultipartBody body = ((HttpRequestWithBody) request).fields(null);
					String loc = "";
					if (file != null) loc = file.getName();
					if (resource != null) loc = resource.getFileName();
					System.out.println(loc);
					// System.out.println(gson.toJson(data));
					body.field("Content-type", "multipart/form-data").field("file", bytes, loc).field("payload_json", gson.toJson(sdata));
					// body.mode("form-data");
					// InputStream is =
					// body.getHttpRequest().getBody().getEntity().getContent();
					// Scanner sc = new Scanner(is);
					// while (sc.hasNext()) {
					// System.out.println(sc.next());
					// }
					// sc.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				((HttpRequestWithBody) request).body(gson.toJson(data));
			}
			break;
		case Methods.PATCH:
			request = Unirest.patch(this.route);
			((HttpRequestWithBody) request).body(gson.toJson(data));
			break;
		case Methods.DELETE:
			request = Unirest.delete(this.route);
			break;
		case DLUtil.Methods.PUT:
			request = Unirest.put(this.route);
			((HttpRequestWithBody) request).body(gson.toJson(this.data));
			break;
		default:
			request = Unirest.get(this.route);
			break;
		}

		return request;
	}

}
