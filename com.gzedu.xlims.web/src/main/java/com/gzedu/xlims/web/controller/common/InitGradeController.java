/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.web.controller.common;

import com.gzedu.xlims.service.CacheService;
import com.gzedu.xlims.service.dictionary.DictionaryService;
import com.gzedu.xlims.service.organization.GjtGradeService;
import com.gzedu.xlims.web.common.Feedback;
import com.gzedu.xlims.web.controller.base.BaseController;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 初始化年级控制器<br/>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年03月23日
 * @version 2.5
 */
@Controller
@RequestMapping( "/static" )
public class InitGradeController extends BaseController {

	/**
	 * 新增年级
	 * @throws IOException
     */
	@RequestMapping(value = "initGrade.html" )
	@ResponseBody
	public Feedback initGrade(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Feedback feedback = new Feedback(true, "新增成功");
		// Spring 上下文
		ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());

		long startTime = System.currentTimeMillis();
		GjtGradeService gjtGradeService = appContext.getBean(GjtGradeService.class);
		gjtGradeService.batchCreateGrade();

		System.out.println("新增年级 success ！耗时:" + (System.currentTimeMillis() - startTime) + "ms");
		feedback.setMessage("新增年级 success ！耗时:" + (System.currentTimeMillis() - startTime) + "ms");
		return feedback;
	}

}