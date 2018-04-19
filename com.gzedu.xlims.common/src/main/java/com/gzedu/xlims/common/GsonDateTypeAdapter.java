package com.gzedu.xlims.common;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
	
 	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public JsonElement serialize(Date arg0, Type arg1,
			JsonSerializationContext arg2) {
		try {
        	return new JsonPrimitive(df.format(arg0));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public Date deserialize(JsonElement arg0, Type arg1,
			JsonDeserializationContext arg2) throws JsonParseException {
		try {
        	return df.parse(arg0.getAsString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
}