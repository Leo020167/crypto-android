package com.bitcnew.social.entity.jasonparser;

import org.json.JSONException;
import org.json.JSONObject;

import com.bitcnew.http.base.AbstractParser;
import com.bitcnew.social.entity.WeiboAtName;

public class WeiboAtNameJason extends AbstractParser<WeiboAtName> {

	@Override
	public WeiboAtName parse(JSONObject json) throws JSONException {
		WeiboAtName weibo = new WeiboAtName();
		if (hasAndNotNull(json, "nickname")) weibo.screen_name = json.get("nickname").toString();
		if (hasAndNotNull(json, "screen_name")) weibo.screen_name = json.get("screen_name").toString();

		return weibo;
	}
}
