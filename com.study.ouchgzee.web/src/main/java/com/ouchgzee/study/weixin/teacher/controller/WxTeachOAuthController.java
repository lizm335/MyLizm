/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.weixin.teacher.controller;

import com.gzedu.xlims.common.AppConfig;
import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.pojo.status.PriRoleInfoEnum;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.MessageCheck;
import com.ouchgzee.study.web.common.Servlets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
@RequestMapping("/wx/teachOauth")
public class WxTeachOAuthController extends BaseController {

	private static final Log log = LogFactory.getLog(WxTeachOAuthController.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;

	@Autowired
	private GjtOrgService gjtOrgService;
	
	@Autowired
	private PriRoleInfoService priRoleInfoService;
	
	@Autowired
	private CommonMapService commonMapService;

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
		GjtUserAccount user = (GjtUserAccount) session.getAttribute(WebConstants.CURRENT_USER_TEACHER);
		if(user != null) {
			if(user.getId() != null) {user = gjtUserAccountService.queryById(user.getId());} // 刷新账号信息
			searchParams.put("openid", user.getWxOpenId());
			searchParams.put("nickname", user.getWxNickName() != null ? URLEncoder.encode(URLEncoder.encode(user.getWxNickName(), Constants.CHARSET), Constants.CHARSET) : null);
			super.outputJs(response, "window.location.href='" + url + (url.indexOf("?") == -1 ? "?" : "&") + FormSubmitUtil.createLinkString(searchParams) + "';");
			return;
		}
		if (OrgUtil.GK_GZ.equals(orgCode)) { // 国家开放大学（广州）实验学院
			searchParams.put("url", URLEncoder.encode(url, Constants.CHARSET)); // 保证接收时该值是一个整体
			String wxOAuthUrl = AppConfig.getProperty("wx.oauth.server")
					+ "/eeapi.php?myaction=oauth&uniacid="
					+ AppConfig.getProperty("wx.publicAccounts.teacher")
					+ "&scope=user&backurl="
					+ URLEncoder.encode(
							AppConfig.getProperty("pcenterStudyServer") + "/wx/teachOauth?" + FormSubmitUtil.createLinkString(searchParams),
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
			ModelMap model) throws CommonException, UnsupportedEncodingException {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		log.error("test searchParams openid:" + searchParams);
		// 解决中文乱码问题 两次encodeURI,第一次编码得到的是UTF-8形式的URL，第二次编码得到的依然是UTF-8形式的URL，但是在效果上相当于首先进行了一 次UTF-8编码(此时已经全部转换为ASCII字符)，再进行了一次iso-8859-1编码，因为对英文字符来说UTF-8编码和ISO- 8859-1编码的效果相同。
		try {
			String nickname = (String) searchParams.get("nickname");
			String province = (String) searchParams.get("province");
			String city = (String) searchParams.get("city");
			String country = (String) searchParams.get("country");
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
		String wxOpenId = (String) searchParams.get("openid");
		if (StringUtils.isBlank(wxOpenId)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "无效的请求参数！");
		}
		String url = (String) searchParams.remove("url"); // 去除url，授权成功后需要跳转的页面地址

		GjtUserAccount user = gjtUserAccountService.queryAdminUserByWxOpenId(wxOpenId);
		if(StringUtils.equals((String) searchParams.get("binding"), Constants.BOOLEAN_1) || user == null) {
			// 跳转到注册页面
			return "redirect:/wx/weixin/faq/transfer.html?path="
					+ URLEncoder.encode("/wx/weixin/faq/teacher/bound.html?" + FormSubmitUtil.createLinkString(searchParams), Constants.CHARSET)
					+ "&" + FormSubmitUtil.createLinkString(searchParams);
		}
		// 进入登录页面销毁当前登录用户
		Subject account = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			account.logout();
		}
		try {
			Session session = SecurityUtils.getSubject().getSession();
			session.setAttribute(WebConstants.CURRENT_USER_TEACHER, user);
			session.setAttribute("userId", user.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.put("msg", "服务器异常！");
			return "/simple/tips";
		}
		return "redirect:" + url + (url.indexOf("?") == -1 ? "?" : "&") + FormSubmitUtil.createLinkString(searchParams);
	}

	/**
	 * 绑定页面
	 * @param orgCode
	 * @throws CommonException
	 * @throws IOException
	 */
	@RequestMapping(value = "/binding", method = RequestMethod.GET)
	public void to(@RequestParam String orgCode,
				   HttpServletRequest request, HttpServletResponse response, HttpSession session,
				   ModelMap model) throws CommonException, IOException {
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		searchParams.put("binding", Constants.BOOLEAN_1);
		if (OrgUtil.GK_GZ.equals(orgCode)) { // 国家开放大学（广州）实验学院
			searchParams.put("url", "");
			String wxOAuthUrl = AppConfig.getProperty("wx.oauth.server")
					+ "/eeapi.php?myaction=oauth&uniacid="
					+ AppConfig.getProperty("wx.publicAccounts.teacher")
					+ "&scope=user&backurl="
					+ URLEncoder.encode(
					AppConfig.getProperty("pcenterStudyServer") + "/wx/teachOauth?" + FormSubmitUtil.createLinkString(searchParams),
					Constants.CHARSET);
			super.outputJs(response, "window.location.href='" + wxOAuthUrl + "';");
		} else {
			throw new CommonException(MessageCode.BIZ_ERROR, "操作异常！");
		}
	}

	/**
	 * 获取院校下的学习中心和招生点
	 * @param orgCode
	 * @throws CommonException
	 */
	@RequestMapping(value = "/getStudyCenters", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getStudyCenters(@RequestParam String orgCode, 
			HttpServletRequest request, HttpSession session) throws CommonException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		GjtOrg org = gjtOrgService.queryByCode(orgCode);
		if(org == null) {
			throw new CommonException(MessageCode.BIZ_ERROR, "院校编码不存在！");
		}
		Map<String, String> studyCenters = commonMapService.getStudyCenterMap(org.getId());
		
		List<Map<String, Object>> studyCenterList = new ArrayList<Map<String,Object>>();
		for (Entry<String, String> e : studyCenters.entrySet()) {
			Map<String, Object> info = new HashMap<String, Object>();
			info.put("studyCenterId", e.getKey());
			info.put("studyCenterName", e.getValue());
			studyCenterList.add(info);
		}
		result.put("infos", studyCenterList);
		return result;
	}

	/**
	 * 绑定账号
	 * @param sjh
	 * @param code
	 * @param realName 真实姓名
	 * @param studyCenterId 学习中心ID
	 * @param openid
	 * @param headimgurl
	 * @param nickname
	 * @throws CommonException
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public void register(@RequestParam String sjh, 
			@RequestParam String code, 
			@RequestParam String realName, 
			@RequestParam String studyCenterId, 
			@RequestParam String openid, 
			HttpServletRequest request, HttpSession session) throws CommonException {
		MessageCheck.doSmsValidateCode(sjh, code, MessageCheck.TEACHER_BANDING, session); // 验证不通过会抛异常CommonException
		
		Map<String, Object> requestParams = Servlets.getParametersStartingWith(request, null);
		GjtUserAccount gjtUserAccount = gjtUserAccountService.queryAdminUserByWxOpenId(openid);
		if(gjtUserAccount != null) {
			if(sjh.equals(gjtUserAccount.getSjh())) {
				// 不做任何业务逻辑
			} else {
				// 正常情况已有openid会微信授权登录的，不会存在注册操作
				throw new CommonException(MessageCode.BIZ_ERROR, "该账号已绑定其他手机号！");
			}
		}
		
		List<String> studyCenters = gjtOrgService.queryByParentId(studyCenterId, "3"); // 获取学习中心ID
		if(studyCenters == null || studyCenters.size() != 1) { // 必须只能查出一条
			studyCenters = gjtOrgService.queryByParentId(studyCenterId, "6");
			if(studyCenters == null || studyCenters.size() != 1) { // 必须只能查出一条
				throw new CommonException(MessageCode.BIZ_ERROR, "学习中心不存在！");
			}
		}
		GjtUserAccount user = gjtUserAccountService.queryAdminUserBySjh(sjh);
		if(user == null) { // 不存在则新增账号
			user = gjtUserAccountService.saveEntity(realName, sjh, studyCenterId, 0); // 以手机号为账号
			PriRoleInfo roleInfo = priRoleInfoService.queryByCode(PriRoleInfoEnum.ROLE_1001.getCode()); // 角色设置为提问者，从角色枚举中获取角色Code
			user.setPriRoleInfo(roleInfo);
			user.setSjh(sjh); // 绑定手机号
			user.setWxOpenId(openid);
			user.setWxUnionId((String) requestParams.get("unionid"));
			user.setWxHeadPortrait((String) requestParams.get("headimgurl"));
			user.setWxNickName((String) requestParams.get("nickname"));
			user.setUpdatedBy("教师-绑定微信公众号账号");
			gjtUserAccountService.update(user);
		} else if(StringUtils.isBlank(user.getWxOpenId())) { // 未绑定微信公众号则绑定账号
			user.setWxOpenId(openid);
			user.setWxUnionId((String) requestParams.get("unionid"));
			user.setWxHeadPortrait((String) requestParams.get("headimgurl"));
			user.setWxNickName((String) requestParams.get("nickname"));
			user.setUpdatedBy("教师-绑定微信公众号账号");
			gjtUserAccountService.update(user);
		} else { // 手机号已绑定过微信公众号
			if(openid.equals(user.getWxOpenId())) {
				// 不做任何业务逻辑
			} else {
				throw new CommonException(MessageCode.BIZ_ERROR, "该手机号已被他人绑定！");
			}
		}
	}

	/**
	 * 发送短信验证码
	 * @param sjh
	 * @throws CommonException
	 */
	@RequestMapping(value = "/sendSmsCode", method = RequestMethod.POST)
	@ResponseBody
	public void sendSmsCode(@RequestParam String sjh, 
			HttpServletRequest request, HttpSession session) throws CommonException {
		MessageCheck.sendSmsCode(sjh, MessageCheck.TEACHER_BANDING, session);
	}

}
