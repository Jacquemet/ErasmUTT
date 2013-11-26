package fr.utt.erasmutt.networkConnection;

import org.json.JSONObject;

public interface JsonHttpCallback {
	public Object call(JSONObject jsonResponse);
}
