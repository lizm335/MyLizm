/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.controller;

import com.gzedu.xlims.common.GsonUtils;
import com.gzedu.xlims.common.ImgBase64Convert;
import com.gzedu.xlims.common.JuheDemo;
import com.gzedu.xlims.common.constants.MessageCode;
import com.gzedu.xlims.common.constants.ResponseModel;
import com.gzedu.xlims.common.exception.CommonException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 验证码识别<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月15日
 * @version 3.0
 */
@RestController
@RequestMapping( "/static" )
public class VCodeRecognitionController {

	/**
	 * 验证码识别
	 * @throws IOException
     */
	@RequestMapping(value = "recognition.html" )
	public ResponseModel recognition(@RequestParam("url") String url,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResponseModel rm = new ResponseModel(MessageCode.RESP_OK, "success");
		if(StringUtils.isNotBlank(url)) {
			String yzm = ImgBase64Convert.encodeImgageToBase64(url);
			String result = JuheDemo.post(JuheDemo.CodeType.CODETYPE_3006.getCode(), yzm);
			Map data = GsonUtils.toBean(result, Map.class);
			int errorCode = ((Double) data.get("error_code")).intValue();
			if(errorCode == 0) {
				rm.setData((String) data.get("result"));
				return rm;
			} else {
				rm.setMsgCode(MessageCode.BIZ_ERROR.getMsgCode());
				rm.setMessage("recognition fail!");
				return rm;
			}
		}
		throw new CommonException(MessageCode.BAD_REQUEST);
	}

}