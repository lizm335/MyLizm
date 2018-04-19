/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.controller.classManager.message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.third.sms.SmsSenderManager;
import com.gzedu.xlims.third.sms.SmsSenderType;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtMessageUser;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.BzrCommonMapService;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageService;
import com.ouchgzee.headTeacher.service.message.BzrGjtMessageUserService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.service.student.BzrGjtStudentService;
import com.ouchgzee.headTeacher.web.common.Feedback;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;

import net.sf.json.JSONObject;

/**
 * 通知公告控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 */
@Controller
@RequestMapping("/home/class/message")
public class MessageController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(MessageController.class);

	@Autowired
	BzrGjtMessageService gjtMessageService;

	@Autowired
	BzrCommonMapService commonMapService;

	@Autowired
	BzrGjtMessageUserService gjtMessageUserService;

	@Autowired
	BzrGjtClassService gjtClassService;

	@Autowired
	private SmsSenderManager smsSenderManager;
	@Autowired
	private BzrGjtStudentService gjtStudentService;

	/**
	 * 我接收的教务通知
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		searchParams.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());
		searchParams2.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());

		String infoTheme = (String) searchParams.get("LIKE_infoTheme");
		if (StringUtils.isNotBlank(infoTheme)) {
			searchParams2.put("LIKE_gjtMessageInfo.infoTheme", infoTheme);
		}
		String createdDtBegin = (String) searchParams.get("GTE_createdDt");
		if (StringUtils.isNotBlank(createdDtBegin)) {
			searchParams2.put("GTE_gjtMessageInfo.createdDt", createdDtBegin);
		}
		String createdDtEnd = (String) searchParams.get("LTE_createdDt");
		if (StringUtils.isNotBlank(createdDtEnd)) {
			searchParams2.put("LTE_gjtMessageInfo.createdDt", createdDtEnd);
		}
		searchParams.put("NE_infoType", 21);

		log.info("我发布的通知信息集合参数：{}", searchParams);
		long pmCount = gjtMessageService.findAllCount(searchParams);
		log.info("我接收的通知信息集合参数：{}", searchParams2);
		Page<BzrGjtMessageUser> infos = gjtMessageUserService.queryAll(searchParams2, pageRequst);

		// 去除HTML标签
		Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
		for (BzrGjtMessageUser info : infos.getContent()) {
			BzrGjtMessageInfo gjtMessageInfo = info.getGjtMessageInfo();
			if (StringUtils.isNotBlank(gjtMessageInfo.getInfoContent())) {
				Matcher m = p.matcher(gjtMessageInfo.getInfoContent());
				String res = m.replaceAll("");
				gjtMessageInfo.setInfoContent(res.length() > 100 ? res.substring(0, 100) + "..." : res);
			}
			boolean bool = false;
			if (gjtMessageInfo.getEffectiveTime() != null) {
				bool = gjtMessageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
			}
			gjtMessageInfo.setIsStick(bool);
		}

		model.addAttribute("infos", infos);
		model.addAttribute("tmCount", infos.getTotalElements());
		model.addAttribute("pmCount", pmCount);
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/message/list";
	}

	/**
	 * 我发布的班级通知
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "publishList", method = RequestMethod.GET)
	public String publishList(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize, Model model, ServletRequest request,
			HttpSession session) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		Map<String, Object> searchParams2 = new HashMap<String, Object>();
		BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
		searchParams.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());
		searchParams2.put("EQ_gjtUserAccount.id", employeeInfo.getGjtUserAccount().getId());

		String infoTheme = (String) searchParams.get("LIKE_infoTheme");
		if (StringUtils.isNotBlank(infoTheme)) {
			searchParams2.put("LIKE_gjtMessageInfo.infoTheme", infoTheme);
		}
		String createdDtBegin = (String) searchParams.get("GTE_createdDt");
		if (StringUtils.isNotBlank(createdDtBegin)) {
			searchParams2.put("GTE_gjtMessageInfo.createdDt", createdDtBegin);
		}
		String createdDtEnd = (String) searchParams.get("LTE_createdDt");
		if (StringUtils.isNotBlank(createdDtEnd)) {
			searchParams2.put("LTE_gjtMessageInfo.createdDt", createdDtEnd);
		}
		String infoType = (String) searchParams.get("EQ_infoType");
		if (StringUtils.isNotBlank(infoType)) {
			searchParams2.put("EQ_gjtMessageInfo.infoType", infoType);
		}
		searchParams.put("NE_infoType", 21);
		searchParams2.put("NE_gjtMessageInfo.infoType", 21);
		log.info("我发布的通知信息集合参数：{}", searchParams);
		Page<BzrGjtMessageInfo> infos = gjtMessageService.findAll(searchParams, pageRequst);
		log.info("我接收的通知信息集合参数：{}", searchParams2);
		long tmCount = gjtMessageUserService.queryAllCount(searchParams2);

		// 去除HTML标签
		Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
		for (BzrGjtMessageInfo info : infos.getContent()) {
			if (StringUtils.isNotBlank(info.getInfoContent())) {
				Matcher m = p.matcher(info.getInfoContent());
				String res = m.replaceAll("");
				info.setInfoContent(res.length() > 100 ? res.substring(0, 100) + "..." : res);
			}
			boolean bool = false;
			if (info.getEffectiveTime() != null) {
				bool = info.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
			}
			info.setIsStick(bool);
		}

		model.addAttribute("infos", infos);
		BzrGjtMessageInfo countMsg = new BzrGjtMessageInfo();
		countMsg.setXxId(employeeInfo.getXxId());
		model.addAttribute("tmCount", tmCount);
		model.addAttribute("pmCount", infos.getTotalElements());
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		// 将搜索条件编码成字符串，用于排序，分页的URL
		model.addAttribute("searchParams", Servlets.encodeParameterStringWithPrefix(searchParams, "search_"));
		return "new/class/message/publish_list";
	}

	/**
	 * 浏览通知
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, ServletRequest request, HttpSession session) {
		BzrGjtMessageInfo messageInfo = gjtMessageService.queryById(id);
		String source = request.getParameter("source");
		if ("1".equals(source)) {
			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			gjtMessageUserService.updateRead(id, employeeInfo.getGjtUserAccount().getId());
		}

		List<Object[]> putMaps = gjtMessageUserService.queryPutMessageIds(id);// 发送总数
		Object putCount = 0;
		for (Object[] info : putMaps) {
			putCount = info[0];
		}

		List<Object[]> readMaps = gjtMessageUserService.queryReadMessageIds(id);// 已读总数
		Object readCount = 0;
		for (Object[] info : readMaps) {
			readCount = info[0];
		}

		boolean bool = false;
		if (messageInfo.getEffectiveTime() != null) {
			bool = messageInfo.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}
		messageInfo.setIsStick(bool);

		model.addAttribute("readCount", readCount);
		model.addAttribute("putCount", putCount);

		model.addAttribute("info", messageInfo);
		model.addAttribute("action", "view");
		model.addAttribute("infoTypeMap", commonMapService.getDates("InfoType"));
		return "new/class/message/view";
	}

	/**
	 * 加载新增班级通知
	 * 
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, ServletRequest request, HttpSession session) {
		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		model.addAttribute("info", new BzrGjtMessageInfo());
		model.addAttribute("infoTypeMap", infoTypeMap);
		model.addAttribute("action", "create");
		return "new/class/message/form";
	}

	/**
	 * 新增班级通知
	 * 
	 * @param messageInfo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Feedback create(@Valid BzrGjtMessageInfo messageInfo, String times, String stick, String days,
			String isWeiXin, HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		BzrGjtUserAccount gjtUserAccount = super.getUser(request.getSession()).getGjtUserAccount();
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		String messageId = UUIDUtils.random();
		String[] classIds = request.getParameterValues("classIds");// 指定班级发送
		Set<String> classIdSet = new HashSet<String>();

		if (classIds != null && classIds.length > 0) {// 去重复
			for (String classId : classIds) {
				classIdSet.add(classId);
			}
		}

		try {// 通知消息已经明细
			messageInfo.setMessageId(messageId);
			messageInfo.setCreatedBy(gjtUserAccount.getRealName());
			messageInfo.setCreatedDt(DateUtils.getNowTime());
			messageInfo.setIsDeleted("N");
			messageInfo.setIsEnabled("0");
			messageInfo.setVersion(new BigDecimal(2.5));
			messageInfo.setInfoTool("1");
			messageInfo.setPutUser(gjtUserAccount.getId());
			messageInfo.setXxId(employeeInfo.getXxzxId());
			messageInfo.setGetUserRole("学员");

			StringBuffer sb = new StringBuffer();
			for (String classId : classIdSet) {
				sb.append(classId + ",");
			}
			messageInfo.setClassId(sb.toString());

			// 是否置顶1是
			if ("1".equals(stick)) {
				if ("0".equals(days)) {// 是否自定义时间，0是
					if (StringUtils.isNotBlank(times)) {
						messageInfo.setEffectiveTime(DateUtils.getStrToDate(times + " 23:59:59"));
					}
				} else {
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(DateUtils.getDate());
					calendar.add(Calendar.DAY_OF_YEAR, +Integer.valueOf(days));
					Date date = calendar.getTime();
					messageInfo.setEffectiveTime(date);
				}
			} else {// 不置顶就取当前时间，好用于排序
				messageInfo.setEffectiveTime(DateUtils.getDate());
			}
			boolean insert = gjtMessageService.insert(messageInfo);

			if (!insert) {
				feedback = new Feedback(false, "创建失败");
			} else {
				Set<String> atidSet = Sets.newHashSet();
				Set<String> userLists = new HashSet<String>();
				Set<String> sfzhList = new HashSet<String>();
				// 指定学生
				String[] userIds = request.getParameterValues("userIds");
				if (userIds != null && userIds.length > 0) {
					for (String id : userIds) {
						userLists.add(id);
					}
					// ATID,身份证
					List<Object[]> atidList = gjtStudentService.queryAtidByIds(userIds);
					for (Object[] objects : atidList) {
						atidSet.add((String) objects[0]);
						sfzhList.add((String) objects[1]);
					}

				}
				for (String classId : classIdSet) {
					List<Map<String, Object>> students = gjtClassService.queryStudentByClassId(classId, null);
					for (Map<String, Object> map : students) {
						userLists.add((String) map.get("ID"));
						atidSet.add((String) map.get("ATID"));
						sfzhList.add((String) map.get("SFZH"));
					}
				}

				gjtMessageUserService.save(userLists, gjtUserAccount.getLoginAccount(), messageId);

				final BzrGjtMessageInfo ms = messageInfo;
				final BzrGjtEmployeeInfo em = employeeInfo;
				final Set<String> atidSets = atidSet;
				final String isWeiXins = isWeiXin;
				final Set<String> weixinLists = sfzhList;

				ExecutorService executor = Executors.newCachedThreadPool();
				executor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							push(ms, em.getXxzxId(), atidSets);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}

						try {
							if ("1".equals(isWeiXins)) {
								List<String> weixinLists2 = new ArrayList<String>();
								weixinLists2.addAll(weixinLists);
								pushApplication(weixinLists2, ms);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				});
			}
		} catch (Exception e) {
			feedback = new Feedback(false, "发生异常");
			log.error(e.getMessage(), e);
		}
		return feedback;
	}

	private void pushApplication(List<String> weixinLists2, BzrGjtMessageInfo info) {

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
			Pattern p = Pattern.compile("(<[^>]*>)", Pattern.CASE_INSENSITIVE);
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

	private void push(BzrGjtMessageInfo entity, String xxzxId, Set<String> atidSet) {
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
		for (String atid : atidSet) {
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
			SmsSenderType senderType = SmsSenderType.getItemByOrgId(xxzxId);
			if (senderType != null) {
				if (atidMap.get(senderType) == null) {
					atidMap.put(senderType, new HashSet<String>());
				}
				atidMap.get(senderType).add(atid);
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
				log.info("开始推送：{}", type.getName());
				smsSenderManager.smsSend(type, params);
				log.info("%s开始推送：{}", type.getName());
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

	/**
	 * 加载班级通知信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model) {
		BzrGjtMessageInfo info = gjtMessageService.queryById(id);
		boolean isStick = false;
		if (info.getEffectiveTime() != null) {
			isStick = info.getEffectiveTime().getTime() > DateUtils.getDate().getTime();
		}
		model.addAttribute("info", info);
		model.addAttribute("isStick", isStick);
		model.addAttribute("action", "update");
		return "new/class/message/form";
	}

	/**
	 * 修改班级通知
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Feedback update(@Valid @ModelAttribute("info") BzrGjtMessageInfo info, HttpSession session, String stick,
			String days, String times) {
		Feedback feedback = new Feedback(true, "更新成功");
		try {

			BzrGjtMessageInfo modifyInfo = gjtMessageService.queryById(info.getMessageId());
			modifyInfo.setInfoTheme(info.getInfoTheme());
			modifyInfo.setInfoType(info.getInfoType());
			modifyInfo.setInfoContent(info.getInfoContent());
			modifyInfo.setFileUrl(info.getFileUrl());
			modifyInfo.setFileName(info.getFileName());
			// 是否置顶1是
			if ("1".equals(stick)) {
				if (StringUtils.isNotBlank(days)) {// 编辑信息，如果没有重新设置时间，则取旧的指定时间
					if ("0".equals(days)) {// 是否自定义时间，0是
						if (StringUtils.isNotBlank(times)) {
							modifyInfo.setEffectiveTime(DateUtils.getStrToDate(times + " 23:59:59"));
						}
					} else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(DateUtils.getDate());
						calendar.add(Calendar.DAY_OF_YEAR, +Integer.valueOf(days));
						Date date = calendar.getTime();
						modifyInfo.setEffectiveTime(date);
					}
				}
			} else {
				modifyInfo.setEffectiveTime(DateUtils.getDate());
			}

			BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
			modifyInfo.setUpdatedBy(employeeInfo.getGjtUserAccount().getId());
			boolean update = gjtMessageService.update(modifyInfo);
			if (!update) {
				feedback = new Feedback(false, "更新失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败" + e.getMessage());
		}
		return feedback;
	}

	/**
	 * 删除班级通知
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "delete")
	public @ResponseBody Feedback delete(String ids, ServletResponse response, HttpSession session) {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			try {
				String[] selectedIds = ids.split(",");
				BzrGjtEmployeeInfo employeeInfo = super.getUser(session);
				boolean bool = gjtMessageService.delete(selectedIds, employeeInfo.getGjtUserAccount().getId());
				if (!bool) {
					fb = new Feedback(false, "删除失败");
				}
			} catch (Exception e) {
				fb = new Feedback(false, "删除失败" + e.getMessage());
				log.error(e.getMessage(), e);
			}
		}
		return fb;
	}

	@RequestMapping(value = "addObject", method = RequestMethod.GET)
	public String addObject(HttpServletRequest request, Model model, String searchClassId, String searchName) {
		String currentClassId = "";
		if (StringUtils.isBlank(searchClassId)) {
			currentClassId = super.getCurrentClassId(request.getSession());
		} else {
			currentClassId = searchClassId;
		}
		List<Map<String, Object>> studetnList = gjtClassService.queryStudentByClassId(currentClassId, searchName);// 当前班级学员
		BzrGjtEmployeeInfo employeeInfo = super.getUser(request.getSession());
		List<Map<String, Object>> classList = gjtClassService.queryClassInfoByTeachId(employeeInfo.getEmployeeId());// 班主任对应的所有教学班级
		model.addAttribute("classList", classList);
		model.addAttribute("studetnList", studetnList);
		model.addAttribute("studentSize", studetnList.size());
		model.addAttribute("classSize", classList.size());
		model.addAttribute("searchClassId", searchClassId);
		model.addAttribute("searchName", searchName);

		return "new/class/message/addObject";
	}

	// 查询信息发送的所有用户
	@RequestMapping(value = "queryPutListById", method = RequestMethod.GET)
	public String queryPutListById(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "7") int pageSize, HttpServletRequest request, String id,
			Model model, String userName, String isRead) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");
		searchParams.put("messageId", id);
		searchParams.put("userName", userName);
		searchParams.put("isRead", isRead);

		log.info("查询信息发送的所有用户参数：{};", searchParams);
		Page<Map> pageInfo = gjtMessageUserService.queryAllByMessageId(searchParams, pageRequst);
		model.addAttribute("messageId", id);
		model.addAttribute("pageInfo", pageInfo);
		return "new/class/message/message_put_detail_list";
	}

}
