
package com.ouchgzee.study.web.controller.educational;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.gzedu.xlims.common.EncryptUtils;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.dao.Collections3;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtOrg;
import com.gzedu.xlims.pojo.GjtSignup;
import com.gzedu.xlims.pojo.GjtSpecialty;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.remote.BillRemoteService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.ouchgzee.study.web.vo.SignInfoVo;

@Controller
@RequestMapping("/pcenter/sign")
public class SignInfoController {

	private static final Logger log = LoggerFactory.getLogger(SignInfoController.class);

	@Autowired
	private BillRemoteService billRemoteService;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;
	@Autowired
	private GjtSignupService gjtSignupService;

	@Value("#{configProperties['eeChatInterface']}")
	private String eeServer;

	@Autowired
	private GjtEmployeeInfoService gjtEmployeeInfoService;

	@Autowired
	private CacheService cacheService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "querySignInfo")
	@ResponseBody
	public SignInfoVo querySignInfo(HttpServletRequest request) throws CommonException {
		SignInfoVo vo = new SignInfoVo();
		GjtUserAccount sessionUser = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);

		GjtStudentInfo student = gjtStudentInfoService.queryById(sessionUser.getGjtStudentInfo().getStudentId());
		if (student != null) {
			GjtOrg gjtOrg = student.getGjtSchoolInfo().getGjtOrg();
			GjtGrade grade = student.getGjtGrade();
			GjtSpecialty specialty = student.getGjtSpecialty();

			/** 报读信息 */
			// 报读时间created_dt 报读时间
			vo.setCreatedDt(com.gzedu.xlims.common.DateUtils.getTimeYM(student.getCreatedDt()));
			// 院校
			if (gjtOrg != null) {
				vo.setSchoolName(gjtOrg.getOrgName());
			}
			if (grade != null) {
				// 年级 className
				vo.setClassName(grade.getGradeName());
			}
			if (specialty != null) {
				// 专业
				vo.setSpecialty(specialty.getZymc());
				// 层次PYCC
				// vo.setPycc(specialty.getPycc());
				// TODO
				vo.setPycc(cacheService.getCachedDictionaryName("TrainingLevel", specialty.getPycc()));
				// 学习方式(2.5年制)-学籍
				vo.setStudyPeriod(specialty.getStudyPeriod());
			}
			// 姓名xm 姓 名
			vo.setXm(student.getXm());
			// 证件类型certificatetype
			vo.setCertificatetype(student.getCertificatetype());
			// 证件号 sfzh 证件号码
			vo.setSfzh(student.getSfzh());
			// 手机号sjh 手机号码
			vo.setSjh(student.getSjh());
			// 邮箱
			vo.setDzxx(student.getDzxx());
			// 服务机构
			if (gjtOrg != null) {
				vo.setOrgName(gjtOrg.getOrgName());
			}
			// 报读状态
			vo.setEnrollStatus("2".equals(student.getGjtSignup().getCharge()) ? "未缴费" : "报读成功");
			// 学籍状态xjzt
			vo.setXjzt(student.getXjzt());

			Map<String, Object> employeeMap = new HashMap<String, Object>();
			employeeMap = gjtEmployeeInfoService.queryHeadTeacherInfo(student.getStudentId());
			String employeeId = (String) employeeMap.get("employeeId");
			String eeUrl = "班主任ID不对";
			if (StringUtils.isNotEmpty(employeeId)) {
				eeUrl = eeServer + "/openChat.do?data=" + EncryptUtils
						.encrypt("{\"USER_ID\":\"" + student.getStudentId() + "\",\"TO_ID\":\"" + employeeId + "\"}");// 旧的接口是传这样的参数
				vo.setEeUrl(eeUrl);
			}
			/** 学费与优惠政策信息 */
			Map<String, Object> policyMap = Maps.newHashMap();
			vo.setPolicyMap(policyMap);

			GjtSignup signup = gjtSignupService.querySignupByStudentId(student.getStudentId());
			String orderSN = "";
			if (signup != null) {
				orderSN = signup.getOrderSn();
			}
			// orderSN = "20170331305426";
			if (!Strings.isNullOrEmpty(orderSN)) {
				try {
					log.info(student.getStudentId() + "查询订单详情信息,订单号:" + orderSN);
					// 调用订单详情接口
					Map<String, Object> orderDetailMap = billRemoteService.getOrderDetail(orderSN);
					// System.out.println(orderDetailMap.get("ORDER_TOTAL_AMT")+":应收总金额");
					// System.out.println(orderDetailMap.get("ORDER_AMT")+":实收总金额");
					// 缴费状态:正常
					policyMap.put("PAY_STATUS_NAME", orderDetailMap.get("PAY_STATUS_NAME"));
					// 缴费方式
					policyMap.put("GKXL_PAYMENT_TYPE", orderDetailMap.get("GKXL_PAYMENT_TYPE"));
					// 所属政策ID
					String policyId = (String) orderDetailMap.get("POLICY_ID");
					log.info(student.getStudentId() + "政策ID:" + policyId);
					// 产品ID
					String productId = (String) orderDetailMap.get("PRODUCT_ID");
					log.info(student.getStudentId() + "产品ID:" + productId);
					// 政策明细查询接口
					if (!Strings.isNullOrEmpty(policyId)) {
						Map<String, Object> policyDetailMap = billRemoteService.getPolicyDetail(policyId);
						List<Map<String, Object>> productList = (List<Map<String, Object>>) policyDetailMap
								.get("PRODUCTLIST");
						if (productList == null) {
							return vo;
						}
						for (Map<String, Object> productMap : productList) {
							String pId = (String) productMap.get("PRODUCT_ID");
							if (ObjectUtils.equals(pId, productId)) {
								policyMap.put("SUBSIDY_RANGE", productMap.get("SUBSIDY_RANGE")); // 减价幅度1000
								policyMap.put("PRODUCT_PRICE", productMap.get("PRODUCT_PRICE")); // 产品原价8600
								policyMap.put("SUBSIDY_PRICE", productMap.get("SUBSIDY_PRICE")); // 优惠价格7600
								policyMap.put("POLICY_NAME", productMap.get("POLICY_NAME")); // 政策名称/优惠政策:报读立减700，送PAD一台
								List<Map<String, Object>> largessList = (List<Map<String, Object>>) productMap
										.get("LARGESSLIST");
								if (Collections3.isNotEmpty(largessList)) {
									Map largessMap = largessList.get(0);
									policyMap.put("largess_id", productMap.get("LARGESS_ID"));// 赠品ID
									policyMap.put("largess_name", productMap.get("LARGESS_NAME "));// 赠品名称/赠送物品:已赠送PAD一台
								}
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			throw new CommonException(MessageCode.BIZ_ERROR, "studentInfo学籍信息错误!");
		}
		return vo;
	}

}
