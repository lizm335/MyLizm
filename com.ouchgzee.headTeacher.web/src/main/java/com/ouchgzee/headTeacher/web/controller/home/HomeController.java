/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.web.controller.home;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.menu.SystemName;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.feedback.LcmsMutualReplyService;
import com.gzedu.xlims.service.feedback.LcmsMutualSubjectService;
import com.gzedu.xlims.service.home.GjtMessageResultService;
import com.gzedu.xlims.service.model.ModelService;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.ouchgzee.headTeacher.pojo.BzrGjtClassInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.pojo.BzrPriRoleInfo;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.systemManage.BzrTblPriLoginLogService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.common.vo.LcmsMutualSubjectVO;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

/**
 * 系统首页<br/>
 * 功能说明：
 *
 * @author 李明 liming@eenet.com
 * @version 2.5
 *
 */
@Controller
public class HomeController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private BzrGjtClassService gjtClassService;

	@Autowired
	private ModelService modelServiceNew;

	@Autowired
	private PriRoleInfoService priRoleInfoServiceNew;

	@Autowired
	BzrGjtUserAccountService gjtUserAccountService;

	@Autowired
	BzrTblPriLoginLogService tblPriLoginLogService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	LcmsMutualSubjectService lcmsMutualSubjectService;

	@Autowired
	LcmsMutualReplyService lcmsMutualReplyService;

	@Autowired
	GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	GjtMessageResultService gjtMessageResultService;

	@Autowired
	GjtOrgService gjtOrgService;

	@Autowired
	GjtClassStudentService gjtClassStudentService;

	@Value("#{configProperties['eeChatInterface']}")
	private String eeServer;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String main() {
		return "redirect:/home/manyClass";
	}

	@RequestMapping(value = "/home/manyClass", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session, String type) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		// 班级状态 1-开启/0-关闭
		if (type == null) {
			type = "1";
		}
		searchParams.put("EQ_isEnabled", type);
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Page<BzrGjtClassInfo> infos = gjtClassService.queryClassInfoByPage(employeeInfo.getEmployeeId(), searchParams,
				pageRequst);

		Map<String, String> gradeMap = commonMapService.getGradeMap(employeeInfo.getXxId());

		model.addAttribute("infos", infos);
		model.addAttribute("info", employeeInfo);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("type", type);
		model.addAttribute("opCount", gjtClassService.countOpenClassNum(employeeInfo.getEmployeeId()));
		model.addAttribute("clCount", gjtClassService.countCloseClassNum(employeeInfo.getEmployeeId()));

		return "/new/manyClass";
	}

	@RequestMapping(value = "/home/manyFeedSubject", method = RequestMethod.GET)
	public String manyFeedSubjectList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request, HttpSession session, String type, String title, String isTranspond,
			String isTimeout, String isSolve, String isAsk) {
		log.info("首页答疑参数pageNumber={},pageSize={},type={},title={},isTranspond={},isTimeout={}", pageNumber, pageSize,
				type, title, isTranspond, isTimeout);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();

		if (StringUtils.isNotBlank(title)) {
			searchParams.put("LIKE_subjectTitle", title);
		}
		searchParams.put("EQ_isdeleted", "N");
		// List<String> orgTree =
		// gjtOrgService.queryByParentId(user.getGjtOrg().getId());
		// searchParams.put("IN_orgId", orgTree);

		if ("F".equals(type)) {// 转出去的
			searchParams.put("EQ_initialAccountId", user.getId());
			searchParams.put("NE_forwardAccountId", user.getId());
			if ("N".equals(isAsk)) {
				searchParams.put("EQ_subjectStatus", "N");
			} else if ("YN".equals(isAsk)) {
				searchParams.put("EQ_subjectStatus", "Y");
				searchParams.put("EQ_isSolve", "0");
			} else if ("YY".equals(isAsk)) {
				searchParams.put("EQ_subjectStatus", "Y");
				searchParams.put("EQ_isSolve", "1");
			} else {

			}
		} else {
			searchParams.put("EQ_forwardAccountId", user.getId());
			if (StringUtils.isNotBlank(type) && !"N".equals(type)) {
				if ("Y".equals(type)) {// Y为已解答
					searchParams.put("EQ_subjectStatus", "Y");
					if (StringUtils.isNotBlank(isSolve)) {// 学生确认是否已经解决
						searchParams.put("EQ_isSolve", isSolve);
					}
				} else {
					searchParams.put("EQ_isCommend", "Y");// 常见问题
				}
			} else {// 待解答问题
				type = "N";
				searchParams.put("EQ_subjectStatus", "N");

				if ("Y".equals(isTranspond)) {// 是否转发Y（ 转发给我的）
					searchParams.put("NE_initialAccountId", user.getId());
				}
				if ("N".equals(isTranspond)) {// 是否转发N （向我提问的）
					searchParams.put("EQ_initialAccountId", user.getId());
				}
				String timeout = "";
				long time = DateUtils.getDate().getTime();
				if ("2".equals(isTimeout)) { // 超2小时未解答
					long time2 = time - (2 * 60 * 60 * 1000);
					Timestamp sql = new Timestamp(time2);
					timeout = DateUtils.getStringToDate(sql);
				}
				if ("24".equals(isTimeout)) {// 超24小时未解答
					long time2 = time - (24 * 60 * 60 * 1000);
					Timestamp sql = new Timestamp(time2);
					timeout = DateUtils.getStringToDate(sql);
				}
				searchParams.put("LT_updatedDt", timeout);
			}
		}

		Page<LcmsMutualSubject> infos = lcmsMutualSubjectService.queryPageInfo(searchParams, pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		List<LcmsMutualSubjectVO> list = new ArrayList<LcmsMutualSubjectVO>();
		for (LcmsMutualSubject item : infos) {
			if ("1".equals(item.getIsTranspond())) {// 转发的显示
				if (item.getForwardAccountId().equals(user.getId())) {// 别人转发给我的
					BzrGjtUserAccount InitialUser = gjtUserAccountService.findOne(item.getInitialAccountId());
					item.setRemark(InitialUser.getRealName() + "(" + InitialUser.getPriRoleInfo().getRoleName() + ")");
					item.setFeedbackContacts("我" + "(" + user.getPriRoleInfo().getRoleName() + ")");
					item.setIsMessage(false);
				} else {// 我转发给别的
					BzrGjtUserAccount forwardUser = gjtUserAccountService.findOne(item.getForwardAccountId());
					item.setRemark("我" + "(" + user.getPriRoleInfo().getRoleName() + ")");
					item.setFeedbackContacts(
							forwardUser.getRealName() + "(" + forwardUser.getPriRoleInfo().getRoleName() + ")");
					// 是否短信催促过
					boolean bool = gjtMessageResultService.queryByIncidentIdAndStartDateAndEndDate(item.getSubjectId());
					item.setIsMessage(bool);
				}
			} else {
				item.setFeedbackContacts("我" + "(" + user.getPriRoleInfo().getRoleName() + ")");
			}
			LcmsMutualSubjectVO vo = null;
			if ("0".equals(item.getSoleType())) {// 老师和学员提问的显示信息是不同的VO
				GjtStudentInfo studentInfo = null;
				GjtUserAccount createAccount = item.getCreateAccount();
				if (createAccount != null) {
					studentInfo = createAccount.getGjtStudentInfo();
				} else {
					if (StringUtils.isNotBlank(item.getCreatedBy())) {
						studentInfo = gjtStudentInfoService.queryById(item.getCreatedBy());// 旧数据
					}
				}
				vo = new LcmsMutualSubjectVO(item, pyccMap, studentInfo);
				if (studentInfo != null) {
					GjtUserAccount userAccount = studentInfo.getGjtUserAccount();
					GjtOrg gjtOrg = userAccount.getGjtOrg();
					vo.setOrgName(gjtOrg != null ? gjtOrg.getOrgName() : "");

					String singUrl = gjtUserAccountService.studentSimulationNew("APP002", studentInfo.getStudentId(),
							studentInfo.getXh(), "2");

					String url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(),
							studentInfo.getXh());

					vo.setUrl(url);
					vo.setSingUrl(singUrl);

					JSONObject object = new JSONObject();
					object.put("USER_ID", employeeInfo.getEeno());
					object.put("TO_IM_USERID", studentInfo.getEeno());
					String eeUrl = eeServer + "/openChatWithEENO.do?data=" + EncryptUtils.encrypt(object.toString());
					vo.setEeUrl(eeUrl);
				}
			} else {
				vo = new LcmsMutualSubjectVO(item);
			}
			list.add(vo);
		}

		Page<LcmsMutualSubjectVO> page = new PageImpl<LcmsMutualSubjectVO>(list, pageRequst, infos.getTotalElements());

		// 删除查询条件，统计总数
		searchParams.remove("EQ_isCommend");
		if ("Y".equals(isTranspond) || "F".equals(type)) {
			searchParams.remove("EQ_initialAccountId");
			searchParams.put("EQ_forwardAccountId", user.getId());
		}
		if ("N".equals(isTranspond) || "F".equals(type)) {
			searchParams.remove("NE_forwardAccountId");
			searchParams.put("EQ_forwardAccountId", user.getId());
		}

		// 为解决
		searchParams.put("EQ_subjectStatus", "N");
		long noSolve = lcmsMutualSubjectService.queryAllCount(searchParams);
		if (StringUtils.isBlank(type) || "N".equals(type)) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (StringUtils.isNotBlank(title)) {
				map.put("LIKE_subjectTitle", title);
			}
			map.put("EQ_subjectStatus", "N");
			map.put("EQ_isdeleted", "N");

			if (EmptyUtils.isNotEmpty(searchParams.get("LT_updatedDt"))) {
				map.put("LT_updatedDt", searchParams.get("LT_updatedDt"));
			}

			map.put("EQ_initialAccountId", user.getId());
			map.put("EQ_forwardAccountId", user.getId());
			long xiangWoTiWenCount = lcmsMutualSubjectService.queryAllCount(map);

			map.remove("EQ_initialAccountId");
			map.put("NE_initialAccountId", user.getId());
			long zhuanfaMeCount = lcmsMutualSubjectService.queryAllCount(map);

			model.addAttribute("xiangWoTiWenCount", xiangWoTiWenCount);
			model.addAttribute("zhuanfaMeCount", zhuanfaMeCount);
		}

		searchParams.put("EQ_subjectStatus", "Y");
		long yesSolve = lcmsMutualSubjectService.queryAllCount(searchParams);
		if ("Y".equals(type)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("EQ_subjectStatus", "Y");
			map.put("EQ_isSolve", "1");
			map.put("EQ_forwardAccountId", user.getId());
			if (StringUtils.isNotBlank(title)) {
				map.put("LIKE_subjectTitle", title);
			}
			map.put("EQ_isdeleted", "N");
			long yesAffirmSolve = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_isSolve", "0");
			long noAffirmSolve = lcmsMutualSubjectService.queryAllCount(map);
			model.addAttribute("yesAffirmSolve", yesAffirmSolve);
			model.addAttribute("noAffirmSolve", noAffirmSolve);
		}

		searchParams.remove("EQ_subjectStatus");
		searchParams.put("EQ_isCommend", "Y");
		long oftenCount = lcmsMutualSubjectService.queryAllCount(searchParams);

		long forwardCount = 0;
		if ("F".equals(type)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("EQ_initialAccountId", user.getId());
			map.put("NE_forwardAccountId", user.getId());
			map.put("EQ_isdeleted", "N");
			if (StringUtils.isNotBlank(title)) {
				map.put("LIKE_subjectTitle", title);
			}
			forwardCount = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_subjectStatus", "N");
			long forwardNoSolveCount = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_subjectStatus", "Y");
			map.put("EQ_isSolve", "0");
			long forwardYnSolve = lcmsMutualSubjectService.queryAllCount(map);

			map.put("EQ_isSolve", "1");
			long forwardYYSolve = lcmsMutualSubjectService.queryAllCount(map);

			model.addAttribute("forwardNoSolveCount", forwardNoSolveCount);
			model.addAttribute("forwardYnSolve", forwardYnSolve);
			model.addAttribute("forwardYYSolve", forwardYYSolve);

		} else {
			searchParams.remove("EQ_isCommend");
			searchParams.remove("EQ_forwardAccountId");
			searchParams.put("EQ_initialAccountId", user.getId());
			searchParams.put("NE_forwardAccountId", user.getId());
			forwardCount = lcmsMutualSubjectService.queryAllCount(searchParams);
		}
		model.addAttribute("pageInfo", page);
		model.addAttribute("noSolve", noSolve);
		model.addAttribute("yesSolve", yesSolve);
		model.addAttribute("oftenCount", oftenCount);
		model.addAttribute("forwardCount", forwardCount);
		model.addAttribute("type", type);
		model.addAttribute("info", employeeInfo);
		model.addAttribute("isTranspond", isTranspond);
		model.addAttribute("isTimeout", isTimeout);
		model.addAttribute("isSolve", isSolve);
		model.addAttribute("isAsk", isAsk);
		model.addAttribute("oftenTypeMap", commonMapService.getDates("OftenType"));

		if ("Y".equals(type)) {// Y为已解决
			return "new/solved_list";
		} else if ("F".equals(type)) {
			return "new/forward_solved_list";
		} else if ("E".equals(type)) {
			return "new/often_list";
		} else {
			return "new/no_solved_list";
		}
	}

	/**
	 * 平台首页，包括头部尾部
	 * 
	 * @return
	 */
	@RequestMapping(value = "/home/main", method = RequestMethod.GET)
	public String index(ModelMap model, HttpServletResponse response, HttpSession session, HttpServletRequest request) {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
		BzrPriRoleInfo roleInfoBzr = user.getPriRoleInfo();
		PriRoleInfo roleInfo = priRoleInfoServiceNew.queryById(roleInfoBzr.getRoleId());

		List<PriModelInfo> priModelInfos = roleInfo.getPriModelInfos();
		List<String> modelNames = new ArrayList<String>();
		List<PriModelInfo> topModels = modelServiceNew.queryTopModel();
		for (PriModelInfo topModel : topModels) {
			if (EmptyUtils.isNotEmpty(priModelInfos)) {
				for (PriModelInfo modelInfo : priModelInfos) {
					if (modelInfo.getParentModel() == null
							&& modelInfo.getModelName().equals(topModel.getModelName())) {
						modelNames.add(topModel.getModelName());
					}
				}
			}
		}
		List<PriModelInfo> modelInfoList = modelServiceNew.queryMainModelIn(modelNames);
		if (roleInfo != null) {
			List<PriModelInfo> roleModelList = roleInfo.getPriModelInfos();
			for (PriModelInfo roleModel : roleModelList) {
				walk(modelInfoList, roleModel);
			}
		}

		BzrGjtClassInfo currentClass = super.getCurrentClass(session);
		if (currentClass == null) {
			Map<String, Object> searchParams = new HashMap();

			// 班级状态 1-开启/0-关闭
			searchParams.put("EQ_isEnabled", Constants.BOOLEAN_1);
			List<BzrGjtClassInfo> infos = gjtClassService.queryClassInfoBy(employeeInfo.getEmployeeId(), searchParams,
					null);
			if (!infos.isEmpty()) {
				if (super.getCurrentClass(session) == null) {
					currentClass = infos.get(0);
					super.setCurrentClass(session, currentClass);
				}

				employeeInfo.setManageClassList(infos);
				super.setUser(session, employeeInfo);
			}
		}

		boolean bool = false;
		try {
			if (!session.getId().equals(user.getCurrentLoginIp()) && "Y".equals(user.getIsOnline())) {
				// 如果浏览器关闭，日志表里面旧的sessionId更改为新的
				tblPriLoginLogService.updateNewSessionByOldSession(user.getCurrentLoginIp(), session.getId());
				// 更改旧的sessionId为新的，要改user表
				bool = gjtUserAccountService.updateSessionId(user.getId(), user.getLoginCount(), session.getId());
			} else {
				if ("N".equals(user.getIsOnline()) || StringUtils.isBlank(user.getIsOnline())) {// Y是刷新,N是离线
					bool = gjtUserAccountService.updateLoginState(user.getId(), user.getLoginCount(), session.getId());
					// 插入日志表
					tblPriLoginLogService.save(user, request, session.getId());
				}
			}
			if (bool) {
				user = gjtUserAccountService.findOne(user.getId());
				session.setAttribute(WebConstants.CURRENT_USER, user);// 更新缓存
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		model.put("modelInfoList", modelInfoList);
		model.put("info", employeeInfo);
		model.put("currentClassInfo", currentClass);
		return "/new/main";
	}

	private void walk(List<PriModelInfo> treeModelList, PriModelInfo model) {
		for (PriModelInfo treeModel : treeModelList) {
			if (treeModel.getModelId().equals(model.getModelId())) {
				treeModel.setIsShow(true);
				break;
			}
			if (treeModel.getChildModelList() != null) {
				walk(treeModel.getChildModelList(), model);
			}
		}
	}

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/home/index", method = RequestMethod.GET)
	public String index2(ModelMap model, HttpServletResponse response, HttpSession session) {
		// 首页
		List<PriModelInfo> modelInfoList = modelServiceNew.queryMainModel(SystemName.班主任平台.name());

		model.put("modelInfoList", modelInfoList);
		return "/new/main_index";
	}

	@RequestMapping(value = "/home/header", method = RequestMethod.GET)
	public String header(ModelMap model, HttpServletResponse response, HttpSession session) {
		// 首页
		List<PriModelInfo> modelInfoList = modelServiceNew.queryMainModel(SystemName.班主任平台.name());

		model.put("modelInfoList", modelInfoList);
		return "/new/layouts/header";
	}

	@RequestMapping(value = "/home/forwardWebEE", method = RequestMethod.GET)
	public void forwardWebEE(ModelMap model, HttpServletResponse response, HttpSession session) throws IOException {
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		Map<String, String> sParaTemp = new HashMap();
		sParaTemp.put("data", EncryptUtils.encrypt("{\"USER_ID\":\"" + employeeInfo.getEmployeeId() + "\"}"));
		String sbHtml = FormSubmitUtil.buildRequest(eeServer + "/urlLoginCheck.do", sParaTemp, "post", "登录ee");

		super.outputHtml(response, sbHtml);
	}

}
