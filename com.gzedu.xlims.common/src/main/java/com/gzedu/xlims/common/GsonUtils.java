package com.gzedu.xlims.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Gson工具类
 * @author fly
 * @date 2014-11-19
 */
public class GsonUtils {
	
	private static final Gson gson = new GsonBuilder()
		.serializeNulls()		// 字段值为空时，json串中就不存在
		.registerTypeAdapter(Date.class, new GsonDateTypeAdapter())
		.setDateFormat("yyyy-MM-dd HH:mm:ss")
		.create();

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}

	public static <T> T toBean(JsonObject jsonObject, Class<T> classOfT) {
		T entity = gson.fromJson(jsonObject, classOfT);
		return entity;
	}

	public static <T> T toBean(String json, Class<T> classOfT) {
		T entity = gson.fromJson(json, classOfT);
		return entity;
	}

	public static String[] toArray(JsonArray jsonArray) {
		String[] array = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			array[i] = jsonArray.get(i).getAsString();
		}
		return array;
	}

	public static String[] toArray(String json) {
		JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
		return toArray(jsonArray);
	}

	public static <T> List<T> toList(JsonArray jsonArray, Class<T> classOfT) {
//		Type type = new TypeToken<List<T>>() {}.getType();
//		List<T> list = gson.fromJson(jsonArray, type);
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < jsonArray.size(); i++) {
			list.add(toBean(jsonArray.get(i).getAsJsonObject(), classOfT));
		}
		return list;
	}

	public static <T> List<T> toList(String json, Class<T> classOfT) {
		JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(json).getAsJsonArray();
		return toList(jsonArray, classOfT);
	}

	public static <T> T toMsgResultBean(String json, Class<T> classOfT) {
		JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        if(jsonObj.get("status").getAsInt() == 1) {
	        // 将result节点下的内容转为JsonObject
	        JsonObject jsonObject = jsonObj.getAsJsonObject("resultValue");
			return toBean(jsonObject, classOfT);
        }
        return null;
	}

	public static String[] toMsgResultArray(String json) {
		JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        if(jsonObj.get("status").getAsInt() == 1) {
	        // 将result节点下的内容转为JsonArray
	        JsonArray jsonArray = jsonObj.getAsJsonArray("resultValue");
			return toArray(jsonArray);
        }
        return null;
	}

	public static <T> List<T> toMsgResultList(String json, Class<T> classOfT) {
		JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(json).getAsJsonObject();
        if(jsonObj.get("status").getAsInt() == 1) {
	        // 将result节点下的内容转为JsonArray
	        JsonArray jsonArray = jsonObj.getAsJsonArray("resultValue");
			return toList(jsonArray, classOfT);
        }
        return null;
	}


	/**
	 * List,Map转换成json
	 * @param list
	 * @return
	 */
	public static String getJsonData(List<?> list){
		Gson gson = new Gson();
		String jsonString = gson.toJson(list);
		return jsonString;
	}

}
