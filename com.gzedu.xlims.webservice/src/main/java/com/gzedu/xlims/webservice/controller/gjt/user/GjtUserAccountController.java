package com.gzedu.xlims.webservice.controller.gjt.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.organization.GjtClassInfoService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtUserAccountService;
import com.gzedu.xlims.service.usermanage.PriRoleInfoService;
import com.gzedu.xlims.webservice.controller.BaseController;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;

/**
 * 用户管理接口
 * 
 * @author lyj
 * @time 2017年5月18日 TODO
 */
@Controller
@RequestMapping("/interface/gjtUserAccount")
public class GjtUserAccountController extends BaseController {

	private final static Logger log = LoggerFactory.getLogger(GjtUserAccountController.class);

	@Autowired
	private GjtUserAccountService gjtUserAccountService;
	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;
	@Autowired
	private GjtClassInfoService gjtClassInfoService;

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private PriRoleInfoService priRoleInfoService;

	@RequestMapping(value = "saveOrUpdate", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseResult saveOrUpdate(GjtUserAccount entity, HttpServletRequest request) {
		ResponseResult result;
		try {
			if (StringUtils.isEmpty(entity.getLoginAccount())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "帐号不能为空!");
			}
			entity.setLoginAccount(entity.getLoginAccount().trim());
			if (StringUtils.isEmpty(entity.getRealName())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "姓名不能为空!");
			}

			if (StringUtils.isEmpty(entity.getSjh())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "手机号不能为空!");
			}
			// entity.setIsEnabled(true);
			if (StringUtils.isEmpty(entity.getId())) {
				if (null != gjtUserAccountService.queryByLoginAccount(entity.getLoginAccount())) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "帐号已存在!");
				}
				if (entity.getGjtOrg() == null || StringUtils.isEmpty(entity.getGjtOrg().getId())) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "院校ID不能为空!");
				}
				if (entity.getPriRoleInfo() == null || StringUtils.isEmpty(entity.getPriRoleInfo().getRoleId())) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "角色ID不能为空!");
				} else {
					// 不能创建超级管理员
					PriRoleInfo role = priRoleInfoService.queryById(entity.getPriRoleInfo().getRoleId());
					if (role == null) {
						return new ResponseResult(ResponseStatus.PARAM_ERROR, "查找不到角色!");
					} else {
						if ("系统管理员".equals(role.getRoleName())) {
							return new ResponseResult(ResponseStatus.PARAM_ERROR, "不能创建系统管理员!");
						}
					}
				}
				entity.setCreatedBy(request.getRequestURL().toString());
				gjtUserAccountService.insert(entity);
			} else {
				GjtUserAccount account = gjtUserAccountService.findOne(entity.getId());
				account.setLoginAccount(entity.getLoginAccount());
				account.setRealName(entity.getRealName());
				account.setSjh(entity.getSjh());
				account.setEmail(entity.getEmail());
				account.setUpdatedBy(request.getRequestURL().toString());
				gjtUserAccountService.update(account);
			}
			result = new ResponseResult(ResponseStatus.SUCCESS, entity.getId());
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "checkUser", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseResult checkUser(String userName, String password) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(userName)) {
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(userName);
			user = user == null ? gjtUserAccountService.queryBySfzh(userName) : user;
			if (user == null) {
				result = new ResponseResult(ResponseStatus.FAIL, userName + "帐号不存在.");
			} else {
				String userInfo = EncryptUtils.encrypt(user.getLoginAccount() + "," + user.getPassword());
				if (StringUtils.isNotEmpty(password)) {
					if (user.getPassword().equals(password) || user.getPassword2().equals(password)) {
						result = new ResponseResult(ResponseStatus.SUCCESS, userInfo);
					} else {
						result = new ResponseResult(ResponseStatus.FAIL, "密码错误.");
					}
				} else {
					/*
					 * if (checkType(user, userType, employeeType)) { result =
					 * new ResponseResult(ResponseStatus.SUCCESS, userName +
					 * "存号已存在"); } else { result = new
					 * ResponseResult(ResponseStatus.FAIL, "该用户类型/教职类型的帐号不存在.");
					 * }
					 */
					result = new ResponseResult(ResponseStatus.FAIL, userName + "存号已存在");
				}
			}
		} else {
			result = new ResponseResult(ResponseStatus.PARAM_ERROR, "参数userName不能为空.");
		}
		return result;
	}

	public ResponseResult checkUser(String userName, String password,
			@RequestParam(defaultValue = "0") Integer userType, Integer employeeType) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(userName)) {
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(userName);
			if (user == null) {
				result = new ResponseResult(ResponseStatus.NUKNOW_ACCOUNT, userName + "帐号不存在.");
			} else {
				String userInfo = EncryptUtils.encrypt(user.getLoginAccount() + "," + user.getPassword2());
				if (StringUtils.isNotEmpty(password)) {
					if (user.getPassword().equals(password) || user.getPassword2().equals(password)) {
						result = new ResponseResult(ResponseStatus.SUCCESS, userInfo);
					} else {
						result = new ResponseResult(ResponseStatus.FAIL, "密码错误.");
					}
				} else {
					if (checkType(user, userType, employeeType)) {
						result = new ResponseResult(ResponseStatus.FAIL, userName + "存号已存在");
					} else {
						result = new ResponseResult(ResponseStatus.FAIL, "该用户类型/教职类型的帐号不存在.");
					}
				}
			}
		} else {
			result = new ResponseResult(ResponseStatus.PARAM_ERROR, "参数userName不能为空.");
		}
		return result;
	}

	/**
	 * 启用停用
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
	public ResponseResult changeStatus(String id, int status) {

		ResponseResult result;
		try {
			int isEnabled = 1; // 1启用,0停用
			if (status == 0) {
				isEnabled = status;
			}
			int b = gjtUserAccountService.updateIsEnabled(id, isEnabled);
			if (b == 1) {
				result = new ResponseResult(ResponseStatus.SUCCESS, "修改状态成功!");
			} else {
				result = new ResponseResult(ResponseStatus.FAIL, "修改状态失败!");
			}
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.FAIL, e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	private boolean checkType(GjtUserAccount user, Integer userType, Integer employeeType) {
		boolean check = false;
		if (userType == user.getUserType()) {
			if (user.getUserType() == 2 || user.getUserType() == 3) { // 2:教师，3:职工
				GjtEmployeeInfo employee = gjtEmployeeInfoService.queryByAccountId(user.getId());
				if (employee.getEmployeeType().equals(employeeType)) {
					check = true;
				}
			} else {
				check = true;
			}
		} else {
			return false;
		}
		return check;
	}

	@RequestMapping(value = "login", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseResult login(String userInfo, HttpServletRequest request, HttpServletResponse response) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(userInfo)) {
			try {
				String userInfos[] = EncryptUtils.decrypt(userInfo).split(",");
				String username = userInfos[0];
				String password = userInfos[1];
				Subject subject = SecurityUtils.getSubject();

				GjtUserAccount oldUser = (GjtUserAccount) subject.getSession().getAttribute(WebConstants.CURRENT_USER);
				if (oldUser != null) {
					subject.logout();
				}
				UsernamePasswordToken token = new UsernamePasswordToken(username, password);
				subject.login(token);
				// 设置session
				GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(username);
				Session session = SecurityUtils.getSubject().getSession();
				session.setAttribute(WebConstants.CURRENT_USER, user);
				session.setAttribute("userId", user.getId());

				// request.getRequestDispatcher("/admin/home/main").forward(request,
				// response);
				response.sendRedirect("/admin/home/main");
			} catch (Exception e) {
				e.printStackTrace();
				result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			}
		} else {
			result = new ResponseResult(ResponseStatus.PARAM_ERROR, "用户名或密码为空.");
		}
		return result;
	}

	@RequestMapping(value = "studentLogin", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseResult studentLogin(String userInfo, HttpServletRequest request, HttpServletResponse response) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(userInfo)) {
			try {
				String userInfos[] = EncryptUtils.decrypt(userInfo).split(",");
				String userName = userInfos[0];
				String password = userInfos[1];
				GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(userName);
				if (user.getPassword().equalsIgnoreCase(password)) {
					GjtStudentInfo studentInfo = user.getGjtStudentInfo();// gjtStudentInfoService.queryById(id);

					String url = gjtUserAccountService.studentSimulation(studentInfo.getStudentId(),
							studentInfo.getXh());
					response.sendRedirect(url);
					// return "redirect:" + url; // 修改完重定向
				} else {
					result = new ResponseResult(ResponseStatus.FAIL, "密码错误.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			}
		} else {
			result = new ResponseResult(ResponseStatus.PARAM_ERROR, "用户名或密码不能为空.");
		}

		return result;
	}

	@RequestMapping(value = "headTeacherLogin", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseResult headTeacherLogin(String userInfo, HttpServletRequest request, HttpServletResponse response) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(userInfo)) {
			try {
				String userInfos[] = EncryptUtils.decrypt(userInfo).split(",");
				String userName = userInfos[0];
				String password = userInfos[1];
				GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(userName);
				if (user.getPassword().equalsIgnoreCase(password)) {
					GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByAccountId(user.getId());

					String url = gjtUserAccountService.headTeacherSimulation(user.getLoginAccount(),
							employeeInfo.getEmployeeId(), "");
					response.sendRedirect(url);
				} else {
					result = new ResponseResult(ResponseStatus.FAIL, "密码错误.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			}
		} else {
			result = new ResponseResult(ResponseStatus.PARAM_ERROR, "用户名或密码不能为空.");
		}

		return result;
	}

	@RequestMapping(value = "teacherLogin", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public ResponseResult teacherLogin(String userInfo, HttpServletRequest request, HttpServletResponse response) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(userInfo)) {
			try {
				String userInfos[] = EncryptUtils.decrypt(userInfo).split(",");
				String userName = userInfos[0];
				String password = userInfos[1];
				GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(userName);
				if (user == null) {
					throw new Exception(userName + "用户不存在.");
				}
				GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryByAccountId(user.getId());

				if ("2".equals(employeeInfo.getEmployeeType())) { // 辅导教师

					String url = gjtUserAccountService.coachTeacherSimulation(employeeInfo.getEmployeeId());
					response.sendRedirect(url);

				} else if ("4".equals(employeeInfo.getEmployeeType())) { // 督导教师
					List<Object[]> classInfoList = gjtClassInfoService
							.findClassIdANDTermcourseId(employeeInfo.getEmployeeId(), "course");
					if (classInfoList != null && classInfoList.size() > 0) {
						for (Object[] object : classInfoList) {
							String termcourseId = ObjectUtils.toString(object[1]);
							String classId = ObjectUtils.toString(object[0]);
							if (StringUtils.isNotEmpty(termcourseId)) {
								// GjtTeachPlan teachPlan =
								// gjtTeachPlanService.findOne(teachPlanId);
								// if (teachPlan != null) {
								String url = gjtUserAccountService.supervisorTeacherSimulation(termcourseId, classId,
										employeeInfo.getEmployeeId());
								response.sendRedirect(url);
								break;
								// }
							}
						}
					}
				} else {
					result = new ResponseResult(ResponseStatus.FAIL, userName + "不是辅导教师");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			}
		}
		return result;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult save(HttpServletRequest request) {
		ResponseResult result;
		try {
			String loginAccount = request.getParameter("loginAccount");
			String realName = request.getParameter("realName");
			String orgCode = request.getParameter("orgCode");// 创建学习中心就传学习中间的orgId,招生点就招生点orgId
			String mobilePhone = request.getParameter("mobilePhone");
			String roleId = request.getParameter("roleId");

			String email = request.getParameter("email");
			String password = request.getParameter("password");

			if (StringUtils.isBlank(loginAccount) || StringUtils.isBlank(realName) || StringUtils.isBlank(orgCode)
					|| StringUtils.isBlank(mobilePhone) || StringUtils.isBlank(roleId)) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "必填参数有为空的，请检查!");
			}

			if (null != gjtUserAccountService.queryByLoginAccount(loginAccount)) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "帐号已存在!");
			}

			GjtOrg org = gjtOrgService.queryByCode(orgCode);
			if (org == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "根据orgCode查询不到对应的机构!");
			}
			if (org.getGjtStudyCenter() == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "教务平台数据不完整，请联系教务平台人员");
			}

			if (!"1".equals(org.getGjtStudyCenter().getAuditStatus())) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心或者招生点未审核通过，或者待审核中，不能创建用户!");
			}

			PriRoleInfo priRoleInfo = priRoleInfoService.queryById(roleId);
			if (priRoleInfo == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "根据roleId查询不到对应的角色!");
			}

			GjtUserAccount user = new GjtUserAccount();
			String realmName = request.getRequestURI();
			user.setCreatedBy(realmName);

			user.setPriRoleInfo(priRoleInfo);
			user.setPassword2(StringUtils.isBlank(password) ? "888888" : password);
			user.setLoginAccount(loginAccount);
			user.setRealName(realName);
			user.setSjh(mobilePhone);
			user.setEmail(email);
			user.setPriRoleInfo(priRoleInfo);
			user.setGjtOrg(org);

			gjtUserAccountService.insert(user);
			result = new ResponseResult(ResponseStatus.SUCCESS, user.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
		}
		return result;
	}

	// @RequestMapping(value = "findRole", method = RequestMethod.GET)
	// @ResponseBody
	// public ResponseResult findRole() {
	// Map<String, String> dates = commonMapService.getDates("RecruitInfo");
	// List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	// for (Map.Entry<String, String> map : dates.entrySet()) {
	// Map<String, String> item = new HashMap<String, String>();
	// item.put("roleId", map.getKey());
	// item.put("roleName", map.getValue());
	// list.add(item);
	// }
	// ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS, list);
	// return result;
	// }

	@RequestMapping(value = "checkUserAndPhpUser", method = RequestMethod.GET)
	@ResponseBody
	public ResponseResult checkUserAndPhpUser(String loginAccount) {
		ResponseResult result = null;
		if (StringUtils.isNotEmpty(loginAccount)) {
			GjtUserAccount user = gjtUserAccountService.queryByLoginAccount(loginAccount);
			if (user == null) { // 教务平台不存在，然后验证应用平台
				// 然后在检测PHP部门的帐号是否存在
				result = new ResponseResult(ResponseStatus.SUCCESS, loginAccount + "帐号不存在!");
			} else {
				result = new ResponseResult(ResponseStatus.FAIL, loginAccount + "帐号已存在!");
			}
		} else {
			result = new ResponseResult(ResponseStatus.PARAM_ERROR, "参数loginAccount不能为空!");
		}
		return result;
	}

	@RequestMapping(value = "updatePwd", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult updatePwd(String loginAccount, String oldPwd, String newPwd) {
		ResponseResult result = null;

		if (StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd) || StringUtils.isBlank(loginAccount)) {
			return new ResponseResult(ResponseStatus.FAIL, "存在必填参数为空！");
		}

		GjtUserAccount userAccount = gjtUserAccountService.queryByLoginAccount(loginAccount);
		if (userAccount == null) {
			return new ResponseResult(ResponseStatus.FAIL, loginAccount + "查询不到帐号!");
		}
		if (!userAccount.getPassword2().equals(oldPwd)) {
			return new ResponseResult(ResponseStatus.FAIL, "原密码不匹配！");
		}
		try {
			// 教学教务修改密码
			gjtUserAccountService.updatePwd(userAccount.getId(), Md5Util.encode(newPwd), newPwd);
			// 应用平台修改密码
			result = new ResponseResult(ResponseStatus.SUCCESS, "密码修改成功！");
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, "修改失败！");
		}

		return result;
	}

}
