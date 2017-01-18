package client.rest;

import org.json.JSONObject;

public class UserAgentManager {
	
	public RESTManager restManager;
	private JSONObject _userAgent;
	
	public UserAgentManager(RESTManager restManager) {
		this.restManager = restManager;
		this._userAgent = new JSONObject().put("url", "https://github.com/R3alCl0ud/Forgecord").put("version", "0.0.1");
	}
	
	public void setAgent(JSONObject info) {
		if (info.has("url")) this._userAgent.put("url", info.get("url"));
		if (info.has("version")) this._userAgent.put("version", info.get("version"));
	}
	
	public JSONObject userAgent() {
		return this._userAgent;
	}
	
}
