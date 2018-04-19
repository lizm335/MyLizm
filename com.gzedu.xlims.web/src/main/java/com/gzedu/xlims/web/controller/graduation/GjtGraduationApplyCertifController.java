package com.gzedu.xlims.web.controller.graduation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.GjtGraApplyFlowRecord;
import com.gzedu.xlims.pojo.graduation.GjtGraduationApplyCertif;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegisterEdu;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationRegisterService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;

/**
 * 毕业，申请毕业证控制层<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017-09-16
 * @version 3.0
 */
@Controller
@RequestMapping("/graduation/applyCertif")
public class GjtGraduationApplyCertifController extends BaseController {

	private static final Log log = LogFactory.getLog(GjtGraduationApplyCertifController.class);

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	/**
	 * 查询毕业申请列表
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_orgId", user.getGjtOrg().getId());
		Page<GjtGraduationApplyCertif> pageInfo = gjtGraduationApplyCertifService
				.queryGraduationApplyCardByPage(searchParams, pageRequst);

		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
		// 查询存在毕业计划的学期
		Map<String, String> graduationMap = commonMapService.getGraduationGradeMap(user.getGjtOrg().getId());
		Map<String, Long> countAuditStateMap = gjtGraduationApplyCertifService.countGroupbyAuditState(searchParams);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("countAuditStateMap", countAuditStateMap);
		model.addAttribute("graduationPlanMap", graduationPlanMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("graduationMap", graduationMap);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "graduation/apply_certif/graduation_apply_certif_list";
	}

	/**
	 * 返回详情页面
	 *
	 * @param id
	 * @param model
	 * @return
	 */
	@RequiresPermissions("/graduation/applyCertif/list$view")
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String view(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtGraduationApplyCertif info = gjtGraduationApplyCertifService.queryById(id);
		info.setGjtGraduationRegister(gjtGraduationRegisterService.queryByStudentId(info.getGjtGraduationPlan().getId(),
				info.getGjtStudentInfo().getStudentId()));
		model.addAttribute("info", info);

		List<GjtGraApplyFlowRecord> flowRecordList = gjtGraduationApplyCertifService
				.queryGraApplyFlowRecordByApplyId(id);
		for (GjtGraApplyFlowRecord item :flowRecordList) {
			GjtUserAccount operator = gjtUserAccountService.findOne(item.getAuditOperator());
			item.setAuditOperator(operator.getRealName());
		}
		model.addAttribute("flowRecordList", flowRecordList);
		model.addAttribute("action", request.getParameter("action") != null ? request.getParameter("action") : "view");
		return "graduation/apply_certif/graduation_apply_certif_form";
	}

	/**
	 * 下载毕业生登记表
	 *
	 * @param studentId
	 * @param response
	 * @throws Exception
	 */
	@SysLog("毕业管理-毕业申请-下载毕业生登记表")
	@RequiresPermissions("/graduation/applyCertif/list$downloadGraduationRegister")
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void download(@RequestParam("studentId") String studentId, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> registerMsg = gjtGraduationApplyCertifService.queryStudentRegisterMsg(studentId);
		String studentName = registerMsg.get("studentName").toString();
		String fileName = "国家开放大学毕业生登记表——" + studentName + ".doc";

		DateFormat df = new SimpleDateFormat("yyyy.MM");
		List<Map<String, String>> eduList = new ArrayList<Map<String, String>>();
		List<GjtGraduationRegisterEdu> registerEduList = gjtGraduationRegisterService.queryEduByStudentId(studentId);
		if (registerEduList != null && registerEduList.size() > 0) {
			int size = registerEduList.size();
			if (size < 3) {
				int n = 0;
				for (GjtGraduationRegisterEdu registerEdu : registerEduList) {
					Map<String, String> edu = new HashMap<String, String>();
					String time = "";
					if (registerEdu.getBeginTime() != null) {
						time += df.format(registerEdu.getBeginTime());
					}
					if (registerEdu.getEndTime() != null) {
						time += "--" + df.format(registerEdu.getEndTime());
					}
					time += " ";
					edu.put("time", time);
					if (registerEdu.getRegion() != null) {
						edu.put("region", registerEdu.getRegion());
					} else {
						edu.put("region", " ");
					}
					if (registerEdu.getSchool() != null) {
						edu.put("school", registerEdu.getSchool());
					} else {
						edu.put("school", " ");
					}

					eduList.add(edu);
					n++;
				}
				for (; n < 4; n++) { // 凑够4行
					Map<String, String> edu = new HashMap<String, String>();
					edu.put("time", " ");
					edu.put("region", " ");
					edu.put("school", " ");

					eduList.add(edu);
				}
			} else { // 只取前3行
				for (int i = 0; i < 3; i++) {
					GjtGraduationRegisterEdu registerEdu = registerEduList.get(i);
					Map<String, String> edu = new HashMap<String, String>();
					String time = "";
					if (registerEdu.getBeginTime() != null) {
						time += df.format(registerEdu.getBeginTime());
					}
					if (registerEdu.getEndTime() != null) {
						time += "--" + df.format(registerEdu.getEndTime());
					}
					time += " ";
					edu.put("time", time);
					if (registerEdu.getRegion() != null) {
						edu.put("region", registerEdu.getRegion());
					} else {
						edu.put("region", " ");
					}
					if (registerEdu.getSchool() != null) {
						edu.put("school", registerEdu.getSchool());
					} else {
						edu.put("school", " ");
					}

					eduList.add(edu);
				}
			}
		} else { // 放4个空行
			for (int i = 0; i < 4; i++) {
				Map<String, String> edu = new HashMap<String, String>();
				edu.put("time", " ");
				edu.put("region", " ");
				edu.put("school", " ");

				eduList.add(edu);
			}
		}
		registerMsg.put("eduList", eduList);

		Object photo = registerMsg.get("photo");
		if (photo != null && !"".equals(photo.toString().trim())) {
			String realPath = request.getSession().getServletContext().getRealPath("");
			try {
				String encode = WordTemplateUtil.getRemoteSourceEncode(photo.toString().trim(), realPath);
				registerMsg.put("photo", encode);
			} catch (Exception e) {
				registerMsg.put("photo", " ");
				log.error(e.getMessage(), e);
			}
		} else {
			registerMsg.put("photo", " ");
		}

		response.setContentType("application/msword;charset=utf-8");
		response.setHeader("Content-Disposition",
				"attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1"));
		WordTemplateUtil.createWord(registerMsg, "国家开放大学毕业生登记表.ftl", response.getOutputStream());
	}

	/**
	 * 毕业申请审核
	 *
	 * @param id
	 * @param auditState
	 * @param auditContent
	 * @return
	 */
	@SysLog("毕业管理-毕业申请-审核")
	@RequiresPermissions("/graduation/applyCertif/list$approval")
	@RequestMapping(value = "audit", method = RequestMethod.POST)
	public String update(@RequestParam("id") String id, @RequestParam("auditState") Integer auditState,
			String auditContent, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "审核成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		try {
			ResultFeedback result = gjtGraduationApplyCertifService.auditGraduationApply(id, auditState, auditContent,
					getAuditRole(request), user.getRealName());
			if (!result.isSuccessful()) {
				feedback = new Feedback(false, result.getMessage());
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "审核失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/graduation/applyCertif/view/" + id + "?action=audit";
	}

	/**
	 * 获取当前登录用户对应的审核角色
	 *
	 * @param request
	 * @return
	 */
	private int getAuditRole(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		return 0;
	}

	/**
	 * 批量下载报名登记表
	 *
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "batchDownloadRegister")
	@ResponseBody
	public void batchDownloadRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("xxId", user.getGjtOrg().getId());
		String outputFilePath = gjtGraduationRegisterService.batchDownloadRegister(searchParams, null,
				request.getSession().getServletContext().getRealPath(""));
		super.downloadFile(request, response, outputFilePath);
		FileKit.delFile(outputFilePath);
	}

	/** 进入导入页面 */
	@RequestMapping(value = "toImport")
	public String toImport(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
		model.addAttribute("graduationPlanMap", graduationPlanMap);
		return "graduation/apply_certif/graduation_apply_certif_import";
	}

	/** 下载文件 */
	@ResponseBody
	@RequestMapping(value = "downloadTemplate")
	public void download(HttpServletRequest request, HttpServletResponse response, String file) throws IOException {
		if ("template".equals(file)) {
			InputStream in = this.getClass().getResourceAsStream("/excel/model/导入毕业审核记录模版.xls");
			super.downloadInputStream(response, in, "导入毕业审核记录模版.xls");
		} else {
			String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "graduation" + File.separator;
			super.downloadFile(request, response, path + file);
		}
	}

	/** 导入毕业审核记录 */
	@SysLog("毕业申请-导入毕业审核记录")
	@RequiresPermissions("/graduation/applyCertif/list$importGraduationAudit")
	@ResponseBody
	@RequestMapping(value = "importGraduationAudit")
	public Map importStuInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("planId") String planId)
			throws Exception {
		String path = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
				+ "graduation" + File.separator;
		String fileName = file.getOriginalFilename();
		File targetFile = new File(path, fileName);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		file.transferTo(targetFile);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map result = gjtGraduationApplyCertifService.importGraduationAudit(targetFile, path, user.getId(), planId);
		targetFile.delete();
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "addCardUrl", method = RequestMethod.POST)
	public Feedback addCardUrl(HttpServletRequest request) {
		Feedback fb = new Feedback(true, "上传成功");
		String[] imgPaths = request.getParameterValues("imgPaths");
		try {
			if (EmptyUtils.isNotEmpty(imgPaths)) {
				List<String> cardNos = new ArrayList<String>();
				for (String imgPath : imgPaths) {
					String cardNo = imgPath.substring(imgPath.lastIndexOf("/") + 1);
					cardNos.add(cardNo.substring(0, cardNo.indexOf(".")));
				}
				List<Map<String, String>> queryBySfzh = gjtStudentInfoService.queryBySfzh(cardNos);
				for (Map<String, String> map : queryBySfzh) {
					String studentId = map.get("studentId");
					String cardNo = map.get("cardNo");
					for (String photoUrl : imgPaths) {
						if (photoUrl.contains(cardNo)) {
							gjtStudentInfoService.updatePhoto(studentId, photoUrl);
							gjtGraduationRegisterService.updatePhoto(studentId, photoUrl);
							gjtGraduationApplyCertifService.updatePhotoUrl(studentId, photoUrl);
							continue;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			fb = new Feedback(false, "上传失败：" + e.getMessage());
		}

		return fb;
	}

	@RequestMapping(value = "exportGraduationApply")
	@SysLog("毕业管理-导出毕业申请记录数据")
	@ResponseBody
	public void exportGraduationApply(HttpServletRequest request, HttpServletResponse response) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		try {
			formMap.put("EQ_orgId", user.getGjtOrg().getId());
			Workbook wb = gjtGraduationApplyCertifService.exportGraduationApply(formMap, request, response);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(("毕业申请记录.xls").getBytes("UTF-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
			request.getSession().setAttribute(user.getSjh(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
