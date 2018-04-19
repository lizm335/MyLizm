package com.gzedu.xlims.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 * 
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年07月24日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

	String value() default "";
}
