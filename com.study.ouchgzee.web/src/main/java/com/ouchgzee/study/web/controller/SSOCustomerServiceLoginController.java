/**
 * Copyright(c) 2017 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.FormSubmitUtil;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.pojo.GjtClassInfo;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.service.organization.GjtClassStudentService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;
import com.gzedu.xlims.third.moor.MoorApiUtil;
import com.ouchgzee.study.web.common.BaseController;
import com.ouchgzee.study.web.common.Servlets;

/**
 * 单点登录控制器<br/>
 * 功能说明：<br/>
 * 		对接移动端的客服系统，查看学员个人详情<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年11月27日
 * @version 3.0
 */
@Controller
@RequestMapping("/sso")
public class SSOCustomerServiceLoginController extends BaseController {

	private static final Log log = LogFactory.getLog(SSOCustomerServiceLoginController.class);

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtClassStudentService gjtClassStudentService;

	/**
	 * 新单点登录接口-跳转到PC学员个人详情<br>
	 * @param qimoClientId 学员ID或微信openId
	 * @param phone 手机号
	 * @param token 客服系统访问地址时携带token的值
	 * @param tokenId 客服系统访问地址时携带tokenId的值
	 * @return
	 */
	@RequestMapping(value = "/signonToStuInfo")
	public ModelAndView signonToStuInfo(String sign, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
	    // {displayName=欧蓉, exten=8002, qimoClientId=oSf0Ms2Y_03WkILa_IelCbJY-5L8, qimoClientName=阿涛, tabType=webchat, token=536e13f82c632d2cc1653afd539f0636, tokenId=12b46670-d34b-11e7-8e5f-992041a99879}
		Map<String, Object> searchParams = Servlets.getParametersStartingWith(request, null);
		log.info("signonToStuInfo searchParams ======== " + searchParams);
        String qimoClientId = (String) searchParams.remove("qimoClientId"); // 用户唯一标识
		// 再次兼容客服系统，有可能只传个手机号
        String phone = (String) searchParams.remove("phone");
		
        String qtoken = (String) searchParams.remove("token");
        String qtokenId = (String) searchParams.remove("tokenId");
		// 校验token
		if (!MoorApiUtil.checkTokenLegal(qtoken, qtokenId)) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("msg", "token校验失败！");
			mv.setViewName("/simple/tips");
			return mv;
		}

		// 获取学员对象
		GjtStudentInfo student = null;
		if(StringUtils.isNotBlank(qimoClientId)) {
			// 新版本客服系统画蛇添足，多加了@东西，如：04CA90EF-02A1-48C8-B93C-3BC8D3DEBEE7@FWoVWrcM@1064277f248348769814663488894308@iphone6splus
			String[] qcIds = qimoClientId.split("@");
			for (String qcId : qcIds) {
				// openId长度28位，studentId长度32位
				if(qcId.length() == 28 || qcId.length() == 32) {
					qimoClientId = qcId;
					break;
				}
			}
			
			student = gjtStudentInfoService.queryByWxOpenId(qimoClientId);
			if (student == null) { // 如果openid查不到，再按照studentId查
				student = gjtStudentInfoService.queryById(qimoClientId);
			}
		} else if(StringUtils.isNotBlank(phone)) {
			student = gjtStudentInfoService.querySSOByXhOrSfzhOrSjh(phone);
		}
		if (student == null || Constants.BOOLEAN_YES.equals(student.getIsDeleted()) || "5".equals(student.getXjzt())) {
			ModelAndView mv = new ModelAndView();
			mv.addObject("msg", "非正式学员！");
			mv.setViewName("/simple/tips");
			return mv;
		}

		Subject account = SecurityUtils.getSubject();
		GjtStudentInfo sessionStudent = (GjtStudentInfo) account.getSession().getAttribute(WebConstants.STUDENT_INFO);
		// 其他账号登录时先销毁当前登录用户
		if (sessionStudent == null || !sessionStudent.getStudentId().equals(student.getStudentId())) {
			account.logout();

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
				ModelAndView mv = new ModelAndView();
				mv.addObject("msg", "服务器异常！");
				mv.setViewName("/simple/tips");
				return mv;
			}
		}
		searchParams.remove("sign");
		ModelAndView mv = new ModelAndView();
		mv.setViewName("redirect:/pcenter/personal-information/index.html?" + FormSubmitUtil.createLinkString(searchParams));
		return mv;
	}

}
