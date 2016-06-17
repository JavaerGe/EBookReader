package geyunpei.utils;

import com.alibaba.fastjson.JSON;

/**
 * JSON工具类
 * @Title:JsonUtil.java
 * @date:2013-5-15
 * @version:V1.0
 */
public class JsonUtil <T>{

	public static String ObjectToJsonString( Object object){
		return JSON.toJSONString(object);
	}
	
	public static <T>T jsonStringToObject(String jsonString,Class<T> clazz){
		return JSON.parseObject(jsonString,clazz);
	}
	
}
