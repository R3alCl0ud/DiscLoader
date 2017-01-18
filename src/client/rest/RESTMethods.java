package client.rest;

import client.Client;

public class RESTMethods {
	
	public Client client;
	public RESTManager rest;
	
	public RESTMethods(RESTManager restManager) {
		this.rest = restManager;
		this.client = restManager.client;
	}
}
