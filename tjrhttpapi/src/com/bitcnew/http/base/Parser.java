package com.bitcnew.http.base;

import org.json.JSONException;
import org.json.JSONObject;

public interface Parser <T extends TaojinluType>{
	public abstract T parse(JSONObject json) throws JSONException;
}
