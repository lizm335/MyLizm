/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CacheService.Value;

import net.sf.json.JSONObject;

/**
 * 功能说明：基础数据的控制器
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月27日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/pcenter/common")
public class CommonController {

	@Autowired
	private CacheService cacheService;
	
	@org.springframework.beans.factory.annotation.Value("#{configProperties['eeChatInterface']}")
	String eeServer;

	@ResponseBody
	@RequestMapping(value = "queryAllArea", method = RequestMethod.GET)
	private Object queryAllArea(HttpServletRequest request, HttpSession session) {
		List<Value> allArea = cacheService.getCachedDictionary(CacheService.DictionaryKey.ALLAREA);
		Map<String, Object> allAreMap = new HashMap<String, Object>();
		// List<Map<String, Object>> provinces = new ArrayList<Map<String,
		// Object>>();
		for (Value p : allArea) {
			if (!p.getCode().endsWith("0000"))
				continue;
			Map<String, Object> province = new HashMap<String, Object>();
			province.put("code", p.getCode());
			province.put("name", p.getName());
			Map<String, Object> cities = new HashMap<String, Object>();
			for (Value c : allArea) {
				if (!c.getCode().endsWith("00") || !c.getCode().startsWith(p.getCode().substring(0, 2)) || c.equals(p))
					continue;
				Map<String, Object> city = new HashMap<String, Object>();
				city.put("name", c.getName());
				Map<String, Object> areaMap = new HashMap<String, Object>();
				for (Value a : allArea) {
					if (a.getCode().startsWith(c.getCode().substring(0, 4)) && !a.equals(c)) {
						areaMap.put(a.getCode(), a.getName());
					}
				}
				city.put("areas", areaMap);
				cities.put(c.getCode(), city);
			}
			province.put("cities", cities);
			allAreMap.put(p.getCode(), province);

		}
		return allAreMap;
	}

	@ResponseBody
	@RequestMapping(value = "findAllArea", method = RequestMethod.GET)
	private Object findAllArea(HttpServletRequest request, HttpSession session) {
		List<Value> allArea = cacheService.getCachedDictionary(CacheService.DictionaryKey.ALLAREA);
		List<Object> areaList = new ArrayList<Object>();
		for (Value p : allArea) {
			if (!p.getCode().endsWith("0000"))
				continue;

			JSONObject province = JSONObject.fromObject(p);
			List<JSONObject> cities = new ArrayList<JSONObject>();
			for (Value c : allArea) {
				if (!c.getCode().endsWith("00") || !c.getCode().startsWith(p.getCode().substring(0, 2)) || c.equals(p))
					continue;
				JSONObject city = JSONObject.fromObject(c);
				List<Value> areas = new ArrayList<Value>();
				for (Value a : allArea) {
					if (a.getCode().startsWith(c.getCode().substring(0, 4)) && !a.equals(c)) {
						areas.add(a);
					}
				}
				city.put("sub", areas);
				cities.add(city);
			}
			province.put("sub", cities);
			areaList.add(province);
		}
		return areaList;
	}

	@RequestMapping(value = "eechat")
	public String eechat(@RequestParam String teacherId, HttpSession session) {
		GjtStudentInfo student = (GjtStudentInfo) session.getAttribute(WebConstants.STUDENT_INFO);
		String eeUrl = eeServer + "/openChat.do?data=" + EncryptUtils
				.encrypt("{\"USER_ID\":\"" + student.getStudentId() + "\",\"TO_ID\":\"" + teacherId + "\"}");
		
		return "redirect:" + eeUrl;
	}

}
