package main.java.com.forgecord.client.rest.RequestHandlers;

import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONObject;

import com.mashape.unirest.http.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.*;
import com.mashape.unirest.request.body.*;

import main.java.com.forgecord.client.rest.APIRequest;
import main.java.com.forgecord.client.rest.RESTManager;
import main.java.com.forgecord.util.promise.Executor;


public class SequentialRequestHandler extends RequestHandler {

	public boolean waiting;
	public Deferred<Object, Long, String> deferred = new DeferredObject();
	
	public SequentialRequestHandler(RESTManager restManager, String endpoint) {
		super(restManager);
		this.waiting = false;
	}
	
	public void push(JSONObject request) {
	    super.push(request);
	    System.out.println(request.toString());
	    this.handle();
	}
	
	public Long execute(APIRequest item) {
		BaseRequest request = null;
		Object body = item.data;
		
		if (body instanceof MultipartBody) {
			
		} else {
			String bodyData = body != null ? body.toString() : null;
			request = this.createRequest(item, bodyData);
		}
		
		try {
			HttpResponse<String> response = request.asString();
			int attempt = 1;
			while (attempt < 4 && response.getStatus() != 429 && response.getBody() != null && response.getBody().startsWith("<")) {
				try {
					Thread.sleep(50 * attempt);
				} catch (InterruptedException ignored) {
					
				}
				
				response = request.asString();
				attempt++;
			}
			
			return null;
		} catch (UnirestException e) {
			return null;
		}
	}
	
	public BaseRequest createRequest(APIRequest apiRequest, String body) {
		BaseRequest request = null;
		switch (apiRequest) {
		case "get": 
			request = Unirest.get(apiRequest.route);
			break;
		}
		return request;
	}
	
	public Promise<Object, Long, String> promise() {
		return deferred.promise();
	}

	public void handle() {
		super.handle();
		
		if (this.waiting || this.queue.size() == 0) return;
		
		JSONObject item = this.queue.get(0);
		
		Promise promise = this.promise();
		
		promise.then(new DoneCallback<JSONObject>() {
			@Override
			public void onDone(JSONObject res) {
				
			}
		});
	}
	
    protected <T extends BaseRequest> T addHeaders(T baseRequest)
    {
        HttpRequest request = baseRequest.getHttpRequest();

        if (this.restManager.client.token != null && request.getUrl().contains("discordapp.com"))
        {
            request.header("authorization", this.restManager.client.token);
        }
        if (!(request instanceof GetRequest) && !(baseRequest instanceof MultipartBody))
        {
            request.header("Content-Type", "application/json");
        }
        request.header("user-agent", "Forgecord");
        request.header("Accept-Encoding", "gzip");
        return baseRequest;
    }
}
