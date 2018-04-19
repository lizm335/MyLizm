/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.third.eechat.data;

/**
 * 功能说明：
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年8月17日
 * @version 2.5
 *
 */
public class EEIMUser {
	String USER_ID;// 用户ID
	String USER_NAME;// 用户名称
	String USER_TYPE;// 用户类型(1 老师 2 班主任 3 学生 4 督导)
	String USER_IDCARD;// 身份证
	String USER_SEX;// 性别(1 男 2 女 3 未知)
	String USER_BIRTHDAY;// 出生日期
	String USER_PHONE;// 手机
	String USER_EMAIL;// 邮箱
	String USER_DOMICILE;// 地址
	String USER_IMG;// 头像
	String USER_MOOD;// 个性签名
	String USER_DEC;// 备注
	String PROVINCE_ID;// 省份ID
	String CITY_ID;// 城市ID
	String AREA_ID;// 区域ID
	String PROVINCE_NAME;// 省份名称
	String CITY_NAME;// 城市名称
	String AREA_NAME;// 区域名称
	String QQ_NO;// QQ账号
	String WEIXIN_NO;// 微信账号
	String USER_CLASS;// 班级名称
	String USER_COMPANY;// 公司名称

	/**
	 * @param uSER_NAME
	 * @param uSER_TYPE
	 * @param uSER_IDCARD
	 * @param uSER_SEX
	 * @param uSER_BIRTHDAY
	 * @param uSER_PHONE
	 * @param uSER_EMAIL
	 * @param uSER_DOMICILE
	 * @param uSER_IMG
	 * @param uSER_MOOD
	 * @param uSER_DEC
	 */
	public EEIMUser(String uSER_ID, String uSER_NAME, String uSER_TYPE, String uSER_IDCARD, String uSER_SEX,
			String uSER_BIRTHDAY, String uSER_PHONE, String uSER_EMAIL, String uSER_DOMICILE, String uSER_IMG,
			String uSER_MOOD, String uSER_DEC) {
		super();
		USER_ID = uSER_ID;
		USER_NAME = uSER_NAME;
		USER_TYPE = uSER_TYPE;
		USER_IDCARD = uSER_IDCARD;
		USER_SEX = uSER_SEX;
		USER_BIRTHDAY = uSER_BIRTHDAY;
		USER_PHONE = uSER_PHONE;
		USER_EMAIL = uSER_EMAIL;
		USER_DOMICILE = uSER_DOMICILE;
		USER_IMG = uSER_IMG;
		USER_MOOD = uSER_MOOD;
		USER_DEC = uSER_DEC;
	}

	/*public EEIMUser(GjtStudentInfo studentInfo) {
		USER_ID = studentInfo.getStudentId();
		USER_NAME = studentInfo.getXm();
		USER_TYPE = "3";// 用户类型(1 老师 2 班主任 3 学生 4 督导)
		USER_IDCARD = studentInfo.getSfzh() == null ? "" : studentInfo.getSfzh();// 身份证
		USER_SEX = "3";// 性别(1 男 2 女 3 未知)
		USER_BIRTHDAY = "";// 出生日期
		USER_PHONE = StringUtils.isBlank(studentInfo.getSjh()) ? "" : studentInfo.getSjh();// 手机
		USER_EMAIL = StringUtils.isBlank(studentInfo.getDzxx()) ? "" : studentInfo.getDzxx();// 邮箱
		USER_DOMICILE = StringUtils.isBlank(studentInfo.getTxdz()) ? "" : studentInfo.getTxdz();// 地址
		USER_IMG = StringUtils.isBlank(studentInfo.getAvatar()) ? "" : studentInfo.getAvatar();// 头像
		USER_MOOD = "";// 个性签名
		USER_DEC = "";// 备注
		PROVINCE_ID = StringUtils.isBlank(studentInfo.getProvince()) ? "" : studentInfo.getProvince();// 省份ID
		CITY_ID = StringUtils.isBlank(studentInfo.getCity()) ? "" : studentInfo.getCity();// 城市ID
		AREA_ID = StringUtils.isBlank(studentInfo.getArea()) ? "" : studentInfo.getArea();// 区域ID
		PROVINCE_NAME = "";// 省份名称
		CITY_NAME = "";// 城市名称
		AREA_NAME = "";// 区域名称
		QQ_NO = "";// QQ账号
		WEIXIN_NO = "";// 微信账号
		USER_CLASS = StringUtils.isBlank(studentInfo.getUserclass()) ? "" : studentInfo.getUserclass();// 班级名称
		USER_COMPANY = StringUtils.isBlank(studentInfo.getScCo()) ? "" : studentInfo.getScCo();// 公司名称
	}

	public EEIMUser(GjtEmployeeInfo employee) {
		USER_ID = employee.getEmployeeId();
		USER_NAME = employee.getXm();
		// private String employeeType;// 1班主任(教学班),2辅导教师(课程班),3其它(职工),4督导教师
		if ("1".equals(employee.getEmployeeType())) {
			USER_TYPE = "2";// 用户类型(1 老师 2 班主任 3 学生 4 督导)
		} else if ("2".equals(employee.getEmployeeType())) {
			USER_TYPE = "1";// 用户类型(1 老师 2 班主任 3 学生 4 督导)
		} else if ("4".equals(employee.getEmployeeType())) {
			USER_TYPE = "4";// 用户类型(1 老师 2 班主任 3 学生 4 督导)
		} else {
			USER_TYPE = "3";// 用户类型(1 老师 2 班主任 3 学生 4 督导)
		}
		USER_IDCARD = StringUtils.isBlank(employee.getSfzh()) ? "" : employee.getSfzh();// 身份证
		USER_SEX = "3";// 性别(1 男 2 女 3 未知)
		USER_BIRTHDAY = "";// 出生日期
		USER_PHONE = StringUtils.isBlank(employee.getSjh()) ? "" : employee.getSjh();// 手机
		USER_EMAIL = StringUtils.isBlank(employee.getDzxx()) ? "" : employee.getDzxx();// 邮箱
		USER_DOMICILE = StringUtils.isBlank(employee.getJtzz()) ? "" : employee.getJtzz();// 地址
		USER_IMG = StringUtils.isBlank(employee.getZp()) ? "" : employee.getZp();// 头像
		USER_MOOD = "";// 个性签名
		USER_DEC = "";// 备注
		PROVINCE_ID = "";// tearcher.getProvince();// 省份ID
		CITY_ID = "";// tearcher.getCity();// 城市ID
		AREA_ID = "";// tearcher.getArea();// 区域ID
		PROVINCE_NAME = "";// 省份名称
		CITY_NAME = "";// 城市名称
		AREA_NAME = "";// 区域名称
		QQ_NO = "";// QQ账号
		WEIXIN_NO = "";// 微信账号
		USER_CLASS = "";// tearcher.getUserclass();// 班级名称
		USER_COMPANY = "";// tearcher.getScCo();// 公司名称
	}*/

	public EEIMUser() {
		super();
	}

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	public String getUSER_NAME() {
		return USER_NAME;
	}

	public void setUSER_NAME(String uSER_NAME) {
		USER_NAME = uSER_NAME;
	}

	public String getUSER_TYPE() {
		return USER_TYPE;
	}

	public void setUSER_TYPE(String uSER_TYPE) {
		USER_TYPE = uSER_TYPE;
	}

	public String getUSER_IDCARD() {
		return USER_IDCARD;
	}

	public void setUSER_IDCARD(String uSER_IDCARD) {
		USER_IDCARD = uSER_IDCARD;
	}

	public String getUSER_SEX() {
		return USER_SEX;
	}

	public void setUSER_SEX(String uSER_SEX) {
		USER_SEX = uSER_SEX;
	}

	public String getUSER_BIRTHDAY() {
		return USER_BIRTHDAY;
	}

	public void setUSER_BIRTHDAY(String uSER_BIRTHDAY) {
		USER_BIRTHDAY = uSER_BIRTHDAY;
	}

	public String getUSER_PHONE() {
		return USER_PHONE;
	}

	public void setUSER_PHONE(String uSER_PHONE) {
		USER_PHONE = uSER_PHONE;
	}

	public String getUSER_EMAIL() {
		return USER_EMAIL;
	}

	public void setUSER_EMAIL(String uSER_EMAIL) {
		USER_EMAIL = uSER_EMAIL;
	}

	public String getUSER_DOMICILE() {
		return USER_DOMICILE;
	}

	public void setUSER_DOMICILE(String uSER_DOMICILE) {
		USER_DOMICILE = uSER_DOMICILE;
	}

	public String getUSER_IMG() {
		return USER_IMG;
	}

	public void setUSER_IMG(String uSER_IMG) {
		USER_IMG = uSER_IMG;
	}

	public String getUSER_MOOD() {
		return USER_MOOD;
	}

	public void setUSER_MOOD(String uSER_MOOD) {
		USER_MOOD = uSER_MOOD;
	}

	public String getUSER_DEC() {
		return USER_DEC;
	}

	public void setUSER_DEC(String uSER_DEC) {
		USER_DEC = uSER_DEC;
	}

	public String getPROVINCE_ID() {
		return PROVINCE_ID;
	}

	public void setPROVINCE_ID(String pROVINCE_ID) {
		PROVINCE_ID = pROVINCE_ID;
	}

	public String getCITY_ID() {
		return CITY_ID;
	}

	public void setCITY_ID(String cITY_ID) {
		CITY_ID = cITY_ID;
	}

	public String getAREA_ID() {
		return AREA_ID;
	}

	public void setAREA_ID(String aREA_ID) {
		AREA_ID = aREA_ID;
	}

	public String getPROVINCE_NAME() {
		return PROVINCE_NAME;
	}

	public void setPROVINCE_NAME(String pROVINCE_NAME) {
		PROVINCE_NAME = pROVINCE_NAME;
	}

	public String getCITY_NAME() {
		return CITY_NAME;
	}

	public void setCITY_NAME(String cITY_NAME) {
		CITY_NAME = cITY_NAME;
	}

	public String getAREA_NAME() {
		return AREA_NAME;
	}

	public void setAREA_NAME(String aREA_NAME) {
		AREA_NAME = aREA_NAME;
	}

	public String getQQ_NO() {
		return QQ_NO;
	}

	public void setQQ_NO(String qQ_NO) {
		QQ_NO = qQ_NO;
	}

	public String getWEIXIN_NO() {
		return WEIXIN_NO;
	}

	public void setWEIXIN_NO(String wEIXIN_NO) {
		WEIXIN_NO = wEIXIN_NO;
	}

	public String getUSER_CLASS() {
		return USER_CLASS;
	}

	public void setUSER_CLASS(String uSER_CLASS) {
		USER_CLASS = uSER_CLASS;
	}

	public String getUSER_COMPANY() {
		return USER_COMPANY;
	}

	public void setUSER_COMPANY(String uSER_COMPANY) {
		USER_COMPANY = uSER_COMPANY;
	}

}
