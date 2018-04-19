/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.constants.OrgUtil;
import com.gzedu.xlims.dao.transaction.GjtSchoolRollTranDao;
import com.gzedu.xlims.pojo.GjtSchoolRollTran;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.TextbookAddressComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录之后的状态控制器<br/>
 * 功能说明：<br/>
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年04月19日
 * @version 2.5
 */
@RestController
@RequestMapping("/pcenter")
public class StatusController extends BaseController {

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	
	@Autowired
	private GjtSchoolRollTranDao gjtSchoolRollTranDao;

	@Autowired
	private TextbookAddressComponent textbookAddressComponent;

	/**
	 * 学员登录后获取相关状态<br>
	 * 返回格式：{"type": "0", "content": {...}}<br>
	 * type的描述：0-审核状态类型 1-逾期状态类型 2-已申请退学 4-休学 5-退学 6-教材地址未确认<br>
	 * @return
	 */
	@RequestMapping(value = "getStatus", method = RequestMethod.GET)
	public Map<String, Object> getStatus(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		boolean flag = false; // 标记以下的某一种情况
		if ("4".equals(user.getGjtStudentInfo().getXjzt())) {
			resultMap.put("type", 4);
			flag = true;
		} else if ("5".equals(user.getGjtStudentInfo().getXjzt())) {
			resultMap.put("type", 5);
			flag = true;
		} else {
			//查询学员是否申请过退学
			List<GjtSchoolRollTran> rollTran = gjtSchoolRollTranDao.queryGjtSchoolRollTran(user.getGjtStudentInfo().getStudentId(), 4, Constants.BOOLEAN_NO);
			if (rollTran != null && rollTran.size() > 0) {
				String isApplyFor = rollTran.get(0).getIsApplyFor();
				if (!"0".equals(isApplyFor) && !flag) {
					resultMap.put("type", 2); //已申请
					flag = true;
				}
			}
		}
		if(!flag) {
			Map<String, Object> payOverdueStatusMap = gjtStudentInfoService.getPayOverdueStatus(user.getGjtStudentInfo().getStudentId());
			if ("0".equals(payOverdueStatusMap.get("isOverdue") + "")) {
				Map<String, Object> auditInfoMap = gjtStudentInfoService.getAuditInfo(user.getGjtStudentInfo().getStudentId());
				resultMap.put("type", 0);
				resultMap.put("content", auditInfoMap);

				// 已审核通过可以进入学习
				if (Constants.BOOLEAN_1.equals(auditInfoMap.get("auditState"))) {
				}
			} else {
				resultMap.put("type", 1);
				payOverdueStatusMap.put("payLink", OrgUtil.getOucnetDomainHost(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode()) + "/home#/user-order");
				resultMap.put("content", payOverdueStatusMap);
			}
		}
		return resultMap;
	}

	/**
	 * 学员登录后获取相关状态<br>
	 * 返回格式：{"type": "0", "content": {...}}<br>
	 * type的描述：0-审核状态类型 1-逾期状态类型 2-已申请退学 4-休学 5-退学 6-教材地址未确认<br>
	 * @return
	 */
	@RequestMapping(value = "getStatusByPC", method = RequestMethod.GET)
	public Map<String, Object> getStatusByPC(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		GjtStudentInfo studentInfo = gjtStudentInfoService.queryById(user.getGjtStudentInfo().getStudentId());
		boolean flag = false; // 标记以下的某一种情况
		if ("4".equals(user.getGjtStudentInfo().getXjzt())) {
			resultMap.put("type", 4);
			flag = true;
		} else if ("5".equals(user.getGjtStudentInfo().getXjzt())) {
			resultMap.put("type", 5);
			flag = true;
		} else {
			//查询学员是否申请过退学
			List<GjtSchoolRollTran> rollTran = gjtSchoolRollTranDao.queryGjtSchoolRollTran(user.getGjtStudentInfo().getStudentId(), 4, Constants.BOOLEAN_NO);
			if (rollTran != null && rollTran.size() > 0) {
				String isApplyFor = rollTran.get(0).getIsApplyFor();
				if (!"0".equals(isApplyFor) && !flag) {
					resultMap.put("type", 2); //已申请
					flag = true;
				}
			}
		}
		if(!flag) {
			Map<String, Object> payOverdueStatusMap = gjtStudentInfoService.getPayOverdueStatus(user.getGjtStudentInfo().getStudentId());
			if ("0".equals(payOverdueStatusMap.get("isOverdue") + "")) {
				Map<String, Object> auditInfoMap = gjtStudentInfoService.getAuditInfo(user.getGjtStudentInfo().getStudentId());
				resultMap.put("type", 0);
				resultMap.put("content", auditInfoMap);

				// 已审核通过可以进入学习
				if (Constants.BOOLEAN_1.equals(auditInfoMap.get("auditState"))) {
					// 判断教材是否确认
					boolean isConfirm = textbookAddressComponent.getCached(user.getGjtStudentInfo().getStudentId());
					if (!isConfirm && !flag) {
						resultMap.put("type", 6);
					}
				}
			} else {
				resultMap.put("type", 1);
				payOverdueStatusMap.put("payLink", OrgUtil.getOucnetDomainHost(studentInfo.getGjtSchoolInfo().getGjtOrg().getCode()) + "/home#/user-order");
				resultMap.put("content", payOverdueStatusMap);
			}
		}
		return resultMap;
	}

	/**
	 * 入学通知书确认<br>
	 * @return
	 */
	@RequestMapping(value = "confirm", method = RequestMethod.GET)
	public void confirm(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		gjtStudentInfoService.confirm(user.getGjtStudentInfo().getStudentId());
	}

	/**
	 * 教材地址确认<br>
	 * @return
	 */
	@RequestMapping(value = "tbaConfirm", method = RequestMethod.GET)
	public void tbaConfirm(HttpServletRequest request) {
		GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
		textbookAddressComponent.setCached(user.getGjtStudentInfo().getStudentId(), true);
	}
	
}
