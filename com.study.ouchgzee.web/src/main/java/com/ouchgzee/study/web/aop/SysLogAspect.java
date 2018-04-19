package com.ouchgzee.study.web.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ObjectUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gzedu.xlims.common.HttpContextUtils;
import com.gzedu.xlims.common.IPUtils;
import com.gzedu.xlims.common.annotation.SysLog;
import com.gzedu.xlims.constants.syslog.executor.SysLogRunnable;
import com.gzedu.xlims.service.SysLogService;

/**
 * 系统日志，切面处理类<br/>
 * 切面通知类型：@Before 前置通知
 * 
 * @AfterReturning 后置通知
 * @After 最终通知
 * @AfterThrowing 异常通知
 * @Around 环绕通知 这5种类型的通知，在内部调用时这样组织<br/>
 *         try {<br/>
 *         调用前置通知 环绕前置处理 调用目标对象方法 环绕后置处理 调用后置通知 }catch(Exception e) {<br/>
 *         调用异常通知 }finally{<br/>
 *         调用最终通知 }<br/>
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年07月24日
 */
@Aspect // 该注解标示该类为切面类
@Component // 注入依赖
public class SysLogAspect {

	private static final Logger log = LoggerFactory.getLogger(SysLogAspect.class);

	@Autowired
	private SysLogService sysLogService;

	// 定义一个切入点 @SysLog/create/update/delete
	@Pointcut("@annotation(com.gzedu.xlims.common.annotation.SysLog) || (execution(* com.ouchgzee.study.web.controller..*.*Controller..*(..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping))")
	public void logPointCut() {
	}

	// 环绕通知 方法访问前后处理
	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Throwable ex = null;
		Object result = null;
		String error = null;
		long beginTime = System.currentTimeMillis();
		try {
			// 执行方法
			result = joinPoint.proceed();
			error = ObjectUtils.toString(result, "");
		} catch (Throwable e) {
			error = ObjectUtils.toString(e.getMessage(), "");
			e.printStackTrace();
			ex = e;
		}
		// 执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		// 由于前期没有加@SysLog，现在需要排除GET的请求记录日志，GET请求时长小于10s不做日志
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		SysLog syslog = method.getAnnotation(SysLog.class);
		if (requestMapping.method().length == 0 || requestMapping.method()[0] == RequestMethod.GET) {
			if (syslog == null && error.indexOf("{") != -1) {
				if (time <= 10000) { // GET请求时长小于10s不做日志
					if (ex != null) {
						// 继续抛出等待过滤器捕捉
						throw ex;
					}
					return result;
				}
			}
		}

		// 获取request,这种方式获取不到PHP请求过来的request参数，所以换一种方式，直接从方法参数中获取
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		HttpServletRequest requestReal = null;
		try {
			Object[] methodArgs = joinPoint.getArgs();
			for (int i = 0; i < methodArgs.length; i++) {
				if (methodArgs[i] instanceof HttpServletRequest) {
					requestReal = (HttpServletRequest) methodArgs[i];
					break;
				}
			}
			// 保存日志线程
			SysLogRunnable slt = new SysLogRunnable(joinPoint, this.sysLogService);
			slt.setRequestParams(requestReal != null ? requestReal.getParameterMap() : request.getParameterMap());
			slt.getSysLog().setMethod(requestReal != null ? requestReal.getServletPath() : request.getServletPath());
			slt.getSysLog().setTime(time);
			slt.getSysLog().setError(error);
			// 设置IP地址
			slt.getSysLog().setIp(requestReal != null ? IPUtils.getIpAddr(requestReal) : IPUtils.getIpAddr(request));
			slt.getSysLog().setPlatfromType(5); // 学习空间
			// // 放入线程池中执行
			// SysLogExecutor.execute(slt);
			// 启动线程时经常遇到的java.lang.OutOfMemoryError: unable to create new native
			// thread，要么加大内存，而我选择不使用线程池了
			slt.run();
		} catch (Exception e) {
			log.error("add syslog error:{}" + e.getMessage());
		}
		if (ex != null) {
			// 继续抛出等待过滤器捕捉
			throw ex;
		}
		return result;
	}

}
