/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.weixin.student.controller;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.remote.OucnetRemoteService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信-微擎第三方授权获得openid<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月22日
 * @version 3.0
 *
 */
@Controller
@RequestMapping("/wx/studentOauth")
public class WxStudentOAuthController extends BaseController {

	private static final Log log = LogFactory.getLog(WxStudentOAuthController.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtClassStudentService gjtClassStudentService;

	@Autowired
	private OucnetRemoteService oucnetRemoteService;

	/**
	 * 微信授权
	 * @param orgCode
	 * @param url 授权成功后需要跳转的页面地址
	 * @throws CommonException
	 * @throws IOException 
	 */
	@RequestMapping(value = "/to", method = RequestMethod.GET)
	public void to(@RequestParam String orgCode,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) throws CommonException, IOException {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		String url = (String) searchParams.remove("url"); // 去除url，授权成功后需要跳转的页面地址
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER);
		if(user != null) {
			if(user.getId() != null) {user = gjtUserAccountService.queryById(user.getId());} // 刷新账号信息
			searchParams.put("hasPermissionOperation", user.getHasPermissionOperation());
			searchParams.put("openid", user.getWxOpenId());
			searchParams.put("nickname", user.getWxNickName() != null ? URLEncoder.encode(URLEncoder.encode(user.getWxNickName(), Constants.CHARSET), Constants.CHARSET) : null);
			super.outputJs(response, "window.location.href='" + url + (url.indexOf("?") == -1 ? "?" : "&") + FormSubmitUtil.createLinkString(searchParams) + "';");
			return;
		}
		if (OrgUtil.GK_GZ.equals(orgCode)) { // 国家开放大学（广州）实验学院
			searchParams.put("url", URLEncoder.encode(url, Constants.CHARSET)); // 保证接收时该值是一个整体
			String wxOAuthUrl = AppConfig.getProperty("wx.oauth.server")
					+ "/eeapi.php?myaction=oauth&uniacid="
					+ AppConfig.getProperty("wx.publicAccounts.student")
					+ "&scope=user&backurl="
					+ URLEncoder.encode(
							AppConfig.getProperty("pcenterStudyServer") + "/wx/studentOauth?" + FormSubmitUtil.createLinkString(searchParams),
							Constants.CHARSET);
			super.outputJs(response, "window.location.href='" + wxOAuthUrl + "';");
		} else {
			throw new CommonException(MessageCode.BIZ_ERROR, "操作异常！");
		}
	}

	/**
	 * 授权成功后登录账号
	 * @return
	 * @throws CommonException
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String oauth(HttpServletRequest request, HttpSession httpSession,
			ModelMap model) throws CommonException {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		log.error("test searchParams openid:" + searchParams);
		// 解决中文乱码问题 两次encodeURI,第一次编码得到的是UTF-8形式的URL，第二次编码得到的依然是UTF-8形式的URL，但是在效果上相当于首先进行了一 次UTF-8编码(此时已经全部转换为ASCII字符)，再进行了一次iso-8859-1编码，因为对英文字符来说UTF-8编码和ISO- 8859-1编码的效果相同。
		String nickname = (String) searchParams.get("nickname");
		String province = (String) searchParams.get("province");
		String city = (String) searchParams.get("city");
		String country = (String) searchParams.get("country");
		try {
			if(StringUtils.isNotBlank(nickname)) {
//				nickname = new String(nickname.getBytes("ISO-8859-1"), Constants.CHARSET);
				searchParams.put("nickname", URLEncoder.encode(URLEncoder.encode(nickname, Constants.CHARSET), Constants.CHARSET));
			}
			if(StringUtils.isNotBlank(province)) {
//				province = new String(nickname.getBytes("ISO-8859-1"), Constants.CHARSET);
				searchParams.put("province", URLEncoder.encode(URLEncoder.encode(province, Constants.CHARSET), Constants.CHARSET));
			}
			if(StringUtils.isNotBlank(city)) {
//				city = new String(nickname.getBytes("ISO-8859-1"), Constants.CHARSET);
				searchParams.put("city", URLEncoder.encode(URLEncoder.encode(city, Constants.CHARSET), Constants.CHARSET));
			}
			if(StringUtils.isNotBlank(country)) {
//				country = new String(nickname.getBytes("ISO-8859-1"), Constants.CHARSET);
				searchParams.put("country", URLEncoder.encode(URLEncoder.encode(country, Constants.CHARSET), Constants.CHARSET));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String orgCode = (String) searchParams.get("orgCode"); // 去除orgCode
		if (StringUtils.isBlank(orgCode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "orgCode不能为空！");
		}
		String wxOpenId = (String) searchParams.get("openid"); // 去除openid
		if (StringUtils.isBlank(wxOpenId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "无效的请求参数！");
		}
		String url = (String) searchParams.remove("url"); // 去除url，授权成功后需要跳转的页面地址

		GjtStudentInfo student = gjtStudentInfoService.queryByWxOpenId(wxOpenId);
		if(student == null) {
			if(oucnetRemoteService.checkBinding(wxOpenId)) {
				// 可以看到常见问题和在线服务，但是不能提问
				GjtUserAccount user = new GjtUserAccount();
				user.setHasPermissionOperation(0);
				user.setWxOpenId(wxOpenId);
				user.setWxNickName(nickname);
				Session session = SecurityUtils.getSubject().getSession();
				session.setAttribute(WebConstants.CURRENT_USER, user);
				searchParams.put("hasPermissionOperation", user.getHasPermissionOperation());
				return "redirect:" + url + (url.indexOf("?") == -1 ? "?" : "&") + FormSubmitUtil.createLinkString(searchParams);
			} else {
				// 跳转到运营平台去登录
				return "redirect:" + AppConfig.getProperty("applicationPlatform.domain") + AppConfig.getProperty("yunying.wx.bound");
			}
		}
		searchParams.put("hasPermissionOperation", student.getGjtUserAccount().getHasPermissionOperation());
		// 进入登录页面销毁当前登录用户
		Subject account = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			account.logout();
		}
		UsernamePasswordToken token = new UsernamePasswordToken(student.getGjtUserAccount().getLoginAccount(),
				student.getGjtUserAccount().getPassword());
		try {
			account.login(token);
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute(WebConstants.CURRENT_USER, student.getGjtUserAccount());
			session.setAttribute("userId", student.getGjtUserAccount().getId());

			if (student.getGjtUserAccount().getUserType() != null && student.getGjtUserAccount().getUserType() == 1) {
				session.setAttribute(WebConstants.STUDENT_INFO, student);

				GjtClassInfo classInfo = gjtClassStudentService.queryTeachClassInfoByStudetnId(student.getStudentId());
				session.setAttribute(WebConstants.TEACH_CLASS, classInfo);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			token.clear();
			model.put("msg", "服务器异常！");
			return "/simple/tips";
		}
		return "redirect:" + url + (url.indexOf("?") == -1 ? "?" : "&") + FormSubmitUtil.createLinkString(searchParams);
	}

}
