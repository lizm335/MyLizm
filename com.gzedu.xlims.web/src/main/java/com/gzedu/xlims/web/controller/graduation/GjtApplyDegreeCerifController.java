/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.graduation;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.FileKit;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzdec.framework.util.WordTemplateUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.graduation.GjtDegreeCollegeDao;
import com.gzedu.xlims.dao.graduation.GjtGraApplyFlowRecordDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationApplyCertifDao;
import com.gzedu.xlims.dao.graduation.GjtGraduationPlanDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.graduation.*;
import com.gzedu.xlims.pojo.status.DegreeAuditRoleEnum;
import com.gzedu.xlims.pojo.status.DegreeRequirementTypeEnum;
import com.gzedu.xlims.pojo.status.EnumUtil;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.graduation.*;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * 功能说明：
 * 
 * @author wangzhuangbi@51feijin.com
 *
 */
@Controller
@RequestMapping("/graduation/degreeCerif")
public class GjtApplyDegreeCerifController extends BaseController {

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtApplyDegreeCertifService gjtApplyDegreeCertifService;

	@Autowired
	private GjtDegreeCollegeDao gjtDegreeCollegeDao;

	@Autowired
	private GjtGraduationPlanDao gjtGraduationPlanDao;

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

	@Autowired
	private GjtGraduationApplyCertifService gjtGraduationApplyCardService;

	@Autowired
	private GjtGraduationRegisterService gjtGraduationRegisterService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	private static final Log log = LogFactory.getLog(GjtApplyDegreeCerifController.class);

	/**
	 * 查询毕业证申请列表
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
		Page<GjtApplyDegreeCertif> pageInfo = gjtGraduationApplyCertifService
				.queryGraduationApplyDegreeByPage(searchParams, pageRequst);

		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
		Map<String, String> studyCenterMap = commonMapService.getStudyCenterMap(user.getGjtOrg().getId());// 中心
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(user.getGjtOrg().getId());// 专业
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
//		Map<String, String> pyccMap = commonMapService.getPyccMap(user.getGjtOrg().getId());
		// 查询存在毕业计划的学期
		Map<String, String> graduationMap = commonMapService.getGraduationGradeMap(user.getGjtOrg().getId());
		Map<String, Long> countAuditStateMap = gjtApplyDegreeCertifService.countGroupbyAuditState(searchParams);

		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("countAuditStateMap", countAuditStateMap);
		model.addAttribute("graduationPlanMap", graduationPlanMap);
		model.addAttribute("studyCenterMap", studyCenterMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
//		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("graduationMap", graduationMap);
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "graduation/degree/certif/apply_degree_certif_list";
	}


	@RequestMapping(value = "importApplyFlowRecord", method = RequestMethod.GET)
	public String importApplyFlowRecord(HttpServletRequest request, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> graduationPlanMap = commonMapService.getGraduationPlanMap(user.getGjtOrg().getId());// 毕业计划
		String phone = ObjectUtils.toString(user.getSjh());
		if (EmptyUtils.isNotEmpty(phone)) {
			model.addAttribute("mobileNumber", phone.substring(phone.length() - 4, phone.length()));
		}
		model.addAttribute("graduationPlanMap", graduationPlanMap);
		return "/graduation/degree/certif/import_form";
	}

	/**
	 * 导入审核记录
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月28日 下午6:01:03
	 * @param file
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "importApplyFlowRecord", method = RequestMethod.POST)
	@ResponseBody
	public ImportFeedback importApplyFlowRecord(HttpServletRequest request, HttpServletResponse response,
												@RequestParam("file") MultipartFile file, @RequestParam("planId") String planId){
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			String[] heads = { "姓名", "学号", "分部审核结果", "分部审核备注", "总部审核结果", "总部审核备注",  "电子注册号", "结果" };
			List<String[]> successList = new ArrayList<String[]>();
			List<String[]> failedList = new ArrayList<String[]>();
			List<String[]> dataList = null;
			try {
				dataList = ExcelUtil.readAsStringList(file.getInputStream(), 3, heads.length - 1);
			} catch (Exception e) {
				return new ImportFeedback(false, "请下载正确表格模版填写");
			}
			Date now = new Date();
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) { // 学号
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}
					GjtStudentInfo studentInfo = gjtStudentInfoService.queryByXh(datas[1]);
					if (studentInfo == null) {
						result[heads.length - 1] = "学号不存在";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) { // 姓名
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if (!datas[0].equals(studentInfo.getXm())) {
						result[heads.length - 1] = "姓名和学号不对应";
						failedList.add(result);
						continue;
					}

					GjtApplyDegreeCertif apply = gjtApplyDegreeCertifService.queryApplyDegreeCertifByStudentIdAndPlanId(studentInfo.getStudentId(), planId);
					if ("".equals(datas[2]) && !"".equals(datas[4]) && apply!= null && apply.getAuditState() ==0 ){
						result[heads.length - 1] = "省校审核人还未审核";
						failedList.add(result);
						continue;
					}
					/*if (apply != null && apply.getInformationAudit()!=11 && !"".equals(datas[4])) {
						result[heads.length - 1] = "学生还未申请学位";
						failedList.add(result);
						continue;
					}*/
					if (apply == null  && !"".equals(datas[4])) {
						result[heads.length - 1] = "学生还未申请学位,中央不能审核";
						failedList.add(result);
						continue;
					}

					if (apply != null) {
						apply.setUpdatedBy(user.getId());
						apply.setUpdatedDt(new Date());
						createRecordByType(apply,datas, 1);
						apply = tranformDegree(apply, datas);
					}else{

						GjtDegreeCollege gjtDegreeCollege = gjtDegreeCollegeDao.queryByOrgID(studentInfo.getXxId());
						if (gjtDegreeCollege == null) {
							result[heads.length - 1] = "该学生对应的院校未添加学位院校";
							failedList.add(result);
							continue;
						}
						apply = createDegree(studentInfo, gjtDegreeCollege, datas, user, planId);
						createRecordByType(apply,datas, 0);
					}
					gjtApplyDegreeCertifService.save(apply);

					result[heads.length - 1] = "导入成功";
					successList.add(result);

				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "degreeApplyAuditRecord_success_" + currentTimeMillis + ".xls";
			String failedFileName = "degreeApplyAuditRecord_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "导入成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "导入失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL + "degreeApplyAuditRecord"
					+ File.separator;
			File f = new File(filePath);
			if (!f.exists()) {
				f.mkdirs();
			}

			File successFile = new File(filePath, successFileName);
			successFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook1, successFile);

			File failedFile = new File(filePath, failedFileName);
			failedFile.createNewFile();
			ExcelUtil.writeWorkbook(workbook2, failedFile);

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName, failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	/**
	 * 记录审核记录
	 * @param entity
	 * @param data
	 * @param type 0-创建 1-修改
	 */
	private void createRecordByType(GjtApplyDegreeCertif entity, String[] data, int type){
		int fbnAuditResult = Strings.isNullOrEmpty(data[2]) ? 0 : ("通过".equals(data[2]) ? 1 : 2);
		int zbnAuditResult = Strings.isNullOrEmpty(data[4]) ? 0 : ("通过".equals(data[4]) ? 1 : 2);

		if(type ==0){
			if(fbnAuditResult != 0){gjtApplyDegreeCertifService.createRecord(entity,fbnAuditResult, 3, data[3]);}
		}
		if(type ==1){
			if( fbnAuditResult !=0 && fbnAuditResult != entity.getAuditState()){gjtApplyDegreeCertifService.createRecord(entity,fbnAuditResult, 3, data[3]);}
			if( zbnAuditResult !=0 && zbnAuditResult+10 != entity.getAuditState()){gjtApplyDegreeCertifService.createRecord(entity,zbnAuditResult, 4, data[5]);}
		}
	}



	/**
	 * 构建学位申请
	 * @param info
	 * @param gjtDegreeCollege
	 * @param data
	 * @param user
	 * @param planId
	 * @return
	 */
	private GjtApplyDegreeCertif createDegree(GjtStudentInfo info, GjtDegreeCollege gjtDegreeCollege, String[] data, GjtUserAccount user, String planId) {
		GjtApplyDegreeCertif apply = new GjtApplyDegreeCertif();

		apply.setApplyId(UUIDUtils.random());
		apply.setCreatedDt(new Date());
		apply.setCreatedBy(user.getId());
		apply.setGjtGraduationPlan(gjtGraduationPlanDao.findById(planId));
		apply.setGjtStudentInfo(info);
		apply.setStudentId(info.getStudentId());
		apply.setGjtDegreeCollege(gjtDegreeCollege);
		apply.setCollegeId(gjtDegreeCollege.getCollegeId());
		apply.setEleRegistrationNumber(data[6]);

		apply.setAuditState(Strings.isNullOrEmpty(data[2]) ? 0 : ("通过".equals(data[2]) ? 1 : 2));
		apply.setInformationAudit(0);
		apply.setApplyDegree(0);
		GjtGraduationApplyCertif certif = gjtGraduationApplyCertifService.queryByStudentIdAndPlanId(info.getStudentId(),planId);
		if( certif !=null){
			apply.setApplyDegree(certif.getApplyDegree());
		}
		apply.setIsReceive(0);
		int degreeCondition = (apply.getAuditState() !=0 && apply.getAuditState() !=2) ? 1: 0;
		apply.setDegreeCondition(degreeCondition);
		log.info("entity:[" + apply + "]");
		return apply;
	}

	/**
	 * 更新学位申请
	 * @param apply
	 * @param datas
	 * @return
	 */
	private GjtApplyDegreeCertif tranformDegree(GjtApplyDegreeCertif apply, String[] datas) {
		if( !"".equals(datas[2])){
			int auditState = "通过".equals(datas[2]) ? 1: 2;
			apply.setAuditState(auditState);
		}
		if( !"".equals(datas[4])){
			int auditState = "通过".equals(datas[4]) ? 11: 12;
			apply.setAuditState(auditState);
		}
		apply.setEleRegistrationNumber(datas[6]);
		int degreeCondition = (apply.getAuditState() !=0 && apply.getAuditState() !=2) ? 1: 0;
		apply.setDegreeCondition(degreeCondition);
		return apply;
	}

	/**
	 * 学位申请详情
	 *
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月21日 下午5:19:09
	 * @param request
	 * @param model
	 * @param applyId
	 * @return
	 */
	@RequestMapping(value = "view", method = RequestMethod.GET)
	public String view(HttpServletRequest request, Model model, String applyId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//		GjtGraduationApplyDegree info = gjtGraduationApplyDegreeService.queryById(applyId);
		GjtApplyDegreeCertif info = gjtApplyDegreeCertifService.queryById(applyId);
		info.setGjtGraduationRegister(gjtGraduationRegisterService.queryByStudentId(info.getGjtGraduationPlan().getId(), info.getGjtStudentInfo().getStudentId()));
		model.addAttribute("info", info);

		// 审核记录
		List<GjtGraApplyFlowRecord> flowRecordList = gjtGraduationApplyCardService.queryGraApplyFlowRecordByApplyId(applyId);
		for (GjtGraApplyFlowRecord item :flowRecordList) {
			GjtUserAccount operator = gjtUserAccountService.findOne(item.getAuditOperator());
			item.setAuditOperator(operator.getRealName());
		}
		model.addAttribute("flowRecordList", flowRecordList);
		if (CollectionUtils.isNotEmpty(flowRecordList)) {
			model.addAttribute("lastRecord", flowRecordList.get(flowRecordList.size() - 1));
		}
		// 获取用户对应的审核角色
		Integer auditRole = DegreeAuditRoleEnum.getCode(user.getPriRoleInfo().getRoleId());
		model.addAttribute("auditRole", auditRole);
		model.addAttribute("baseTypeMap", EnumUtil.getDegreeRequirementTypeMap());
		return "graduation/degree/certif/degree_apply_form";
	}

	/**
	 * 审核毕业资料
	 * @param request
	 * @param applyId
	 * @param state
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "informationAudit", method = RequestMethod.POST)
	public Map<String, Object> informationAudit(HttpServletRequest request, String applyId, int state, String content) {
		Map<String, Object> result = Maps.newHashMap();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtApplyDegreeCertif gjtApplyDegreeCertif = gjtApplyDegreeCertifService.queryById(applyId);
		if(gjtApplyDegreeCertif == null){
			result.put("state", false);
			result.put("message", "系统异常");
			return result;
		}
		gjtApplyDegreeCertif.setInformationAudit(state);
		gjtApplyDegreeCertif.setInformationAuditOpinion(content);
		gjtApplyDegreeCertif.setUpdatedDt(new Date());
		gjtApplyDegreeCertif.setUpdatedBy(user.getId());
		gjtApplyDegreeCertifService.save(gjtApplyDegreeCertif);
		result.put("state", true);
		return result;
	}

	@RequestMapping(value = "exportDegreeApply")
	@SysLog("毕业管理-导出学位申请记录数据")
	@ResponseBody
	public void exportDegreeApply(HttpServletRequest request, HttpServletResponse response) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map formMap = Servlets.getParametersStartingWith(request, "");
		try {
			formMap.put("EQ_orgId", user.getGjtOrg().getId());
			Workbook wb = commonMapService.exportDegreeApply(formMap, request, response);
			response.setContentType("application/x-msdownload;charset=utf-8");
			response.setHeader("Content-Disposition",
					"attachment; filename=" + new String(("学位申请记录.xls").getBytes("UTF-8"), "ISO8859-1"));
			wb.write(response.getOutputStream());
			request.getSession().setAttribute(user.getSjh(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量下载学位申请资料
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "downloadReqFile", method = RequestMethod.GET)
	public String downloadReqFile(HttpServletRequest request, Model model) {

		return "/graduation/degree/certif/download_form";
	}

	@RequestMapping(value = "download", method = RequestMethod.GET)
	public void downloadReqFile(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
			searchParams.put("EQ_orgId", user.getGjtOrg().getId());
			String outputFilePath = gjtApplyDegreeCertifService.downloadReqFile(searchParams,
					request.getSession().getServletContext().getRealPath(""));
			super.downloadFile(request, response, outputFilePath);
			FileKit.delFile(outputFilePath);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
