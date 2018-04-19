/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.web.controller.systemManage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.common.gzedu.SignUtil;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.TblSysDataService;
import com.gzedu.xlims.service.api.ApiOucSyncService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.common.Servlets;

import net.sf.json.JSONObject;

/**
 * 
 * 功能说明： 操作管理，使用Restful风格的Urls
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/system/user")
public class UserAccountController {

	private static final Logger log = LoggerFactory.getLogger(UserAccountController.class);

	@Autowired
	GjtUserAccountService gjtUserAccountService;

	@Autowired
	PriRoleInfoService roleInfoService;

	@Autowired
	CommonMapService commonMapService;

	@Autowired
	TblSysDataService tblSysDataService;

	@Autowired
	GjtOrgService gjtOrgService;
	
	@Autowired
	ApiOucSyncService apiOucSyncService;

	@Value("#{configProperties['api.enroll.domain']}")
	String enrollServer;

	@RequestMapping(value = "list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "page.size", defaultValue = "10") int pageSize,
			@RequestParam(value = "orderProperty", defaultValue = "createdDt") String orderProperty,
			@RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection, Model model,
			HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		PageRequest pageRequst = Servlets.buildPageRequest(pageNumber, pageSize, orderProperty, orderDirection);

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, "search_");

		Map<String, String> roles = roleInfoService.queryRoles(user.getGrantType(), user.getPriRoleInfo().getRoleId());

		model.addAttribute("roles", roles);
		if (EmptyUtils.isEmpty(searchParams.get("EQ_priRoleInfo.roleId"))) {
			// String roleIds = StringUtils.join(roles.keySet(), ",");
			searchParams.put("IN_priRoleInfo.roleId", roles.keySet());
		}
		if ("0".equals(user.getGrantType())) {
			searchParams.put("isAdmin", "yes");
		}

		Page<Map<String, Object>> pageInfo = gjtUserAccountService.queryPage(user.getGjtOrg().getId(), searchParams,
				pageRequst);
		model.addAttribute("pageInfo", pageInfo);

		Map<String, String> orgMap = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);
		model.addAttribute("orgMap", orgMap);

		// 获取权限
		Subject subject = SecurityUtils.getSubject();
		model.addAttribute("isBtnCreate", subject.isPermitted("/system/user/list$create"));
		model.addAttribute("isBtnUpdate", subject.isPermitted("/system/user/list$update"));
		model.addAttribute("isBtnReset", subject.isPermitted("/system/user/list$reset"));
		model.addAttribute("isBtnDelete", subject.isPermitted("/system/user/list$delete"));

		return "systemManage/user/list";
	}

	@RequiresPermissions("/system/user/list$create")
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		// List<PriRoleInfo> roles = roleInfoService.queryAll();

		Map<String, String> roles = roleInfoService.queryRoles(user.getGrantType(), user.getPriRoleInfo().getRoleId());

		model.addAttribute("roles", roles);

		Map<String, String> orgTree = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);
		model.addAttribute("orgs", orgTree);

		model.addAttribute("entity", new GjtUserAccount());
		model.addAttribute("action", "create");
		return "systemManage/user/form";
	}

	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		// https://api.emp.eenet.com/Learncenterapi/updateAdmin.html
		String url = "https://api.emp.eenet.com/Learncenterapi/updateAdmin.html";
		params.put("LEARNCENTER_CODE", "GRHZXXZX");// 编码
		params.put("USER_NAME", "testceshi004");
		params.put("USER_ACCOUNT", "testceshi004");
		params.put("TELEPHONE", "1380013800");
		params.put("USER_PASSWORD", "888888");
		params.put("ROLE_CODE", "xxzx_zszr");// 备注

		// long time = DateUtils.getDate().getTime();
		// 额外参数;签名
		params.put("sign", SignUtil.formatUrlMap(params, 1512727617733L));// 字母要大写
		params.put("appid", SignUtil.APPID);// APPID不需要参与加密
		params.put("time", String.valueOf(1512727617733L));

		log.info("招生平台创建用户接口参数：url={},{}", url, params);
		String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
		if (StringUtils.isNotEmpty(result)) {
			JSONObject json = JSONObject.fromObject(result);
			int bo = (Integer) json.get("status");
			System.out.println(json);
		}
	}

	@SysLog("用户管理-新增用户")
	@RequiresPermissions("/system/user/list$create")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid GjtUserAccount entity, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Feedback feedback = new Feedback(true, "创建成功");
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		// boolean bool1 =
		// "a87f2558fb814b20949a402b03c7ea4c".equals(user.getPriRoleInfo().getRoleId());//
		// 学习中心主任创建下属帐号同步到招生平台
		boolean bool2 = "a87f2558fb814b20949a402b03c7ea4c".equals(entity.getPriRoleInfo().getRoleId());// 创建学习中心主任也推送给招生平台
		boolean bool3 = "06141fff2ca84575bd9bc9fd55803b57".equals(entity.getPriRoleInfo().getRoleId());// 创建学习中心主任也推送给招生平台
		// boolean bool4 =
		// "06141fff2ca84575bd9bc9fd55803b57".equals(user.getPriRoleInfo().getRoleId());//
		// 学习中心主任创建下属帐号同步到招生平台

		// 国开在线模式把账号同步到国开学习网
		boolean syncFlg = true;
		if ("5".equals(user.getGjtOrg().getSchoolModel())) {
			Map formMap = new HashMap();
			Map dataMap = new HashMap();
			dataMap.put("OrgCode", user.getGjtOrg().getCode());
			dataMap.put("UserName", entity.getLoginAccount());
			dataMap.put("RealName", entity.getRealName());
			dataMap.put("TeacherNO", entity.getLoginAccount());
			dataMap.put("Password", entity.getPassword2());
			dataMap.put("EMAIL", entity.getEmail());
			
			formMap.put("synchDATA", dataMap);
			formMap.put("operatingUserName", user.getLoginAccount());
			formMap.put("functionType", "SynchTeacher");
			Map jsonMap = apiOucSyncService.addAPPDataSynch(formMap);
			
			String code = ObjectUtils.toString(jsonMap.get("code"));
			
			if ("1".equals(code)) {
				syncFlg = true;
			} else {
				String ErrorMessage = "同步数据到国开总部失败！";
				if (EmptyUtils.isNotEmpty(ObjectUtils.toString(jsonMap.get("data")))) {
					List dataList = (List)JSON.parse(ObjectUtils.toString(jsonMap.get("data")));
					Map dataMaps = (Map)dataList.get(0);
					ErrorMessage = ObjectUtils.toString(dataMaps.get("ErrorMessage"));
				}
				feedback = new Feedback(false, ErrorMessage+"，如有疑问请联系管理员！");
				syncFlg = false;
			}
		}
		
		if (syncFlg) {
			if (bool2 || bool3) {
				try {
					Map<String, String> params = new HashMap<String, String>();
					String url = enrollServer + "/Learncenterapi/updateAdmin.html";
					GjtOrg gjtOrg = gjtOrgService.queryById(entity.getGjtOrg().getId());
					PriRoleInfo priRoleInfo = roleInfoService.queryById(entity.getPriRoleInfo().getRoleId());
					params.put("LEARNCENTER_CODE", gjtOrg.getCode());// 编码
					params.put("USER_NAME", entity.getRealName());
					params.put("USER_ACCOUNT", entity.getLoginAccount());
					if (StringUtils.isBlank(entity.getSjh())) {// 招生接口是必填的，没必要
						params.put("TELEPHONE", "13800138000");
					} else {
						params.put("TELEPHONE", entity.getSjh());
					}
					params.put("USER_PASSWORD", entity.getPassword2());
					params.put("ROLE_CODE", priRoleInfo.getRoleCode());// 备注

					long time = DateUtils.getDate().getTime();
					// 额外参数;签名
					params.put("sign", SignUtil.formatUrlMap(params, time).toUpperCase());// 字母要大写
					params.put("appid", SignUtil.APPID);// APPID不需要参与加密
					params.put("time", String.valueOf(time));

					log.info("招生平台创建用户接口参数：url={},{}", url, params);
					String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
					log.info("招生平台创建用户接口结果：{}", result);
					if (StringUtils.isNotEmpty(result)) {
						JSONObject json = JSONObject.fromObject(result);
						int bo = (Integer) json.get("status");
						if (bo == 1) {// 成功，顺便把教学教务的帐号也创建了
							entity.setLoginAccount(entity.getLoginAccount().trim());
							gjtUserAccountService.insert(entity);
						} else {
							feedback = new Feedback(false, "招生平台接口发生异常！" + json.get("msg"));
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					feedback = new Feedback(false, "招生平台接口发生异常！");
				}
			} else {
				try {
					entity.setLoginAccount(entity.getLoginAccount().trim());
					gjtUserAccountService.insert(entity);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					feedback = new Feedback(false, "发生异常，创建失败");
				}
			}
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/user/list";
	}

	@RequiresPermissions("/system/user/list$update")
	@RequestMapping(value = "update/{id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> roles = roleInfoService.queryRoles(user.getGrantType(), user.getPriRoleInfo().getRoleId());

		model.addAttribute("roles", roles);

		Map<String, String> orgTree = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);
		model.addAttribute("orgs", orgTree);

		model.addAttribute("entity", gjtUserAccountService.findOne(id));
		model.addAttribute("action", "update");
		return "systemManage/user/form";
	}

	@SysLog("用户管理-编辑用户")
	@RequiresPermissions("/system/user/list$update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("entity") GjtUserAccount entity,
			RedirectAttributes redirectAttributes) {
		Feedback feedback = new Feedback(true, "更新成功");

		GjtUserAccount modifyInfo = gjtUserAccountService.findOne(entity.getId());
		modifyInfo.setLoginAccount(entity.getLoginAccount().trim());
		modifyInfo.setRealName(entity.getRealName());
		modifyInfo.setGjtOrg(entity.getGjtOrg());
		modifyInfo.setPriRoleInfo(entity.getPriRoleInfo());
		modifyInfo.setSjh(entity.getSjh());
		modifyInfo.setEmail(entity.getEmail());
		modifyInfo.setPassword(Md5Util.encode(entity.getPassword2()));
		modifyInfo.setPassword2(entity.getPassword2());
		// modifyInfo.setUserType(entity.getUserType());
		modifyInfo.setIsEnabled(entity.getIsEnabled());

		try {
			gjtUserAccountService.update(modifyInfo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			feedback = new Feedback(false, "更新失败");
		}
		redirectAttributes.addFlashAttribute("feedback", feedback);
		return "redirect:/system/user/list";
	}

	@RequestMapping(value = "view/{id}", method = RequestMethod.GET)
	public String viewForm(@PathVariable("id") String id, Model model, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		Map<String, String> roles = roleInfoService.queryRoles(user.getGrantType(), user.getPriRoleInfo().getRoleId());
		model.addAttribute("roles", roles);

		Map<String, String> orgTree = commonMapService.getOrgTree(user.getGjtOrg().getId(), false);
		model.addAttribute("orgs", orgTree);

		model.addAttribute("info", gjtUserAccountService.findOne(id));
		model.addAttribute("action", "view");
		return "systemManage/user/form";
	}

	@SysLog("用户管理-删除用户")
	@RequiresPermissions("/system/user/list$delete")
	@RequestMapping(value = "delete")
	@ResponseBody
	public Feedback delete(String ids, ServletResponse response) throws IOException {
		Feedback fb = new Feedback(true, "删除成功");
		if (StringUtils.isNotBlank(ids)) {
			List<String> selectedIds = new ArrayList<String>(Arrays.asList(ids.split(",")));
			try {
				gjtUserAccountService.delete(selectedIds);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "删除失败");
			}
		}
		return fb;
	}

	// 校验帐号是否存在
	@RequestMapping(value = "checkloginAccount")
	@ResponseBody
	public Feedback checkLogin(String loginAccount) throws IOException {
		Boolean boolean1 = gjtUserAccountService.existsByLoginAccount(loginAccount.trim());
		Feedback fe = new Feedback(boolean1, "");
		fe.setSuccessful(boolean1);
		return fe;
	}

	@SysLog("用户管理-重置密码")
	@RequiresPermissions("/system/user/list$reset")
	@RequestMapping(value = "resetPassword")
	@ResponseBody
	public Feedback resetPassword(String id, HttpServletRequest request, ServletResponse response) throws IOException {
		log.info("id:" + id);
		Feedback fb = new Feedback(true, "重置成功");
		if (StringUtils.isNotBlank(id)) {
			try {
				GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
				GjtUserAccount gjtUserAccount = gjtUserAccountService.findOne(id);
				gjtUserAccount.setPassword(Md5Util.encode("888888"));
				gjtUserAccount.setPassword2("888888");
				gjtUserAccount.setUpdatedBy(user.getId());
				gjtUserAccount.setUpdatedDt(DateUtils.getNowTime());
				gjtUserAccountService.update(gjtUserAccount);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				fb = new Feedback(false, "重置失败");
			}
		}
		return fb;
	}

	@RequestMapping(value = "getUserByRoleId")
	@ResponseBody
	public Map<String, String> getUserByRoleId(@RequestParam String roleId, HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		Map<String, String> result = gjtUserAccountService.findUserByRoleId(roleId, user.getGjtOrg().getId());
		return result;
	}
}
