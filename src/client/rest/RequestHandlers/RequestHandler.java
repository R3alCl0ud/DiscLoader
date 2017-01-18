package client.rest.RequestHandlers;

import client.rest.APIRequest;
import client.rest.RESTManager;

public class RequestHandler {
	
	public RESTManager restManager;
	public APIRequest[] queue;
	
	public RequestHandler(RESTManager restManager) {
		this.restManager = restManager;
	}
}
