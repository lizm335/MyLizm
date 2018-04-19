/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.constants.shiro.filter;

import java.util.Date;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月4日
 * @version 2.5
 *
 */
public class ShiroSessionListener extends SessionListenerAdapter {
	private static final Logger log = LoggerFactory.getLogger(ShiroSessionListener.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	TblPriLoginLogService tblPriLoginLogService;

	@Override
	public void onStart(Session session) {// 会话创建时触发
		log.debug("会话创建：" + session.getId());

	}

	@Override
	public void onExpiration(Session session) {// 会话过期时触发
		log.info("会话过期：{}", session.getId());

		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		if (user != null) {
			updateSate(user, session.getId().toString());
		}
	}

	@Override
	public void onStop(Session session) {// 退出时触发
		log.info("会话停止：{}", session.getId());

		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		if (user != null) {
			updateSate(user, session.getId().toString());
		}
	}

	public void updateSate(GjtUserAccount user, String sessionId) {
		try {
			// 第一次登陆时候的session时间
			GjtUserAccount userAccount = gjtUserAccountService.findOne(user.getId());
			log.info("==================>currentLoginIp：{},session-currentLoginIp：{},sessionId：{}", userAccount.getCurrentLoginIp(),
					user.getCurrentLoginIp(), sessionId);
			boolean updateQuitState = gjtUserAccountService.updateQuitStateBySessionId(sessionId);
			log.info("==================>修改离线状态：updateQuitState={}", updateQuitState);
			if(updateQuitState) {
				Date currentLoginTime = userAccount.getCurrentLoginTime();
				long loingTime = currentLoginTime.getTime();
				long logoutTime = DateUtils.getDate().getTime();
				long min = (logoutTime - loingTime) / 60 / 1000;// 数据库存分钟
				log.info("==================>在线时长参数：currentLoginTime={},sessionId={},logoutTime={},min={}", currentLoginTime,
						userAccount.getCurrentLoginIp(), logoutTime, min);
				tblPriLoginLogService.updateBySessionId(userAccount.getCurrentLoginIp(), min);
			}
			/*if (userAccount.getCurrentLoginIp().equals(sessionId)) {
				// 修改为离线状态
				boolean updateQuitState = gjtUserAccountService.updateQuitState(user.getId());
				log.info("修改离线状态：updateQuitState={}", updateQuitState);
				Date currentLoginTime = userAccount.getCurrentLoginTime();
				long loingTime = currentLoginTime.getTime();
				long logoutTime = DateUtils.getDate().getTime();
				long min = (logoutTime - loingTime) / 60 / 1000;// 数据库存分钟
				log.info("在线时长参数：currentLoginTime={},sessionId={},logoutTime={},min={}", currentLoginTime,
						userAccount.getCurrentLoginIp(), logoutTime, min);
				tblPriLoginLogService.updateBySessionId(userAccount.getCurrentLoginIp(), min);
			}*/
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}
}
