/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.home;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtMessageResult;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualSubject;

/**
 * 
 * 功能说明：短信记录表
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年11月20日
 * @version 2.5
 *
 */
public interface GjtMessageResultService {

	public Page<GjtMessageResult> findAll(Map<String, Object> map, PageRequest pageRequest);

	// 通过id查询通知公告
	public GjtMessageResult queryById(String id);

	// 是否已提醒
	public boolean queryByIncidentIdAndStartDateAndEndDate(String incidentId);

	public Boolean save(GjtMessageResult entity);

	/**
	 * 发送短息催促，并且把发送记录，保存到数据库
	 * 
	 * @param id
	 * @param account
	 * @param zhuanfaId
	 * @param studentName
	 * @return
	 */
	public GjtMessageResult messageSave(String id, GjtUserAccount account, String zhuanfaId, String studentName);

	/**
	 * 把数据短信通知学员，并且微信公众号推送信息
	 * 
	 * @param entity
	 */
	public void replyMessage(LcmsMutualSubject entity);
}
