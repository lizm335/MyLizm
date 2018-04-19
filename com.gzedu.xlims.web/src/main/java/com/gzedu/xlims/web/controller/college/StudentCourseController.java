/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.college;

import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.dto.StudentCourseDto;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 学员选课管理控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年05月31日
 * @version 2.5
 */
@Controller
@RequestMapping( "/edumanage/studentCourseCollege" )
public class StudentCourseController extends BaseController {

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtGradeService gjtGradeService;

	private File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * 学员选课列表
	 * @param pageNumber
	 * @param pageSize
     * @return
     */
	@RequestMapping(value = "/list")
	public String querySource(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
							  @RequestParam(value = "page.size", defaultValue = "10") int pageSize, ModelMap model,
							  HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		String xxId = searchParams.get("EQ_gjtSchoolInfo.id") != null ? searchParams.get("EQ_gjtSchoolInfo.id").toString() : null;
		if(StringUtils.isBlank(xxId)) {
			xxId = user.getGjtOrg().getId();
			searchParams.put("EQ_gjtSchoolInfo.id", xxId);
			model.addAttribute("defaultXxId", xxId);
		}
		Object gradeId = searchParams.get("EQ_viewStudentInfo.gradeId");
		if(gradeId == null) {
			gradeId = gjtGradeService.getCurrentGradeId(xxId);
			searchParams.put("EQ_viewStudentInfo.gradeId", gradeId);
			model.addAttribute("defaultGradeId", gradeId);
		}
		Page<StudentCourseDto> page = gjtStudentInfoService.queryStudentCourseByPage(searchParams, pageRequst);

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
		return "edumanage/college/studentCourse/college_student_course_list";
	}

	/**进入导入页面*/
	@RequiresPermissions("/edumanage/studentCourseCollege/list$import")
	@RequestMapping(value = "toImport" )
	public String toImport(){
		return "edumanage/college/studentCourse/college_student_course_import";
	}

	/**
	 * 导入学员选课
	 * @param file
	 * @return
     * @throws Exception
     */
	@SysLog("学员选课-导入学员选课")
	@RequiresPermissions("/edumanage/studentCourseCollege/list$import")
	@ResponseBody
	@RequestMapping(value = "importStudentCourse")
	public Map importStudentCourse(HttpServletRequest request,  HttpServletResponse response, @RequestParam(value = "file", required = false) MultipartFile file)throws Exception{
		String path = request.getSession().getServletContext().getRealPath("")
				+ WebConstants.EXCEL_DOWNLOAD_URL + "studentCollege" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if(!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		file.transferTo(targetFile);

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map result = gjtStudentInfoService.importCollegeStudentTakeCourse(targetFile, path, user.getGjtOrg().getId(), user.getId());
		targetFile.delete();
		return result;
	}

	/**下载文件*/
	@ResponseBody
	@RequestMapping(value = "download" )
	public void download(HttpServletRequest request,  HttpServletResponse response, String file) throws IOException {
		if ("template".equals(file)) {
			InputStream in = this.getClass().getResourceAsStream("/excel/model/学员选课导入表.xls");
			super.downloadInputStream(response, in, "学员选课导入表.xls");
		} else {
			String path = request.getSession().getServletContext().getRealPath("")
					+ WebConstants.EXCEL_DOWNLOAD_URL + "studentCollege" + File.separator;
			super.downloadFile(request, response, path + file);
		}
	}

}