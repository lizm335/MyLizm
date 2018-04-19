package com.gzedu.xlims.webservice.controller.gjt.message;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.google.common.collect.Maps;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.json.JsonUtils;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.third.sms.SmsSenderManager;
import com.gzedu.xlims.third.sms.SmsSenderType;
import com.gzedu.xlims.webservice.common.MessageAddThread;
import com.gzedu.xlims.webservice.controller.gjt.message.vo.MessageUserRoleMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.service.home.message.GjtMessageInfoService;
import com.gzedu.xlims.service.home.message.GjtMessageUserService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.webservice.common.Servlets;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;

/**
 * 通知公告接口
 * 
 * @author lyj
 * @time 2017年7月10日 
 */
@Controller
@RequestMapping("/interface/message")
public class GjtMessageInfoController {

	private final static Logger log = LoggerFactory.getLogger(GjtMessageInfoController.class);

	@Autowired
	private GjtMessageInfoService gjtMessageInfoService;
	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	GjtMessageUserService gjtMessageUserService;

	@Resource
	SmsSenderManager smsSenderManager;



	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseResult list(HttpServletRequest request,String orgCode, String infoType, String receiveType,
			@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
		ResponseResult result = null;
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// 通知类型
			if (StringUtils.isNotBlank(infoType)) {
				searchParams.put("EQ_infoType", infoType);
			}
			// 院校
			if (StringUtils.isNotEmpty(orgCode)) {
				GjtOrg org = gjtOrgService.queryByCode(orgCode);
				if(org == null) {
					throw new RuntimeException("没有找到院校编码对应的院校!");
				}
				List<String> orgIdList = gjtOrgService.queryByParentId(org.getId());
				if (Collections3.isEmpty(orgIdList)) {
					throw new RuntimeException("没有找到院校编码对应的院校!");
				} else {
					searchParams.put("IN_gjtOrg.id", orgIdList);
				}
			} else {
				throw new RuntimeException("院校编码不能为空!");
			}
			// 接收身份
			if(StringUtils.isNotEmpty(receiveType)) {
				searchParams.put("LIKE_getUserRole", receiveType);
			} else {
				//searchParams.put("LIKE_getUserRole", "学员");
			}
			// 开始时间-结束时间
			try {
				String startDate = request.getParameter("startDate");
				String endDate = request.getParameter("endDate");
				if(StringUtils.isNotEmpty(startDate)) {
					searchParams.put("GTE_createdDt", startDate+" 00:00:00");
				}
				if(StringUtils.isNotEmpty(endDate)) {
					searchParams.put("LTE_createdDt", endDate+" 23:59:59");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
			searchParams.put("EQ_isDeleted", "N");
			Page<GjtMessageInfo> pages = gjtMessageInfoService.queryAll(searchParams, pageRequst);
			List<MessageInfoVo> list = Lists.newArrayList();
			for (GjtMessageInfo message : pages) {
				MessageInfoVo vo = new MessageInfoVo(message);
				list.add(vo);
			}
			resultMap.put("pageInfo", list);
			resultMap.put("count", pages.getTotalElements());
			result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResponseResult(ResponseStatus.FAIL, e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseResult info(String messageInfoId) {
		ResponseResult result = null;
		try {
			if(StringUtils.isEmpty(messageInfoId)) {
				throw new RuntimeException("用户消息不能为空!");
			}
			GjtMessageInfo gjtMessageInfo = gjtMessageInfoService.queryById(messageInfoId);
			if(gjtMessageInfo == null) {
				throw new RuntimeException("找不到对应的通知详情!");
			}
			Map<String,Object> resultMap = Collections3.entityToMap(new MessageInfoVo(gjtMessageInfo));
			resultMap.put("infoContent", gjtMessageInfo.getInfoContent());
			resultMap.put("fileUrl", gjtMessageInfo.getFileUrl());
			resultMap.put("fileName", gjtMessageInfo.getFileName());
			result = new ResponseResult(ResponseStatus.SUCCESS,resultMap);
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.FAIL, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	@RequestMapping(value = "/create")
	@ResponseBody
	public ResponseResult create(HttpServletRequest request) throws UnsupportedEncodingException{
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResponseResult result = null;
		String collegeCode = ObjectUtils.toString(searchParams.get("collegeCode"));
		String infoTheme = ObjectUtils.toString(searchParams.get("infoTheme"));
		String infoType = ObjectUtils.toString(searchParams.get("infoType"));
		String roleTypes = ObjectUtils.toString(searchParams.get("roleType"));
		if ("1".equals(roleTypes)){
			roleTypes = "student";
		}
		String infoContent = ObjectUtils.toString(searchParams.get("infoContent"));
		String stick = ObjectUtils.toString(searchParams.get("stick"));
		String learncenterCode = ObjectUtils.toString(searchParams.get("learncenterCode"));
		String gradeId = ObjectUtils.toString(searchParams.get("gradeId"));
		String pycc = ObjectUtils.toString(searchParams.get("pycc"));
		String specialtyCode = ObjectUtils.toString(searchParams.get("specialtyCode"));
		String fileName = null;
		if (StringUtils.isNotEmpty(ObjectUtils.toString(searchParams.get("fileName")))){
			fileName = URLDecoder.decode(ObjectUtils.toString(searchParams.get("fileName")),"UTF-8");
		}

		String fileUrl = ObjectUtils.toString(searchParams.get("fileUrl"));
		String days = ObjectUtils.toString(searchParams.get("days"));
		String custom = ObjectUtils.toString(searchParams.get("custom"));
		String xxId = null;

		try {
			if (StringUtils.isNotEmpty(collegeCode)){
				GjtOrg org = gjtOrgService.queryByCode(collegeCode);
				if(org == null) {
					throw new RuntimeException("没有找到院校编码对应的院校!");
				}
				List<String> orgIdList = gjtOrgService.queryByParentId(org.getId());
				if (Collections3.isEmpty(orgIdList)) {
					throw new RuntimeException("没有找到院校编码对应的院校!");
				} else {
					xxId = ObjectUtils.toString(org.getId());
				}
			}else {
				throw new RuntimeException("院校编码不能为空!");
			}
			if (StringUtils.isBlank(infoTheme)){
				throw new RuntimeException("标题不能为空!");
			}else {
				infoTheme = URLDecoder.decode(infoTheme,"UTF-8");
			}
			if (StringUtils.isBlank(infoType)){
				throw new RuntimeException("通知类型不能为空!");
			}
			if (StringUtils.isBlank(roleTypes)) {
				throw new RuntimeException("接收身份不能为空!");
			}
			if (StringUtils.isBlank(infoContent)){
				throw new RuntimeException("通知内容不能为空!");
			}else {
				infoContent = URLDecoder.decode(infoContent,"UTF-8");
			}
			if (StringUtils.isBlank(stick)){
				throw new RuntimeException("是否置顶为必输!");
			} else if ("1".equals(stick)){
				if (StringUtils.isBlank(days)){
					throw new RuntimeException("置顶时间为必填!");
				}
			} else if ("0".equals(stick)){
				if (StringUtils.isBlank(custom)){
					throw new RuntimeException("置顶有效时间为必填!");
				}
			}
			String messageId = UUIDUtils.random();
			StringBuffer roleTypeName = new StringBuffer();
			Map<String, String> getUserRoleMap = MessageUserRoleMap.getUserRoleMap;
			roleTypeName.append(getUserRoleMap.get(roleTypes) + ",");
			final GjtMessageInfo entity = new GjtMessageInfo();
			GjtOrg gjtOrg = new GjtOrg();
			GjtMessageInfo saveEntity = null;
			try {
				entity.setMessageId(messageId);
				entity.setInfoTheme(infoTheme);
				entity.setInfoType(infoType);
				entity.setInfoContent(infoContent);
				entity.setFileName(fileName);
				entity.setFileUrl(fileUrl);
				entity.setCreatedBy(xxId);
				entity.setCreatedDt(DateUtils.getNowTime());
				entity.setIsDeleted("N");
				entity.setIsEnabled("0");
				entity.setVersion(new BigDecimal(1));
				entity.setInfoTool("1");
				entity.setGetUserRole(roleTypeName.toString());
				gjtOrg.setId(xxId);
				entity.setGjtOrg(gjtOrg);
				// 是否置顶1是
				if ("1".equals(stick)){
					if ("0".equals(days)){ // 是否自定义时间，0是
						entity.setEffectiveTime(DateUtils.getStrToDate(custom + " 23:59:59"));

					}else {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(DateUtils.getDate());
						calendar.add(Calendar.DAY_OF_YEAR, +Integer.valueOf(days));
						Date date = calendar.getTime();
						entity.setEffectiveTime(date);
					}
				}else {
					entity.setEffectiveTime(DateUtils.getDate());
				}
				saveEntity = gjtMessageInfoService.saveEntity(entity);
			}catch (Exception e){
				log.error(e.getMessage(), e);
			}

			if (saveEntity == null){
				result = new ResponseResult(ResponseStatus.FAIL,"发送失败，发生异常");
				return result;
			}

			String specialtyIds = "";
			String specialtyCodes = "";
			String gradeIds = "";
			String pyccs = "";
			if (StringUtils.isNotBlank(specialtyCode)){
				specialtyCodes = getString(specialtyCode);
			}
			if (StringUtils.isNotBlank(gradeId)){
				gradeIds = getString(gradeId);
			}

			if (StringUtils.isNotBlank(pycc)){
				pyccs = getString(pycc);
			}

			Map spCodeMap = new HashMap();
			spCodeMap.put("specialtyCodes",specialtyCodes);
			spCodeMap.put("xxId",xxId);
			List codeList = gjtMessageInfoService.getSpecialtyByCode(spCodeMap);
			StringBuffer spBuffer = new StringBuffer();
			if (EmptyUtils.isNotEmpty(codeList)){
				for (int i=0;i<codeList.size();i++){
					Map tMap = (Map) codeList.get(i);
					//spBuffer.append(ObjectUtils.toString(tMap.get("SPECIALTY_ID")));
					spBuffer.append("'" + ObjectUtils.toString(tMap.get("SPECIALTY_ID")) + "'");
					if (i < (codeList.size() - 1)) {
						spBuffer.append(",");
					}
				}
			}
			specialtyIds = spBuffer.toString();

			String xxzxIds = "";
			StringBuffer learnBuffer = new StringBuffer();
			if (StringUtils.isNotBlank(learncenterCode)){
				Map learnMap = new HashMap();
				learnMap.put("learncenterCodes",getString(learncenterCode));
				List learnList = gjtMessageInfoService.getXXIdByCode(learnMap);
				if (EmptyUtils.isNotEmpty(learnList)){
					for (int i = 0; i < learnList.size(); i++) {
						Map tMap = (Map) learnList.get(i);
						learnBuffer.append("'" + ObjectUtils.toString(tMap.get("ID")) + "'");
						if (i < (learnList.size() - 1)) {
							learnBuffer.append(",");
						}
					}
				}
			}
			xxzxIds = learnBuffer.toString();
			List<String> allLists = new ArrayList<String>();
			List<Object> lists = Lists.newArrayList();
			log.info("specialtyIds:{},gradeIds:{}, pyccs:{}, orgId:{},user:{}", specialtyIds, gradeIds, pyccs, xxId);
			try {
				if (roleTypes.equals("student")){
					long startTime = System.currentTimeMillis();
					Map studentMap = new HashMap();
					studentMap.put("gradeIds",gradeIds);
					studentMap.put("pyccs",pyccs);
					studentMap.put("specialtyIds",specialtyIds);
					studentMap.put("xxId",xxId);
					studentMap.put("xxzxIds",xxzxIds);
					lists = gjtMessageInfoService.queryStudentInfo(studentMap);
					log.info("查询时间：" + (System.currentTimeMillis() - startTime));
					for(Object object : lists){
						Map<String, Object> map = (Map<String, Object>) object;
						String id = (String) map.get("ID");
						allLists.add(id);
					}
					log.info("学生人数：{}", lists.size());
				}
			}catch (Exception e){
				log.error(e.getMessage(), e);
			}

			try {
				log.info("一共发送人数：{}", allLists.size());
				threadSaveMessageUser(allLists,messageId,xxId);
				final GjtMessageInfo entity2 = entity;
				final List<Object> lists2 = lists;

				cachedThreadPool.execute(new Runnable() {
					@Override
					public void run() {
						try {
							push(entity2,lists2);
						}catch (Exception e){
							log.error(e.getMessage(), e);
						}
					}
				});

			}catch (Exception e){
				log.error(e.getMessage(), e);
			}

			resultMap.put("messageId",messageId);
			result = new ResponseResult(ResponseStatus.SUCCESS, resultMap);

		}catch (Exception e){
			result = new ResponseResult(ResponseStatus.FAIL,e.getMessage());
			e.printStackTrace();
		}

		return result;
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


	private void threadSaveMessageUser(List<String> lists, String messageId,String xxId){
		int size = lists.size();
		if (lists != null && size > 0) {
			if (size > 500){ // 如果size大于500，就开多线程执行，把list拆成5份
				int nThreads = 5;// 开启多少个线程 list的size必需大于线程数
				ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
				for (int i = 0; i < nThreads; i++) {
					int start = size / nThreads * i;
					int end = size / nThreads * (i + 1);
					if (!(i < nThreads - 1)) {
						end = size;// 最后一份有余数。直接取size最后一个下标
					}
					final List<String> subList = lists.subList(start, end);
					log.info("当前循环次数为第{}次；当前开始下标为开始：{}，下标结束：{}", i, start, end);
					MessageAddThread mat = new MessageAddThread(gjtMessageUserService, messageId, xxId, subList);
					fixedThreadPool.execute(mat);
				}
			}else {
				MessageAddThread mat = new MessageAddThread(gjtMessageUserService, messageId,xxId,lists);
				cachedThreadPool.execute(mat);
			}
		}
	}

	private void push(GjtMessageInfo entity, List<Object> lists){
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
		params.put("type", entity.getInfoType());// 1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒 具体查看数据字典
		// 按学员推送,一次推送最多 1000 个。
		Map<SmsSenderType,Set<String>> atidMap = Maps.newHashMap();
		for (Object object : lists){
			Map<String, String> map = (Map<String, String>) object;
			String atid = map.get("ATID");
			String xxzxId = map.get("XXZX_ID");

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
		for (Map.Entry<SmsSenderType, Set<String>> entry : atidMap.entrySet()) {
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



	}



}
