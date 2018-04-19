package com.gzedu.xlims.web.controller.personal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.EeSmsUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;

import net.spy.memcached.MemcachedClient;
/**
 * 教学教务组织平台-个人资料管理
 * @author lyj
 * @time 2017年5月5日 
 * TODO
 */
@RequestMapping("/personal")
@Controller
public class PersonalController {
	
	@Autowired
	private GjtUserAccountService gjtUserAccountService;
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	@RequestMapping("/index")
	public String index(ModelMap model,HttpServletRequest request) {
		String userId = (String) request.getSession().getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		PersonalVo vo = new PersonalVo();
		// 帐号id
		vo.setId(user.getId());
		// 平台帐号
		vo.setAccount(user.getLoginAccount());
		// 院校
		if(user.getGjtOrg()!=null) {
			vo.setOrgName(user.getGjtOrg().getOrgName());
		}
		// 角色
		if(user.getPriRoleInfo() != null) {
			vo.setRoleName(user.getPriRoleInfo().getRoleName());
		}
		// 真实性名
		vo.setRealName(user.getRealName());
		// 手机
		vo.setMobile(user.getSjh());
		// 联系电话
		vo.setPhone(user.getTelephone());
		// email
		vo.setEmail(user.getEmail());
		// 签名
		vo.setSignPhoto(user.getSignPhoto());
		model.addAttribute("personal",vo);
		return "personal/index";
	}
	
	@ResponseBody @RequestMapping(value="/update" ,method=RequestMethod.POST)
	public Map<String,Object> update(PersonalVo vo) {
		String userId = vo.getId();
		Map<String,Object> map = Maps.newHashMap();
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		if(user != null) {
			try {
				if(!Strings.isNullOrEmpty(vo.getRealName())) {
					user.setRealName(vo.getRealName());
				}
				if(!Strings.isNullOrEmpty(vo.getEmail())) {
					user.setEmail(vo.getEmail());
				}
				if(!Strings.isNullOrEmpty(vo.getPhone())) {
					user.setTelephone(vo.getPhone());
				}
				gjtUserAccountService.update(user);
				map.put("message", "更新成功!");
			} catch (Exception e) {
				map.put("message", "更新发生异常！");
				e.printStackTrace();
			}
		} else {
			map.put("message", "查找不到当前用户的信息!");
		}
		return map;
	}
	
	
	/* 更换手机 */ 
	@RequestMapping("toUnBindMobile")
	public String toUnBindMobile(ModelMap model,HttpServletRequest request) {
		String userId = (String) request.getSession().getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		if(!Strings.isNullOrEmpty(user.getSjh())) {
			model.addAttribute("mobile",user.getSjh());
		}
		return "personal/unBindMobile";
	}

	@ResponseBody
	@RequestMapping("sendSmsCode")
	public Map<String, Object> sendSmsCode(String mobile, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		// 验证码
		// String random = StringUtils.getRandomStringAndNum(5);
		String cacheCode = (String) memcachedClient.get("validateCode_"+mobile);
		Map<String,Object> resultMap = Maps.newHashMap();
		if(!Strings.isNullOrEmpty(cacheCode)) {
			resultMap.put("result", "success");
			request.getSession().setAttribute("validateCode_"+mobile, cacheCode);
		} else {
			Map<String, String> param = new HashMap<String, String>();
			param.put("USER_NAME", ObjectUtils.toString(user.getRealName()));
			param.put("MOBILE", mobile);
			Map<String, Object> map = Maps.newHashMap();
			map = EeSmsUtils.getSmsCodeServc(param);
			// map.put("result", "success");
			// map.put("code", "642708");
			if ("success".equals(map.get("result"))) {
				resultMap.put("result", "success");
				request.getSession().setAttribute("validateCode_"+mobile, map.get("code"));
				//添加到memcached缓存
				memcachedClient.add("validateCode_"+mobile, 900,map.get("code"));// 15分钟
			}
		}
		return resultMap;
	}
	
	@RequestMapping(value="bindMobile",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bindMobile(HttpServletRequest request, String mobile,String firstValidateCode,String newValidateCode) {
		String userId = (String) request.getSession().getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		Map<String,Object> map = Maps.newHashMap();
		boolean firstMobileValidate = true;
		if(!Strings.isNullOrEmpty(user.getSjh())) {
			firstMobileValidate = false;
			String firstSessionCode = (String) request.getSession().getAttribute("validateCode_"+user.getSjh());
			if(firstSessionCode != null && firstValidateCode != null && firstValidateCode.equals(firstSessionCode)) {
				firstMobileValidate = true;
			}
		}
		if(firstMobileValidate) {
			String newSessionCode = (String) request.getSession().getAttribute("validateCode_"+mobile);
			if(newSessionCode != null && newValidateCode != null && newValidateCode.equals(newSessionCode)) {
				user.setSjh(mobile);
				gjtUserAccountService.update(user);
				map.put("result", "success");
				map.put("message", "绑定成功!");
			} else {
				map.put("result", "error");
				map.put("message", "新手机验证码错误!");
			}
		} else {
			map.put("result", "error");
			map.put("message", "原手机验证码错误!!");
		}
		return map;
	}
	
	
	/* 修改密码 */
	@RequestMapping("resetPassword")
	@ResponseBody
	public Map<String,Object> resetPassword(HttpServletRequest request) {
		String userId = (String) request.getSession().getAttribute("userId");
		GjtUserAccount user = gjtUserAccountService.findOne(userId);
		String password = user.getPassword();
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");
		Map<String,Object> map = Maps.newHashMap();
		if(password.equals(Md5Util.encode(oldPassword))) {
			user.setPassword2(newPassword);
			user.setPassword(Md5Util.encode(newPassword));
			gjtUserAccountService.update(user);
			map.put("result", "success");
			map.put("message", "修改密码成功!");
		} else {
			// 密码错误
			map.put("result", "error");
			map.put("message", "密码错误");
		}
		return map;
	}
	/* 修改头像  */
	public String updateStudentAvatar() {
		return null;
	}

	@RequestMapping("updateSignPhoto")
	@ResponseBody
	public Map<String,Object> updateSignPhoto(HttpServletRequest request, String signPhoto) {
		Map<String,Object> map = Maps.newHashMap();
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			user.setSignPhoto(signPhoto);
			gjtUserAccountService.update(user);
			map.put("result", "success");
			map.put("message", "保存成功!");
		} catch (Exception e) {
			map.put("result", "error");
			map.put("message", "保存失败");
		}
		
		return map;
	}

}
