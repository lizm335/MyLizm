package com.gzedu.xlims.common.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Title: iframework<br>
 * Description:全局通用属性文件属性获取，1.2增加原有的SystemConfig
 * 不推荐使用 <br>
 * Copyright: 广州远程教育中心 2009 <br>
 * Create DateTime: 2009-12-25 下午01:56:22 <br>
 * CVS last modify person: lianghuahuang <br>
 * CVS last modify DateTime: 2009-12-25 下午01:56:22  <br>
 * CVS last version:  <br>
 * @author 梁华璜
 */

public class CommonConfig {
	/*
	 * 配置文件路径
	 */
	private static final String cfgFile = "/common.properties";
	
	private final static  Log logger = LogFactory.getLog(CommonConfig.class);
	
	/**
	 * 读出的属性
	 */
	private static Properties properties;
	
	static {
		properties = new Properties();
		InputStream is = CommonConfig.class.getResourceAsStream(cfgFile);
		try {
			properties.load(is);
		} catch (IOException e) {
			logger.error("read common.properties file fail please check you properties file is exists "+e);
			throw new RuntimeException("读取common.propertise属性文件失败，请检查该属性文件是否存在！");
		}
	}

	/**
	 * 返回一个属性值
	 * 
	 * @param propertyName
	 *            属性名
	 * @return 返回指定属性名的值
	 */
	public static String getProperty(String propertyName) {		
		if (properties == null) {
			logger.error("system error,properties is null!");
			throw new RuntimeException("系统错误：读取common.properties属性失败！");
		} else {
			return properties.getProperty(propertyName);
		}
	}
}
