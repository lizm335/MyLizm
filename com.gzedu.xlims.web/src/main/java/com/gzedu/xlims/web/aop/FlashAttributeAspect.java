package com.gzedu.xlims.web.aop;

import com.gzedu.xlims.common.HttpContextUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统日志，切面处理类<br/>
 * 切面通知类型：@Before 前置通知
 *          @AfterReturning 后置通知
 *          @After 最终通知
 *          @AfterThrowing 异常通知
 *          @Around 环绕通知
 * 这5种类型的通知，在内部调用时这样组织<br/>
 * try {<br/>
 *  调用前置通知
 *  环绕前置处理
 *  调用目标对象方法
 *  环绕后置处理
 *  调用后置通知
 * }catch(Exception e) {<br/>
 *  调用异常通知
 * }finally{<br/>
 *  调用最终通知
 * }<br/>
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年07月24日
 */
@Aspect		// 该注解标示该类为切面类
@Component	// 注入依赖
public class FlashAttributeAspect {

	// 定义一个切入点 Spring MVC 请求
	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void attrPointCut() {}

	/**
	 * 后置通知 方法访问之后执行<br/>
	 * redirectAttributes.addFlashAttribute("feedback", feedback)
	 * 解决页面一直提示信息，第二次不消失问题，原因是addFlashAttribute会存入到session中，没有地方被清除
	 */
	@AfterReturning("attrPointCut()")
	public void around(JoinPoint joinPoint) {
		try {
			//获取request
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();

			request.getSession().removeAttribute("org.springframework.web.servlet.support.SessionFlashMapManager.FLASH_MAPS");
		} catch (Exception e) {

		}
	}
	
}
