package main.java.com.forgecord.client.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class APIResponse {
	public final Exception exception;
	public final int code;
	public final String responseText;
	public JSONObject data;
	
	protected APIResponse(int code, String response) {
		this.code = code;
		this.responseText = response;			
		this.data = responseText == null ? null : new JSONObject(responseText);
		this.exception = null;
	}
	
    protected APIResponse(Exception exception)
    {
        this.code = -1;
        this.responseText = null;
        this.exception = exception;

    }	
}
