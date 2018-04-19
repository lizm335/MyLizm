/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.college;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.StudentSignupInfoDto;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 学员信息管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月23日
 * @version 2.5
 */
@Controller
@RequestMapping("/edumanage/studentCollege")
public class StudentController extends BaseController {

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private CacheService cacheService;

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@RequestMapping(value = "/list")
	public String querySource(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String xxId = searchParams.get("EQ_gjtSchoolInfo.id") != null
				? searchParams.get("EQ_gjtSchoolInfo.id").toString() : null;
		if (StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_gjtSchoolInfo.id", xxId);
			model.addAttribute("defaultXxId", xxId);
		}
		Object gradeId = searchParams.get("EQ_viewStudentInfo.gradeId");
		if (gradeId == null) {
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("EQ_viewStudentInfo.gradeId", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}
		Page<StudentSignupInfoDto> page = gjtStudentInfoService.queryStudentInfoByPage(searchParams, pageRequst);

		Map<String, String> schoolInfoMap = commonMapService.getOrgMap(user.getId()); // 学校
		// Map<String, String> orgMap =
		// commonMapService.getOrgMap(user.getId()); // 机构
		Map<String, String> specialtyMap = commonMapService.getSchoolModelSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> yearMap = commonMapService.getYearMap(user.getGjtOrg().getId());// 年级
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());

		model.addAttribute("pageInfo", page);
		model.addAttribute("schoolInfoMap", schoolInfoMap);
		model.addAttribute("yearMap", yearMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		// model.addAttribute("orgMap", orgMap);
		model.addAttribute("specialtyMap", specialtyMap);
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "edumanage/college/student/college_student_list";
	}

	@RequiresPermissions("/edumanage/studentCollege/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String queryById(@PathVariable("id") String id, ModelMap model) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);

		// 证件资料
		Map<String, String> signupCopyData = gjtSignupService.getSignupCopyData(id);
		model.addAttribute("signupCopyData", signupCopyData);

		model.addAttribute("info", studentInfo);
		model.addAttribute("action", "update");
		return "edumanage/college/student/college_student_form";
	}

	// 修改对象
	@SysLog("学员管理-修改资料")
	@RequiresPermissions("/edumanage/studentCollege/list$update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpServletRequest request, RedirectAttributes redirectAttributes, GjtStudentInfo item) {
		Feedback feedback = new Feedback(true, "更新成功");
		GjtStudentInfo info = gjtStudentInfoService.queryById(item.getStudentId());
		info.setSjh(item.getSjh());
		info.setDzxx(item.getDzxx());
		Boolean updateEntity = gjtStudentInfoService.updateEntityAndFlushCache(info);
		
		String zp = request.getParameter("zp");
		if (StringUtils.isNotBlank(zp)) {
			gjtSignupService.updateSignupCopyData(item.getStudentId(), "zp", zp);
		}
		if (!updateEntity) {
			feedback = new Feedback(true, "服务器异常");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/edumanage/studentCollege/list";
	}

	// 模拟登录
	@RequiresPermissions("/edumanage/studentCollege/list$simulation")
	@RequestMapping(value = "/simulation", method = RequestMethod.GET)
	public String simulation(String id,HttpServletRequest request) {
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(id);
		String url = "";
		if (studentInfo != null) {
			url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(), studentInfo.getXh());
		}
		return "redirect:" + url; // 修改完重定向
	}

	// 重置密码
	@SysLog("学员管理-重置用户密码")
	@RequiresPermissions("/edumanage/studentCollege/list$reset")
	@RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updatePwd(String id) throws Exception {
		Feedback feedback = new Feedback(false, "网络异常");
		int i = gjtUserAccountService.updatePwd(id, Md5Util.encode(Constants.STUDENT_ACCOUNT_PWD_DEFAULT),
				Constants.STUDENT_ACCOUNT_PWD_DEFAULT);
		if (i > 0) {
			feedback = new Feedback(true, "密码重置成功");
		}
		return feedback;
	}

	/** 进入导入页面 */
	@RequiresPermissions("/edumanage/studentCollege/list$import")
	@RequestMapping(value = "toImport")
	public String toImport() {
		return "edumanage/college/student/college_student_import";
	}

	/** 导入学员信息 */
	@SysLog("学员管理-导入学员信息")
	@RequiresPermissions("/edumanage/studentCollege/list$import")
	@ResponseBody
	@RequestMapping(value = "importStuInfo")
	public Map importStuInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
		String path = request.getSession().getServletContext().getRealPath("")
				+ WebConstants.EXCEL_DOWNLOAD_URL + "studentCollege" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		file.transferTo(targetFile);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map result = gjtStudentInfoService.importCollegeStudentInfo(targetFile, path, user.getGjtOrg().getId(),
				user.getId());
		targetFile.delete();
		return result;
	}

	/** 下载文件 */
	@ResponseBody
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response, String file) throws IOException {
		if ("template".equals(file)) {
			InputStream in = this.getClass().getResourceAsStream("/excel/model/学员导入表.xls");
			super.downloadInputStream(response, in, "学员导入表.xls");
		} else {
			String path = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "studentCollege" + File.separator;
			super.downloadFile(request, response, path + file);
		}
	}

}