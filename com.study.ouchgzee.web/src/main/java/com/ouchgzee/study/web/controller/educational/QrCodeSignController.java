package com.ouchgzee.study.web.controller.educational;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.common.exception.CommonException;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.flow.GjtFlowService;
import com.gzedu.xlims.service.signup.GjtSignupService;
import com.gzedu.xlims.service.usermanage.GjtStudentInfoService;

import net.spy.memcached.MemcachedClient;

/**
 * 二维码公共插件控制器<br/>
 * 功能说明：
 * @author 黄一飞 huangyifei@eenet.com
 * @CreateDate 2017年01月24日
 * @EditDate 2017年01月24日
 * @version 2.5
 *
 */
@Controller
@RequestMapping("/pcenter/qrcode")
public class QrCodeSignController  {

	// 静态常量
	private final static String ID = "id";
	private final static String SIGN_TYPE = "signType";
	private final static String SIGN = "sign";
	
	@Autowired
	private MemcachedClient memcachedClient;

	@Autowired
	private GjtStudentInfoService gjtStudentInfoService;

	@Autowired
	private GjtSignupService gjtSignupService;

	@Autowired
	private GjtFlowService gjtFlowService;
	
	/**
	 * 获取签名Token
	 * @param signType 1-学籍签名
	 * @throws IOException 
	 */
	@RequestMapping("/getToken")
	@ResponseBody
	public Object getToken(String signType,
						   HttpServletRequest request) {
		ResultFeedback feedback = new ResultFeedback(true, "success");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
			if(user == null) {
				feedback = new ResultFeedback(false, "请先登录!");
			} else {
				String token = createSignToken(user.getGjtStudentInfo().getStudentId(), request.getSession().getId());
				Map<String, Object> datas = new HashMap<String, Object>();
				datas.put(ID, user.getGjtStudentInfo().getStudentId()); // 缓存学员ID
				datas.put(SIGN_TYPE, signType);
				ResultFeedback data = new ResultFeedback(true, "未签", "0");
				data.setObj(datas);
				memcachedClient.set(token, WebConstants.EXPIRE,  data);
				feedback.setObj(token);
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new ResultFeedback(false, "服务器异常，请重试！");
		}
		return feedback;
	}
	
	/**
	 * 签名页面
	 * @return
	 */
	@RequestMapping("/signInput")
	@ResponseBody
	public Object signInput(HttpServletRequest request) {
		ResultFeedback feedback = new ResultFeedback(true, "success");
		try {
			final String token = request.getParameter("token");
			if(StringUtils.isNotBlank(token)) {
				ResultFeedback data = (ResultFeedback) memcachedClient.get(token);
				if(data != null && "0".equals(data.getType())) {
					feedback.setObj(token);
				} else {
					feedback = new ResultFeedback(false, "无效的或者失效的签名请求，请重新生成二维码签名！");
				}
			} else {
				feedback = new ResultFeedback(false, "非法的请求链接");
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new ResultFeedback(false, "服务器异常，请重试！");
		}

		if(!feedback.isSuccessful()) {
			feedback.setSuccessful(false);
		}
		return feedback;
	}
	
	/**
	 * 签名确认，提交
	 * @return
	 */
	
	@RequestMapping(value="/signConfirm",method=RequestMethod.POST)
	@ResponseBody
	public Object signConfirm(HttpServletRequest request) {
		ResultFeedback feedback = new ResultFeedback(true, "签名成功");
		try {
			String token = request.getParameter("token");
			String sign = request.getParameter("autograph"); // 签名
			if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(sign)) {
				ResultFeedback data = (ResultFeedback) memcachedClient.get(token);
				if(data != null) {
					Map<String, Object> datas = (Map<String, Object>) data.getObj();
					String signType = (String) datas.get(SIGN_TYPE);
					if(StringUtils.equals(signType, "1")) { // 如果是学籍签名
						/**
						 * 由于班主任经常会直接模拟登录学员的账号生成二维码给到学生去签名，这样会有一个时间反应问题，如果学员签名好了，但是老师没有点下一步则这次签名无效了;
						 * 所以为了解决这个问题，学员提交签名后直接就给保存到数据库中。
						 */
						final Map<String, String> signupCopyData = new HashMap<String, String>();
						if (sign.contains("base64,")) {
							String tmpFolderPath = request.getSession().getServletContext().getRealPath("")
									+ WebConstants.EXCEL_DOWNLOAD_URL + DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
							String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
							if (signPath != null) {
								signupCopyData.put("sign", signPath); // 签名
								sign = signPath; // 上传成功赋值给sign
							} else {
								throw new CommonException(MessageCode.BIZ_ERROR, "签名图片上传失败！");
							}
						} else {
							signupCopyData.put("sign", sign); // 签名
						}
						String studentId = (String) datas.get(ID);
						gjtStudentInfoService.updateEntityAndFlushCache(gjtStudentInfoService.queryById(studentId));
						gjtSignupService.updateSignupCopyData(studentId, signupCopyData);
						// 更新完善资料状态
						gjtStudentInfoService.perfectSignupAndCertificateInfo(studentId);
						// 确认提交资料
						gjtFlowService.initAuditSignupInfo(studentId);
					}

					data.setType("1");
					datas.put(SIGN, sign);
					data.setObj(datas);
					data.setMessage("已签");
					// 更新签名状态
					memcachedClient.set(token, WebConstants.EXPIRE,  data);
				} else {
					feedback = new ResultFeedback(false, "无效的或者失效的签名请求，请重新生成二维码签名！");
				}
			} else {
				feedback = new ResultFeedback(false, "网络异常，请重新扫码签名！"); // 缺少参数 - 经实战，有可能是网络慢导致数据丢失
			}
		} catch (Exception e) {
			feedback = new ResultFeedback(false, "服务器异常，请重试！");
		}
		return feedback;
	}
	
	/**
	 * 检测签名是否确认，提交了
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/signCheck")
	@ResponseBody
	public Object signCheck(HttpServletRequest request) throws IOException {
		ResultFeedback feedback = new ResultFeedback(true, "操作成功");
		try {
			String token = request.getParameter("token");
			if(StringUtils.isBlank(token)) {
				GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
				token = createSignToken(user.getGjtStudentInfo().getStudentId(),request.getSession().getId());
			}
			// 获取签名状态 1-已签 0-未签 null-无效或者失效的
			ResultFeedback data = (ResultFeedback) memcachedClient.get(token);
			if(data != null) {
				if("1".equals(data.getType())) {
					feedback.setType("1");
					Map<String, Object> datas = (Map<String, Object>) data.getObj();
					feedback.setObj(datas.get(SIGN));
					
					// 60s开始执行调度，移除签名状态
					final String TOKEN_KEY = token;
					new Timer().schedule(new TimerTask() {
						@Override
						public void run() {
							memcachedClient.delete(TOKEN_KEY);
						}
					}, 60000);
				} else {
					feedback.setType("0");
					feedback.setMessage("wait...");
				}
			} else {
				feedback = new ResultFeedback(false, "无效的或者失效的签名请求，请重新生成二维码签名！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			feedback = new ResultFeedback(false, "服务器异常，请重试！");
		}
		return feedback;
	}
	
	/**
	 * 生成签名页的token
	 * @param studentId
	 * @return
	 * @throws Exception
	 */
	private String createSignToken(String studentId,String sessionId) throws Exception  {
		return Md5Util.encrypt(studentId + sessionId);
	}
}
