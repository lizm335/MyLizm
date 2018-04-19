package com.ouchgzee.study.web.vo;

import com.gzedu.xlims.pojo.GjtStudentInfo;

public class GjtStudentVo {

	private String studentId;
	// 头像
	private String pictureURL;
	/** 基础信息 */
	// 姓名
	private String userName;
	// 学号
	private String studentNo;
	// 性别
	private String gender;
	// 证件类型
	private String certType;
	// 证件号
	private String certNo;
	// 籍贯
	private String nativePlace;
	// 民族
	private String nation;
	// 政治面貌
	private String politicsFace;
	// 婚烟状态
	private String marryStatus;
	// 户口性质
	private String householdNature;
	// 出生日期
	private String birthDate;
	// 在职情况
	private String jobCondition;
	// 户籍所在地
	private String householdLocal;

	/** 通讯信息 */
	// 手机号码
	private String mobile;
	// 固定电话
	private String telephone;
	// 邮箱
	private String email;
	// 通讯地址
	private String postalAddress;
	// 省
	// private String province;
	// 市
	// private String city;
	// 区
	// private String area;
	// 邮编
	private String postcode;
	// 所在单位
	private String company;
	// 单位地址
	private String companyAddress;
	// 岗位职务
	private String jobPost;

	// 第二联系人
	// private String secondLinkman;
	// 第二联系人电话
	// private String secondLinkmanMobile;
	
	// 报读专业
	private String specialty;
	// 入学学期
	private String gradeName;
	// 专业层次
	private String pycc;
	// 专业层次
	private String pyccName;
	// 教务班级
	private String className;
	// 入学年级
	private String yearName;
	// 所属院校
	private String orgName;
	
	public GjtStudentVo() {
	}

	public GjtStudentVo(GjtStudentInfo si) {
		studentId = si.getStudentId();
		// 头像
		pictureURL = si.getAvatar();
		/** 基础信息 */
		// 姓名
		userName = si.getXm();
		// 学号
		studentNo = si.getXh();
		// 性别
		gender = si.getXbm(); // TODO :码(0,1,2)
		// 证件类型
		certType = si.getCertificatetype()==null?"身份证":si.getCertificatetype();
		// 证件号
		certNo = si.getSfzh();
		// 籍贯
		nativePlace = si.getNativeplace();
		// 民族
		nation = si.getMzm();// TODO:码
		// 政治面貌
		politicsFace = si.getPoliticsstatus();
		// 婚烟状态
		marryStatus = si.getHyzkm(); // TODO:码(0,1,3)
		// 户口性质
		householdNature = si.getHkxzm();// TODO:码(1,2)
		// 培养层次
		pycc = si.getPycc();
		// 出生日期
		birthDate = si.getCsrq();
		// 在职情况
		jobCondition = si.getIsonjob();// 
		// 户籍所在地
		householdLocal = si.getHkszd();
		/** 通讯信息 */
		// 手机号码
		mobile = si.getSjh();
		// 固定电话
		telephone = si.getLxdh();
		// 邮箱
		email = si.getDzxx();
		// 通讯地址
		postalAddress = si.getTxdz();
		// 省
		//province = si.getProvince();// TODO:码
		// 市
		//city = si.getCity();// TODO:码
		// 区
		//area = si.getArea();// TODO:码
		// 邮编
		postcode = si.getYzbm();
		// 所在单位
		company = si.getScCo();
		// 单位地址
		companyAddress = si.getScCoAddr();
		// 岗位职务
		if(si.getGjtSignup() != null) {
			jobPost = si.getGjtSignup().getJobPost();
		}

	}
	
	
	public GjtStudentVo(GjtStudentInfo si,String pycc, String className, String orgName) {
		studentId = si.getStudentId();
		// 头像
		pictureURL = si.getAvatar();
		/** 基础信息 */
		
		// 姓名
		userName = si.getXm();
		// 学号
		studentNo = si.getXh();
		// 证件类型
		certType = si.getCertificatetype()==null?"身份证":si.getCertificatetype();
		// 证件号
		certNo = si.getSfzh();
		// 报读专业
		if(si.getGjtSpecialty() != null) {
			specialty = si.getGjtSpecialty().getZymc();// 报读专业
		}
		// 入学学期/入学年级
		if(si.getGjtGrade() != null) {
			gradeName = si.getGjtGrade().getGradeName();// 入学学期
			if(si.getGjtGrade().getGjtYear() != null) {
				yearName = si.getGjtGrade().getGjtYear().getName();//入学年级
			}
		}
		// 专业层次
		this.pycc = pycc;
		// 教务班级
		this.className = className;
		// 所属院校
		this.orgName = orgName;
		// 手机号码
		mobile = si.getSjh();
		// 邮箱 
		email = si.getDzxx();
		
	}
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getPoliticsFace() {
		return politicsFace;
	}

	public void setPoliticsFace(String politicsFace) {
		this.politicsFace = politicsFace;
	}

	public String getMarryStatus() {
		return marryStatus;
	}

	public void setMarryStatus(String marryStatus) {
		this.marryStatus = marryStatus;
	}

	public String getHouseholdNature() {
		return householdNature;
	}

	public void setHouseholdNature(String householdNature) {
		this.householdNature = householdNature;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getJobCondition() {
		return jobCondition;
	}

	public void setJobCondition(String jobCondition) {
		this.jobCondition = jobCondition;
	}

	public String getHouseholdLocal() {
		return householdLocal;
	}

	public void setHouseholdLocal(String householdLocal) {
		this.householdLocal = householdLocal;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostalAddress() {
		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getJobPost() {
		return jobPost;
	}

	public void setJobPost(String jobPost) {
		this.jobPost = jobPost;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getYearName() {
		return yearName;
	}

	public void setYearName(String yearName) {
		this.yearName = yearName;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getPyccName() {
		return pyccName;
	}

	public void setPyccName(String pyccName) {
		this.pyccName = pyccName;
	}

}
