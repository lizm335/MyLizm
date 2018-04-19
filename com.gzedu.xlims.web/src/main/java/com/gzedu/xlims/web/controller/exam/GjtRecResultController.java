
package com.gzedu.xlims.web.controller.exam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.exam.GjtExamAppointmentNewService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.ActionCacheLogKit;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.cn
 * @Date 2016年12月21日
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/edumanage/exam/score")
public class GjtRecResultController {

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtRecResultService gjtRecResultService;

	@Autowired
	private GjtExamAppointmentNewService gjtExamAppointmentNewService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber, @RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Page<Map<String, Object>> page = gjtRecResultService.queryAllBySql(user.getGjtOrg().getId(), searchParams, pageRequst);

		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<BigDecimal, String> examTypeMap = commonMapService.getExamTypeMap();
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId()); //年级

		model.addAttribute("pageInfo", page);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "edumanage/exam/exam_score_list";
	}

	@RequestMapping(value = "importUnifySource", method = RequestMethod.GET)
	public String importUnifySource(Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		return "edumanage/exam/mydialog/exam_score_import_unify";
	}

	@RequestMapping(value = "importRegisterSource", method = RequestMethod.GET)
	public String importRegisterSource(Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		return "edumanage/exam/mydialog/exam_score_import_register";
	}

	@RequestMapping(value = "exportSource", method = RequestMethod.GET)
	public String exportSource(Model model, HttpServletRequest request, HttpServletResponse response) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		model.addAttribute("user",user);
		return "edumanage/exam/mydialog/exam_score_export";
	}

	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public String queryById(String id, ModelMap model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> map = gjtRecResultService.queryStudent(id); // 查询学员个人信息
		List<Map<String, String>> list = gjtRecResultService.queryTerm(id);// 总共多少学期
		List<List<Map<String, String>>> listResult = new ArrayList<List<Map<String, String>>>();
		for (Map entity : list) {
			BigDecimal bigDecimal = (BigDecimal) entity.get("TERM_TYPE_CODE");
			List<Map<String, String>> list1 = gjtRecResultService.queryStudentSourceDetail(id, String.valueOf(bigDecimal));// 每个学期的明细
			listResult.add(list1);
		}

		Map<String, String> pyccMap = commonMapService.getPyccMap();// 培养层次
		Map<BigDecimal, String> examTypeMap = commonMapService.getExamTypeMap(); // 考试方式
		String specialtyId = (String) map.get("SPECIALTY_ID");
		List studentand = gjtRecResultService.getCreditInfoAnd(id, specialtyId);
		List studentandPass = gjtRecResultService.getPassCreditInfoAnd(id, specialtyId);
		List minandsum = gjtRecResultService.getMinAndSum(id, specialtyId);

		Integer totalCoursePass = 0;
		Integer totalCourse = 0;
		if (studentand != null && studentand.size() > 0) {
			int num = 0;
			for (int i = 0; i < studentand.size(); i++) {
				Map bean = (Map) studentand.get(i);
				String s_course_code = String.valueOf(bean.get("KCLBM_CODE"));// 拿到KCLBM_CODE
				String s_kssl = String.valueOf(bean.get("KCSL"));
				int number = 0;
				if (studentandPass != null && studentandPass.size() > 0) {
					for (int j = 0; j < studentandPass.size(); j++) {
						Map pbean = (Map) studentandPass.get(j);
						String p_course_code = String.valueOf(pbean.get("KCLBM_CODE"));// 拿到KCLBM_CODE
						if (s_course_code.equals(p_course_code)) {// 比较KCLBM_CODE一样的就是PASS的
							bean.put("pass", String.valueOf(pbean.get("PASS")));
							bean.put("zt", String.valueOf(pbean.get("PASS")).equals(s_kssl) ? "1" : "0");
							if (pbean.get("PASS").equals(s_kssl)) {// 通过的课程 累加
								number++;
							}
						}
					}
				}
				totalCoursePass += number;
				num++;
			}
			totalCourse += num;
		}
		Integer yxxf = 0; // 已修分数
		Integer zxf = 0; // 总分数
		Integer zdxf = 0;// 最低分数
		for (int i = 0; i < minandsum.size(); i++) {
			Map aa = (Map) minandsum.get(i);
			yxxf = NumberUtils.toInt((String) aa.get("YXXF"));
			zxf = NumberUtils.toInt((String) aa.get("XF"));
			zdxf = NumberUtils.toInt((String) aa.get("ZDXF"));
		}
		Map studentCredit = new HashMap();
		studentCredit.put("yxxf", yxxf);
		studentCredit.put("zxf", zxf);
		studentCredit.put("zdxf", zdxf);
		studentCredit.put("studentand", studentand);
		studentCredit.put("minandsum", minandsum);

		model.addAttribute("totalCourse", totalCourse);
		model.addAttribute("totalCoursePass", totalCoursePass);
		model.addAttribute("studentCredit", studentCredit);
		model.addAttribute("examTypeMap", examTypeMap);
		model.addAttribute("map", map);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("listResult", listResult);
		return "edumanage/exam/exam_score_list_detail";
	}
	
	@RequestMapping(value = "/getSmsCode",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSmsCode (HttpServletRequest request,HttpServletResponse response){
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("APP_ID", "YX0001SMSCHECK");// 短信模板 固定
        param.put("MOBILE", ObjectUtils.toString(user.getSjh()));
        param.put("REAL_NAME", ObjectUtils.toString(user.getRealName()));
        Map result = ActionCacheLogKit.getSmsCode(param);
        return result;
	}

}
