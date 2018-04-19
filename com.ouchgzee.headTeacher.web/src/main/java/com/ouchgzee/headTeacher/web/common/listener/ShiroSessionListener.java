/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.web.common.listener;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.service.systemManage.TblPriLoginLogService;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtUserAccount;
import com.ouchgzee.headTeacher.service.account.BzrGjtUserAccountService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年5月4日
 * @version 2.5
 *
 */
public class ShiroSessionListener extends SessionListenerAdapter {
	private final static Logger log = LoggerFactory.getLogger(ShiroSessionListener.class);

	@Autowired
	BzrGjtUserAccountService gjtUserAccountService;

	@Autowired
	private TblPriLoginLogService tblPriLoginLogService;

	@Override
	public void onStart(Session session) {// 会话创建时触发
		log.debug("会话创建：" + session.getId());

	}

	@Override
	public void onExpiration(Session session) {// 会话过期时触发
		log.info("会话过期：{}", session.getId());

		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) session.getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		if (employeeInfo != null) {
			BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
			if (user != null) {
				updateSate(user, session.getId().toString());
			}
		}
	}

	@Override
	public void onStop(Session session) {// 退出时触发
		log.info("会话停止：{}", session.getId());

		BzrGjtEmployeeInfo employeeInfo = (BzrGjtEmployeeInfo) session.getAttribute(Servlets.SESSION_EMPLOYEE_NAME);
		if (employeeInfo != null) {
			BzrGjtUserAccount user = employeeInfo.getGjtUserAccount();
			if (user != null) {
				updateSate(user, session.getId().toString());
			}
		}
	}

	/**
	 * 会话销毁以及过期，更改离线状态，记录登录时长
	 * 
	 * @param user
	 */
	public void updateSate(BzrGjtUserAccount user, String sessionId) {
		try {
			// 第一次登陆时候的session时间
			BzrGjtUserAccount userAccount = gjtUserAccountService.findOne(user.getId());
			log.info("第一次登陆时候的sessionId={},退出时候的的sessionId={}", userAccount.getCurrentLoginIp(), sessionId);
			if (userAccount.getCurrentLoginIp().equals(sessionId)) {
				// 修改为离线状态
				boolean updateQuitState = gjtUserAccountService.updateQuitState(user.getId());
				log.info("修改离线状态：updateQuitState={}", updateQuitState);
				Date currentLoginTime = userAccount.getCurrentLoginTime();
				long loingTime = currentLoginTime.getTime();
				long logoutTime = DateUtils.getDate().getTime();
				long min = (logoutTime - loingTime) / 60 / 1000;// 数据库存分钟
				log.info("在线时长参数：currentLoginTime={},sessionId={},logoutTime={},min={]", currentLoginTime,
						userAccount.getCurrentLoginIp(), logoutTime, min);
				tblPriLoginLogService.updateBySessionId(userAccount.getCurrentLoginIp(), min);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}
}
