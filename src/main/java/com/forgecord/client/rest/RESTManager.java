package main.java.com.forgecord.client.rest;

import java.io.File;

import org.json.JSONObject;

import java.lang.reflect.Method;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.client.rest.RequestHandlers.RequestHandler;
import main.java.com.forgecord.client.rest.RequestHandlers.SequentialRequestHandler;

import java.util.concurrent.*;


public class RESTManager {
	
	public Client client;
	public RESTMethods methods;
	public JSONObject handlers;
		
	public RESTManager(Client client) {
		this.client = client;
		this.methods = new RESTMethods(this);
		this.handlers = new JSONObject();
	}
	
}
