/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.api;

import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.service.student.BzrGjtClassService;
import com.ouchgzee.headTeacher.web.common.Servlets;
import com.ouchgzee.headTeacher.web.controller.base.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 教师模拟登录接口<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月12日
 * @version 2.5
 */
@Controller
@RequestMapping("/api")
public class SimulationLoginApi extends BaseController {

	private static Logger log = LoggerFactory.getLogger(SimulationLoginApi.class);

	@Autowired
	private BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private BzrGjtClassService gjtClassService;

	/**
	 * 模拟登录
	 * 
	 * @param userInfo
	 *            值为userAccount+","+employeeId+","+timestamp加密的结果<br/>
	 *            userAccount - 教职工登录账号，employeeId - 教职工ID，timestamp - 时间戳<br/>
	 *            以英文分号拼接，整体加密<br/>
	 *            EncryptUtils的加密encrypt方法进行加密<br/>
	 * @return
	 */
	@RequestMapping(value = "/simulationLogin", method = RequestMethod.GET)
	public String simulationLogin(@RequestParam String userInfo, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
		UsernamePasswordToken token = null;
		try {
			String info = EncryptUtils.decrypt(userInfo);
			log.error("simulationLogin 解析结果 {}", info);
			String[] infos = info.split(",");
			String userAccount = infos[0];
			String employeeId = infos[1];
			String timestamp = infos[2];

			long now = System.currentTimeMillis();
			long mills = NumberUtils.toLong(timestamp);
			boolean timeout = Math.abs(now - mills) > DateUtil.ONE_MINUTE * 30;
			if (timeout) { // 30分钟内才能登录
				log.error("simulationLogin 登录异常 {}", "登录超时！");
				super.outputJsAlertCloseWindow(response, "登录超时！");
				return null;
			}

			BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(employeeId);
			// 验证账号是否正确
			Assert.isTrue(employeeInfo.getGjtUserAccount().getLoginAccount().equals(userAccount));


			// 进入登录页面销毁当前登录用户
			Subject account = SecurityUtils.getSubject();
			if (SecurityUtils.getSubject().getSession() != null) {
				account.logout();
			}

			String host = request.getRemoteHost();
			token = new UsernamePasswordToken(userAccount, employeeInfo.getGjtUserAccount().getPassword());
			account.login(token);

			// 因为httpSession不能set值，直接set到shrio

			SecurityUtils.getSubject().getSession().setAttribute(Servlets.SESSION_EMPLOYEE_NAME, employeeInfo);

			String cid = request.getParameter("cid"); // 默认选择一个班级
			if (StringUtils.isNotBlank(cid)) {
				SecurityUtils.getSubject().getSession().setAttribute(Servlets.SESSION_CURRENT_CLASS_NAME,
						gjtClassService.queryById(cid));
			}
			return "redirect:/home/main";
		} catch (Exception e) {
			if (token != null)
				token.clear();
			log.error("simulationLogin 登录异常 {}", e.getMessage());
			super.outputJsAlertCloseWindow(response, "服务器异常，请稍后再试！");
			return null;
		}
	}

}
