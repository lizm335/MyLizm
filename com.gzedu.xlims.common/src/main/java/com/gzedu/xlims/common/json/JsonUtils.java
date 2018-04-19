package com.gzedu.xlims.common.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	protected static Logger log = LoggerFactory.getLogger(JsonUtils.class);
	
	public static <T> String toJson(T obj) {
		String json = null;
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			json = mapper.writeValueAsString(obj);// 把map或者是list转换成
		} catch (Exception e) {
			e.printStackTrace();
			log.error("生成json串成产生异常", e);
		} finally {
			mapper = null;
		}
		return json;
	}

	public static <T> T toObject(String jsonStr, Class<T> clazz) {
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			return mapper.readValue(jsonStr, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("json串成转换为对象产生异常", e);
		} finally {
			mapper = null;
		}
		return null;
	}

	public static <T> T toObject(String jsonStr, TypeReference<T> typeReference) {
		ObjectMapper mapper = null;
		try {
			mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			return mapper.readValue(jsonStr, typeReference);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("json串成转换为对象产生异常", e);
		} finally {
			mapper = null;
		}
		return null;
	}

}
