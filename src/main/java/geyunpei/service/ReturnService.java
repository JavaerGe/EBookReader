package geyunpei.service;

import java.util.HashMap;
import java.util.Map;

import geyunpei.service.base.BaseService;
import geyunpei.utils.JsonUtil;

public class ReturnService extends BaseService{
	
	public String result(Integer code, String message) {
		Map<String, Object> map = new HashMap<>();
		map.put("resultCode", code);
		map.put("message", message);
		return JsonUtil.ObjectToJsonString(map);
	}
}
