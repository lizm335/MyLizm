/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.signup;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 学籍统计控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年09月20日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/schoolRollStatistics")
public class RecruitstatisticsController extends BaseController {

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private CommonMapService commonMapService;

	/**
	 * 加载学籍统计页
     * @return
     */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String viewForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Object gradeId = searchParams.get("gradeId");
		if(gradeId == null) {
			String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(user.getGjtOrg().getId());
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("gradeId", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 年级
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());// 培养层次

		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("action", "view");
		return "edumanage/schoolRollStatistics/view";
	}

}
