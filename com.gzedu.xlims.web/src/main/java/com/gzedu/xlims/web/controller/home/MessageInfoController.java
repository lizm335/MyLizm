/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.home;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzdec.framework.util.ExcelUtil;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.message.GjtMessageClassify;
import com.gzedu.xlims.pojo.message.GjtMessageComment;
import com.gzedu.xlims.pojo.message.GjtMessageCommentDetail;
import com.gzedu.xlims.pojo.message.GjtMessageFeedback;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.pojo.message.GjtMessageInportUser;
import com.gzedu.xlims.pojo.message.GjtMessagePutObject;
import com.gzedu.xlims.pojo.message.GjtMessageSearch;
import com.gzedu.xlims.pojo.message.GjtMessageUser;
import com.gzedu.xlims.pojo.status.MessageInfoRoleTypeEnum;
import com.gzedu.xlims.pojo.status.MessagePlatformEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.GjtCourseService;
import com.gzedu.xlims.service.home.message.GjtMessageClassifyService;
import com.gzedu.xlims.service.home.message.GjtMessageCommentDetailService;
import com.gzedu.xlims.service.home.message.GjtMessageCommentLikeService;
import com.gzedu.xlims.service.home.message.GjtMessageCommentService;
import com.gzedu.xlims.service.home.message.GjtMessageFeedbackService;
import com.gzedu.xlims.service.home.message.GjtMessageInfoService;
import com.gzedu.xlims.service.home.message.GjtMessageInportUserService;
import com.gzedu.xlims.service.home.message.GjtMessagePutObjectService;
import com.gzedu.xlims.service.home.message.GjtMessageSearchService;
import com.gzedu.xlims.service.home.message.GjtMessageUserService;
import com.gzedu.xlims.service.organization.GjtSpecialtyService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.third.sms.SmsSenderManager;
import com.gzedu.xlims.third.sms.SmsSenderType;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.ImportFeedback;
import com.gzedu.xlims.web.common.MessageAddThread;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.message.vo.MessageInfoVo;
import com.gzedu.xlims.web.common.message.vo.MessageUserRoleMap;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：通知公告
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月8日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/admin/messageInfo")
public class MessageInfoController {

	private final static Logger log = LoggerFactory.getLogger(MessageInfoController.class);

	@Autowired
	private GjtMessageInfoService gjtMessageInfoService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	private GjtCourseService gjtCourseService;

	@Autowired
	private GjtMessageUserService gjtMessageUserService;

	@Autowired
	private SmsSenderManager smsSenderManager;

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtMessageClassifyService gjtMessageClassifyService;

	@Autowired
	private GjtSpecialtyService gjtSpecialtyService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtMessageInportUserService gjtMessageInportUserService;

	@Autowired
	private GjtMessageSearchService gjtMessageSearchService;

	@Autowired
	private GjtMessagePutObjectService gjtMessagePutObjectService;

	@Autowired
	private GjtMessageFeedbackService gjtMessageFeedbackService;

	@Autowired
	private PriRoleInfoService priRoleInfoService;

	@Autowired
	private GjtMessageCommentService gjtMessageCommentService;

	@Autowired
	private GjtMessageCommentLikeService gjtMessageCommentLikeService;

	@Autowired
	GjtMessageCommentDetailService gjtMessageCommentDetailService;

	@RequestMapping(value = "putList", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "6") int pageSize, Model model,
			HttpServletRequest request, String isEffective) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtUserAccount.id", user.getId());
		searchParams.put("EQ_gjtOrg.id", user.getGjtOrg().getId());
		searchParams.put("EQ_isDeleted", "N");
		if (StringUtils.isNotBlank(isEffective)) {
			searchParams.put("GT_effectiveTime", DateUtils.getStringToDate(DateUtils.getDate()));
		}

		Page<GjtMessageInfo> oldPage = gjtMessageInfoService.queryAll(searchParams, pageRequst);

		List<MessageInfoVo> list = new ArrayList<MessageInfoVo>();

		List<String> messageIds = new ArrayList<String>();
		for (GjtMessageInfo item : oldPage) {
			messageIds.add(item.getMessageId());
		}
		log.info("统计参数messageIds：{}", messageIds);
		if (messageIds.size() > 0) {
			List<Object[]> putMaps = gjtMessageUserService.queryPutMessageIds(messageIds);// 发送总数
			Map<String, Object> putMap = new HashMap<String, Object>();
			for (Object[] info : putMaps) {
				putMap.put((String) info[1], info[0]);
			}

			List<Object[]> readMaps = gjtMessageUserService.queryReadMessageIds(messageIds);// 已读总数
			Map<String, Object> readMap = new HashMap<String, Object>();
			for (Object[] info : readMaps) {
				readMap.put((String) info[1] + "-" + (String) info[2], info[0]);
			}

			List<Object[]> likeLists = gjtMessageUserService.queryLikeMessageIds(messageIds);// 点赞数
			Map<String, Object> likeMap = new HashMap<String, Object>();
			for (Object[] info : likeLists) {
				likeMap.put((String) info[1], info[0]);
			}

			List<Object[]> ticklingLists = gjtMessageUserService.queryTicklingMessageIds(messageIds);// 反馈数
			Map<String, Object> ticklingMap = new HashMap<String, Object>();
			for (Object[] info : ticklingLists) {
				ticklingMap.put((String) info[1], info[0]);
			}

			List<Object[]> commLists = gjtMessageUserService.queryCommMessageIds(messageIds);// 评论数
			Map<String, Object> commMap = new HashMap<String, Object>();
			for (Object[] info : commLists) {
				commMap.put((String) info[1], info[0]);
			}

			for (GjtMessageInfo gjtMessageInfo : oldPage) {
				MessageInfoVo vo = new MessageInfoVo(gjtMessageInfo, putMap, readMap, likeMap, ticklingMap, commMap);
				list.add(vo);
			}
		}
		Page<MessageInfoVo> pageInfo = new PageImpl<MessageInfoVo>(list, pageRequst, oldPage.getTotalElements());

		long totalNum = gjtMessageInfoService.queryAllCount(searchParams);
		searchParams.put("EQ_degree", "1");
		long importantNum = gjtMessageInfoService.queryAllCount(searchParams);
		searchParams.put("EQ_degree", "0");
		long genericNum = gjtMessageInfoService.queryAllCount(searchParams);

		searchParams.remove("EQ_degree");
		searchParams.put("GT_effectiveTime", DateUtils.getStringToDate(DateUtils.getDate()));
		long efectiveNum = gjtMessageInfoService.queryAllCount(searchParams);

		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("isEffective", isEffective);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("importantNum", importantNum);
		model.addAttribute("genericNum", genericNum);
		model.addAttribute("efectiveNum", efectiveNum);
		return "home/messageInfo/message_put_list";

	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String getList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		searchParams.put("EQ_userId", user.getId());
		searchParams.put("EQ_gjtMessageInfo.isDeleted", "N");

		Page<GjtMessageUser> infos = gjtMessageUserService.queryAll(searchParams, pageRequst);

		List<MessageInfoVo> list = new ArrayList<MessageInfoVo>();
		for (GjtMessageUser gjtMessageUser : infos) {
			MessageInfoVo vo = new MessageInfoVo(gjtMessageUser);
			list.add(vo);
		}

		Page<MessageInfoVo> page = new PageImpl<MessageInfoVo>(list, pageRequst, infos.getTotalElements());

		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("pageInfo", page);

		return "home/messageInfo/message_get_list";

	}

	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(Model model, HttpServletRequest request, @PathVariable(value = "id") String id) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtMessageInfo info = gjtMessageInfoService.queryById(id);
		Map<String, String> guanliyuanMap = MessageUserRoleMap.guanliyuanMap;
		Map<String, String> infoTypeMap = new HashMap<String, String>();
		Map<String, String> dates = commonMapService.getDates("InfoType");// 字典表通知类型
		String roleId = user.getPriRoleInfo().getRoleId();
		String infoTypeId = info.getInfoType();
		String roleName = guanliyuanMap.get(roleId);
		// 先判断是不是特殊的角色，特殊的角色可以发很多种通知
		if (StringUtils.isNotBlank(roleName)) {
			infoTypeMap = dates;// 什么通知都有
		} else {
			infoTypeMap.put(infoTypeId, dates.get(infoTypeId));
		}

		boolean isStick = false;
		if (info.getEffectiveTime() != null) {
			isStick = info.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}

		model.addAttribute("isStick", isStick);
		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("infoTypeId", infoTypeId);
		model.addAttribute("action", "update");
		model.addAttribute("info", info);
		return "home/messageInfo/message_form";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid GjtMessageInfo entity, String gradeId, String pycc, String specialtyId, String days,
			String times, String stick, HttpServletRequest request, String messageId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtMessageInfo info = gjtMessageInfoService.queryById(messageId);
		Feedback feedback = new Feedback(true, "修改成功！");
		try {
			info.setInfoTheme(entity.getInfoTheme());
			info.setInfoType(entity.getInfoType());
			String parameter = request.getParameter("r3");
			String infoContent = request.getParameter("infoContent" + parameter);
			info.setInfoContent(infoContent);
			info.setFileUrl(entity.getFileUrl());
			info.setFileName(entity.getFileName());
			info.setAttachment(entity.getAttachment());
			info.setDegree(entity.getDegree());
			// 是否置顶1是
			if ("1".equals(stick)) {
				if ("0".equals(days)) {// 是否自定义时间，0是
					if (StringUtils.isNotBlank(times)) {
						info.setEffectiveTime(DateUtils.getStrToDate(times + " 23:59:59"));
					}
				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtils.getDate());
					calendar.add(Calendar.DAY_OF_YEAR, +Integer.valueOf(days));
					Date date = calendar.getTime();
					info.setEffectiveTime(date);
				}
			} else {// 不置顶就取当前时间，好用于排序
				info.setEffectiveTime(DateUtils.getDate());
			}
			info.setUpdatedBy(user.getId());
			info.setUpdatedDt(DateUtils.getNowTime());
			Boolean updateEntity = gjtMessageInfoService.updateEntity(info);
			if (!updateEntity) {
				feedback = new Feedback(true, "保存失败！");
			}
		} catch (Exception e) {
			feedback = new Feedback(true, "修改失败，发生异常！");
			log.error(e.getMessage(), e);
		}
		return feedback;
	}

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
		Map<String, String> guanliyuanMap = MessageUserRoleMap.guanliyuanMap;

		Map<String, String> benshenMap = MessageUserRoleMap.benshenMap;// key=roleId;value=InfoType的Code
		Map<String, String> infoTypeMap = new HashMap<String, String>();
		Map<String, String> dates = commonMapService.getDates("InfoType");// 字典表通知类型
		String roleId = user.getPriRoleInfo().getRoleId();
		String infoTypeId = "";

		// 接收人角色
		Map<String, String> getUserRoleMap = MessageUserRoleMap.getUserRoleMap(roleId);

		String roleName = guanliyuanMap.get(roleId);
		// 先判断是不是特殊的角色，特殊的角色可以发很多种通知
		if (StringUtils.isNotBlank(roleName)) {
			infoTypeMap = dates;// 什么通知都有
		} else {
			infoTypeId = benshenMap.get(roleId);// 本身对应的角色通知类型
			infoTypeMap.put(infoTypeId, dates.get(infoTypeId));
		}

		Map<String, String> userTypeMap = commonMapService.getDates("USER_TYPE");// 学员类型
		Map<String, String> xjztMap = commonMapService.getDates("StudentNumberStatus");// 学籍状态

		Map<String, String> getScopeObjectMap = commonMapService.getDates("GetScopeObject");// 发送范围对象

		// List<PriRoleInfoRun> priRoleInfoList =
		// gjtUserAccountService.findOne(user.getId()).getPriRoleInfo()
		// .getPriRoleInfoList();
		// List<String> searchRoleIds = new LinkedList<String>();

		// 写死的角色，坑爹的设计
		List<String> roleIds = new ArrayList<String>();
		roleIds.add("fcf94a20da1c44a1a31f94eafaf4b707");// 班主任
		roleIds.add("7c0d7c8a39f315d610377458894050ae");// 教务管理员
		roleIds.add("03799e0b9fa7d6fe56c548df0cc3b150");// 考务管理员
		roleIds.add("409891a89c2e4e0ca12381e207e3d9bb");// 教材管理员
		roleIds.add("3f5f7ca336a64c42bc5d3a4c1986289e");// 学籍管理员
		roleIds.add("97e31c2c70a442208443751fdeede0ff");// 毕业管理员
		roleIds.add("449c71cbac1082b468a5941fbba4e5d6");// 教学管理员
		roleIds.add("be60d336bc1946a5a24f88d5ae594b17");// 辅导教师
		roleIds.add("699f6f83acf54548bfae7794915a3cf3");// 督导教师
		roleIds.add("9a6f05b3e24d456fb84435dd75e934c2");// 学支管理员
		roleIds.add("bf59965fdc774f21bae6394ed8fe8ceb");// 平台运管员
		roleIds.add("K5f2034cdd64840e5946f72b5ab3a0ffb");// 招生管理员
		roleIds.add("d4b27a66c0a87b010120da231915c223");// 院长

		// for (PriRoleInfoRun priRoleInfoRun : priRoleInfoList) {
		// for (String str : roleIds) {
		// if (priRoleInfoRun.getSubsetRoleId().equals(str)) {
		// searchRoleIds.add(str);
		// }
		// }
		// }

		Map<String, Object> shuchuMap = new LinkedHashMap<String, Object>();
		// if (EmptyUtils.isNotEmpty(searchRoleIds)) {
		List<Map<String, Object>> byOrg = gjtUserAccountService.queryUserRoleByOrg(orgId, roleIds);
		for (String str : roleIds) {
			List<Map<String, Object>> yonghuMap = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : byOrg) {
				if (str.equals(map.get("roleId"))) {
					yonghuMap.add(map);
				}
			}
			shuchuMap.put(str, yonghuMap);
		}
		// }

		model.addAttribute("shuchuMap", shuchuMap);
		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("xjztMap", xjztMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		// model.addAttribute("infoTypeId", infoTypeId);
		model.addAttribute("getUserRoleMap", getUserRoleMap);
		model.addAttribute("action", "create");
		String uuid = UUIDUtils.random().toString();
		model.addAttribute("newMessageId", uuid);

		return "home/messageInfo/message_form";
	}

	// corePoolSize 核心线程池大小
	// maximumPoolSize 线程池最大容量大小
	// keepAliveTime 线程池空闲时，线程存活的时间
	// TimeUnit 时间单位
	// ThreadFactory 线程工厂
	// BlockingQueue任务队列
	// RejectedExecutionHandler 线程拒绝策略
	// ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
	// workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
	// LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；

	static ExecutorService threadPool = new ThreadPoolExecutor(1, 5, 30, TimeUnit.MILLISECONDS,
			new LinkedBlockingDeque<Runnable>(1024), Executors.defaultThreadFactory(),
			new ThreadPoolExecutor.AbortPolicy());

	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtMessageInfo entity, String gradeIdAll, String pyccIdAll, String specialtyIdAll,
			String courseIdAll, String userTypeAll, String xjztTypeAll, String days, String times, String stick,
			HttpServletRequest request, String isWeiXin, String newMessageId, String weixinContent, String weixinTitle,
			String weixinTime, String weixinRemark) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String orgId = user.getGjtOrg().getId();
		String messageId = StringUtils.isBlank(newMessageId) ? UUIDUtils.random().toString() : newMessageId;

		List<GjtMessageSearch> messageSearchList = new ArrayList<GjtMessageSearch>();
		Map<String, Object> searchMap = new HashMap<String, Object>(); // 查询条件
		searchMap.put("gradeIdAll", gradeIdAll);
		searchMap.put("pyccIdAll", pyccIdAll);
		searchMap.put("specialtyIdAll", specialtyIdAll);
		searchMap.put("courseIdAll", courseIdAll);
		searchMap.put("userTypeAll", userTypeAll);
		searchMap.put("xjztTypeAll", xjztTypeAll);
		searchMap.put("orgId", orgId);

		if ("0".equals(entity.getIsAppoint())) {// 0-按照条件查询，1是导入
			GjtMessagePutObject gjtMessagePutObject = new GjtMessagePutObject();
			String gjtMessagePutObjectIdStudent = UUIDUtils.random();
			gjtMessagePutObject.setId(gjtMessagePutObjectIdStudent);
			gjtMessagePutObject.setCode("1");
			gjtMessagePutObject.setName("学员");
			gjtMessagePutObject.setMessageId(messageId);
			gjtMessagePutObject.setOrderSort(1);
			gjtMessagePutObjectService.save(gjtMessagePutObject);

			if ("1".equals(gradeIdAll)) {
				GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
				gjtMessageSearch.setId(UUIDUtils.random());
				gjtMessageSearch.setConditionCode("10");
				gjtMessageSearch.setConditionName("学期");
				gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
				gjtMessageSearch.setConditionContent("全部学期");
				gjtMessageSearch.setOrderSort(10);
				messageSearchList.add(gjtMessageSearch);
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
					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode("10");
					gjtMessageSearch.setConditionName("学期");
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(gradeNames.toString());
					gjtMessageSearch.setOrderSort(10);
					messageSearchList.add(gjtMessageSearch);

					searchMap.put("gradeIds", gradeIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(pyccIdAll)) {
				GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
				gjtMessageSearch.setId(UUIDUtils.random());
				gjtMessageSearch.setConditionCode("20");
				gjtMessageSearch.setConditionName("层次");
				gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
				gjtMessageSearch.setConditionContent("全部层次");
				gjtMessageSearch.setOrderSort(20);
				messageSearchList.add(gjtMessageSearch);
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

					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode("20");
					gjtMessageSearch.setConditionName("层次");
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(pyccNames.toString());
					gjtMessageSearch.setOrderSort(20);
					messageSearchList.add(gjtMessageSearch);

					searchMap.put("pyccIds", pyccIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(specialtyIdAll)) {
				GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
				gjtMessageSearch.setId(UUIDUtils.random());
				gjtMessageSearch.setConditionCode("30");
				gjtMessageSearch.setConditionName("专业");
				gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
				gjtMessageSearch.setConditionContent("全部专业");
				gjtMessageSearch.setOrderSort(30);
				messageSearchList.add(gjtMessageSearch);
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

					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode("30");
					gjtMessageSearch.setConditionName("专业");
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(specialtyNames.toString());
					gjtMessageSearch.setOrderSort(30);
					messageSearchList.add(gjtMessageSearch);

					searchMap.put("specialtyIds", specialtyIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(courseIdAll)) {
				GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
				gjtMessageSearch.setId(UUIDUtils.random());
				gjtMessageSearch.setConditionCode("40");
				gjtMessageSearch.setConditionName("课程");
				gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
				gjtMessageSearch.setConditionContent("全部课程");
				gjtMessageSearch.setOrderSort(40);
				messageSearchList.add(gjtMessageSearch);
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

					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode("40");
					gjtMessageSearch.setConditionName("课程");
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(courseNames.toString());
					gjtMessageSearch.setOrderSort(40);
					messageSearchList.add(gjtMessageSearch);

					searchMap.put("courseIds", courseIds_.toString());// 搜索条件
				}
			}

			if ("1".equals(userTypeAll)) {
				GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
				gjtMessageSearch.setId(UUIDUtils.random());
				gjtMessageSearch.setConditionCode("50");
				gjtMessageSearch.setConditionName("学生类型");
				gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
				gjtMessageSearch.setConditionContent("全部学生类型");
				gjtMessageSearch.setOrderSort(50);
				messageSearchList.add(gjtMessageSearch);
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

					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode("50");
					gjtMessageSearch.setConditionName("学生类型");
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(userTypeNames.toString());
					gjtMessageSearch.setOrderSort(50);
					messageSearchList.add(gjtMessageSearch);

					searchMap.put("userTypes", userTypes_.toString());// 搜索条件
				}
			}

			if ("1".equals(xjztTypeAll)) {
				GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
				gjtMessageSearch.setId(UUIDUtils.random());
				gjtMessageSearch.setConditionCode("60");
				gjtMessageSearch.setConditionName("学籍状态");
				gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
				gjtMessageSearch.setConditionContent("全部学籍状态");
				gjtMessageSearch.setOrderSort(60);
				messageSearchList.add(gjtMessageSearch);
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
					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode("60");
					gjtMessageSearch.setConditionName("学籍状态");
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(xjztTypeNames.toString());
					gjtMessageSearch.setOrderSort(60);
					messageSearchList.add(gjtMessageSearch);
					searchMap.put("xjztTypes", xjztTypes_.toString());// 搜索条件
				}
			}
			gjtMessageSearchService.save(messageSearchList);
		}

		// 除了学生，其他都是角色表
		String[] userIds = request.getParameterValues("userIds");// 所有除开学员的用户Id
		List<GjtMessageSearch> messageSearchUserList = new ArrayList<GjtMessageSearch>();
		List<GjtMessagePutObject> messagePutObjectList = new ArrayList<GjtMessagePutObject>();
		List<String> asList = new ArrayList<String>();
		if (EmptyUtils.isNotEmpty(userIds)) {
			Set<String> putObject = new HashSet<String>();// 发送对象
			for (String values : userIds) {
				String[] split = values.split("_");
				if (!user.getId().equals(split[0])) {
					asList.add(split[0]);
				}
				putObject.add(split[1]);
			}

			Map<String, String> dates = commonMapService.getDates("GetScopeObject");
			for (String putObjectType : putObject) {
				GjtMessagePutObject gjtMessagePutObject = new GjtMessagePutObject();
				String gjtMessagePutObjectIdStudent = UUIDUtils.random();
				gjtMessagePutObject.setId(gjtMessagePutObjectIdStudent);
				gjtMessagePutObject.setCode(putObjectType);
				gjtMessagePutObject.setName(dates.get(putObjectType));
				gjtMessagePutObject.setMessageId(messageId);
				gjtMessagePutObject.setOrderSort(Integer.valueOf(putObjectType));
				messagePutObjectList.add(gjtMessagePutObject);

				Set<String> putRole = new HashSet<String>();// 发送角色
				for (String values : userIds) {
					String[] split = values.split("_");
					if (putObjectType.equals(split[1])) {
						putRole.add(split[2]);
					}
				}
				List<String> putRoleList = new ArrayList<String>(putRole);
				for (int i = 0; i < putRoleList.size(); i++) {
					StringBuffer sbs = new StringBuffer();
					for (String values : userIds) {
						String[] split = values.split("_");
						if (putRoleList.get(i).equals(split[2])) {
							sbs.append(split[3] + ",");
						}
					}
					GjtMessageSearch gjtMessageSearch = new GjtMessageSearch();
					gjtMessageSearch.setId(UUIDUtils.random());
					gjtMessageSearch.setConditionCode(i + "0");
					gjtMessageSearch.setConditionName(putRoleList.get(i));// 角色名
					gjtMessageSearch.setPutObjectId(gjtMessagePutObjectIdStudent);
					gjtMessageSearch.setConditionContent(sbs.toString());
					gjtMessageSearch.setOrderSort(i + 10);
					messageSearchUserList.add(gjtMessageSearch);
				}
				gjtMessageSearchService.save(messageSearchUserList);
			}
			gjtMessagePutObjectService.save(messagePutObjectList);
		}

		Feedback feedback = new Feedback(true, "发送成功");

		/*
		 * if (StringUtils.isBlank(entity.getInfoContent().trim())) { feedback =
		 * new Feedback(false, "创建失败，内容不能为空"); return feedback; }
		 */

		if (StringUtils.isBlank(entity.getInfoTheme().trim())) {
			feedback = new Feedback(false, "创建失败，标题不能为空");
			return feedback;
		}

		GjtMessageInfo saveEntity = null;
		try {
			String parameter = request.getParameter("r3");
			String infoContent = request.getParameter("infoContent" + parameter);
			entity.setInfoContent(infoContent);
			entity.setMessageId(messageId);
			entity.setCreatedBy(user.getRealName());
			entity.setCreateRoleName(user.getPriRoleInfo().getRoleName());
			entity.setCreatedDt(DateUtils.getNowTime());
			entity.setIsDeleted("N");
			entity.setIsEnabled("0");
			entity.setVersion(new BigDecimal(2.5));
			entity.setInfoTool("1");
			entity.setPutUser(user.getId());// 发送者的userId
			entity.setGjtOrg(user.getGjtOrg());

			// 是否置顶1是
			if ("1".equals(stick)) {
				if ("0".equals(days)) {// 是否自定义时间，0是
					if (StringUtils.isNotBlank(times)) {
						entity.setEffectiveTime(DateUtils.getStrToDate(times + " 23:59:59"));
					}
				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtils.getDate());
					calendar.add(Calendar.DAY_OF_YEAR, +Integer.valueOf(days));
					Date date = calendar.getTime();
					entity.setEffectiveTime(date);
				}
			} else {// 不置顶就取当前时间，好用于排序
				entity.setEffectiveTime(DateUtils.getDate());
			}
			saveEntity = gjtMessageInfoService.saveEntity(entity);

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		if (saveEntity == null) {
			feedback = new Feedback(false, "发送失败，发生异常");
			return feedback;
		}

		if ("1".equals(entity.getIsFeedback())) {// 反馈表
			String[] feedbackContents = request.getParameterValues("feedbackContents");
			if (EmptyUtils.isNotEmpty(feedbackContents)) {
				List<GjtMessageFeedback> feedbackList = new ArrayList<GjtMessageFeedback>();
				for (int i = 0; i < feedbackContents.length; i++) {
					GjtMessageFeedback item = new GjtMessageFeedback();
					item.setId(UUIDUtils.random());
					item.setMessageId(messageId);
					item.setName(feedbackContents[i]);
					item.setCode(String.valueOf(i));
					feedbackList.add(item);
				}
				gjtMessageFeedbackService.save(feedbackList);
			}
		}

		Set<String> allSets = new HashSet<String>();// 所有用户
		List<Map<String, Object>> lists = Lists.newArrayList();// student推送到手机APP
		List<String> weixinLists = Lists.newArrayList();// 身份证推送到微信应用平台
		log.info("查询条件：searchMap={},messageId={}", searchMap, messageId);
		try {
			// 查询院校下所有的学生
			long startTime = System.currentTimeMillis();
			if ("0".equals(entity.getIsAppoint())) {
				lists = gjtStudentInfoService.queryPutStudent(searchMap);
			} else {
				lists = gjtMessageInportUserService.queryImportStudent(messageId);
			}
			System.err.println("查询时间：" + (System.currentTimeMillis() - startTime));
			for (Map<String, Object> map : lists) {
				String id = (String) map.get("ID");
				String sfzh = (String) map.get("SFZH");
				allSets.add(id);
				weixinLists.add(sfzh);
			}
			log.info("学生人数：{}", allSets.size());

			// List<String> asList = Arrays.asList(userIds);// 除了学生，其他都是角色表
			allSets.addAll(asList);
			log.info("对应人数：{}", asList.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		try {
			log.info("一共发送人数：{}", allSets.size());
			List<String> allLists = new ArrayList<String>(allSets);
			threadSaveMessageUser(allLists, messageId, user);
			// 推送
			final GjtMessageInfo entity2 = entity;
			final List<Map<String, Object>> lists2 = lists;

			final String isWeiXins = isWeiXin;
			final List<String> weixinLists2 = weixinLists;
			// saveEntity.setInfoContent(weixinContent);
			// saveEntity.setInfoTheme(weixinTitle);
			final GjtMessageInfo info = saveEntity;

			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					String devMode = AppConfig.getProperty("devMode");
					if ("1".equals(devMode)) {
						log.info("测试环境不推送到APP！");
					} else {
						try {
							push(entity2, lists2);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
						try {
							if ("1".equals(isWeiXins)) {
								if (EmptyUtils.isNotEmpty(weixinLists2)) {
									pushApplication(weixinLists2, info);
								}
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				}
			});

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return feedback;
	}

	private void pushApplication(List<String> weixinLists2, GjtMessageInfo info) {

		StringBuffer sb = new StringBuffer();// 身份证
		sb.append("[");
		for (int i = 0; i < weixinLists2.size(); i++) {
			sb.append("\"" + weixinLists2.get(i) + "\"");
			if (i < (weixinLists2.size() - 1)) {
				sb.append(",");
			}
		}
		sb.append("]");

		Map<String, String> dates = commonMapService.getDates("InfoType");// 字典表通知类型
		String url = AppConfig.getProperty("applicationPlatform.domain") + "/api/wechat/send/msg";
		Map<String, Object> params = new HashMap<String, Object>();
		// params.put("spaceid",
		// info.getGjtOrg().getCode());//要么就定死81要么不传
		params.put("title", info.getInfoTheme());

		String content = info.getInfoContent();
		if (StringUtils.isNotEmpty(content)) {
			// 去除HTML标签
			String reg = "(<[^>]*>)";
			Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(content);
			String res = m.replaceAll("");
			content = res.length() > 100 ? res.substring(0, 100) + "..." : res;
		}
		params.put("description", content);
		params.put("type", dates.get(info.getInfoType()));
		params.put("idcards", sb.toString());
		params.put("content", info.getInfoContent());
		log.info("查询参数：{}", params);
		String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
		if (StringUtils.isNotEmpty(result)) {
			JSONObject json = JSONObject.fromObject(result);
			log.info("推送到应用平台：{}", json);
		}
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
					MessageAddThread mat = new MessageAddThread(gjtMessageUserService, messageId, user.getRealName(),
							subList);
					// new Thread(mat).start();
					threadPool.execute(mat);
				}
			} else {
				MessageAddThread mat = new MessageAddThread(gjtMessageUserService, messageId, user.getRealName(),
						lists);
				threadPool.execute(mat);
			}
		}

	}

	private void push(GjtMessageInfo entity, List<Map<String, Object>> lists) {

		String content = entity.getInfoContent();
		if (StringUtils.isNotEmpty(content)) {
			// 去除HTML标签
			String reg = "(<[^>]*>)";
			Pattern p = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(content);
			String res = m.replaceAll("");
			content = res.length() > 40 ? res.substring(0, 40) + "..." : res;
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", entity.getMessageId());
		params.put("title", entity.getInfoTheme());// 标题
		params.put("alert", content);// 内容
		params.put("time", DateUtils.getStringToDate(entity.getCreatedDt()));
		params.put("type", entity.getInfoType());

		// 按学员推送,一次推送最多 1000 个。
		Map<SmsSenderType, Set<String>> atidMap = Maps.newHashMap();
		for (Map<String, Object> map : lists) {
			String atid = (String) map.get("ATID");
			String xxzxId = (String) map.get("XXZX_ID");
			/*
			 * String xxId = map.get("XX_ID"); // 国家开放大学（广州）实验学院 if
			 * (SmsSenderType.guangzhoushiyanxueyuan.getOrgId().equals(xxId)) {
			 * if (atidMap.get(SmsSenderType.guangzhoushiyanxueyuan) == null) {
			 * atidMap.put(SmsSenderType.guangzhoushiyanxueyuan, new
			 * HashSet<String>()); }
			 * atidMap.get(SmsSenderType.guangzhoushiyanxueyuan).add(atid); } //
			 * 国家开放大学实验学院 if
			 * (SmsSenderType.guangzhoushiyanxueyuan.getOrgId().equals(xxId)) {
			 * if (atidMap.get(SmsSenderType.guokaishiyanxueyuan) == null) {
			 * atidMap.put(SmsSenderType.guokaishiyanxueyuan, new
			 * HashSet<String>()); }
			 * atidMap.get(SmsSenderType.guokaishiyanxueyuan).add(atid); }
			 */
			// 国家开放大学（广州）实验学院
			if (atidMap.get(SmsSenderType.guangzhoushiyanxueyuan) == null) {
				atidMap.put(SmsSenderType.guangzhoushiyanxueyuan, new HashSet<String>());
			}
			atidMap.get(SmsSenderType.guangzhoushiyanxueyuan).add(atid);
			// 国家开放大学实验学院
			if (atidMap.get(SmsSenderType.guokaishiyanxueyuan) == null) {
				atidMap.put(SmsSenderType.guokaishiyanxueyuan, new HashSet<String>());
			}
			atidMap.get(SmsSenderType.guokaishiyanxueyuan).add(atid);
			SmsSenderType type = SmsSenderType.getItemByOrgId(xxzxId);
			if (type != null) {
				if (atidMap.get(type) == null) {
					atidMap.put(type, new HashSet<String>());
				}
				atidMap.get(type).add(atid);
			} else { // TODO test
				if (atidMap.get(SmsSenderType.jpushTestSender) != null) {
					atidMap.get(SmsSenderType.jpushTestSender).add(atid);
				} else {
					atidMap.put(SmsSenderType.jpushTestSender, new HashSet<String>());
					atidMap.get(SmsSenderType.jpushTestSender).add(atid);
				}
			}

		}

		log.info(JsonUtils.toJson(atidMap));
		for (Entry<SmsSenderType, Set<String>> entry : atidMap.entrySet()) {
			SmsSenderType type = entry.getKey();
			List<String> atidList = Lists.newArrayList(entry.getValue());
			int batch = atidList.size() / 1000;
			int iter = batch;
			while (iter >= 0) {
				int end;
				if (atidList.size() > ((batch - iter) * 1000 + 1000)) {
					end = (batch - iter) * 1000 + 1000;
				} else {
					end = atidList.size();
				}
				List<String> subList = new ArrayList<String>(atidList.subList((batch - iter) * 1000, end));
				params.put("alias", subList);
				log.info("开始推送:{}", type.getName());
				smsSenderManager.smsSend(type, params);
				log.info("推送完成:{}", type.getName());
				iter--;
			}
		}
		// test
		/*
		 * List<String> atidList = Lists.newArrayList();
		 * atidList.add("BC9F294E91854E2CAE62058CCE540136");
		 * atidList.add("D13B0F1E603E4FF6BCD32674E664AB2E"); params.put("alias",
		 * atidList); smsSenderManager.smsSend(sendType, params);
		 */
	}

	public String getString(String str) {
		String[] strs = str.split(",");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strs.length; i++) {
			sb.append("'" + strs[i] + "'");
			if (i < (strs.length - 1)) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	// 查看别人发我的明细，并修改为已读
	@RequestMapping(value = "/detail")
	public String detail(@RequestParam(value = "id") String id, HttpServletRequest request, Model model) {
		GjtMessageInfo gjtMessageInfo = gjtMessageInfoService.queryById(id);

		List<String> messageIds = new ArrayList<String>();
		messageIds.add(id);
		List<Object[]> putMaps = gjtMessageUserService.queryPutMessageIds(messageIds);// 发送总数
		Object putCount = 0;
		for (Object[] info : putMaps) {
			putCount = info[0];
		}

		List<Object[]> readMaps = gjtMessageUserService.queryReadMessageIds(messageIds);// 已读总数
		Object readCount = 0;
		for (Object[] info : readMaps) {
			readCount = info[0];
		}

		boolean isStick = false;
		if (gjtMessageInfo.getEffectiveTime() != null) {
			isStick = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}

		model.addAttribute("readCount", readCount);
		model.addAttribute("putCount", putCount);
		model.addAttribute("gjtMessageInfo", gjtMessageInfo);
		model.addAttribute("view", true);
		model.addAttribute("isStick", isStick);
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		return "home/messageInfo/detail";
	}

	public String getRoleType(String str) {
		for (int i = 0; i < MessageInfoRoleTypeEnum.values().length; i++) {
			String string = MessageInfoRoleTypeEnum.getName(i).toString();
			if (str.equals(string)) {
				return String.valueOf(i);
			}
		}
		return "10";
	}

	@RequestMapping(value = "cleanStick", method = RequestMethod.GET)
	@ResponseBody
	public Feedback cleanStick(String id) {
		Feedback fb = new Feedback(true, "成功");
		try {
			GjtMessageInfo entity = gjtMessageInfoService.queryById(id);
			entity.setEffectiveTime(DateUtils.getNowTime());
			gjtMessageInfoService.updateEntity(entity);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "出错了" + e.getMessage());
		}
		return fb;
	}

	// 查看我发送给别人的明细
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model) {
		GjtMessageInfo gjtMessageInfo = gjtMessageInfoService.queryById(id);

		List<String> messageIds = new ArrayList<String>();
		messageIds.add(id);
		List<Object[]> putMaps = gjtMessageUserService.queryPutMessageIds(messageIds);// 发送总数
		Object putCount = 0;
		for (Object[] info : putMaps) {
			putCount = info[0];
		}

		List<Object[]> readMaps = gjtMessageUserService.queryReadMessageIds(messageIds);// 已读总数
		Object readCount = 0;
		for (Object[] info : readMaps) {
			readCount = info[0];
		}

		boolean isStick = false;
		if (gjtMessageInfo.getEffectiveTime() != null) {
			isStick = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}

		List<GjtMessagePutObject> putObjectList = gjtMessageInfo.getGjtMessagePutObjects();
		model.addAttribute("readCount", readCount);
		model.addAttribute("putCount", putCount);
		model.addAttribute("gjtMessageInfo", gjtMessageInfo);
		model.addAttribute("view", true);
		model.addAttribute("isStick", isStick);
		model.addAttribute("putObjectList", putObjectList);
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		return "home/messageInfo/view";
	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				gjtMessageInfoService.delete(ids);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败，原因:" + e.getMessage());
			}
		}
		return fb;
	}

	// 查询信息发送的所有用户
	@RequestMapping(value = "queryPutListById", method = RequestMethod.GET)
	public String queryPutListById(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request, String id,
			Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		String orgId = user.getGjtOrg().getId();
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyCodeMap(orgId);
		List<PriRoleInfo> roleList = priRoleInfoService.queryAll();

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("messageId", id);
		String isRead = (String) searchParams.get("EQ_isRead");
		if ("1".equals(isRead)) {
			searchParams.put("platform", "0");
			searchParams.put("isRead", "1");
		} else if ("2".equals(isRead)) {
			searchParams.put("platform", "1");
			searchParams.put("isRead", "1");
		} else if ("3".equals(isRead)) {
			searchParams.put("platform", "2");
			searchParams.put("isRead", "1");
		} else if ("0".equals(isRead)) {
			searchParams.put("isRead", "0");
		}
		log.info("查询信息发送的所有用户参数：{};", searchParams);
		Page<Map> pageInfo = gjtMessageUserService.queryAllByMessageId(searchParams, pageRequst);

		List<Object[]> readMaps = gjtMessageUserService.queryReadMessageIds(Arrays.asList(id));// 已读按照平台
		long yesReadPc = 0;
		long yesReadAPP = 0;
		long yesReadComm = 0;
		for (Object[] info : readMaps) {
			if ("0".equals(info[2])) {
				yesReadPc = Long.valueOf(info[0].toString());
			}
			if ("1".equals(info[2])) {
				yesReadAPP = Long.valueOf(info[0].toString());
			}
			if ("2".equals(info[2])) {
				yesReadComm = Long.valueOf(info[0].toString());
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("EQ_messageId", id);
		map.put("EQ_isEnabled", "0");
		long noRead = gjtMessageUserService.queryAllCount(map);

		model.addAttribute("messageId", id);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("roleList", roleList);
		model.addAttribute("yesReadPc", yesReadPc);
		model.addAttribute("yesReadAPP", yesReadAPP);
		model.addAttribute("yesReadComm", yesReadComm);
		model.addAttribute("noRead", noRead);
		return "home/messageInfo/statistics_view";
	}

	@RequestMapping(value = "queryTypeClassifyByInfoType", method = RequestMethod.GET)
	@ResponseBody
	public Feedback queryTypeClassifyByInfoType(String infoType) {
		log.info("根据类型查询分类，infoType={}", infoType);
		List<GjtMessageClassify> findList = gjtMessageClassifyService.findList(infoType);
		Feedback fb = new Feedback(true, "", findList);
		return fb;
	}

	@RequestMapping(value = "addClassify", method = RequestMethod.POST)
	@ResponseBody
	public Feedback addClassify(String text, String infoType) {
		log.info("新增类型分类，text={}", text);
		Feedback fb = new Feedback(true, "成功");
		try {
			GjtMessageClassify item = new GjtMessageClassify();
			item.setName(text);
			item.setInfoType(infoType);
			GjtMessageClassify save = gjtMessageClassifyService.save(item);
			if (save == null) {
				fb = new Feedback(false, "操作失败");
			} else {
				fb = new Feedback(true, "成功", save.getId());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, e.getMessage());
		}

		return fb;
	}

	@RequestMapping(value = "deleteClassify", method = RequestMethod.POST)
	@ResponseBody
	public Feedback deleteClassify(String id) {
		log.info("删除类型分类，id={}", id);
		Feedback fb = new Feedback(true, "成功");
		try {
			long count = gjtMessageInfoService.findByTypeClassify(id);
			if (count > 0) {
				fb = new Feedback(false, "分类已被使用，不能删除，只能编辑！");
			} else {
				gjtMessageClassifyService.delete(id);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, e.getMessage());
		}

		return fb;
	}

	@RequestMapping(value = "updateClassify", method = RequestMethod.POST)
	@ResponseBody
	public Feedback updateClassify(String text, String id) {
		log.info("修改类型分类，text={}", text);
		Feedback fb = new Feedback(true, "成功");
		try {
			GjtMessageClassify item = gjtMessageClassifyService.queryById(id);
			item.setName(text);
			gjtMessageClassifyService.save(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, e.getMessage());
		}

		return fb;
	}

	@RequestMapping(value = "findSpecialty", method = RequestMethod.GET)
	public String findSpecialty(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		// String[] specialtyIds = request.getParameterValues("specialtyIds");
		// if (EmptyUtils.isEmpty(specialtyIds)) {
		// searchParams.put("NOTIN_specialtyId", specialtyIds);
		// }
		Page<GjtSpecialty> pageInfo = gjtSpecialtyService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("pageInfo", pageInfo);
		return "home/messageInfo/specialty_list";
	}

	@RequestMapping(value = "findCourse", method = RequestMethod.GET)
	public String findCourse(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Map<String, String> typeMap = commonMapService.getDates("CourseCategoryInfo");
		Map<Integer, String> courserTypeMap = new HashMap<Integer, String>();
		for (Map.Entry<String, String> map : typeMap.entrySet()) {
			courserTypeMap.put(Integer.valueOf(map.getKey()), map.getValue());
		}

		Page<GjtCourse> pageInfo = gjtCourseService.queryAll(user.getGjtOrg().getId(), searchParams, pageRequst);
		model.addAttribute("courserTypeMap", courserTypeMap);
		model.addAttribute("pageInfo", pageInfo);
		return "home/messageInfo/course_list";
	}

	@RequestMapping(value = "getStudentCount", method = RequestMethod.POST)
	@ResponseBody
	public Feedback getStudentCount(HttpServletRequest request, String gradeIds, String gradeIdAll, String pyccIds,
			String pyccIdAll, String specialtyIds, String specialtyIdAll, String courseIds, String courseIdAll,
			String userTypes, String userTypeAll, String xjztTypes, String xjztTypeAll) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("gradeIds", gradeIds);
		searchMap.put("gradeIdAll", gradeIdAll);
		searchMap.put("pyccIds", pyccIds);
		searchMap.put("pyccIdAll", pyccIdAll);
		searchMap.put("specialtyIds", specialtyIds);
		searchMap.put("specialtyIdAll", specialtyIdAll);
		searchMap.put("courseIds", courseIds);
		searchMap.put("courseIdAll", courseIdAll);
		searchMap.put("userTypes", userTypes);
		searchMap.put("userTypeAll", userTypeAll);
		searchMap.put("xjztTypes", xjztTypes);
		searchMap.put("xjztTypeAll", xjztTypeAll);

		searchMap.put("orgId", user.getGjtOrg().getId());
		long count = gjtStudentInfoService.queryPutStudentCount(searchMap);
		Feedback fb = new Feedback(true, "", count);
		return fb;
	}

	// 按照一堆条件查询跳转
	@RequestMapping(value = "searchStudent", method = RequestMethod.GET)
	public String searchStudent(HttpServletRequest request, Model model, String isTranse) {
		model.addAttribute("isTranse", isTranse);
		return "home/messageInfo/search_student_list";
	}

	@RequestMapping(value = "searchStudentPage", method = RequestMethod.POST)
	public String searchStudentPage(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, HttpServletRequest request,
			String gradeIds, String gradeIdAll, String pyccIds, String pyccIdAll, String specialtyIds,
			String specialtyIdAll, String courseIds, String courseIdAll, String userTypes, String userTypeAll,
			String xjztTypes, String xjztTypeAll, String xh, String xm, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);

		// 其实这里要取父页面的条件，然后在做成下拉，但是判断太多了，如果不这样做，这个页面很多数据都是没意义的
		// 不然就要做成，比如选择了3个年级，现在所有年级应该就3个，然后选择其中一个，这样就不会又其他的年级查询条件
		// 如果不这样做，就要in(选择的查询条件) and eq(选择的年级) //这个简单点 只是sql要在写一层查询条件
		// 反正这个功能怎么写都复杂
		// Map<String, Object> searchMap = new HashMap<String, Object>();
		// String gradeId = (String) searchParams.get("EQ_gradeId");
		// if (StringUtils.isNotBlank(gradeId)) {
		// searchMap.put("gradeIds", gradeId);
		// } else {
		// searchMap.put("gradeIds", gradeIds);
		// }
		// searchMap.put("gradeIdAll", gradeIdAll);
		//
		// String pycc = (String) searchParams.get("EQ_pycc");
		// if (StringUtils.isNotBlank(pycc)) {
		// searchMap.put("pyccIds", pycc);
		// } else {
		// searchMap.put("pyccIds", pyccIds);
		// }
		// searchMap.put("pyccIdAll", pyccIdAll);
		//
		// String specialtyId = (String) searchParams.get("EQ_specialtyId");
		// if (StringUtils.isNotBlank(specialtyId)) {
		// searchMap.put("specialtyIds", specialtyId);
		// } else {
		// searchMap.put("specialtyIds", specialtyIds);
		// }
		// searchMap.put("specialtyIdAll", specialtyIdAll);
		//
		// String courseId = (String) searchParams.get("EQ_courseId");
		// if (StringUtils.isNotBlank(courseId)) {
		// searchMap.put("courseIds", courseId);
		// } else {
		// searchMap.put("courseIds", courseIds);
		// }
		// searchMap.put("courseIdAll", courseIdAll);
		// searchMap.put("userTypes", userTypes);
		// searchMap.put("userTypeAll", userTypeAll);
		// searchMap.put("xjztTypes", xjztTypes);
		// searchMap.put("xjztTypeAll", xjztTypeAll);
		// searchMap.put("orgId", user.getGjtOrg().getId());

		String orgId = user.getGjtOrg().getId();
		Map<String, Object> searchMap = Servlets.getParametersStartingWith(request, "search_");
		searchMap.put("gradeIds", gradeIds);
		searchMap.put("gradeIdAll", gradeIdAll);
		searchMap.put("pyccIds", pyccIds);
		searchMap.put("pyccIdAll", pyccIdAll);
		searchMap.put("specialtyIds", specialtyIds);
		searchMap.put("specialtyIdAll", specialtyIdAll);
		searchMap.put("courseIds", courseIds);
		searchMap.put("courseIdAll", courseIdAll);
		searchMap.put("userTypes", userTypes);
		searchMap.put("userTypeAll", userTypeAll);
		searchMap.put("xjztTypes", xjztTypes);
		searchMap.put("xjztTypeAll", xjztTypeAll);
		searchMap.put("orgId", user.getGjtOrg().getId());
		Page<Map<String, Object>> page = gjtStudentInfoService.queryPutStudent(searchMap, pageRequst);

		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> xjztMap = commonMapService.getDates("StudentNumberStatus");// 学籍状态
		Map<String, String> orgMap = commonMapService.getStudyCenterMap(orgId);// 学习中心
		model.addAttribute("orgMap", orgMap);

		model.addAttribute("pageInfo", page);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("xjztMap", xjztMap);
		return "home/messageInfo/search_student_list";
	}

	@RequestMapping(value = "importStudent", method = { RequestMethod.POST, RequestMethod.GET })
	public String importStudent(HttpServletRequest request, Model model, String newMessageId) {
		model.addAttribute("newMessageId", newMessageId);
		return "home/messageInfo/importStudent";
	}

	@RequestMapping(value = "saveImport", method = RequestMethod.POST)
	@ResponseBody
	public ImportFeedback saveImport(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model,
			String newMessageId) {

		String[] heads = null;
		heads = new String[4];
		heads[0] = "姓名";
		heads[1] = "学号";
		heads[2] = "专业";
		heads[3] = "导入结果";

		List<String[]> successList = new ArrayList<String[]>();
		List<String[]> failedList = new ArrayList<String[]>();
		List<String[]> dataList = null;
		try {
			dataList = ExcelUtil.readAsStringList(file.getInputStream(), 2, heads.length - 1);
		} catch (Exception e) {
			return new ImportFeedback(false, "请下载正确表格模版填写");
		}

		try {
			if (dataList != null && dataList.size() > 0) {
				for (String[] datas : dataList) {
					String[] result = new String[heads.length]; // 记录导入结果
					System.arraycopy(datas, 0, result, 0, Math.min(datas.length, heads.length - 1)); // 先拷贝数据

					if (datas.length < heads.length - 1) {
						result[heads.length - 1] = "数据不全";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[0])) {
						result[heads.length - 1] = "姓名不能为空";
						failedList.add(result);
						continue;
					}

					if ("".equals(datas[1])) {
						result[heads.length - 1] = "学号不能为空";
						failedList.add(result);
						continue;
					}

					String userId = gjtStudentInfoService.checkImportStudent(datas[0], datas[1], datas[2]);

					if (StringUtils.isBlank(userId)) {
						result[heads.length - 1] = "根据条件查找不到该学员";
						failedList.add(result);
						continue;
					}

					try {
						GjtMessageInportUser item = new GjtMessageInportUser();
						String id = UUIDUtils.random().toString();
						item.setId(id);
						item.setMessageId(newMessageId);
						item.setUserId(userId);
						gjtMessageInportUserService.save(item);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
						result[heads.length - 1] = "新增出错！";
						failedList.add(result);
						continue;
					}

					result[heads.length - 1] = "新增成功";
					successList.add(result);
				}
			}

			/* 创建记录成功和失败记录的文件 */
			long currentTimeMillis = System.currentTimeMillis();
			String successFileName = "messagebook_success_" + currentTimeMillis + ".xls";
			String failedFileName = "message_failed_" + currentTimeMillis + ".xls";

			Workbook workbook1 = ExcelUtil.getWorkbook(heads, successList, "活动组织导入学员成功记录");
			Workbook workbook2 = ExcelUtil.getWorkbook(heads, failedList, "活动组织导入学员失败记录");

			String filePath = request.getSession().getServletContext().getRealPath("") + WebConstants.EXCEL_DOWNLOAD_URL
					+ "message" + File.separator;
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

			return new ImportFeedback(true, dataList.size(), successList.size(), failedList.size(), successFileName,
					failedFileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ImportFeedback(false, "系统异常！");
		}
	}

	@RequestMapping(value = "getImportStudentCount", method = RequestMethod.GET)
	@ResponseBody
	public Feedback getImportStudentCount(String messageId) {
		Feedback fb = new Feedback(true, "");
		try {
			long count = gjtMessageInportUserService.getImportStudentCount(messageId);
			fb = new Feedback(true, "", count);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "searchOlineStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchOlineStudent(HttpServletRequest request, Model model, String newMessageId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

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
		Map<String, String> orgMap = commonMapService.getStudyCenterMap(orgId);// 学习中心

		searchParams.put("EQ_gjtUserAccount.isOnline", "Y");
		List<Map<String, Object>> olineList = gjtStudentInfoService.findOline(orgId, searchParams);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("xjztMap", xjztMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("olineList", olineList);
		model.addAttribute("newMessageId", newMessageId);
		return "home/messageInfo/search_oline_student_list";
	}

	@RequestMapping(value = "saveOlineStudent", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Feedback saveOlineStudent(String newMessageId, String userIds) {
		Feedback fb = new Feedback(true, "操作成功");
		try {
			List<GjtMessageInportUser> entitys = new ArrayList<GjtMessageInportUser>();
			if (StringUtils.isNotBlank(userIds)) {
				String[] split = userIds.split(",");
				for (String userId : split) {
					GjtMessageInportUser item = new GjtMessageInportUser();
					item.setId(UUIDUtils.random().toString());
					item.setMessageId(newMessageId);
					item.setUserId(userId);
					entitys.add(item);
				}
			}
			gjtMessageInportUserService.save(entitys);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "searchImportStudent", method = { RequestMethod.GET, RequestMethod.POST })
	public String searchImportStudent(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request,
			Model model, String newMessageId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		PageRequest pageRequest = Servlets.buildPageRequest(pageNumber, pageSize);

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
		Map<String, String> orgMap = commonMapService.getStudyCenterMap(orgId);// 学习中心

		searchParams.put("newMessageId", newMessageId);
		Page<Map<String, Object>> page = gjtStudentInfoService.findImportStudentPage(orgId, searchParams, pageRequest);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("userTypeMap", userTypeMap);
		model.addAttribute("xjztMap", xjztMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("orgMap", orgMap);
		model.addAttribute("pageInfo", page);
		model.addAttribute("newMessageId", newMessageId);
		return "home/messageInfo/search_import_student_list";
	}

	@RequestMapping(value = "commentList", method = RequestMethod.GET)
	public String commentList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request, String id,
			Model model, String is_personage) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// Map<String, Object> searchParams =
		// Servlets.getParametersStartingWith(request, "search_");

		Map<String, Object> platformMap = new HashMap<String, Object>();
		platformMap.put("0", "PC");
		platformMap.put("1", "APP");
		platformMap.put("2", "公众号");

		// GjtMessageInfo messageInfo = gjtMessageInfoService.queryById(id);
		// List<GjtMessageComment> list = messageInfo.getGjtMessageComments();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", user.getId());
		map.put("messageId", id);

		List<Map<String, Object>> list = gjtMessageCommentService.queryAll(map);
		for (Map<String, Object> map2 : list) {
			String comid = (String) map2.get("id");
			List<Map<String, Object>> queryByCommId = gjtMessageCommentDetailService.queryByCommId(comid, user.getId());
			map2.put("gjtMessageCommentDetails", queryByCommId);
		}

		model.addAttribute("size", list.size());
		model.addAttribute("list", list);
		model.addAttribute("messageId", id);
		model.addAttribute("is_personage", is_personage);
		model.addAttribute("userId", user.getId());
		// model.addAttribute("info", messageInfo);
		model.addAttribute("platformMap", platformMap);
		return "home/messageInfo/comment_view";
	}

	@RequestMapping(value = "likeList", method = RequestMethod.GET)
	public String likeList(HttpServletRequest request, String id, Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_isLike", "1");
		searchParams.put("EQ_messageId", id);
		List<GjtMessageUser> list = gjtMessageUserService.queryAll(searchParams);

		model.addAttribute("messageId", id);
		model.addAttribute("list", list);
		model.addAttribute("listSize", list.size());
		return "home/messageInfo/like_view";
	}

	@RequestMapping(value = "feedbackList", method = RequestMethod.GET)
	public String feedbackList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, HttpServletRequest request, String id,
			Model model) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		String orgId = user.getGjtOrg().getId();
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyCodeMap(orgId);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("messageId", id);

		log.info("查询信息发送的所有用户参数：{};", searchParams);
		Page<Map> pageInfo = gjtMessageUserService.queryAllByMessageId(searchParams, pageRequst);

		List<Map<String, Object>> ticktingList = gjtMessageUserService.queryTicktingList(id);
		long totalCount = 0;
		Map<String, Long> ticktingMap = new HashMap<String, Long>();

		for (Map<String, Object> map : ticktingList) {
			BigDecimal num = (BigDecimal) map.get("NUM");
			totalCount += num.longValue();
			ticktingMap.put((String) map.get("FEEDBACK_TYPE"), num.longValue());
		}
		DecimalFormat df = new DecimalFormat("######0.00");
		List<GjtMessageFeedback> list = gjtMessageFeedbackService.findByMessageId(id);

		Map<String, String> ticktingNameMap = new HashMap<String, String>();
		for (GjtMessageFeedback item : list) {
			String code = item.getCode();
			ticktingNameMap.put(code, item.getName());
			Long temp = ticktingMap.get(code);
			if (temp != null) {
				item.setNum(temp);
				Double percentage = temp * 100.0 / totalCount;
				item.setPercentage(df.format(percentage));
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("EQ_messageId", id);
		map.put("EQ_isTickling", "0");
		long noTickting = gjtMessageUserService.queryAllCount(map);

		map.put("EQ_isTickling", "1");
		long yesTickting = gjtMessageUserService.queryAllCount(map);

		model.addAttribute("messageId", id);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		model.addAttribute("list", list);
		model.addAttribute("yesTickting", yesTickting);
		model.addAttribute("noTickting", noTickting);
		model.addAttribute("ticktingNameMap", ticktingNameMap);

		return "home/messageInfo/feedback_view";
	}

	@RequestMapping(value = "settingsConstraintAll", method = RequestMethod.POST)
	@ResponseBody
	public Feedback settingsConstraintAll(String messageId, String type) {
		log.info("设置强制阅读:messageId={},type={}", messageId, type);
		Feedback fb = new Feedback(true, "成功");
		try {
			if ("message".equals(type)) {// 短信通知全部未读的人
				List<Map<String, Object>> list = gjtMessageUserService.findEntityByMessageId(messageId); // 用户名和手机号
				GjtMessageInfo messageInfo = gjtMessageInfoService.queryById(messageId);
				String title = messageInfo.getInfoTheme();
				if (StringUtils.isNotBlank(title)) {
					if (title.length() > 6) {
						title = messageInfo.getInfoTheme().substring(0, 5);
					}
				}
				String content = "你有一条关于" + title + "的通知，请上平台查看！";
				int size = list.size();
				if (size > 50) {
					int nThreads = 10;
					for (int i = 0; i < nThreads; i++) {
						int start = size / nThreads * i;
						int end = size / nThreads * (i + 1);
						if (!(i < nThreads - 1)) {
							end = size;// 最后一份有余数。直接取size最后一个下标
						}
						List<Map<String, Object>> subList = list.subList(start, end);
						StringBuilder manyPhones = new StringBuilder();
						for (Map<String, Object> map : subList) {
							manyPhones.append(map.get("SJH")).append(",");
						}
						int smsResult = SMSUtil.sendMessage(manyPhones.toString(), content, "gk");
						log.info("组织活动短信提醒发送的手机号：" + manyPhones.toString());
					}
				} else {
					StringBuilder manyPhones = new StringBuilder();
					for (Map<String, Object> map : list) {
						manyPhones.append(map.get("SJH")).append(",");
					}
					int smsResult = SMSUtil.sendMessage(manyPhones.toString(), content, "gk");
					log.info("组织活动短信提醒发送的手机号：" + manyPhones.toString());
				}
			} else { // 强制所有未读的人
				gjtMessageUserService.updateConstraint(messageId);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "settingsSingleConstraint", method = RequestMethod.POST)
	@ResponseBody
	public Feedback settingsSingleConstraint(String id, String isConstraint) {
		log.info("设置和取消强制阅读:id={},isConstraint={}", id, isConstraint);
		Feedback fb = new Feedback(true, "操作成功");
		try {
			gjtMessageUserService.settingsIsConstraint(id, isConstraint);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "操作失败！错误信息：" + e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "addComment", method = RequestMethod.POST)
	public String addComment(HttpServletRequest request, String messageId, String content,
			RedirectAttributes redirectAttributes) {
		log.info("设置和取消强制阅读:id={},isConstraint={}", messageId, content);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback fb = new Feedback(true, "操作成功");
		String[] imgUrls = request.getParameterValues("imgUrl");
		StringBuffer imgUrl = new StringBuffer();
		if (EmptyUtils.isNotEmpty(imgUrls)) {
			for (String string : imgUrls) {
				imgUrl.append(string + ",");
			}
		}
		try {
			gjtMessageCommentService.save(messageId, content, imgUrl.toString(), user,
					MessagePlatformEnum.PC.getCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "操作失败！错误信息：" + e.getMessage());
		}
		redirectAttributes.addFlashAttribute("feedback", fb);
		return "redirect:/admin/messageInfo/commentList?id=" + messageId;
	}

	@RequestMapping(value = "addLike", method = RequestMethod.POST)
	@ResponseBody
	public Feedback addLike(String id, HttpServletRequest request) {
		log.info("点赞评论:id={}", id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback fb = new Feedback(true, "操作成功");
		try {
			gjtMessageCommentLikeService.save(id, user);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "操作失败！错误信息：" + e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "deleteAsk", method = RequestMethod.POST)
	@ResponseBody
	public Feedback deleteAsk(String id, HttpServletRequest request, String type) {
		log.info("点赞删除评论:id={}", id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback fb = new Feedback(true, "操作成功");
		try {
			if ("parent".equals(type)) {
				GjtMessageComment entity = gjtMessageCommentService.queryById(id);
				entity.setIsDeleted("Y");
				entity.setUpdatedBy(user.getId());
				entity.setUpdatedDt(DateUtils.getNowTime());
				gjtMessageCommentService.update(entity);
			} else {
				GjtMessageCommentDetail entity = gjtMessageCommentDetailService.queryById(id);
				entity.setIsDeleted("Y");
				entity.setUpdatedBy(user.getId());
				entity.setUpdatedDt(DateUtils.getNowTime());
				gjtMessageCommentDetailService.update(entity);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "操作失败！错误信息：" + e.getMessage());
		}
		return fb;
	}

	@RequestMapping(value = "addAsk", method = RequestMethod.POST)
	@ResponseBody
	public Feedback addAsk(String id, HttpServletRequest request, String content) {
		log.info("点赞评论:id={}", id);
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Feedback fb = new Feedback(true, "操作成功");
		try {
			gjtMessageCommentDetailService.save(id, content, user, MessagePlatformEnum.PC.getCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			fb = new Feedback(false, "操作失败！错误信息：" + e.getMessage());
		}
		return fb;
	}

}