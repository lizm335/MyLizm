package com.gzedu.xlims.webservice.controller.gjt.student;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.exam.GjtExamRecordNewService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyCertifService;
import com.gzedu.xlims.service.graduation.GjtGraduationApplyDegreeService;
import com.gzedu.xlims.service.graduation.GjtGraduationPlanService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtSetOrgCopyrightService;
import com.gzedu.xlims.service.signup.GjtSignUpInfoDataService;
import com.gzedu.xlims.service.studymanage.StudyManageService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.controller.BaseController;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 职技院校云综合管理平台关于招生、学员的数据接口
 * 
 * @author lyj
 * @time 2017年6月12日 TODO
 */
@Controller
@RequestMapping("/interface/gjtStudentInfo")
public class GjtStudyInfoController extends BaseController {

    private static final Logger log = Logger.getLogger(GjtStudyInfoController.class);

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtOrgService gjtOrgService;
	@Autowired
	private StudyManageService studyManageService;
	
	@Autowired
	private GjtGradeService gjtGradeService;

	@Autowired
	private GjtSetOrgCopyrightService gjtSetOrgCopyrightService;
	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtSignUpInfoDataService gjtSignUpInfoDataService;

	@Autowired
	private GjtExamRecordNewService gjtExamRecordNewService;

    @Autowired
    private GjtGraduationPlanService gjtGraduationPlanService;

    @Autowired
    private GjtGraduationApplyCertifService gjtGraduationApplyCertifService;

    @Autowired
    private GjtGraduationApplyDegreeService gjtGraduationApplyDegreeService;

	@Autowired
	private CommonMapService commonMapService;
	
	@RequestMapping("/counts")
	@ResponseBody
	public ResponseResult countStudentInfo(String orgCode) {
		GjtOrg org = gjtOrgService.queryByCode(orgCode);
		ResponseResult result = null;
		if (org == null) {
			result = new ResponseResult(ResponseStatus.FAIL, "机构不存在,请检验机构编码!");
		} else {
			List<String> orgList = gjtOrgService.queryByParentId(org.getId());
			List<Map<String, String>>list = gjtStudentInfoService.countStudentInfoByOrgId(orgList);
			if(list != null && list.size() > 0) {
				result = new ResponseResult(ResponseStatus.SUCCESS, list.get(0));
			} else {
				result = new ResponseResult(ResponseStatus.SUCCESS, "查询没有数据返回!");
			}
		}
		return result;
	}

	/**
	 * 教学教务接口<br/>
	 * 13. 院校学期列表接口<br/>
	 * @param orgCode
	 * @return
     */
	@RequestMapping("gradeList")
	@ResponseBody
	public ResponseResult gradeList(String orgCode) {
		GjtOrg org = gjtOrgService.queryByCode(orgCode);
		if (org == null) {
			return new ResponseResult(ResponseStatus.FAIL, "机构不存在,请检验机构编码!");
		}
		List<GjtGrade> gradeList = gjtGradeService.findGradeIdByOrgId(org.getId());
		List<Object> list = Lists.newArrayList();
		int index = 0;
		for(GjtGrade g : gradeList) {
			Map<String,Object> map = Maps.newHashMap();
			map.put("id", g.getGradeId());
			map.put("name", g.getGradeName());
			map.put("code", g.getGradeCode());
			map.put("startDate", g.getStartDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(g.getStartDate()) : null);
			map.put("endDate", g.getEndDate() != null ? DateFormatUtils.ISO_DATE_FORMAT.format(g.getEndDate()) : null);
			map.put("index", index++);
			list.add(map);
		}
		return new ResponseResult(ResponseStatus.SUCCESS, list);
	}

	@RequestMapping("studyOrder")
	@ResponseBody
	public ResponseResult study(String orgCode, String gradeId, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer pageSize) {
		GjtOrg org = gjtOrgService.queryByCode(orgCode);
		ResponseResult result = null;
		if (org == null) {
			return new ResponseResult(ResponseStatus.FAIL, "机构不存在,请检验机构编码!");
		}
		if (StringUtils.isEmpty(gradeId)) {
			return new ResponseResult(ResponseStatus.FAIL, "请输入学期!");
		}
		Map<String, Object> searchParams = Maps.newHashMap();
		List<String> orgList = gjtOrgService.queryByParentId(org.getId());
		searchParams.put("XXZX_ID", orgList);
		searchParams.put("GRADE_ID", gradeId);
		Map<String, String> orderMap = Maps.newHashMap();
		orderMap.put("SUM_LOGIN_COUNT", "DESC");
		orderMap.put("SUM_LOGIN_TIME", "DESC");
		Page<Map<String, Object>> pageInfo = studyManageService.getStudentStudyList(searchParams,
				orderMap,new PageRequest(page, pageSize));

		int pageIndex = pageInfo.getNumber();
		long total = pageInfo.getTotalElements();
		List<Object> list = Lists.newArrayList();
		int index = page * pageSize;
		for (Map<String, Object> map : pageInfo.getContent()) {
			Map<String,Object> m = Maps.newHashMap();
			m.put("name", ObjectUtils.toString(map.get("NAME"))); // 姓名
			m.put("profession", ObjectUtils.toString(map.get("PROFESSION"))); // 专业
			m.put("gainCredit", ObjectUtils.toString(map.get("SUM_GET_CREDITS"))); // 获得学分
			m.put("studyNumberOfTimes", ObjectUtils.toString(map.get("SUM_LOGIN_COUNT"))); // 学习次数
			m.put("avatar", ObjectUtils.toString(map.get("AVATAR"))); // 头像
			String time = ObjectUtils.toString(map.get("SUM_LOGIN_TIME"));// 学习时长
			if (StringUtils.isNotEmpty(time)) {
				m.put("studyTimeLength", new BigDecimal(Double.parseDouble(time)/60)
						.setScale(2, BigDecimal.ROUND_HALF_UP).toString()); 
			}
			m.put("orderNo", index++);
			list.add(m);
		}
		Map<String,Object> resultMap = Maps.newHashMap();
		resultMap.put("result", list);
		resultMap.put("total", total);
		resultMap.put("pageIndex", pageIndex);
		result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
		return result;
	}

	@RequestMapping("list")
	@ResponseBody
	public ResponseResult list(String orgCode,String name, @RequestParam(defaultValue = "0") Integer page,
			@RequestParam(defaultValue = "20") Integer pageSize,HttpServletRequest request) {
		GjtOrg org = gjtOrgService.queryByCode(orgCode);
		ResponseResult result = null;
		if (org == null) {
			result = new ResponseResult(ResponseStatus.FAIL, "机构不存在,请检验机构编码!");
		} else {
			Map<String, Object> searchParams = Maps.newHashMap();
			List<String> orgList = gjtOrgService.queryByParentId(org.getId());
			searchParams.put("XXZX_ID", orgList);
			if(StringUtils.isNotEmpty(name)) {
				searchParams.put("NAME", name);
			}
			Page<Map<String, Object>> pageInfo = studyManageService.getStudentStudyList(searchParams,
					new PageRequest(page, pageSize));

			List<StudyVo> voList = Lists.newArrayList();
			for (Map<String, Object> map : pageInfo.getContent()) {
				StudyVo vo = new StudyVo();
				vo.setName(ObjectUtils.toString(map.get("NAME")));
				vo.setProfession(ObjectUtils.toString(map.get("PROFESSION")));
				vo.setPycc(ObjectUtils.toString(map.get("PYCC")));
				vo.setCreditStatus(ObjectUtils.toString(map.get("CREDIT_STATUS")));
				vo.setGrade(ObjectUtils.toString(map.get("GRADE")));
				vo.setCredit(ObjectUtils.toString(map.get("SUM_GET_CREDITS")));
				vo.setCreditSum(ObjectUtils.toString(map.get("ZXF")));
				vo.setStudyNumberOfTimes(ObjectUtils.toString(map.get("SUM_LOGIN_COUNT")));
				String time = ObjectUtils.toString(map.get("SUM_LOGIN_TIME"));
				if (StringUtils.isNotEmpty(time)) {
					vo.setStudyTimeLength(
							new BigDecimal(Double.parseDouble(time)/60).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
				}
				String enrollmentStatus = ObjectUtils.toString(map.get("IS_ENTERING_SCHOOL"));
				vo.setEnrollmentStatus("1".equals(enrollmentStatus) ? "已入学" : "未入学"); // 是否入学，默认为0(1:是 ,0:否)
				// 个人中心单点登录url
				String studentId = ObjectUtils.toString(map.get("STUDENT_ID"));
				String loginAccount = ObjectUtils.toString(map.get("LOGIN_ACCOUNT"));
				vo.setLoginUrl(buildStudyLoginUrl(studentId,loginAccount));
				voList.add(vo);
			}
			Map<String,Object> resultMap = Maps.newHashMap();
			resultMap.put("result", voList);
			resultMap.put("total", pageInfo.getTotalElements());
			resultMap.put("pageIndex", pageInfo.getNumber());
			result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
		}
		return result;
	}
	
	private String buildStudyLoginUrl(String studentId,String loginAccount) {
		String url = gjtUserAccountService.studentSimulation(studentId, loginAccount);
		return url;
	}

	/**
	 * 根据手机号查询学员身份证、姓名、学号
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getStudentBaseInfo")
	@ResponseBody
	public Map<String,Object> getStudentBaseInfo(Mode model,HttpServletRequest request,HttpSession session){
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("sjh", searchParams.get("sjh"));

		Map resultMap = studyManageService.getStudentBaseInfo(searchParams);

		return resultMap;

	}


	/**
	 * 1.学员报读专业名称
	 * 2.学员当前学期
	 * 3.学员已获得学分/总学分
	 * 4.学员待预约考试数
	 * 5.学员待考试科目数
	 * 6.当前学习课程数/总课程数
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/countStudyInfo")
	@ResponseBody
	public Map<String,Object> countStudyInfo(Model model, HttpServletRequest request, HttpSession session){
		Map searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams.put("atId", searchParams.get("atId"));

		log.info(searchParams.toString());

		//兼容专本连读情况,返回多条数据
		Map resultMap = studyManageService.countStudentLearnInfo(searchParams);

		return resultMap;
	}
	@RequestMapping(value="/countSpecialtyStudentInfo" ,method=RequestMethod.GET )
	@ResponseBody
	public ResponseResult countSpecialtyStudentInfo(String orgCode,String specialtyName) {
		// 1.校验参数
		if(StringUtils.isEmpty(orgCode)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "机构编码不能为空.");
		}
		// 2.根据机构编码统计各专业学员数
		ResponseResult result = null;
		// 2.1.查询机构
		GjtOrg org = gjtOrgService.queryByCode(orgCode);
		if(org == null) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "机构不存在.");
		}
		// 2.2.查询下属机构
		List<String> orgIdList = null;
		// 如果orgType为招生点(6),则不需要再查询下属的机构
		if("6".equals(org.getOrgType())) { 
			orgIdList = Lists.newArrayList(org.getId());
		} else {
			orgIdList = gjtOrgService.queryByParentId(org.getId());
		}
		// 2.3.统计专业学员数量
		List<Map<String,Object>> list = studyManageService.countByOrgIdsAndSpecialtyName(orgIdList,specialtyName);
		result = new ResponseResult(ResponseStatus.SUCCESS, list);
		// 3.返回数据
		return result;
	}


	/**
	 * 学籍管理列表
	 * @param page
	 * @param pageSize
	 * @param request
	 * @return
	 * @throws CommonException
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "/getRollDataInfo")
	@ResponseBody
	public Map<String, Object> getRollDataInfo(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize,HttpServletRequest request) throws CommonException, UnsupportedEncodingException {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
        PageRequest pageRequst = Servlets.buildPageRequest(page, pageSize, null);
		Map resultMap = new LinkedHashMap();
        searchParams = getSearchParams(request,"");
		Page pageInfo = studyManageService.getRollDataInfo(searchParams,pageRequst);
		resultMap.put("pageInfo", pageInfo);
		Map result = studyManageService.getRollCountInfo(searchParams);
		resultMap.putAll(result);

		return resultMap;
	}

	/**
	 * 学籍公共接口数据
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getRollBaseData")
	@ResponseBody
	public Map<String,Object> getRollBaseData(Model model,HttpServletRequest request,HttpSession session){
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		searchParams = getSearchParams(request,"");
		return studyManageService.getRollBaseData(searchParams);
	}


	/**
	 * 查询专业和报读人数TOP5
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSpecialAndSingUp")
	@ResponseBody
	public Map<String,Object> getSpecialAndSingUp(HttpServletRequest request){
		Map<String,Object> searchParams = getSearchParams(request,"");
		return studyManageService.getSpecialAndSingUp(searchParams);
	}


    /**
     * 统计学员信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getStatisticalStudents")
    @ResponseBody
	public Map<String,Object> getStatisticalStudents(HttpServletRequest request){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams = getSearchParams(request,"");
        log.info(searchParams.toString());
        return studyManageService.getRollCountInfo(searchParams);
    }


    /**
	 * 获得学员的成绩
	 * @param pageNumber
	 * @param pageSize
	 * @param model
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getExamScoreList")
	@ResponseBody
	public Map<String,Object> getExamScoreList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
											  @RequestParam(value = "pageSize", defaultValue = "5") int pageSize, Model model, HttpServletRequest request,
											  HttpSession session){
		Map resultMap = new LinkedHashMap();
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Map result = studyManageService.getExamScoreCount(searchParams);
		
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, null);
		Page pageInfo = studyManageService.getExamScoreList(searchParams,pageRequst);
		resultMap.putAll(result);
		resultMap.put("pageInfo", pageInfo);

		return resultMap;
	}


	/**
	 * 报读专业统计（全部）
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getSingUpSpeciality")
	@ResponseBody
	public Map<String,Object> getSingUpSpeciality(HttpServletRequest request){
		Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams = getSearchParams(request,"countBySpecial");
		return gjtSignUpInfoDataService.userCountBySpecial(searchParams);
	}

	/**
	 * 报读资料统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEnrolmentCount")
    @ResponseBody
	public Map<String,Object> getEnrolmentCount(HttpServletRequest request){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams = getSearchParams(request,"enrolmentCount");
        return studyManageService.getEnrolmentCount(searchParams);
    }


	/**
	 * 报读缴费统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPaymentCount")
    @ResponseBody
    public Map<String,Object> getPaymentCount(HttpServletRequest request){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams = getSearchParams(request,"paymentCount");
        return gjtSignUpInfoDataService.queryPaymentCount(searchParams);

    }

	/**
	 * 学历层次统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getPyccCount")
	@ResponseBody
    public Map<String,Object> getPyccCount(HttpServletRequest request){
		Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams = getSearchParams(request,"pyccCount");
		return gjtSignUpInfoDataService.userPyccCount(searchParams);
	}


	/**
	 * 统计企业整体报读情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getEnterpriseSignUp")
	@ResponseBody
	public Map<String,Object> getEnterpriseSignUp(HttpServletRequest request){
		return studyManageService.getEnterpriseSignUp(getSearchParams(request,"enterpriseSignUp"));
	}

	/**
	 * 统计当期优秀学员学习排行TOP3
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getLearningRank")
	@ResponseBody
	public Map<String,Object> getLearningRank(HttpServletRequest request){
		return studyManageService.getLearningRank(getSearchParams(request,"learningRank"));
	}

	@RequestMapping(value = "/getStudyCenter")
    @ResponseBody
	public Map<String,Object> getStudyCenter(HttpServletRequest request){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        searchParams = getSearchParams(request,"studyCenter");
	    return gjtSignUpInfoDataService.queryStudyCenter(searchParams);
    }

	public Map<String,Object> getSearchParams(HttpServletRequest request,String type){
        Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,"");
        String xxzx_id = ObjectUtils.toString(searchParams.get("XXZX_ID"),"").trim();
        String xxzx_code = ObjectUtils.toString(searchParams.get("XXZX_CODE"),"").trim();
        if (EmptyUtils.isEmpty(xxzx_id) && EmptyUtils.isEmpty(xxzx_code)){
            searchParams.put("result","0");
            searchParams.put("message","XXZX_ID或者XXZX_CODE不能为空！");
            return searchParams;
        }

        if (EmptyUtils.isNotEmpty(xxzx_id)){
            Map<String, String> schoolMap = commonMapService.getxxIdByxxzxId(ObjectUtils.toString(searchParams.get("XXZX_ID")));
            if(EmptyUtils.isNotEmpty(schoolMap)){
                searchParams.put("XXZX_ID",schoolMap.keySet().iterator().next());
            }else {
                searchParams.put("XXZX_ID",xxzx_id);
            }
        }

        if ("pyccCount".equals(type)){
            searchParams.put("PYCC_GRADE_ID",ObjectUtils.toString(searchParams.get("GRADE_ID"),""));
            searchParams.put("PYCC_SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID"),""));
            searchParams.put("TYPE_FLG","1");
        }

        if ("paymentCount".equals(type)){
            searchParams.put("gradeId",ObjectUtils.toString(searchParams.get("GRADE_ID"),""));
            searchParams.put("pycc",ObjectUtils.toString(searchParams.get("PYCC"),""));
            searchParams.put("signupSpecialtyId",ObjectUtils.toString(searchParams.get("SPECIALTY_ID"),""));
            searchParams.put("TYPE_FLG","1");
        }

        if ("enrolmentCount".equals(type)){
            searchParams.put("gradeId",ObjectUtils.toString(searchParams.get("GRADE_ID"),""));
            searchParams.put("pycc",ObjectUtils.toString(searchParams.get("PYCC"),""));
            searchParams.put("signupSpecialtyId",ObjectUtils.toString(searchParams.get("SPECIALTY_ID"),""));
            searchParams.put("TYPE_FLG","1");
        }

        if ("countBySpecial".equals(type)){
            searchParams.put("SPECIAL_GRADE_ID",ObjectUtils.toString(searchParams.get("GRADE_ID"),""));
            searchParams.put("SPECIAL_PYCC_ID",ObjectUtils.toString(searchParams.get("PYCC"),""));
            searchParams.put("TYPE_FLG","1");
        }

        /*
        if ("studyCenter".equals(type)){
            searchParams.put("CENTER_GRADE_ID",ObjectUtils.toString(searchParams.get("GRADE_ID"),""));
            searchParams.put("CENTER_PYCC_ID",ObjectUtils.toString(searchParams.get("PYCC"),""));
            searchParams.put("CENTER_SPECIALTY_ID",ObjectUtils.toString(searchParams.get("SPECIALTY_ID"),""));
            searchParams.put("TYPE_FLG","1");
        } */

	    return searchParams;
    }

	/**
	 * 学员学习情况统计
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/countStudentStudySituation")
	@ResponseBody
	public ResponseResult countStudentStudySituation(HttpServletRequest request){
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);

		String termId = (String) requestParams.get("termId");
		if (StringUtils.isEmpty(termId)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学期不能为空!");
		}
		String learncenterCode = request.getParameter("learncenterCode");
		if (StringUtils.isEmpty(learncenterCode)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
		}
		GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
		if (learncenterOrg == null) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_studyId", learncenterOrg.getId());
		searchParams.put("GRADE_ID", termId);
		Map<String, Object> data = studyManageService.countStudentStudySituation(searchParams);
		return new ResponseResult(ResponseStatus.SUCCESS, data);
	}

	/**
	 * 学员考试情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/countStudentExamSituation")
	@ResponseBody
	public ResponseResult countStudentExamSituation(HttpServletRequest request){
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);

		String termId = (String) requestParams.get("termId");
		if (StringUtils.isEmpty(termId)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学期不能为空!");
		}
		String learncenterCode = request.getParameter("learncenterCode");
		if (StringUtils.isEmpty(learncenterCode)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
		}
		GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
		if (learncenterOrg == null) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		// 默认选择当前期(批次)
		String xxId = gjtOrgService.getSystemAdministrativeOrganizationByOrgId(learncenterOrg.getId());
		if(EmptyUtils.isEmpty(requestParams)|| StringUtils.isBlank((String) requestParams.get("examBatchCode"))) {
			String code = commonMapService.getCurrentGjtExamBatchNew(xxId);
			searchParams.put("examBatchCode", code);
		}
		searchParams.put("EQ_studyId", learncenterOrg.getId());
		searchParams.put("GRADE_ID", termId);
		Map<String, Object> data = gjtExamRecordNewService.countStudentExamSituation(searchParams);
		return new ResponseResult(ResponseStatus.SUCCESS, data);
	}

	/**
	 * 学员毕业及学位情况
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/countStudentGraduationSituation")
	@ResponseBody
	public ResponseResult countStudentGraduationSituation(HttpServletRequest request){
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);

		String termId = (String) requestParams.get("termId");
		if (StringUtils.isEmpty(termId)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学期不能为空!");
		}
		String learncenterCode = request.getParameter("learncenterCode");
		if (StringUtils.isEmpty(learncenterCode)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不能为空!");
		}
		GjtOrg learncenterOrg = gjtOrgService.queryByCode(learncenterCode);
		if (learncenterOrg == null) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心编码不存在：请确认学习中心是否已经同步!");
		}
		Map<String, Object> searchParams = new HashMap<String, Object>();
		searchParams.put("EQ_studyId", learncenterOrg.getId());
		searchParams.put("GRADE_ID", termId);

        Map<String, Object> data = gjtGraduationApplyCertifService.countStudentApplyCertifSituation(searchParams);
        Map<String, Object> data2 = gjtGraduationApplyDegreeService.countStudentApplyDegreeSituation(searchParams);
        data.putAll(data2);
		return new ResponseResult(ResponseStatus.SUCCESS, data);
	}

}
