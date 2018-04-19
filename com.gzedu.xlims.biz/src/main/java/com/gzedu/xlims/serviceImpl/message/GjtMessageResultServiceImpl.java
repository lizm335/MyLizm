/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.serviceImpl.message;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SMSUtil;
import com.gzedu.xlims.dao.message.GjtMessageResultDao;
import com.gzedu.xlims.dao.organization.GjtOrgDao;
import com.gzedu.xlims.dao.usermanage.GjtUserAccountDao;
import com.gzedu.xlims.pojo.GjtMessageResult;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.service.home.GjtMessageResultService;
import com.gzedu.xlims.third.weixin.IMessageSender;
import com.gzedu.xlims.third.weixin.MessageTemplate;

/**
 * 
 * 功能说明：短信记录表
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年11月20日
 * @version 2.5
 *
 */
@Service
public class GjtMessageResultServiceImpl implements GjtMessageResultService {

	private Logger log = LoggerFactory.getLogger(GjtMessageResultServiceImpl.class);

	@Autowired
	private GjtMessageResultDao gjtMessageResultDao;

	@Autowired
	private GjtUserAccountDao gjtUserAccountDao;

	@Autowired
	private GjtOrgDao gjtOrgDao;

	@Autowired
	private IMessageSender messageSender;

	@Override
	public Page<GjtMessageResult> findAll(Map<String, Object> map, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GjtMessageResult queryById(String id) {
		return gjtMessageResultDao.findOne(id);
	}

	@Override
	public boolean queryByIncidentIdAndStartDateAndEndDate(String incidentId) {
		boolean bool = false; // 是否存在
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			Timestamp startDate = DateUtils.getNowTime(sd.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
			Timestamp endDate = DateUtils.getNowTime(sd.format(calendar.getTime()));
			List<GjtMessageResult> findByIncidentId = gjtMessageResultDao.findByIncidentId(incidentId, startDate,
					endDate);

			if (EmptyUtils.isNotEmpty(findByIncidentId)) {
				if (findByIncidentId.size() > 0) {
					bool = true;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return bool;
	}

	@Override
	public Boolean save(GjtMessageResult entity) {
		GjtMessageResult save = gjtMessageResultDao.save(entity);
		return save != null ? true : false;
	}

	@Override
	public GjtMessageResult messageSave(String id, GjtUserAccount user, String zhuanfaId, String studentName) {
		if (StringUtils.isBlank(zhuanfaId)) {
			return null;
		}
		GjtUserAccount account = gjtUserAccountDao.findOne(zhuanfaId);

		if (account == null) {
			return null;
		}

		String remark = user.getRealName() + "(" + user.getPriRoleInfo().getRoleName() + ")" + "向你转发了一条" + studentName
				+ "(学员)的问题未解答，请尽快登录平台查看并解答问题！";
		int smsResult = SMSUtil.sendMessage(account.getTelephone(), remark, "gk");
		log.info("催促提醒别人回答问题参数remark={},result={}", remark, smsResult);

		GjtMessageResult gmr = new GjtMessageResult();
		gmr.setId(UUIDUtils.random().toString());
		gmr.setCreatedBy(user.getId());
		gmr.setCreatedUser(user.getRealName());
		gmr.setCreatedDt(DateUtils.getNowTime());
		gmr.setIncidentId(id);
		gmr.setReceiveId(account.getId());
		gmr.setReceiveUser(account.getRealName());
		return gjtMessageResultDao.save(gmr);
	}

	@Override
	public void replyMessage(LcmsMutualSubject entity) {
		// 发送短信
		GjtUserAccount createAccount = entity.getCreateAccount();// 创建者
		GjtUserAccount askUser = gjtUserAccountDao.findOne(entity.getForwardAccountId());// 回答问题者
		String tile = "";
		if (entity.getSubjectTitle().length() < 6) {
			tile = entity.getSubjectTitle();
		} else {
			tile = entity.getSubjectTitle().substring(0, 5);
		}
		String remark = "您提的\"" + tile + "\"问题已被" + askUser.getRealName() + "(" + askUser.getPriRoleInfo().getRoleName()
				+ ")解答，请及时登录平台查看并确认问题是否已解决！";
		int smsResult = SMSUtil.sendMessage(createAccount.getTelephone(), remark, "gk");
		log.info("问题解决短信提醒参数remark={},result={}", remark, smsResult);

		// 发送微信APP，短信推送给老师
		GjtUserAccount initiaUser = gjtUserAccountDao.findOne(entity.getInitialAccountId());// 最原始的回答问题者
		String code = "";
		GjtOrg gjtOrg = initiaUser.getGjtOrg();
		if ("1".equals(gjtOrg.getOrgType())) {
			code = gjtOrg.getCode();
		} else {
			code = gjtOrgDao.queryParentBySonId(gjtOrg.getId()).getCode();
		}
		String type = StringUtils.isNotBlank(entity.getOftenType()) ? entity.getOftenType() : "3";
		messageSender.sendCustomMsg(AppConfig.getProperty("wx.publicAccounts.teacher"), initiaUser.getWxOpenId(),
				MessageTemplate.FEEDBACK_REPLY,
				AppConfig.getProperty("pcenterStudyServer") + "/wx/teachOauth/to?orgCode=" + code + "&url="
						+ URLEncoder.encode("/wx/weixin/faq/transfer.html?path="
								+ URLEncoder.encode("/wx/weixin/faq/teacher/question_details.html?id="
										+ entity.getSubjectId() + "&orgCode=" + code + "&oftenType=" + type)));

		messageSender.sendCustomMsg(AppConfig.getProperty("wx.publicAccounts.student"), createAccount.getWxOpenId(),
				MessageTemplate.FEEDBACK_REPLY,
				AppConfig.getProperty("pcenterStudyServer") + "/wx/studentOauth/to?orgCode=" + code + "&url="
						+ URLEncoder.encode("/wx/weixin/faq/transfer.html?path="
								+ URLEncoder.encode(
										"/wx/weixin/faq/student/question_details.html?id=" + entity.getSubjectId())
								+ "&orgCode=" + code));
	}

}
