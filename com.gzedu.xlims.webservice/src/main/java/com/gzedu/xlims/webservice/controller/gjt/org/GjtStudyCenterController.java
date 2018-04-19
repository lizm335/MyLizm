/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.webservice.controller.gjt.org;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.HttpClientUtils;
import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtStudyCenter;
import com.gzedu.xlims.pojo.GjtStudyCenterAudit;
import com.gzedu.xlims.service.CommonMapService;
import com.gzedu.xlims.service.organization.GjtOrgService;
import com.gzedu.xlims.service.organization.GjtStudyCenterAuditService;
import com.gzedu.xlims.service.organization.GjtStudyCenterService;
import com.gzedu.xlims.webservice.response.ResponseResult;
import com.gzedu.xlims.webservice.response.ResponseStatus;

import net.sf.json.JSONObject;

/**
 * 功能说明：学习中心对外接口
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年8月1日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/interface/gjtStudyCenter")
public class GjtStudyCenterController {

	private final static Logger log = LoggerFactory.getLogger(GjtStudyCenterController.class);

	@Autowired
	private GjtOrgService gjtOrgService;

	@Autowired
	private GjtStudyCenterService gjtStudyCenterService;

	@Autowired
	private GjtStudyCenterAuditService gjtStudyCenterAuditService;

	@Autowired
	private CommonMapService commonMapService;

	// 审核接口
	// @ResponseBody
	// @RequestMapping(value = "audit", method = RequestMethod.POST)
	// public ResponseResult audit(String studyCode, String audit) {
	//
	// ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS,
	// "修改成功！");
	//
	// if (StringUtils.isBlank(studyCode) || StringUtils.isBlank(audit)) {
	// return new ResponseResult(ResponseStatus.PARAM_ERROR, "有必填参数为空！");
	// }
	//
	// GjtOrg gjtOrg = gjtOrgService.queryByCode(studyCode);
	// if (gjtOrg == null) {
	// return new ResponseResult(ResponseStatus.PARAM_ERROR,
	// "根据studyCode查询不到学习中心或者招生点！");
	// }
	//
	// try {
	// boolean status = gjtStudyCenterService.updateAudit(gjtOrg.getId(),
	// audit);
	// if (!status) {
	// result = new ResponseResult(ResponseStatus.FAIL, studyCode);
	// }
	// } catch (Exception e) {
	// result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
	// log.error(e.getMessage(), e);
	// }
	// return result;
	// }

	// 启用接口
	// @ResponseBody
	// @RequestMapping(value = "startUsing", method = RequestMethod.POST)
	// public ResponseResult startUsing(String studyCode, String status) {
	// ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS,
	// "修改成功！");
	//
	// if (StringUtils.isBlank(studyCode) || StringUtils.isBlank(status)) {
	// return new ResponseResult(ResponseStatus.PARAM_ERROR, "有必填参数为空！");
	// }
	//
	// GjtOrg gjtOrg = gjtOrgService.queryByCode(studyCode);
	// if (gjtOrg == null) {
	// return new ResponseResult(ResponseStatus.PARAM_ERROR,
	// "根据studyCode查询不到学习中心或者招生点！");
	// }
	//
	// try {
	// boolean bool = gjtStudyCenterService.updateStatus(gjtOrg.getId(),
	// status);
	// if (!bool) {
	// result = new ResponseResult(ResponseStatus.FAIL, studyCode);
	// }
	// } catch (Exception e) {
	// result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
	// log.error(e.getMessage(), e);
	// }
	// return result;
	// }

	@ResponseBody
	@RequestMapping(value = "addStudyCenter", method = RequestMethod.POST)
	public ResponseResult addStudyCenter(HttpServletRequest request, String schoolCode, String studyCode) {
		ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS, "新增成功！");
		GjtOrg schoolOrg = null;
		GjtOrg studyOrg = null;

		String orgType = request.getParameter("orgType");// 机构类型
		String scCode = request.getParameter("scCode");// 机构编码
		String scName = request.getParameter("scName");// 机构名称
		String officeAddr = request.getParameter("officeAddr");// 机构所在地址
		String linkTel = request.getParameter("linkTel");// 联系电话
		String linkman = request.getParameter("linkman");// 联系人
		String district = request.getParameter("district");// 区域编码
		String memo = request.getParameter("memo");// 备注
		String serversType = request.getParameter("serversType");// 服务项
		String pactNo = request.getParameter("pactNo");// 合同编号

		String createOrgByName = request.getParameter("createOrgByName");// 创建机构的人名
		String createOrgDate = request.getParameter("createOrgDate");// 创建机构时间
		String auditName = request.getParameter("auditName");// 审批人
		String auditDate = request.getParameter("auditDate");// 审批时间
		String auditContent = request.getParameter("auditContent");// 审批内容

		if (StringUtils.isBlank(orgType) || StringUtils.isBlank(scCode) || StringUtils.isBlank(scName)
				|| StringUtils.isBlank(officeAddr) || StringUtils.isBlank(linkTel) || StringUtils.isBlank(linkman)
				|| StringUtils.isBlank(createOrgByName) || StringUtils.isBlank(auditName)
				|| StringUtils.isBlank(createOrgDate) || StringUtils.isBlank(auditDate)
				|| StringUtils.isBlank(serversType) || StringUtils.isBlank(pactNo)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "有必填参数为空！");
		}

		StringBuffer serviceArea = new StringBuffer();// 不要信任其他平台传来的参数
		String[] serversTypes = serversType.split(",");
		for (int i = 0; i < serversTypes.length; i++) {
			serviceArea.append(serversTypes[i]);
			if (serversTypes.length - 1 > i) {
				serviceArea.append(",");
			}
		}

		Date createOrgDates;
		Date auditDates;
		try {
			createOrgDates = DateUtils.getDateToString(createOrgDate);
			auditDates = DateUtils.getDateToString(auditDate);
		} catch (Exception e) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "时间格式错误，正确格式：1990-01-01 12:00:00");
		}

		try {
			GjtOrg gjtOrg = gjtOrgService.queryByCode(scCode);
			if (gjtOrg != null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心或者招生点已存在！");
			}
			if ("3".equals(orgType)) {// 新增学习中心
				// 根据院校Code拿到院校信息
				schoolOrg = gjtOrgService.queryByCode(schoolCode);
				if (schoolOrg == null || !"1".equals(schoolOrg.getOrgType())) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "院校Code填写错误，或不存在");
				}
			}
			if ("6".equals(orgType)) {// 新增招生点
				// 根据学习中心Code拿到学习中心信息
				studyOrg = gjtOrgService.queryByCode(studyCode);
				if (studyOrg == null || !"3".equals(studyOrg.getOrgType())) {
					return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心Code填写错误，或不存在");
				}
			}
			GjtStudyCenter studyCenter = new GjtStudyCenter();
			studyCenter.setGjtOrg(new GjtOrg());
			studyCenter.getGjtOrg().setOrgType(orgType);// 机构类型
			if ("3".equals(orgType)) {
				studyCenter.getGjtOrg().setParentGjtOrg(schoolOrg); // 所属院校
			} else {
				studyCenter.getGjtOrg().setParentGjtOrg(studyOrg); // 所属学习中心
			}

			String id = UUIDUtils.random().toString();
			studyCenter.setId(id);// id
			studyCenter.setScCode(scCode); // 学习中心代码
			studyCenter.setScName(scName); // 学习中心名称
			studyCenter.setCenterType(orgType); // 中心类型
			studyCenter.setOfficeAddr(officeAddr); // 学习中心地址
			studyCenter.setLinkTel(linkTel); // 联系电话
			studyCenter.setLinkman(linkman); // 联系人
			studyCenter.setDistrict(district); // 所在地行政区
			studyCenter.setMemo(memo); // 描述
			studyCenter.setCreatedBy(request.getRequestURI());// 创建者
			studyCenter.setServiceArea(serviceArea.toString());
			studyCenter.setCompactNo(pactNo);

			boolean insert = gjtStudyCenterService.insert(studyCenter);
			if (!insert) {
				result = new ResponseResult(ResponseStatus.FAIL, "发生未知错误，新增不成功！");
			} else {
				// 创建审核记录
				GjtStudyCenterAudit item = new GjtStudyCenterAudit();
				item.setId(UUIDUtils.random().toString());
				item.setAuditContent(StringUtils.isBlank(auditContent) ? "同意！" : auditContent);
				item.setAuditDt(auditDates);
				item.setAuditOperator(auditName);
				item.setAuditOperatorRole("招生管理员");
				item.setAuditState("1");
				item.setAuditStep("1");// 进度
				item.setCreatedBy(createOrgByName);
				item.setCreatedDt(createOrgDates);
				item.setIncidentId(id);
				item.setIsDeleted("N");
				item.setVersion(new BigDecimal(1));
				gjtStudyCenterAuditService.save(item);
			}
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			log.error(e.getMessage(), e);
		}
		return result;
	}

	// protected ResponseResult addUser(GjtStudyCenter studyCenter,
	// HttpServletRequest request) {
	//
	// String loginAccount = request.getParameter("loginAccount");
	// String realName = request.getParameter("realName");
	// String mobilePhone = request.getParameter("mobilePhone");
	//
	// String roleId = "7ab10371c2f040df8289b09cde4b9510";// 学习中心学支管理员
	//
	// String email = request.getParameter("email");
	// String password = request.getParameter("password");
	//
	// if (StringUtils.isBlank(loginAccount) || StringUtils.isBlank(realName) ||
	// StringUtils.isBlank(mobilePhone)
	// || StringUtils.isBlank(roleId)) {
	// return new ResponseResult(ResponseStatus.PARAM_ERROR, "必填参数有为空的，请检查!");
	// }
	//
	// if (null != gjtUserAccountService.queryByLoginAccount(loginAccount)) {
	// return new ResponseResult(ResponseStatus.PARAM_ERROR, "帐号已存在!");
	// }
	//
	// // 先推送给应用平台，PHP部门
	//
	// // // 成功以后在教学平台创建
	// PriRoleInfo priRoleInfo = new PriRoleInfo(roleId);
	// GjtUserAccount user = new GjtUserAccount();
	// try {
	// String realmName = request.getRequestURI();
	// user.setCreatedBy(realmName);
	// user.setPriRoleInfo(priRoleInfo);
	// user.setPassword2(StringUtils.isBlank(password) ? "888888" : password);
	// user.setLoginAccount(loginAccount);
	// user.setRealName(realName);
	// user.setSjh(mobilePhone);
	// user.setEmail(email);
	// user.setPriRoleInfo(priRoleInfo);
	// user.setGjtOrg(studyCenter.getGjtOrg());
	// gjtUserAccountService.insert(user);
	// } catch (Exception e) {
	// log.error(e.getMessage(), e);
	// return new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
	// }
	// return new ResponseResult(ResponseStatus.SUCCESS, user.getId());
	// }

	@ResponseBody
	@RequestMapping(value = "againAudit", method = RequestMethod.POST)
	public ResponseResult againAudit(HttpServletRequest request) {
		ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS, "重新提交审核成功！");
		String scCode = request.getParameter("scCode");// 机构编码
		String auditName = request.getParameter("auditName");// 审批人
		String auditDate = request.getParameter("auditDate");// 审批时间
		String auditContent = request.getParameter("auditContent");// 审批内容

		if (StringUtils.isBlank(scCode) || StringUtils.isBlank(auditName) || StringUtils.isBlank(auditDate)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "有必填参数为空！");
		}

		GjtOrg gjtOrg = gjtOrgService.queryByCode(scCode);
		if (gjtOrg == null) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "学习中心或者招生点不存在");
		}

		Date auditDates;
		try {
			auditDates = DateUtils.getDateToString(auditDate);
		} catch (Exception e) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "时间格式错误，正确格式：1990-01-01 12:00:00");
		}

		try {
			GjtStudyCenterAudit item = new GjtStudyCenterAudit();
			item.setId(UUIDUtils.random().toString());
			item.setAuditContent(StringUtils.isBlank(auditContent) ? "同意！" : auditContent);
			item.setAuditDt(auditDates);
			item.setAuditOperator(auditName);
			item.setAuditOperatorRole("招生管理员");
			item.setAuditState("1");
			item.setAuditStep("1");// 进度
			item.setCreatedBy(auditName);
			item.setCreatedDt(auditDates);
			item.setIncidentId(gjtOrg.getId());
			item.setIsDeleted("N");
			item.setVersion(new BigDecimal(1));
			gjtStudyCenterAuditService.save(item);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "queryServiceAreas", method = RequestMethod.GET)
	public ResponseResult queryServiceAreas(HttpServletRequest request, String schoolCode, String studyCode) {
		List<Map<String, Object>> dateList = commonMapService.getDateList("StudyServiceInfo");
		ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS, dateList);
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "updateStudyCenter", method = RequestMethod.POST)
	public ResponseResult updateStudyCenter(HttpServletRequest request, String schoolCode, String studyCode) {
		ResponseResult result = new ResponseResult(ResponseStatus.SUCCESS, "修改成功！");
		// GjtOrg studyOrg = null;

		String scCode = request.getParameter("scCode");
		String newScCode = request.getParameter("newScCode");
		String scName = request.getParameter("scName");
		String officeAddr = request.getParameter("officeAddr");
		String linkTel = request.getParameter("linkTel");
		String linkman = request.getParameter("linkman");
		String district = request.getParameter("district");
		String memo = request.getParameter("memo");

		String serversType = request.getParameter("serversType");// 服务项
		String pactNo = request.getParameter("pactNo");// 合同编号

		if (StringUtils.isBlank(scCode) || StringUtils.isBlank(scName) || StringUtils.isBlank(officeAddr)
				|| StringUtils.isBlank(linkTel) || StringUtils.isBlank(linkman) || StringUtils.isBlank(serversType)
				|| StringUtils.isBlank(pactNo)) {
			return new ResponseResult(ResponseStatus.PARAM_ERROR, "有必填参数为空！");
		}

		StringBuffer serviceArea = new StringBuffer();// 不要信任其他平台传来的参数
		String[] serversTypes = serversType.split(",");
		for (int i = 0; i < serversTypes.length; i++) {
			serviceArea.append(serversTypes[i]);
			if (serversTypes.length - 1 > i) {
				serviceArea.append(",");
			}
		}

		try {

			GjtOrg gjtOrg = gjtOrgService.queryByCode(scCode);
			if (gjtOrg == null) {
				return new ResponseResult(ResponseStatus.PARAM_ERROR, "根据Code查询不到学习中心或招生点" + scCode);
			}

			// if ("6".equals(gjtOrg.getOrgType())) {// 新增招生点 -会影响分班
			// // 根据学习中心Code拿到学习中心信息
			// studyOrg = gjtOrgService.queryByCode(studyCode);
			// if (studyOrg == null || !"3".equals(studyOrg.getOrgType())) {
			// return new ResponseResult(ResponseStatus.PARAM_ERROR,
			// "学习中心Code填写错误，或不存在");
			// }
			// }

			GjtStudyCenter studyCenter = gjtOrg.getGjtStudyCenter();
			// if ("6".equals(gjtOrg.getOrgType())) {
			// studyCenter.getGjtOrg().setParentGjtOrg(studyOrg); // 所属学习中心
			// }
			if (StringUtils.isNotBlank(newScCode)) {// 学习中心代码
				studyCenter.setScCode(newScCode);
			}
			studyCenter.setScName(scName); // 学习中心名称
			studyCenter.setOfficeAddr(officeAddr); // 学习中心地址
			studyCenter.setLinkTel(linkTel); // 联系电话
			studyCenter.setLinkman(linkman); // 联系人
			studyCenter.setDistrict(district); // 所在地行政区
			studyCenter.setMemo(memo); // 描述
			studyCenter.setUpdatedBy(request.getRequestURI());
			studyCenter.setCompactNo(pactNo);
			studyCenter.setServiceArea(serviceArea.toString());

			GjtOrg org = studyCenter.getGjtOrg();
			org.setOrgName(scName);
			org.setCode(studyCenter.getScCode());
			org.setParentGjtOrg(studyCenter.getGjtOrg().getParentGjtOrg());
			org.setSchoolModel(studyCenter.getGjtOrg().getParentGjtOrg().getSchoolModel()); // 办学模式和父机构相同
			org.setUpdatedDt(DateUtils.getNowTime());
			org.setUpdatedBy(studyCenter.getUpdatedBy());

			// 成功以后在修改教学教务的
			boolean insert = gjtStudyCenterService.updateEntity(studyCenter);
			if (!insert) {
				result = new ResponseResult(ResponseStatus.FAIL, "发生未知错误，新增不成功！");
			}
		} catch (Exception e) {
			result = new ResponseResult(ResponseStatus.UNKNOW_ERROR, e.getMessage());
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public static void main(String[] args) {
		check();
		tuisong();
	}

	public static void tuisong() {
		// 先推送给应用平台，PHP部门
		String url = "http://test.oucnet.cn/api/v1/space/create";

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("data[username]", "test0003");
		params.put("data[password]", "888888");
		params.put("data[project_code]", "study003");
		params.put("data[groupid]", "19");
		params.put("data[phone]", "13926402025");
		params.put("data[email]", "54545@163.com");
		System.out.println("新增请求参数：" + params);
		String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
		// System.out.println(result);
		if (StringUtils.isNotEmpty(result)) {
			JSONObject json = JSONObject.fromObject(result);
			System.out.println(json);
			Object code = json.get("code");
			Integer i = Integer.valueOf(code.toString());
			if (i == 200) {
				JSONObject json2 = JSONObject.fromObject(json.get("data"));
			}
		}
	}

	public static String check() {
		// 先推送给应用平台，PHP部门
		String url = "http://test.oucnet.cn/api/v1/space/check";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("data[username]", "test0003");
		System.out.println("查询参数：" + params);
		String result = HttpClientUtils.doHttpPost(url, params, 3000, "utf-8");
		if (StringUtils.isNotEmpty(result)) {
			JSONObject json = JSONObject.fromObject(result);
			System.out.println(json);
		}
		return result;
	}

}
