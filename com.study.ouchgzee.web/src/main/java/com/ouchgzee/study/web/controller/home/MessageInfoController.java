/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.message.GjtMessageClassify;
import com.gzedu.xlims.pojo.message.GjtMessageComment;
import com.gzedu.xlims.pojo.message.GjtMessageCommentDetail;
import com.gzedu.xlims.pojo.message.GjtMessageCommentLike;
import com.gzedu.xlims.pojo.message.GjtMessageFeedback;
import com.gzedu.xlims.pojo.message.GjtMessageInfo;
import com.gzedu.xlims.pojo.message.GjtMessageUser;
import com.gzedu.xlims.pojo.status.MessagePlatformEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.home.message.GjtMessageClassifyService;
import com.gzedu.xlims.service.home.message.GjtMessageCommentDetailService;
import com.gzedu.xlims.service.home.message.GjtMessageCommentLikeService;
import com.gzedu.xlims.service.home.message.GjtMessageCommentService;
import com.gzedu.xlims.service.home.message.GjtMessageFeedbackService;
import com.gzedu.xlims.service.home.message.GjtMessageInfoService;
import com.gzedu.xlims.service.home.message.GjtMessageUserService;
import com.ouchgzee.study.web.common.Servlets;
import com.ouchgzee.study.web.vo.MessageInfoVo;

/**
 * 功能说明：个人中心 首页-通知公告
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月24日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/pcenter/home/message")
public class MessageInfoController {

	private final static Logger log = LoggerFactory.getLogger(MessageInfoController.class);

	@Autowired
	private GjtMessageUserService gjtMessageUserService;
	@Autowired
	private CommonMapService commonMapService;
	@Autowired
	private GjtMessageInfoService gjtMessageInfoService;
	@Autowired
	private GjtMessageFeedbackService gjtMessageFeedbackService;
	@Autowired
	private GjtMessageCommentService gjtMessageCommentService;
	@Autowired
	private GjtMessageCommentDetailService gjtMessageCommentDetailService;
	@Autowired
	private GjtMessageCommentLikeService gjtMessageCommentLikeService;
	@Autowired
	private GjtMessageClassifyService gjtMessageClassifyService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Model model,
			HttpServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String isEnabled = request.getParameter("isEnabled");
		String infoType = request.getParameter("infoType");
		String classifyType = request.getParameter("classifyType");
		String infoTheme = request.getParameter("infoTheme");
		searchParams.put("EQ_userId", user.getId());
		List<GjtMessageClassify> classifyList = null;
		if (StringUtils.isNotBlank(classifyType)) {
			searchParams.put("EQ_gjtMessageInfo.typeClassify", classifyType);
		}
		if (StringUtils.isNotBlank(infoType)) {
			searchParams.put("EQ_gjtMessageInfo.infoType", infoType);
			classifyList = gjtMessageClassifyService.findList(infoType);
		}
		if (StringUtils.isNotBlank(infoTheme)) {
			searchParams.put("LIKE_gjtMessageInfo.infoTheme", infoTheme);
		}
		if (StringUtils.isNotBlank(isEnabled)) {
			searchParams.put("EQ_isEnabled", isEnabled);
		}
		// else {
		// searchParams.put("EQ_isEnabled", "0");// 0信箱未读,1已读
		// }
		searchParams.put("EQ_isDeleted", "N");
		searchParams.put("EQ_gjtMessageInfo.isDeleted", "N");
		log.info("通知公告查询参数：{}", searchParams);
		Page<GjtMessageUser> infos = gjtMessageUserService.queryAll(searchParams, pageRequst);
		List<MessageInfoVo> list = new ArrayList<MessageInfoVo>();
		List<String> messageIds = new ArrayList<String>();
		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		for (GjtMessageUser item : infos) {
			messageIds.add(item.getMessageId());
		}
		if (messageIds.size() > 0) {
			List<Object[]> readMaps = gjtMessageUserService.findReadMessageIds(messageIds);// 已读总数
			Map<String, Object> readMap = new HashMap<String, Object>();
			for (Object[] info : readMaps) {
				readMap.put((String) info[1], info[0]);
			}

			List<Object[]> likeLists = gjtMessageUserService.queryLikeMessageIds(messageIds);// 点赞数
			Map<String, Object> likeMap = new HashMap<String, Object>();
			for (Object[] info : likeLists) {
				likeMap.put((String) info[1], info[0]);
			}

			List<Object[]> commLists = gjtMessageUserService.queryCommMessageIds(messageIds);// 评论数
			Map<String, Object> commMap = new HashMap<String, Object>();
			for (Object[] info : commLists) {
				commMap.put((String) info[1], info[0]);
			}

			for (GjtMessageUser gjtMessageUser : infos) {
				gjtMessageUser.getGjtMessageInfo()
						.setInfoType(infoTypeMap.get(gjtMessageUser.getGjtMessageInfo().getInfoType()));
				MessageInfoVo vo = new MessageInfoVo(gjtMessageUser, readMap, likeMap, commMap);
				list.add(vo);
			}
		}
		Page<MessageInfoVo> page = new PageImpl<MessageInfoVo>(list, pageRequst, infos.getTotalElements());

		searchParams.put("EQ_isEnabled", "0");
		long unreadCount = gjtMessageUserService.queryAllCount(searchParams);

		searchParams.put("EQ_isEnabled", "1");
		long readCount = gjtMessageUserService.queryAllCount(searchParams);

		resultMap.put("pageInfo", page);
		resultMap.put("unreadCount", unreadCount);
		resultMap.put("readCount", readCount);
		resultMap.put("infoTypeMap", infoTypeMap);
		resultMap.put("classifyList", classifyList);
		return resultMap;
	}

	@RequestMapping(value = "findInfoType", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findInfoType(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map<String, Object>> findTypeCount = gjtMessageUserService.findTypeCount(user.getId());
		List<Map<String, Object>> infoTypeList = commonMapService.getDateList("InfoType");
		Integer totalTypeCount = 0;
		for (Map<String, Object> map : infoTypeList) {
			String code = (String) map.get("code");
			Integer typeCount = 0;
			for (Map<String, Object> mapCount : findTypeCount) {
				String code2 = (String) mapCount.get("INFO_TYPE");
				if (code.equals(code2)) {
					typeCount = ((BigDecimal) mapCount.get("NUMS")).intValue();
					continue;
				}
			}
			map.put("typeCount", typeCount);
			// totalTypeCount += typeCount;
		}

		for (Map<String, Object> mapCount : findTypeCount) {
			totalTypeCount += ((BigDecimal) mapCount.get("NUMS")).intValue();
		}

		resultMap.put("infoTypeList", infoTypeList);
		resultMap.put("totalTypeCount", totalTypeCount);
		return resultMap;
	}

	@RequestMapping(value = "firstList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> firstList(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "1") int pageSize, Model model,
			HttpServletRequest request) {
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize);
		Map<String, Object> searchParams = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		String infoTypes = request.getParameter("infoTypes");
		searchParams.put("EQ_userId", user.getId());

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		if (StringUtils.isNotBlank(infoTypes)) {
			String[] infoTypeList = infoTypes.split(",");
			for (String infoType : infoTypeList) {
				Map<String, Object> resMap = new HashMap<String, Object>();
				Page<GjtMessageUser> infos = null;
				searchParams.put("EQ_gjtMessageInfo.infoType", infoType);
				searchParams.put("EQ_gjtMessageInfo.isDeleted", "N");
				searchParams.put("EQ_isEnabled", "0");
				searchParams.put("EQ_isDeleted", "N");
				log.info("根据通知类型查询通知公告参数：{}", searchParams);
				infos = gjtMessageUserService.queryAll(searchParams, pageRequst);
				if (infos.getTotalElements() == 0) {// 没有找到未读的，就取最近一条已读的
					searchParams.put("EQ_isEnabled", "1");
					infos = gjtMessageUserService.queryAll(searchParams, pageRequst);
				}
				if (infos.getTotalElements() > 0) {
					for (GjtMessageUser gjtMessageUser : infos) {
						gjtMessageUser.getGjtMessageInfo().setInfoType(infoTypeMap.get(infoType));
						MessageInfoVo vo = new MessageInfoVo(gjtMessageUser);
						resMap.put(infoType, vo);
						list.add(resMap);
					}
				} else {// 未读和已读的都不存在，返回类型和空
					resMap.put(infoType, null);
					list.add(resMap);
				}
			}
		}
		resultMap.put("list", list);
		return resultMap;
	}

	@RequestMapping(value = "view", method = RequestMethod.GET)
	@ResponseBody
	public MessageInfoVo viewForm(String id, HttpServletRequest request, String platform, String isAll) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		if (org.apache.commons.lang3.StringUtils.isBlank(id)) {
			return null;
//			throw new CommonException(MessageCode.BAD_REQUEST, "缺少参数");
		}
		GjtMessageInfo gjtMessageInfo = gjtMessageInfoService.queryById(id);
		GjtMessageUser messageUser = gjtMessageUserService.queryByUserIdAndMessageId(user.getId(), id);

		Map<String, String> infoTypeMap = commonMapService.getDates("InfoType");
		List<String> messageIds = new ArrayList<String>();
		messageIds.add(gjtMessageInfo.getMessageId());

		long num = 0;
		if ("1".equals(gjtMessageInfo.getIsLike())) {// 是否开启点赞
			List<Object[]> likeLists = gjtMessageUserService.queryLikeMessageIds(messageIds);// 点赞数
			if (EmptyUtils.isNotEmpty(likeLists)) {
				Object[] objects = likeLists.get(0);
				num = ((BigDecimal) objects[0]).longValue();
			}
		}
		long feebackTotal = 0;
		List<GjtMessageFeedback> gjtMessageFeedbackList = null;
		List<Map<String, Object>> commentList = null;

		if (StringUtils.isBlank(isAll) || "1".equals(isAll)) {
			if ("1".equals(gjtMessageInfo.getIsFeedback())) {// 是否开启反馈
				gjtMessageFeedbackList = gjtMessageFeedbackService.findByMessageId(id);// 反馈列表
				List<Map<String, Object>> ticktingList = gjtMessageUserService.queryTicktingList(id);// 反馈列表统计人数
				for (GjtMessageFeedback gjtMessageFeedback : gjtMessageFeedbackList) {
					for (Map<String, Object> map : ticktingList) {
						BigDecimal selectNum = (BigDecimal) map.get("NUM");
						String feedbackType = (String) map.get("FEEDBACK_TYPE");
						if (gjtMessageFeedback.getCode().equals(feedbackType)) {
							gjtMessageFeedback.setNum(selectNum.longValue());
						}
					}
					if (gjtMessageFeedback.getCode().equals(messageUser.getFeedbackType())) {// 是否选中
						gjtMessageFeedback.setCheck(true);
					} else {
						gjtMessageFeedback.setCheck(false);
					}
				}

				for (Map<String, Object> map : ticktingList) {
					BigDecimal selectNum = (BigDecimal) map.get("NUM");
					feebackTotal += selectNum.longValue();
				}

			}

			if ("1".equals(gjtMessageInfo.getIsComment())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("userId", user.getId());
				map.put("messageId", id);
				commentList = gjtMessageCommentService.queryAll(map);
				for (Map<String, Object> map2 : commentList) {
					String comid = (String) map2.get("id");
					String platform2 = (String) map2.get("platform");
					map2.put("platform", MessagePlatformEnum.getName(Integer.valueOf(platform2)));

					String imgUrls2 = (String) map2.get("imgUrls");
					if (StringUtils.isNotBlank(imgUrls2)) {
						map2.put("imgUrls", Arrays.asList(imgUrls2.split(",")));
					} else {
						map2.put("imgUrls", new ArrayList<String>());
					}

					List<Map<String, Object>> queryByCommId = gjtMessageCommentDetailService.queryByCommId(comid,
							user.getId());
					for (Map<String, Object> map3 : queryByCommId) {
						String platform3 = (String) map3.get("platform");
						map3.put("platform", MessagePlatformEnum.getName(Integer.valueOf(platform3)));
					}
					map2.put("commentDetailList", queryByCommId);
				}
			}
		}
		MessageInfoVo vo = new MessageInfoVo(gjtMessageInfo);
		vo.setInfoType(infoTypeMap.get(gjtMessageInfo.getInfoType()));
		vo.setFeedbackList(gjtMessageFeedbackList);
		vo.setLikeCount(num);
		vo.setCommentList(commentList);
		vo.setIfLikeCheck("1".equals(messageUser.getIsLike()) ? true : false);
		vo.setFeebackTotal(feebackTotal);
		if ("1".equals(gjtMessageInfo.getIsFeedback())) {
			if (StringUtils.isNotBlank(messageUser.getFeedbackType())) {
				vo.setIfFeedbackCheck(true);
			} else {
				vo.setIfFeedbackCheck(false);
			}
		}
		vo.setMessageUserId(messageUser.getId());
		if (messageUser != null) {
			String isEnabled = messageUser.getIsEnabled();
			if ("0".equals(isEnabled)) {
				try {
					if (StringUtils.isBlank(platform)) {
						platform = "1";// 如果没传参数，默认先选APP吧，PC有传的
					}
					gjtMessageInfoService.updateIsRead(id, user.getId(), platform);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return vo;
	}

	// 废弃
	@RequestMapping(value = "updateRead", method = RequestMethod.GET)
	@ResponseBody
	public void updateRead(HttpServletRequest request, String messageUserId, String platform) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtMessageUser messageUser = gjtMessageUserService.queryById(messageUserId);
		if (messageUser != null) {
			String isEnabled = messageUser.getIsEnabled();
			if ("0".equals(isEnabled)) {
				gjtMessageInfoService.updateIsRead(messageUser.getMessageId(), user.getId(), platform);
			}
		}
	}

	@RequestMapping(value = "likeMessage", method = RequestMethod.POST)
	@ResponseBody
	public void likeMessage(HttpServletRequest request, String messageUserId) throws CommonException {
		if (StringUtils.isBlank(messageUserId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "messageUserId参数为空！");
		}
		GjtMessageUser messageUser = gjtMessageUserService.queryById(messageUserId);

		if (messageUser == null) {
			throw new CommonException(MessageCode.BAD_REQUEST, "根据messageUserId参数查询不到信息！");
		}
		GjtMessageInfo gjtMessageInfo = messageUser.getGjtMessageInfo();
		if ("0".equals(gjtMessageInfo.getIsLike())) {
			throw new CommonException(MessageCode.BIZ_ERROR, "没有开启点赞功能！");
		}
		if ("1".equals(messageUser.getIsLike())) {
			throw new CommonException(MessageCode.BIZ_ERROR, "已经点赞过了！");
		}

		messageUser.setIsLike("1");
		messageUser.setUpdatedDt(DateUtils.getNowTime());
		gjtMessageUserService.update(messageUser);
	}

	@RequestMapping(value = "likeComment", method = RequestMethod.POST)
	@ResponseBody
	public void likeComment(HttpServletRequest request, String commentId) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isBlank(commentId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "commentId参数为空！");
		}

		GjtMessageCommentLike item = gjtMessageCommentLikeService.findByCommentIdAndUserId(commentId, user.getId());
		if (item != null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "已经点赞过了！");
		}

		GjtMessageCommentLike entity = new GjtMessageCommentLike();
		entity.setId(UUIDUtils.random());
		entity.setIsLike("1");
		entity.setUserId(user.getId());
		entity.setCommentId(commentId);
		gjtMessageCommentLikeService.save(entity);
	}

	@RequestMapping(value = "addFeedback", method = RequestMethod.POST)
	@ResponseBody
	public void addFeedback(HttpServletRequest request, String messageUserId, String code) throws CommonException {
		if (StringUtils.isBlank(messageUserId) || StringUtils.isBlank(code)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "messageUserId or code 参数为空！");
		}
		GjtMessageUser messageUser = gjtMessageUserService.queryById(messageUserId);

		if (messageUser == null) {
			throw new CommonException(MessageCode.BAD_REQUEST, "根据messageUserId参数查询不到信息！");
		}
		GjtMessageInfo gjtMessageInfo = messageUser.getGjtMessageInfo();
		if ("0".equals(gjtMessageInfo.getIsFeedback())) {
			throw new CommonException(MessageCode.BIZ_ERROR, "没有开启反馈功能！");
		}
		if (StringUtils.isNotBlank(messageUser.getFeedbackType())) {
			throw new CommonException(MessageCode.BIZ_ERROR, "已经反馈过了！");
		}

		messageUser.setFeedbackType(code);
		messageUser.setFeedbackDt(DateUtils.getNowTime());
		gjtMessageUserService.update(messageUser);

	}

	@RequestMapping(value = "addComment", method = RequestMethod.POST)
	@ResponseBody
	public void addComment(HttpServletRequest request, String messageUserId, String content, String imgUrls,
			String platform) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isBlank(messageUserId) || StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "messageUserId or conntent 参数为空！");
		}
		GjtMessageUser messageUser = gjtMessageUserService.queryById(messageUserId);

		if (messageUser == null) {
			throw new CommonException(MessageCode.BAD_REQUEST, "根据messageUserId参数查询不到信息！");
		}
		GjtMessageInfo gjtMessageInfo = messageUser.getGjtMessageInfo();
		if ("0".equals(gjtMessageInfo.getIsComment())) {
			throw new CommonException(MessageCode.BIZ_ERROR, "没有开启评论功能！");
		}

		GjtMessageComment item = new GjtMessageComment();
		item.setId(UUIDUtils.random());
		item.setContent(content);
		item.setCreatedBy(user.getId());
		item.setCreatedDt(DateUtils.getNowTime());
		item.setImgUrls(imgUrls);
		item.setLikecount(0);
		item.setMessageId(messageUser.getMessageId());
		item.setPlatform(platform);
		item.setUserName(user.getRealName());
		item.setIsDeleted("N");
		gjtMessageCommentService.save(item);
	}

	@RequestMapping(value = "addCommentDetail", method = RequestMethod.POST)
	@ResponseBody
	public void addCommentDetail(HttpServletRequest request, String commentId, String content, String platform)
			throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		if (StringUtils.isBlank(commentId) || StringUtils.isBlank(content)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "commentId or conntent 参数为空！");
		}
		GjtMessageCommentDetail item = new GjtMessageCommentDetail();
		item.setId(UUIDUtils.random());
		item.setContent(content);
		item.setCreatedBy(user.getId());
		item.setCreatedDt(DateUtils.getNowTime());
		item.setLikecount(0);
		item.setCommentId(commentId);
		item.setPlatform(platform);
		item.setUserName(user.getRealName());
		item.setIsDeleted("N");
		gjtMessageCommentDetailService.save(item);
	}

	@RequestMapping(value = "findImportanceList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findImportanceList(HttpServletRequest request) throws CommonException {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		List<Map<String, Object>> list = gjtMessageUserService.findImportanceList(user.getId());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("list", list);
		int size = EmptyUtils.isEmpty(list) ? 0 : list.size();
		resultMap.put("total", size);
		return resultMap;
	}
}
