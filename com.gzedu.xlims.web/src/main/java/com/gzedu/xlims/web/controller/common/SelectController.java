/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.web.controller.common.vo.CommonSelect;

/**
 * 
 * 功能说明：省市区
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月23日
 * @version 2.5
 * @since JDK1.7
 *
 */
@Controller
@RequestMapping("/select")
public class SelectController {
	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtGradeService gjtGradeService;

	@Autowired
	GjtSpecialtyService gjtGradeSpecialtyService;

	// 根据年级联动专业
	@RequestMapping(value = "querySpecialtyByGradeId", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> querySpecialtyByGradeId(String id) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		try {
			GjtGrade gjtGrade = gjtGradeService.queryById(id);

			List<GjtSpecialty> gjtSpecialtys = gjtGradeSpecialtyService.findSpecialtyByGradeId(gjtGrade.getGradeId());
			for (GjtSpecialty gjtSpecialty : gjtSpecialtys) {
				list.add(new CommonSelect(gjtSpecialty.getSpecialtyId(),
						gjtSpecialty.getZymc() + "(" + gjtSpecialty.getZyh() + ")"));
			}
		} catch (Exception e) {
		}
		return list;
	}

	// 查询全部市
	@RequestMapping(value = "queryCity", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> queryCity(String code) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(code)) {
			Map<String, String> cityMap = commonMapService.getCityMap(code);
			for (Entry<String, String> en : cityMap.entrySet()) {
				list.add(new CommonSelect(en.getKey(), en.getValue()));
			}
		}
		return list;
	}

	// 查询全部区
	@RequestMapping(value = "queryDistrict", method = RequestMethod.GET)
	@ResponseBody
	public List<CommonSelect> queryDistrict(String code) {
		List<CommonSelect> list = new ArrayList<CommonSelect>();
		if (StringUtils.isNotBlank(code)) {
			Map<String, String> map = commonMapService.getDistrictMap(code);
			for (Entry<String, String> en : map.entrySet()) {
				list.add(new CommonSelect(en.getKey(), en.getValue()));
			}
		}
		return list;
	}
}