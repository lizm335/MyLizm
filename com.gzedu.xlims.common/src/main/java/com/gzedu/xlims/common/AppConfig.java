/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


/**
 * 功能说明：系统配置类,提供统一的系统配置入口.
 * 缺省从配置文件DEFAULT_CONFIG常量所指的文件gzdec-config.properties中读取配置；
 * 当不存在此文件时，则在指定名为 DB_CONFIG_PATH常量值的 系统变量中获取配置文件路径和名称；
 * 该类为commlib中的最基本包，log，jdbc等的处理都通过其读取配置信息；
 * 
 * @author 朱恒勇 zhuhengyong@eenet.com
 * @Date 2016年4月29日
 * @version 2.5
 *
 */
public class AppConfig {
	
private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
	
    /**
     * 缺省配置文件名
     */
    public String defaultConfig;

	public void setDefaultConfig(String defaultConfig) {
		this.defaultConfig = defaultConfig;
	}

	/**
     * 不存在缺省的配置文件时，则查找是否该常量所指的系统变量System.properties中的值，作为配置文件；
     */
    public static final String DB_CONFIG_PATH = "db_config_path";

    /**
     * 读出的属性
     */
    private static Properties config;

    /**
     * 私有方法，单对象模式；
     */
    private AppConfig() {
    }

    /**
     * 当属性未初始化时，读取属性文件进行初始化 ，使用system.out输出调试信息；
     */
    private void init() {
        try {
            config = new Properties();
            
            Class cfgClass = Class.forName("com.gzedu.xlims.common.AppConfig");
            if (cfgClass == null) {
                log.info("com.gzedu.xlims.common.AppConfig: cfgClass is null");
            }
            log.info("com.gzedu.xlims.common.AppConfig: cfgClass loaded ");
            InputStream is = cfgClass.getResourceAsStream(defaultConfig);
            if (is == null) {
                try {
                    is = new FileInputStream(new File(System.getProperty(DB_CONFIG_PATH)));
                    try {
                        config.load(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Cannot found config file, please check it out and try again.");
                }
            } else {
                log.info("com.gzedu.xlims.common.AppConfig:  Getted input stream of configuration file");
                try {
                    config.load(is);
                    log.info("com.gzedu.xlims.common.AppConfig:  successfully loaded gzdec-config.properties file");
                } catch (IOException e) {
                    log.info("com.gzedu.xlims.common.AppConfig:  Cannot configure system param, please check out the gzdec-config.properties file");
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            log.info("com.gzedu.xlims.common.AppConfig:Cannot load class com.gzedu.xlims.common.AppConfig");
            e.printStackTrace();
        }
    }

    /**
     * 取得所有配置，
     *
     * @return 返回所有配置
     */
    public static Properties getProperties() {
        if (config == null) {
            log.info("Can not find config properties ");
            throw new RuntimeException("Can not find config properties ");
        } else {
            return config;
        }
    }

    /**
     * 返回一个属性值
     *
     * @param propertyName 属性名
     * @return 返回指定属性名的值
     */
    public static String getProperty(String propertyName){
        if (config == null) {
            log.info("Can not find config properties ");
            throw new RuntimeException("Can not find config properties ");
        } else {
            return config.getProperty(propertyName);
        }
    }

    /**
     * 返回指定的名称的属性值，
     *
     * @param propertyName
     * @param defaultValue
     * @return String类型的属性值；
     */
    public static String getProperty(String propertyName, String defaultValue) {
        if (config == null) {
            log.info("Can not find config properties ");
            throw new RuntimeException("Can not find config properties ");
        } else {
            return config.getProperty(propertyName, defaultValue);
        }
    }

    /**
     * 返回所有的属性名
     *
     * @return 返回指定属性名
     */
    public Enumeration getKeys() {
        if (config == null) {
            log.info("Can not find config properties ");
            throw new RuntimeException("Can not find config properties ");
        } else {
            return config.keys();
        }
    }

    /**
     * 返回一个布尔型的值
     *
     * @param propertyName
     * @return 返回属性名的bool值
     */
    public static boolean getBooleanProperty(String propertyName) {
        if (config == null) {
            log.info("Can not find config properties ");
            throw new RuntimeException("Can not find config properties ");
        } else {
            String value = config.getProperty(propertyName).toLowerCase();
            return Boolean.getBoolean(value);
        }
    }

    /**
     * 返回一个int型的值
     *
     * @param propertyName
     * @return 返回属性名的数字值；
     */
    public static int getIntegerProperty(String propertyName) {
        if (config == null) {
            log.info("Can not find config properties ");
            throw new RuntimeException("Can not find config properties ");
        } else {
            String value = config.getProperty(propertyName);
            return Integer.parseInt(value);
        }
    }

    public static void setProperty(String name, String value){
    	config.setProperty(name, value);
    }

}
