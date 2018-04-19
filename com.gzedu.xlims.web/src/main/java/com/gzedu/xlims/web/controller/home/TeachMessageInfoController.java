/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.pojo.message.GjtMessageUser;
import com.gzedu.xlims.pojo.status.MessageInfoRoleTypeEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.edumanage.GjtRecResultService;
import com.gzedu.xlims.service.home.message.GjtMessageInfoService;
import com.gzedu.xlims.service.home.message.GjtMessageUserService;
import com.gzedu.xlims.service.organization.GjtSchoolInfoService;
import com.gzedu.xlims.service.organization.GjtTermCourseinfoService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.third.sms.SmsSenderManager;
import com.gzedu.xlims.third.sms.SmsSenderType;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.MessageAddThread;
import com.gzedu.xlims.web.common.Servlets;
import com.gzedu.xlims.web.common.message.vo.MessageInfoVo;
import com.gzedu.xlims.web.common.message.vo.MessageUserRoleMap;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明：课程平台通知公共
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年10月31日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/admin/teachMessageInfo")
public class TeachMessageInfoController {

	private final static Logger log = LoggerFactory.getLogger(TeachMessageInfoController.class);

	@Autowired
	private GjtMessageInfoService gjtMessageInfoService;

	@Autowired
	private CommonMapService commonMapService;

	@Autowired
	GjtSchoolInfoService gjtSchoolInfoService;

	@Autowired
	GjtMessageUserService gjtMessageUserService;

	@Autowired
	private SmsSenderManager smsSenderManager;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtTermCourseinfoService gjtTermCourseinfoService;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private GjtRecResultService gjtRecResultService;

	@RequestMapping(value = "putList", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("EQ_gjtUserAccount.id", user.getId());
		searchParams.put("EQ_gjtOrg.id", user.getGjtOrg().getId());
		searchParams.put("EQ_isDeleted", "N");
		Page<GjtMessageInfo> oldPage = gjtMessageInfoService.queryAll(searchParams, pageRequst);

		List<MessageInfoVo> list = new ArrayList<MessageInfoVo>();

		List<String> messageIds = new ArrayList<String>();
		for (GjtMessageInfo item : oldPage) {
			messageIds.add(item.getMessageId());
		}

		if (messageIds.size() > 0) {
			List<Object[]> putMaps = gjtMessageUserService.queryPutMessageIds(messageIds);// 发送总数
			Map<String, Object> putMap = new HashMap<String, Object>();
			for (Object[] info : putMaps) {
				putMap.put((String) info[1], info[0]);
			}

			List<Object[]> readMaps = gjtMessageUserService.queryReadMessageIds(messageIds);// 已读总数
			Map<String, Object> readMap = new HashMap<String, Object>();
			for (Object[] info : readMaps) {
				readMap.put((String) info[1], info[0]);
			}
			for (GjtMessageInfo gjtMessageInfo : oldPage) {
				MessageInfoVo vo = new MessageInfoVo(gjtMessageInfo, putMap, readMap);
				list.add(vo);
			}
		}
		Page<MessageInfoVo> pageInfo = new PageImpl<MessageInfoVo>(list, pageRequst, oldPage.getTotalElements());

		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("pageInfo", pageInfo);
		return "home/teachMessageInfo/message_put_list";

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

		return "home/teachMessageInfo/message_get_list";

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
		return "home/teachMessageInfo/form";
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
			info.setInfoContent(entity.getInfoContent());
			info.setFileUrl(entity.getFileUrl());
			info.setFileName(entity.getFileName());
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

		Map<String, String> benshenMap = MessageUserRoleMap.benshenMap;// key=roleId;value=InfoType的Code
		Map<String, String> infoTypeMap = new HashMap<String, String>();
		Map<String, String> dates = commonMapService.getDates("InfoType");// 字典表通知类型
		String roleId = user.getPriRoleInfo().getRoleId();

		String infoTypeId = benshenMap.get(roleId);// 本身对应的角色通知类型
		infoTypeMap.put(infoTypeId, dates.get(infoTypeId));

		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("infoTypeId", infoTypeId);
		model.addAttribute("action", "create");
		return "home/teachMessageInfo/form";
	}

	// threadFactory：线程工厂，主要用来创建线程；
	static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

	// ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。

	// workQueue：一个阻塞队列，用来存储等待执行的任务，这个参数的选择也很重要，会对线程池的运行过程产生重大影响，一般来说，这里的阻塞队列有以下几种选择：
	// LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；

	static ExecutorService threadPool = new ThreadPoolExecutor(1, 5, 30, TimeUnit.MILLISECONDS,
			new LinkedBlockingDeque<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Feedback create(@Valid GjtMessageInfo entity, String days, String times, String stick,
			HttpServletRequest request, String isWeiXin) {

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String[] courseIds = request.getParameterValues("courseIds");
		String[] gradeIds = request.getParameterValues("gradeIds");

		Feedback feedback = new Feedback(true, "发送成功");

		if (StringUtils.isBlank(entity.getInfoContent().trim())) {
			feedback = new Feedback(false, "创建失败，内容不能为空");
			return feedback;
		}

		if (StringUtils.isBlank(entity.getInfoTheme().trim())) {
			feedback = new Feedback(false, "创建失败，标题不能为空");
			return feedback;
		}

		if (courseIds == null || courseIds.length < 1 || gradeIds == null || gradeIds.length < 1) {
			feedback = new Feedback(false, "创建失败，接受对象不能为空");
			return feedback;
		}

		String messageId = UUIDUtils.random();
		Map<String, String> getUserRoleMap = MessageUserRoleMap.getUserRoleMap;

		GjtMessageInfo saveEntity = null;
		try {
			entity.setMessageId(messageId);
			entity.setCreatedBy(user.getRealName());
			entity.setCreatedDt(DateUtils.getNowTime());
			entity.setIsDeleted("N");
			entity.setIsEnabled("0");
			entity.setVersion(new BigDecimal(2.5));
			entity.setInfoTool("1");
			entity.setPutUser(user.getId());// 发送者的userId
			entity.setGetUserRole(getUserRoleMap.get("student"));
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

		List<String> allLists = new ArrayList<String>();// 所有用户
		List<Map<String, Object>> lists = Lists.newArrayList();// student推送到手机APP
		// List<String> weixinLists = Lists.newArrayList();// 身份证推送到微信应用平台
		log.info("recIds:{}, user:{}", courseIds, user.getId());

		try {
			// 查询选课的学生
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("courseIds", Arrays.asList(courseIds));
			params.put("gradeIds", Arrays.asList(gradeIds));
			lists = gjtMessageInfoService.queryStudentByGradeIdCourseId(params);

			for (Map<String, Object> map : lists) {
				String id = (String) map.get("ID");
				// String sfzh = (String) map.get("SFZH");
				allLists.add(id);
				// weixinLists.add(sfzh);
			}
			log.info("学生人数：{}", lists.size());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		try {
			log.info("一共发送人数：{}", allLists.size());
			threadSaveMessageUser(allLists, messageId, user);
			// 推送
			final GjtMessageInfo entity2 = entity;
			final List<Map<String, Object>> lists2 = lists;

			// final String isWeiXins = isWeiXin;
			// final List<String> weixinLists2 = weixinLists;
			// final GjtMessageInfo info = saveEntity;

			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						push(entity2, lists2);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
					// try {
					// if ("1".equals(isWeiXins)) {
					// if (EmptyUtils.isEmpty(weixinLists2)) {
					// pushApplication(weixinLists2, info);
					// }
					// }
					// } catch (Exception e) {
					// log.error(e.getMessage(), e);
					// }
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
			// 替换html标签
			content = content.replaceAll("<.?\\w+\\s*[\\w+='?\"?\\w+'?\"?\\s*]*>", "");
		}
		Map<String, Object> params = Maps.newHashMap();
		params.put("id", entity.getMessageId());
		params.put("title", entity.getInfoTheme());// 标题
		params.put("alert", content);// 内容
		params.put("time", DateUtils.getStringToDate(entity.getCreatedDt()));
		params.put("type", entity.getInfoType());// 1-系统消息 2-教务通知 11-班级公告
													// 12-考试通知 13-学习提醒 具体查看数据字典

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

		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String messageId = gjtMessageInfo.getMessageId();
		String userId = user.getId();
		GjtMessageUser messageUser = gjtMessageUserService.queryByUserIdAndMessageId(userId, messageId);
		if (messageUser != null) {
			String isEnabled = messageUser.getIsEnabled();
			if ("0".equals(isEnabled)) {
				gjtMessageInfoService.updateIsRead(messageUser.getMessageId(), user.getId(), "0");
			}
		}

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

		model.addAttribute("readCount", readCount);
		model.addAttribute("putCount", putCount);
		model.addAttribute("gjtMessageInfo", gjtMessageInfo);
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		return "home/teachMessageInfo/view";
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

		model.addAttribute("readCount", readCount);
		model.addAttribute("putCount", putCount);
		model.addAttribute("gjtMessageInfo", gjtMessageInfo);
		model.addAttribute("view", true);
		model.addAttribute("isStick", isStick);
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		return "home/teachMessageInfo/view";
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
			@RequestParam(value = "page.size", defaultValue = "5") int pageSize, HttpServletRequest request, String id,
			Model model, String userName, String isRead, String pycc, String specialtyId, String gradeId) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		String orgId = user.getGjtOrg().getId();
		Map<String, String> gradeMap = commonMapService.getGradeMap(orgId);
		Map<String, String> pyccMap = commonMapService.getPyccMap();
		Map<String, String> specialtyMap = commonMapService.getSpecialtyMap(orgId);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("messageId", id);
		searchParams.put("userName", userName);
		searchParams.put("isRead", isRead);

		log.info("查询信息发送的所有用户参数：{};", searchParams);
		Page<Map> pageInfo = gjtMessageUserService.queryAllByMessageId(searchParams, pageRequst);
		model.addAttribute("messageId", id);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("pyccMap", pyccMap);
		model.addAttribute("specialtyMap", specialtyMap);
		return "home/teachMessageInfo/message_put_detail_list";
	}

	@RequestMapping(value = "addObject", method = RequestMethod.GET)
	public String addObject(HttpServletRequest request, Model model, String gradeId, String courseName) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> gradeMap = commonMapService.getGradeMap(user.getGjtOrg().getId());// 学期
		GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByAccountId(user.getId());
		List<Map<String, Object>> courseList = gjtTermCourseinfoService
				.queryCourseByTeacherId(employeeInfo.getEmployeeId());

		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("courseName", courseName);

		if (StringUtils.isNotBlank(gradeId)) {
			parm.put("gradeId", gradeId);
		} else {
			parm.put("gradeId", gradeMap.keySet());
		}
		List<String> courseIds = new ArrayList<String>();
		for (Map<String, Object> map : courseList) {
			courseIds.add((String) map.get("COURSE_ID"));
		}
		parm.put("courseIds", courseIds);
		log.info("任课平台发送通知公告参数：{}", parm);
		List<Map<String, Object>> list = gjtRecResultService.queryGradeCourseStudentNo(parm);

		model.addAttribute("list", list);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("courseName", courseName);
		model.addAttribute("listSize", list.size());
		model.addAttribute("gradeMap", gradeMap);
		return "home/teachMessageInfo/addObject";
	}

}