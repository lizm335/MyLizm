/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.common;

import java.util.Random;

import javax.servlet.http.HttpSession;

import com.gzedu.xlims.common.StringUtils;
import com.gzedu.xlims.common.ValidateUtil;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.common.gzedu.SMSUtil;

/**
 * 功能说明：短信通知公共类
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年10月16日
 * @version 2.5
 *
 */
public class MessageCheck {

	/**
	 * 重修
	 */
	public final static String REBUILDCODE = "rebuildCode";

	/**
	 * 教师微信公众号绑定账号
	 */
	public final static String TEACHER_BANDING = "teacherBanding";

	/**
	 * 发送短信验证码
	 * @param sjh
	 * @param typeCode
	 * @param session
	 * @throws CommonException
	 */
	public static void sendSmsCode(String sjh, String typeCode, HttpSession session) throws CommonException {
		if (sjh != null && ValidateUtil.isMobile(sjh)) {
			String code = (100000 + new Random().nextInt(900000)) + "";
			try {
				// 发送短信验证码
				SMSUtil.sendTemplateMessageCode(sjh, code, "gk");
				session.removeAttribute(typeCode + sjh);
				session.setAttribute(typeCode + sjh, code); // 加手机号设置属性，是为了确定短信验证码下发到该手机号下
			} catch (Exception e) {
				throw new CommonException(MessageCode.BIZ_ERROR, "短信发送失败！");
			}
		} else {
			throw new CommonException(MessageCode.BAD_REQUEST, "手机号为空或有误！");
		}
	}

	/**
	 * 校验短信码
	 * @param sjh
	 * @param code
	 * @param typeCode
	 * @param session
	 * @throws CommonException
	 */
	public static void doSmsValidateCode(String sjh, String code, String typeCode, HttpSession session) throws CommonException {
		if (StringUtils.isEmpty(code)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "验证码不能为空！");
		}
		Object scode = session.getAttribute(typeCode + sjh); // 加手机号设置属性，是为了确定短信验证码下发到该手机号下
		if (StringUtils.isEmpty(scode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "验证码超时已失效，请重新获取！");
		}
		if (!code.equals(scode)) {
			throw new CommonException(MessageCode.BAD_REQUEST, "验证码不正确！");
		}
	}
}
