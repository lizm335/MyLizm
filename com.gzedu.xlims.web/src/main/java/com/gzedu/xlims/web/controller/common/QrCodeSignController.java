package com.gzedu.xlims.web.controller.common;

import com.gzedu.xlims.common.Md5Util;
import com.gzedu.xlims.common.ResultFeedback;
import com.gzedu.xlims.common.constants.WebConstants;
import com.gzedu.xlims.common.eefile.EEFileUploadUtil;
import com.gzedu.xlims.pojo.GjtUserAccount;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

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
@RequestMapping("/qrcode")
public class QrCodeSignController  {

	// 静态常量
	private final static String ID = "id";
	private final static String SIGN = "sign";
	
	@Autowired
	private MemcachedClient memcachedClient;
	
	/**
	 * 获取签名Token
	 * @throws IOException 
	 */
	@RequestMapping("/getToken")
	@ResponseBody
	public ResultFeedback getToken(HttpServletRequest request) {
		ResultFeedback feedback = new ResultFeedback(true, "success");
		try {
			GjtUserAccount user = (GjtUserAccount) request.getSession().getAttribute(WebConstants.CURRENT_USER);
//			if(user == null) {
//				feedback = new ResultFeedback(false, "请先登录!");
//			} else {
				String token = createSignToken(request.getSession().getId());
				Map<String, Object> datas = new HashMap<String, Object>();
				datas.put(ID, user != null ? user.getId() : null); // 缓存用户ID
				ResultFeedback data = new ResultFeedback(true, "未签", "0");
				data.setObj(datas);
				memcachedClient.set(token, WebConstants.EXPIRE,  data);
				feedback.setObj(token);
//			}
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
	public String signInput(HttpServletRequest request, Model model) {
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

		model.addAttribute("feedback", feedback);
		
		if(!feedback.isSuccessful()) {
			return "sign_input_result";
		}
		
		return "sign_input";
	}
	
	/**
	 * 签名确认，提交
	 * @return
	 */
	
	@RequestMapping(value="/signConfirm",method=RequestMethod.POST)
	public String signConfirm(HttpServletRequest request, Model model) {
		ResultFeedback feedback = new ResultFeedback(true, "签名成功");
		try {
			String token = request.getParameter("token");
			String sign = request.getParameter("autograph"); // 签名
			if(StringUtils.isNotBlank(token) && StringUtils.isNotBlank(sign)) {
				ResultFeedback data = (ResultFeedback) memcachedClient.get(token);
				if(data != null) {
					if(sign.contains("base64,")) {
						String tmpFolderPath = request.getSession().getServletContext().getRealPath("")
								+ WebConstants.EXCEL_DOWNLOAD_URL + DateFormatUtils.ISO_DATE_FORMAT.format(Calendar.getInstance());
						String signPath = EEFileUploadUtil.uploadImageBase64ToUrl(sign, tmpFolderPath);
						if(signPath != null) {
							sign = signPath; // 上传成功赋值给sign
						} else {
							feedback = new ResultFeedback(false, "签名图片上传失败！");
							model.addAttribute("feedback", feedback);
							return "sign_input_result";
						}
					}
					
					@SuppressWarnings("unchecked")
					Map<String, Object> datas = (Map<String, Object>) data.getObj();
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
		
		model.addAttribute("feedback", feedback);
		return "sign_input_result";
	}
	
	/**
	 * 检测签名是否确认，提交了
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/signCheck")
	@ResponseBody
	public ResultFeedback signCheck(HttpServletRequest request) throws IOException {
		ResultFeedback feedback = new ResultFeedback(true, "操作成功");
		try {
			String token = request.getParameter("token");
			if(StringUtils.isBlank(token)) {
				token = createSignToken(request.getSession().getId());
			}
			
			// 获取签名状态 1-已签 0-未签 null-无效或者失效的
			ResultFeedback data = (ResultFeedback) memcachedClient.get(token);
			if(data != null) {
				if("1".equals(data.getType())) {
					feedback.setType("1");
					@SuppressWarnings("unchecked")
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
	 * 生成签名页的token<br/>
	 * 由于有些时候是不需要登录就可以签名的，所以token就按照sessionId来生成好了<br/>
	 * @param sessionId
	 * @return
	 * @throws Exception
	 */
	private String createSignToken(String sessionId) throws Exception  {
		return Md5Util.encrypt(sessionId);
	}
}
