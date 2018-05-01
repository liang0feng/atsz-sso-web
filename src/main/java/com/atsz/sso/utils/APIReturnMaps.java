package com.atsz.sso.utils;

import java.util.HashMap;

public class APIReturnMaps {

	public static HashMap<String, Object> map(String status ,String msg, Object data ){
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", status);
		hashMap.put("msg", msg);
		hashMap.put("data", data);
		
		return hashMap;
	}
}
