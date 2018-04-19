package com.gzedu.xlims.web.controller.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.gzedu.AnalyXmlUtil;
import com.gzedu.xlims.service.api.ApiService;

/**
 * 缴费信息接口
 */
@Controller
@RequestMapping("/api/fees")
public class FeesController {
	
	@Autowired
	ApiService apiService;

	@RequestMapping(value = "addFeesRecord", method = RequestMethod.POST)
	@ResponseBody
	public Map addFeesRecord(HttpServletResponse response, HttpServletRequest request) {
		Map resultMap = new HashMap();
		List resultList = new ArrayList();
		try {
			resultMap = apiService.addFeesRecord(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}
}
