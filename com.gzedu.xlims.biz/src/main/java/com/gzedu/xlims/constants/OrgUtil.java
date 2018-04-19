package com.gzedu.xlims.constants;

import org.apache.commons.lang3.StringUtils;

import com.gzedu.xlims.common.AppConfig;

public class OrgUtil {
	/**
	 * 广州市广播电视大学
	 */
	public static final String GZDD = "085";
	/**
	 * 广东外语外贸大学
	 */
	public static final String WYWM = "084";
	/**
	 * 杭州市工人业余大学
	 */
	public static final String HZGR = "083";
	/**
	 * 国家开放大学（杭州）在线教育
	 */
	public static final String GK_HZ = "082";
	/**
	 * 国家开放大学实验学院
	 */
	public static final String GK_SY = "081";
	/**
	 * 高校在线教育平台
	 */
	public static final String GXZX = "079";
	/**
	 * 深圳市龙岗社区学院
	 */
	public static final String SZLG = "075";
	/**
	 *  国家开放大学（广州）实验学院
	 */
	public static final String GK_GZ = "041";

	/**
	 * 是否提供居住证或工牌
	 * @param schoolCode
	 * @param sfzh
     * @return
     */
	public static boolean isOffsite(String schoolCode, String sfzh) {
		// 国家开放大学实验学院不需要提供
		if(GK_SY.equals(schoolCode)) {
			return false;
		}
		return !sfzh.startsWith("44"); // 是否是异地学员(非广东)，需提供居住证或工牌
	}

	/**
	 * 获取院校学籍审核人
	 * @param schoolCode
	 * @return
	 */
	public static String getSignupPersonApproving(String schoolCode) {
		String loginAccount;
		if (StringUtils.equals(schoolCode, GK_SY)) { // 国家开放大学实验学院
			// 学籍管理员
			loginAccount = "bjxjadmin001";
		} else { // 其他 国家开放大学（广州）实验学院
			// 梁世鹏
			loginAccount = "gzxjadmin001";
		}
		return loginAccount;
	}

	/**
	 * 获取门户的域名
	 * @return
	 */
	public static String getOucnetDomainHost(String schoolCode) {
		String oucnetDomain;
		if (StringUtils.equals(schoolCode, GK_SY)) { // 国家开放大学实验学院
			oucnetDomain = AppConfig.getProperty("applicationPlatform.domain.bj");
		} else { // 其他 国家开放大学（广州）实验学院
			oucnetDomain = AppConfig.getProperty("applicationPlatform.domain");
		}
		return oucnetDomain;
	}

	/**
	 * 获取门户的域名
	 * @return
	 */
	public static String getOucnetDomain(String schoolCode) {
		String oucnetDomain;
		if (StringUtils.equals(schoolCode, GK_SY)) { // 国家开放大学实验学院
			oucnetDomain = AppConfig.getProperty("synlogin.url.bj");
		} else { // 其他 国家开放大学（广州）实验学院
			oucnetDomain = AppConfig.getProperty("synlogin.url");
		}
		return oucnetDomain;
	}

	// 固定EEChat的APPID
	public static final String EECHAT_SZLG_APPID = "APP034"; // 深圳市龙岗社区学院
	public static final String EECHAT_GK_GZ_APPID = "APP014"; // 国家开放大学（广州）实验学院
	public static final String EECHAT_GK_SY_APPID = "APP038"; // 国家开放大学实验学院

	/**
	 * 获取EEChat的APPID
	 * @return
	 */
	public static String getEEChatAppId(String schoolCode) {
		String appId;
		if (StringUtils.equals(schoolCode, SZLG)) { // 深圳龙岗
			appId = EECHAT_SZLG_APPID;
		} else if (StringUtils.equals(schoolCode, GK_SY)) { // 国家开放大学实验学院
			appId = EECHAT_GK_SY_APPID;
		} else { // 其他 国家开放大学（广州）实验学院
			appId = EECHAT_GK_GZ_APPID;
		}
		return appId;
	}

}
