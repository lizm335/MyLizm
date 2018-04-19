/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.mobileMessage.GjtMobileMessage;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageAudit;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageSearch;
import com.gzedu.xlims.pojo.mobileMessage.GjtMoblieMessageUser;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.home.message.GjtMessageInportUserService;
import com.gzedu.xlims.service.home.mobileMessage.GjtMobileMessageService;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageAuditService;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageSearchService;
import com.gzedu.xlims.service.home.mobileMessage.GjtMoblieMessageUserService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.MobileMessageAddThread;
import com.gzedu.xlims.web.common.Servlets;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年3月19日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/admin/mobileMessage")
public class GjtMobileMessageController {

	private final static Logger log = LoggerFactory.getLogger(GjtMobileMessageController.class);

	@Autowired
	private GjtMobileMessageService gjtMobileMessageService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtMoblieMessageSearchService gjtMoblieMessageSearchService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtMessageInportUserService gjtMessageInportUserService;

	@Autowired
	private GjtMoblieMessageUserService gjtMoblieMessageUserService;

	@Autowired
	GjtMoblieMessageAuditService gjtMoblieMessageAuditService;

	// 查询列表
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Page<GjtMobileMessage> page = gjtMobileMessageService.queryAll(searchParams, pageRequst);

		List<String> ids = new ArrayList<String>();
		List<String> auditList = new ArrayList<String>();
		for (GjtMobileMessage item : page) {
			ids.add(item.getId());
			if ("1".equals(item.getAuditStatus())) {
				auditList.add(item.getId());
			}
		}
		if (EmptyUtils.isNotEmpty(ids)) {// 查询总发送数//发送成功数//发送失败数
			List<Map<String, Object>> sendCount = gjtMoblieMessageUserService.findSendCount(ids);
			for (GjtMobileMessage item : page) {
				for (Map<String, Object> map : sendCount) {
					if (item.getId().equals(((String) map.get("id")))) {
						item.setSendCount(((BigDecimal) map.get("num")).intValue());
						continue;
					}
				}
			}
		}

		searchParams.put("EQ_auditStatus", 0);
		long waitAuditCount = gjtMobileMessageService.queryAllCount(searchParams);
		searchParams.put("EQ_auditStatus", 2);
		long noAuditCount = gjtMobileMessageService.queryAllCount(searchParams);
		searchParams.put("EQ_auditStatus", 1);
		long yesAuditCount = gjtMobileMessageService.queryAllCount(searchParams);

		model.addAttribute("pageInfo", page);
		model.addAttribute("waitAuditCount", waitAuditCount);
		model.addAttribute("noAuditCount", noAuditCount);
		model.addAttribute("yesAuditCount", yesAuditCount);
		model.addAttribute("typeMap", typeMap());
		model.addAttribute("signatureMap", signatureMap());
		model.addAttribute("customMap", customMap());
		return "home/mobileMessage/mobileMessage_list";

	}

	// 新增跳转
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> specialtyMap = null;
		String orgId = user.getGjtOrg().getId();
		Subject subject = SecurityUtils.getSubject();
		boolean permitted = subject.isPermitted("/admin/messageInfo/list$schoolModel");
		if (permitted) {// 院校模式下专业表不一样
			specialtyMap = commonMapService.getSchoolModelSpecialtyMap(orgId);
		} else {
			// specialtyMap = commonMapService.getSpecialtyMap(orgId);
			specialtyMap = commonMapService.getSpecialtyCodeMap(orgId);
		}
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> userTypeMap = commonMapService.getDates("USER_TYPE");// 学员类型
		Map<String, String> xjztMap = commonMapService.getDates("StudentNumberStatus");// 学籍状态

		// 短信类型
		// 签名类型

		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("xjztMap", xjztMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("action", "create");
		String uuid = UUIDUtils.random().toString();
		model.addAttribute("newMessageId", uuid);
		model.addAttribute("typeMap", typeMap());
		model.addAttribute("signatureMap", signatureMap());
		model.addAttribute("customMap", customMap());
		return "home/mobileMessage/mobileMessage_form";
	}

	static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
	static ExecutorService threadPool = new ThreadPoolExecutor(1, 5, 30, TimeUnit.MILLISECONDS,
			new LinkedBlockingDeque<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

	// 新增
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtMobileMessage entity, String gradeIdAll, String pyccIdAll, String specialtyIdAll,
			String courseIdAll, String userTypeAll, String xjztTypeAll, HttpServletRequest request,
			String newMessageId) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		String mobileMessageId = StringUtils.isBlank(newMessageId) ? UUIDUtils.random().toString() : newMessageId;

		List<GjtMoblieMessageSearch> messageSearchList = new ArrayList<GjtMoblieMessageSearch>();
		Map<String, Object> searchMap = new HashMap<String, Object>(); // 查询条件
		searchMap.put("gradeIdAll", gradeIdAll);
		searchMap.put("pyccIdAll", pyccIdAll);
		searchMap.put("specialtyIdAll", specialtyIdAll);
		searchMap.put("courseIdAll", courseIdAll);
		searchMap.put("userTypeAll", userTypeAll);
		searchMap.put("xjztTypeAll", xjztTypeAll);
		searchMap.put("orgId", orgId);

		if ("0".equals(entity.getIsAppoint())) {// 0-按照条件查询，1是导入

			if ("1".equals(gradeIdAll)) {
				GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
				mesaageSearch.setId(UUIDUtils.random());
				mesaageSearch.setConditionCode("10");
				mesaageSearch.setConditionName("学期");
				mesaageSearch.setMobileMessageId(mobileMessageId);
				mesaageSearch.setConditionContent("全部学期");
				mesaageSearch.setOrderSort(10);
				messageSearchList.add(mesaageSearch);
			} else {
				String[] gradeIds = request.getParameterValues("gradeIds");
				if (EmptyUtils.isNotEmpty(gradeIds)) {
					StringBuffer gradeIds_ = new StringBuffer();
					StringBuffer gradeNames = new StringBuffer();
					for (int i = 0; i < gradeIds.length; i++) {
						String[] split = gradeIds[i].split("_"); // 前台传id_name
						gradeIds_.append(split[0]);
						gradeNames.append(split[1]);
						if (i < gradeIds.length - 1) {
							gradeIds_.append(",");
							gradeNames.append(",");
						}
					}
					GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
					mesaageSearch.setId(UUIDUtils.random());
					mesaageSearch.setConditionCode("10");
					mesaageSearch.setConditionName("学期");
					mesaageSearch.setMobileMessageId(mobileMessageId);
					mesaageSearch.setConditionContent(gradeNames.toString());
					mesaageSearch.setOrderSort(10);
					messageSearchList.add(mesaageSearch);
					searchMap.put("gradeIds", gradeIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(pyccIdAll)) {
				GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
				mesaageSearch.setId(UUIDUtils.random());
				mesaageSearch.setConditionCode("20");
				mesaageSearch.setConditionName("层次");
				mesaageSearch.setMobileMessageId(mobileMessageId);
				mesaageSearch.setConditionContent("全部层次");
				mesaageSearch.setOrderSort(20);
				messageSearchList.add(mesaageSearch);
			} else {
				String[] pyccIds = request.getParameterValues("pyccIds");
				if (EmptyUtils.isNotEmpty(pyccIds)) {
					StringBuffer pyccIds_ = new StringBuffer();
					StringBuffer pyccNames = new StringBuffer();
					for (int i = 0; i < pyccIds.length; i++) {
						String[] split = pyccIds[i].split("_"); // 前台传id_name
						pyccIds_.append(split[0]);
						pyccNames.append(split[1]);
						if (i < pyccIds.length - 1) {
							pyccIds_.append(",");
							pyccNames.append(",");
						}
					}

					GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
					mesaageSearch.setId(UUIDUtils.random());
					mesaageSearch.setConditionCode("20");
					mesaageSearch.setConditionName("层次");
					mesaageSearch.setMobileMessageId(mobileMessageId);
					mesaageSearch.setConditionContent(pyccNames.toString());
					mesaageSearch.setOrderSort(20);
					messageSearchList.add(mesaageSearch);

					searchMap.put("pyccIds", pyccIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(specialtyIdAll)) {
				GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
				mesaageSearch.setId(UUIDUtils.random());
				mesaageSearch.setConditionCode("30");
				mesaageSearch.setConditionName("专业");
				mesaageSearch.setMobileMessageId(mobileMessageId);
				mesaageSearch.setConditionContent("全部专业");
				mesaageSearch.setOrderSort(30);
				messageSearchList.add(mesaageSearch);
			} else {
				String[] specialtyIds = request.getParameterValues("specialtyIds");
				if (EmptyUtils.isNotEmpty(specialtyIds)) {
					StringBuffer specialtyIds_ = new StringBuffer();
					StringBuffer specialtyNames = new StringBuffer();
					for (int i = 0; i < specialtyIds.length; i++) {
						String[] split = specialtyIds[i].split("_"); // 前台传id_name
						specialtyIds_.append(split[0]);
						specialtyNames.append(split[1]);
						if (i < specialtyIds.length - 1) {
							specialtyIds_.append(",");
							specialtyNames.append(",");
						}
					}

					GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
					mesaageSearch.setId(UUIDUtils.random());
					mesaageSearch.setConditionCode("30");
					mesaageSearch.setConditionName("专业");
					mesaageSearch.setMobileMessageId(mobileMessageId);
					mesaageSearch.setConditionContent(specialtyNames.toString());
					mesaageSearch.setOrderSort(30);
					messageSearchList.add(mesaageSearch);

					searchMap.put("specialtyIds", specialtyIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(courseIdAll)) {
				GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
				mesaageSearch.setId(UUIDUtils.random());
				mesaageSearch.setConditionCode("40");
				mesaageSearch.setConditionName("课程");
				mesaageSearch.setMobileMessageId(mobileMessageId);
				mesaageSearch.setConditionContent("全部课程");
				mesaageSearch.setOrderSort(40);
				messageSearchList.add(mesaageSearch);
			} else {
				String[] courseIds = request.getParameterValues("courseIds");
				if (EmptyUtils.isNotEmpty(courseIds)) {
					StringBuffer courseIds_ = new StringBuffer();
					StringBuffer courseNames = new StringBuffer();
					for (int i = 0; i < courseIds.length; i++) {
						String[] split = courseIds[i].split("_"); // 前台传id_name
						courseIds_.append(split[0]);
						courseNames.append(split[1]);
						if (i < courseIds.length - 1) {
							courseIds_.append(",");
							courseNames.append(",");
						}
					}

					GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
					mesaageSearch.setId(UUIDUtils.random());
					mesaageSearch.setConditionCode("40");
					mesaageSearch.setConditionName("课程");
					mesaageSearch.setMobileMessageId(mobileMessageId);
					mesaageSearch.setConditionContent(courseNames.toString());
					mesaageSearch.setOrderSort(40);
					messageSearchList.add(mesaageSearch);

					searchMap.put("courseIds", courseIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(userTypeAll)) {
				GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
				mesaageSearch.setId(UUIDUtils.random());
				mesaageSearch.setConditionCode("50");
				mesaageSearch.setConditionName("学生类型");
				mesaageSearch.setMobileMessageId(mobileMessageId);
				mesaageSearch.setConditionContent("全部学生类型");
				mesaageSearch.setOrderSort(50);
				messageSearchList.add(mesaageSearch);
			} else {
				String[] userTypes = request.getParameterValues("userTypes");
				if (EmptyUtils.isNotEmpty(userTypes)) {
					StringBuffer userTypes_ = new StringBuffer();
					StringBuffer userTypeNames = new StringBuffer();
					for (int i = 0; i < userTypes.length; i++) {
						String[] split = userTypes[i].split("_"); // 前台传id_name
						userTypes_.append(split[0]);
						userTypeNames.append(split[1]);
						if (i < userTypes.length - 1) {
							userTypes_.append(",");
							userTypeNames.append(",");
						}
					}

					GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
					mesaageSearch.setId(UUIDUtils.random());
					mesaageSearch.setConditionCode("50");
					mesaageSearch.setConditionName("学生类型");
					mesaageSearch.setMobileMessageId(mobileMessageId);
					mesaageSearch.setConditionContent(userTypeNames.toString());
					mesaageSearch.setOrderSort(50);
					messageSearchList.add(mesaageSearch);

					searchMap.put("userTypes", userTypes_.toString());// 搜索条件
				}
			}

			if ("1".equals(xjztTypeAll)) {
				GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
				mesaageSearch.setId(UUIDUtils.random());
				mesaageSearch.setConditionCode("60");
				mesaageSearch.setConditionName("学籍状态");
				mesaageSearch.setMobileMessageId(mobileMessageId);
				mesaageSearch.setConditionContent("全部学籍状态");
				mesaageSearch.setOrderSort(60);
				messageSearchList.add(mesaageSearch);
			} else {
				String[] xjztTypes = request.getParameterValues("xjztTypes");
				if (EmptyUtils.isNotEmpty(xjztTypes)) {
					StringBuffer xjztTypes_ = new StringBuffer();
					StringBuffer xjztTypeNames = new StringBuffer();
					for (int i = 0; i < xjztTypes.length; i++) {
						String[] split = xjztTypes[i].split("_"); // 前台传id_name
						xjztTypes_.append(split[0]);
						xjztTypeNames.append(split[1]);
						if (i < xjztTypes.length - 1) {
							xjztTypes_.append(",");
							xjztTypeNames.append(",");
						}
					}
					GjtMoblieMessageSearch mesaageSearch = new GjtMoblieMessageSearch();
					mesaageSearch.setId(UUIDUtils.random());
					mesaageSearch.setConditionCode("60");
					mesaageSearch.setConditionName("学籍状态");
					mesaageSearch.setMobileMessageId(mobileMessageId);
					mesaageSearch.setConditionContent(xjztTypeNames.toString());
					mesaageSearch.setOrderSort(60);
					messageSearchList.add(mesaageSearch);
					searchMap.put("xjztTypes", xjztTypes_.toString());// 搜索条件
				}
			}
			gjtMoblieMessageSearchService.save(messageSearchList);
		}

		Feedback feedback = new Feedback(true, "发送成功");
		if (StringUtils.isBlank(entity.getContent())) {
			feedback = new Feedback(false, "创建失败，内容不能为空");
			return feedback;
		}

		GjtMobileMessage saveEntity = null;
		try {
			entity.setId(mobileMessageId);
			entity.setCreatedBy(user.getId());
			entity.setCreateUsername(user.getRealName());
			entity.setCreateRoleName(user.getPriRoleInfo().getRoleName());
			entity.setCreatedDt(DateUtils.getNowTime());
			entity.setIsDeleted("N");
			entity.setAuditStatus("0");

			saveEntity = gjtMobileMessageService.save(entity);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (saveEntity == null) {
			feedback = new Feedback(false, "创建失败，发生异常");
			return feedback;
		}

		// 创建审核记录
		GjtMoblieMessageAudit item = new GjtMoblieMessageAudit();
		item.setId(UUIDUtils.random());
		item.setAuditContent("提交审核");
		item.setSubmitPersion(user.getId());
		item.setSubmitUserName(user.getRealName());
		item.setSubmitRoleName(user.getPriRoleInfo().getRoleName());
		item.setCreatedDt(DateUtils.getNowTime());
		item.setMobileMessageId(mobileMessageId);
		gjtMoblieMessageAuditService.save(item);

		Set<String> allSets = new HashSet<String>();// 所有用户
		List<Map<String, Object>> lists = Lists.newArrayList();// student推送到手机APP
		log.info("查询条件：searchMap={},messageId={}", searchMap, mobileMessageId);
		try {
			// 查询院校下所有的学生
			long startTime = System.currentTimeMillis();
			if ("0".equals(entity.getIsAppoint())) {
				lists = gjtStudentInfoService.queryPutStudent(searchMap);
			} else {
				lists = gjtMessageInportUserService.queryImportStudent(mobileMessageId);
			}
			System.err.println("查询时间：" + (System.currentTimeMillis() - startTime));
			for (Map<String, Object> map : lists) {
				String id = (String) map.get("ID");
				allSets.add(id);
			}
			log.info("学生人数：{}", allSets.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		List<String> allLists = new ArrayList<String>(allSets);
		threadSaveMessageUser(allLists, mobileMessageId, user);
		return feedback;
	}

	private void threadSaveMessageUser(List<String> lists, String messageId, GjtUserAccount user) {
		int size = lists.size();
		if (lists != null && size > 0) {
			if (size > 500) {// 如果size大于500，就开多线程执行，把list拆成5份
				int nThreads = 5;// 开启多少个线程 list的size必需大于线程数
				for (int i = 0; i < nThreads; i++) {
					int start = size / nThreads * i;
					int end = size / nThreads * (i + 1);
					if (!(i < nThreads - 1)) {
						end = size;// 最后一份有余数。直接取size最后一个下标
					}
					final List<String> subList = lists.subList(start, end);
					log.info("当前循环次数为第{}次；当前开始下标为开始：{}，下标结束：{}", i, start, end);
					MobileMessageAddThread mat = new MobileMessageAddThread(gjtMoblieMessageUserService, messageId,
							user.getRealName(), subList);
					// new Thread(mat).start();
					threadPool.execute(mat);
				}
			} else {
				MobileMessageAddThread mat = new MobileMessageAddThread(gjtMoblieMessageUserService, messageId,
						user.getRealName(), lists);
				threadPool.execute(mat);
			}
		}

	}

	// 审核
	@RequestMapping(value = "updateAudit", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateAudit(String messageId, String content, String auditStatus, HttpServletRequest request) {
		Feedback fb = new Feedback(true, "操作成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtMobileMessage entity = gjtMobileMessageService.queryById(messageId);
		entity.setAuditStatus(auditStatus);
		try {
			GjtMoblieMessageAudit item = new GjtMoblieMessageAudit();
			item.setAuditContent(content);
			item.setAuditStatus(auditStatus);
			item.setAuditPerson(user.getId());
			item.setAuditUserName(user.getRealName());
			item.setAuditRoleName(user.getPriRoleInfo().getRoleName());
			item.setAuditTime(DateUtils.getNowTime());
			item.setCreatedDt(DateUtils.getNowTime());
			item.setId(UUIDUtils.random());
			item.setMobileMessageId(entity.getId());
			gjtMoblieMessageAuditService.save(item);

			if ("1".equals(auditStatus)) {
				entity.setSendTime(DateUtils.getNowTime());
			}
			gjtMobileMessageService.update(entity);
		} catch (Exception e) {
			log.error(e.getMessage());
			fb = new Feedback(false, "操作失败：" + e.getMessage());
		}

		if ("1".equals(auditStatus)) {// 调用短信接口发送短信
			List<Map<String, Object>> list1 = gjtMoblieMessageUserService.findMobileMessageUser(entity.getId());
			final List<Map<String, Object>> list = list1;
			final String messageContent = entity.getContent();
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					// String devMode = AppConfig.getProperty("devMode");
					// if ("1".equals(devMode)) {
					// log.info("测试环境不发送短信");
					// } else {
					log.info("发送短信中.........");
					for (Map<String, Object> map : list) {
						log.info("姓名：" + map.get("REAL_NAME") + "\t\t 手机号：" + map.get("SJH"));
						SMSUtil.sendMessage((String) map.get("SJH"), messageContent, "gk");
					}
					// }
				}
			});
		}

		return fb;

	}

	// 查看
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		GjtMobileMessage info = gjtMobileMessageService.queryById(id);
		List<GjtMoblieMessageAudit> auditList = info.getAuditList();
		List<GjtMoblieMessageSearch> searchList = info.getSearchList();
		model.addAttribute("auditList", auditList);
		model.addAttribute("searchList", searchList);
		model.addAttribute("info", info);
		model.addAttribute("view", true);
		model.addAttribute("typeMap", typeMap());
		model.addAttribute("signatureMap", signatureMap());
		model.addAttribute("customMap", customMap());
		return "home/mobileMessage/view";
	}

	// 审核跳转
	@RequestMapping(value = "audit/{id}", method = RequestMethod.GET)
	public String auditForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtMobileMessage info = gjtMobileMessageService.queryById(id);
		List<GjtMoblieMessageAudit> auditList = info.getAuditList();
		List<GjtMoblieMessageSearch> searchList = info.getSearchList();
		Boolean isDean = "d4b27a66c0a87b010120da231915c223".equals(user.getPriRoleInfo().getRoleId());// 院长Id
		model.addAttribute("auditList", auditList);
		model.addAttribute("searchList", searchList);
		model.addAttribute("info", info);
		model.addAttribute("isDean", isDean);
		return "home/mobileMessage/view";
	}

	// 修改跳转
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		GjtMobileMessage info = gjtMobileMessageService.queryById(id);
		model.addAttribute("info", info);
		model.addAttribute("action", "update");
		model.addAttribute("typeMap", typeMap());
		model.addAttribute("signatureMap", signatureMap());
		model.addAttribute("customMap", customMap());
		return "home/mobileMessage/mobileMessage_form";
	}

	// 修改跳转
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid GjtMobileMessage entity, String id, Model model, HttpServletRequest request) {
		Feedback fb = new Feedback(true, "操作成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtMobileMessage info = gjtMobileMessageService.queryById(id);
		info.setType(entity.getType());
		info.setSignature(entity.getSignature());
		info.setContent(entity.getContent());
		try {
			if ("2".equals(entity.getAuditStatus())) {
				info.setAuditStatus("0");// 修改完变成待审核
				// 记录审核信息提交信息
				GjtMoblieMessageAudit item = new GjtMoblieMessageAudit();
				item.setAuditContent("重新提交审核");
				item.setSubmitPersion(user.getId());
				item.setSubmitUserName(user.getRealName());
				item.setSubmitRoleName(user.getPriRoleInfo().getRoleName());
				item.setCreatedDt(DateUtils.getNowTime());
				item.setId(UUIDUtils.random());
				item.setMobileMessageId(info.getId());
				gjtMoblieMessageAuditService.save(item);
			}
			gjtMobileMessageService.update(entity);
		} catch (Exception e) {
			log.error(e.getMessage());
			fb = new Feedback(false, "操作失败" + e.getMessage());
		}
		return fb;
	}

	public Map<String, String> typeMap() {
		Map<String, String> typeMap = new HashMap<String, String>();
		typeMap.put("1", "普通短信");
		typeMap.put("2", "模板短信");
		return typeMap;
	}

	public Map<String, String> signatureMap() {
		Map<String, String> signatureMap = new HashMap<String, String>();
		signatureMap.put("1", "国开在线");
		return signatureMap;
	}

	public Map<String, String> customMap() {
		Map<String, String> customMap = new HashMap<String, String>();
		customMap.put("1", "自定义模板");
		customMap.put("2", "续费催缴模板");
		customMap.put("3", "网考通知模板");
		return customMap;
	}

	@RequestMapping(value = "queryPutListById", method = RequestMethod.GET)
	public String queryPutListById(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request,
			String mobileMessageId, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		String orgId = user.getGjtOrg().getId();
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyCodeMap(orgId);
		Map<String, String> userTypeMap = commonMapService.getDates("USER_TYPE");// 学员类型
		Map<String, String> xjztMap = commonMapService.getDates("StudentNumberStatus");// 学籍状态

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_mobileMessageId", mobileMessageId);

		Page<GjtMoblieMessageUser> page = gjtMoblieMessageUserService.queryAll(searchParams, pageRequst);

		searchParams.put("EQ_sendStauts", "0");// 待发送
		long waitCount = gjtMoblieMessageUserService.queryAllCount(searchParams);

		searchParams.put("EQ_sendStauts", "1");// 发送
		long successfulCount = gjtMoblieMessageUserService.queryAllCount(searchParams);

		searchParams.put("EQ_sendStauts", "2");// 待发送
		long failureCount = gjtMoblieMessageUserService.queryAllCount(searchParams);

		model.addAttribute("mobileMessageId", mobileMessageId);
		model.addAttribute("pageInfo", page);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("xjztMap", xjztMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("waitCount", waitCount);
		model.addAttribute("successfulCount", successfulCount);
		model.addAttribute("failureCount", failureCount);

		return "home/mobileMessage/statistics_view";
	}

}
