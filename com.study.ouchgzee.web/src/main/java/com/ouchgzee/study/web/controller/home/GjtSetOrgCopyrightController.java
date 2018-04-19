/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.home;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.PlatfromTypeEnum;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtSetOrgCopyright;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月15日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/pcenter/home/copyright")
public class GjtSetOrgCopyrightController {

	@Autowired
	GjtSetOrgCopyrightService gjtSetOrgCopyrightService;

	// 获取个人中心页面版权信息
	@RequestMapping(value = "getCopyright", method = RequestMethod.GET)
	@ResponseBody
	public GjtSetOrgCopyright getCopyright(HttpServletRequest request) {
		GjtStudentInfo student = (GjtStudentInfo) request.getSession().getAttribute(WebConstants.STUDENT_INFO);
		String schoolId = student.getGjtSchoolInfo().getId();
		GjtSetOrgCopyright item = gjtSetOrgCopyrightService.findBySchoolIdAndPlatfromType(schoolId,
				PlatfromTypeEnum.PERCENTPLATFORM.getNum());
		item.setXxCode(student.getGjtSchoolInfo().getGjtOrg().getCode());
		return item;
	}
}
