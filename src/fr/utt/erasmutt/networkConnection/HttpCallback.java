package fr.utt.erasmutt.networkConnection;

import org.json.JSONObject;

public interface HttpCallback {
	public Object call(JSONObject jsonResponse);
}
