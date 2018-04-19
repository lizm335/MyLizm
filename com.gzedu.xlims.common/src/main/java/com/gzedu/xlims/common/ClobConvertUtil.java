package com.gzedu.xlims.common;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gzedu.xlims.common.gzedu.EmptyUtils;

public class ClobConvertUtil {

	
	public static Map converClobMap(Map map){
		if(EmptyUtils.isNotEmpty(map)){
			Set set = map.keySet();
			Iterator iter = set.iterator();
			while(iter.hasNext()){
				Object key = iter.next();
				Object value = map.get(key);
				if(value instanceof Clob){
					Clob c = (Clob) map.get(key);
					Long len = null;
					try{
						len = c.length();
						String clobValue = c.getSubString(1, len.intValue());
						map.put(key, clobValue);
					}catch(SQLException e){
						throw new RuntimeException(e);
					}
				}
			}
		}
		return map;
	}
	
	
	public static List clobValue(List list){
		List mapList = null;
		if(EmptyUtils.isEmpty(list)){
			return Collections.EMPTY_LIST;
		}
		mapList = new ArrayList();
		for(Object objMap:list){
			if(objMap instanceof Map){
				Map beanMap = (Map) objMap;
				beanMap = converClobMap(beanMap);
				mapList.add(beanMap);
			}
		}
		return mapList;
	}


	/**
	 * Clob 转 String
	 * @param clob
	 * @return
	 */
	public static String clobToString(Clob clob){
		String transferString = "";
		try {
			if (clob == null || clob.getCharacterStream() == null)
				return "";
			Reader is = clob.getCharacterStream();
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s!=null){
				sb.append(s);
				s=br.readLine();
			}
			transferString = sb.toString();
		}catch (Exception e){

		}
		return transferString;
	}


}
