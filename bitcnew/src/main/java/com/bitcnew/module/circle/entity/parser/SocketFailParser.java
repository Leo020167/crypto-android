package com.bitcnew.module.circle.entity.parser;

import com.bitcnew.module.circle.entity.SocketFail;
import com.bitcnew.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class SocketFailParser extends AbstractParser<SocketFail> {
	@Override
	public SocketFail parse(JSONObject json) throws JSONException {
		SocketFail socketFail=new SocketFail();

		if(hasNotNullAndIsIntOrLong(json, "code")){
			socketFail.code=json.getInt("code");
		}

		if(hasAndNotNull(json, "msg")){
			socketFail.msg=json.getString("msg");
		}
		if(hasAndNotNull(json, "success")){
			socketFail.success=json.getBoolean("success");
		}

		return socketFail;
	}

}
