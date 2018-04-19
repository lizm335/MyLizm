/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

/**
 * 学位专业信息DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年02月16日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentSignupInfoDto {

    private static final long serialVersionUID = 1L;

    private String studentId; // 学生ID

    private String accountId; // 账号ID

    private String xm; // 姓名

    private String xh;// 学号

    private String xbm; // 性别码

    private String certificatetype; // 证件类型

    private String sfzh;// 身份证号

    private String nativeplace; // 籍贯 省市以空格(' ')隔开

    private String mzm; // 民族码

    private String politicsstatus; // 政治面貌

    private String hyzkm; // 婚姻状态码

    private String hkxzm; // 户口性质码

    private String csrq; // 出生日期 格式yyyyMMdd

    private String isonjob; // 在职状况

    private String hkszd; // 户籍所在地 省市以空格(' ')隔开

    private String sjh;// 手机号

    private String lxdh; // 固定电话

    private String dzxx; // 邮箱

    private String province; // 省代码

    private String city; // 市代码

    private String area; // 县代码

    private String txdz; // 通信地址

    private String yzbm; // 邮编

    private String scCo; // 所在单位

    private String scCoAddr; // 单位地址

    private String jobPost; // 岗位职务

    private String yearName; // 年级名称

    private String gradeName; // 学期名称

    private String specialtyName; // 专业名称

    private String pycc;// 培养层次

    private String academic; // 学习方式

    private String xxmc; // 报读院校

    private String bjmc; // 班级名称

    private String xxzxName; // 学习中心

    private String exedulevel; // 原学历层次

    private String exschool; // 原毕业学校

    private String exgraduatedtime; // 毕业时间 格式yyyy-MM-dd

    private String exsubject; // 原学科

    private String exsubjectkind; // 原学科门类

    private String exedubaktype; // 原学历学习类型

    private String exedumajor; // 原学历所学专业

    private String excertificatenum; // 原学历毕业证书编号

    private String excertificateprove; // 原学历证明材料

    private String excertificateprovenum; // 原学历证明材料编号

    private String exeduname; // 原学历姓名

    private String exeducertificate; // 原学历证件类型

    private String exedunum; // 原学历证件号码

    private String isgraduatebytv; // 是否电大毕业

    private Integer perfectStatus; // 资料完善状态，默认0 1-已完善 0-未完善，进入第一步标识-确认个人信息 2-进入第二步标识-确认通讯信息 3-进入第三步标识-确认报读信息 4-进入第四步标识-确认原最高学历 5-进入第五步标识-确认证件信息 6-进入第六步标识-确认签名

    private Integer isUndergraduateCourse; // 培养层次是否为本科 1-是本科 0-非本科

    private String signupAuditState; // 资料审核最终状态 0-不通过 1-通过 其他-待审核

    private String avatar; // 头像地址

    private String xjzt; // 学籍状态

    private String userType; // 学员类型 11-正式生 12-正式跟读生 13-非正式跟读生 21-体验学员 31-测试学员 41-课程预读生 42-电大续读生 51-外校预科生 61-本科预读生

    private Integer flowAuditOperatorRole; // 审核记录最新审核人角色 1-学员 2-班主任 3-招生办 4-学籍科主任

    private Integer flowAuditState; // 审核记录最新审核状态 0-待审核 1-通过 2-不通过

    private String sign; // 学员电子签名

    private String xbmName; // 性别码对应的Name

    private String pyccName; // 培养层次码对应的Name

    private String isEnteringSchool; // 是否入学，默认为0 1-是 0-否

    private String base64Code; //加密串，模拟登陆个人中心使用

    private String synUrl;

    private String xxCode;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public String getXh() {
        return xh;
    }

    public void setXh(String xh) {
        this.xh = xh;
    }

    public String getXbm() {
        return xbm;
    }

    public void setXbm(String xbm) {
        this.xbm = xbm;
    }

    public String getCertificatetype() {
        return certificatetype;
    }

    public void setCertificatetype(String certificatetype) {
        this.certificatetype = certificatetype;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getMzm() {
        return mzm;
    }

    public void setMzm(String mzm) {
        this.mzm = mzm;
    }

    public String getPoliticsstatus() {
        return politicsstatus;
    }

    public void setPoliticsstatus(String politicsstatus) {
        this.politicsstatus = politicsstatus;
    }

    public String getHyzkm() {
        return hyzkm;
    }

    public void setHyzkm(String hyzkm) {
        this.hyzkm = hyzkm;
    }

    public String getHkxzm() {
        return hkxzm;
    }

    public void setHkxzm(String hkxzm) {
        this.hkxzm = hkxzm;
    }

    public String getCsrq() {
        return csrq;
    }

    public void setCsrq(String csrq) {
        this.csrq = csrq;
    }

    public String getIsonjob() {
        return isonjob;
    }

    public void setIsonjob(String isonjob) {
        this.isonjob = isonjob;
    }

    public String getHkszd() {
        return hkszd;
    }

    public void setHkszd(String hkszd) {
        this.hkszd = hkszd;
    }

    public String getSfzh() {
        return sfzh;
    }

    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    public String getSjh() {
        return sjh;
    }

    public void setSjh(String sjh) {
        this.sjh = sjh;
    }

    public String getLxdh() {
        return lxdh;
    }

    public void setLxdh(String lxdh) {
        this.lxdh = lxdh;
    }

    public String getDzxx() {
        return dzxx;
    }

    public void setDzxx(String dzxx) {
        this.dzxx = dzxx;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTxdz() {
        return txdz;
    }

    public void setTxdz(String txdz) {
        this.txdz = txdz;
    }

    public String getYzbm() {
        return yzbm;
    }

    public void setYzbm(String yzbm) {
        this.yzbm = yzbm;
    }

    public String getScCo() {
        return scCo;
    }

    public void setScCo(String scCo) {
        this.scCo = scCo;
    }

    public String getScCoAddr() {
        return scCoAddr;
    }

    public void setScCoAddr(String scCoAddr) {
        this.scCoAddr = scCoAddr;
    }

    public String getJobPost() {
        return jobPost;
    }

    public void setJobPost(String jobPost) {
        this.jobPost = jobPost;
    }

    public String getYearName() {
        return yearName;
    }

    public void setYearName(String yearName) {
        this.yearName = yearName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getPycc() {
        return pycc;
    }

    public void setPycc(String pycc) {
        this.pycc = pycc;
    }

    public String getAcademic() {
        return academic;
    }

    public void setAcademic(String academic) {
        this.academic = academic;
    }

    public String getXxmc() {
        return xxmc;
    }

    public void setXxmc(String xxmc) {
        this.xxmc = xxmc;
    }

    public String getBjmc() {
        return bjmc;
    }

    public void setBjmc(String bjmc) {
        this.bjmc = bjmc;
    }

    public String getXxzxName() {
        return xxzxName;
    }

    public void setXxzxName(String xxzxName) {
        this.xxzxName = xxzxName;
    }

    public String getExedulevel() {
        return exedulevel;
    }

    public void setExedulevel(String exedulevel) {
        this.exedulevel = exedulevel;
    }

    public String getExschool() {
        return exschool;
    }

    public void setExschool(String exschool) {
        this.exschool = exschool;
    }

    public String getExgraduatedtime() {
        return exgraduatedtime;
    }

    public void setExgraduatedtime(String exgraduatedtime) {
        this.exgraduatedtime = exgraduatedtime;
    }

    public String getExsubject() {
        return exsubject;
    }

    public void setExsubject(String exsubject) {
        this.exsubject = exsubject;
    }

    public String getExsubjectkind() {
        return exsubjectkind;
    }

    public void setExsubjectkind(String exsubjectkind) {
        this.exsubjectkind = exsubjectkind;
    }

    public String getExedubaktype() {
        return exedubaktype;
    }

    public void setExedubaktype(String exedubaktype) {
        this.exedubaktype = exedubaktype;
    }

    public String getExedumajor() {
        return exedumajor;
    }

    public void setExedumajor(String exedumajor) {
        this.exedumajor = exedumajor;
    }

    public String getExcertificatenum() {
        return excertificatenum;
    }

    public void setExcertificatenum(String excertificatenum) {
        this.excertificatenum = excertificatenum;
    }

    public String getExcertificateprove() {
        return excertificateprove;
    }

    public void setExcertificateprove(String excertificateprove) {
        this.excertificateprove = excertificateprove;
    }

    public String getExcertificateprovenum() {
        return excertificateprovenum;
    }

    public void setExcertificateprovenum(String excertificateprovenum) {
        this.excertificateprovenum = excertificateprovenum;
    }

    public String getExeduname() {
        return exeduname;
    }

    public void setExeduname(String exeduname) {
        this.exeduname = exeduname;
    }

    public String getExeducertificate() {
        return exeducertificate;
    }

    public void setExeducertificate(String exeducertificate) {
        this.exeducertificate = exeducertificate;
    }

    public String getExedunum() {
        return exedunum;
    }

    public void setExedunum(String exedunum) {
        this.exedunum = exedunum;
    }

    public String getIsgraduatebytv() {
        return isgraduatebytv;
    }

    public void setIsgraduatebytv(String isgraduatebytv) {
        this.isgraduatebytv = isgraduatebytv;
    }

    public Integer getPerfectStatus() {
        return perfectStatus;
    }

    public void setPerfectStatus(Integer perfectStatus) {
        this.perfectStatus = perfectStatus;
    }

	public Integer getIsUndergraduateCourse() {
		return isUndergraduateCourse;
	}

	public void setIsUndergraduateCourse(Integer isUndergraduateCourse) {
		this.isUndergraduateCourse = isUndergraduateCourse;
	}

	public String getSignupAuditState() {
        return signupAuditState;
    }

    public void setSignupAuditState(String signupAuditState) {
        this.signupAuditState = signupAuditState;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getXjzt() {
        return xjzt;
    }

    public void setXjzt(String xjzt) {
        this.xjzt = xjzt;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getFlowAuditOperatorRole() {
        return flowAuditOperatorRole;
    }

    public void setFlowAuditOperatorRole(Integer flowAuditOperatorRole) {
        this.flowAuditOperatorRole = flowAuditOperatorRole;
    }

    public Integer getFlowAuditState() {
        return flowAuditState;
    }

    public void setFlowAuditState(Integer flowAuditState) {
        this.flowAuditState = flowAuditState;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getXbmName() {
        return xbmName;
    }

    public void setXbmName(String xbmName) {
        this.xbmName = xbmName;
    }

    public String getPyccName() {
        return pyccName;
    }

    public void setPyccName(String pyccName) {
        this.pyccName = pyccName;
    }

    public String getIsEnteringSchool() {
        return isEnteringSchool;
    }

    public void setIsEnteringSchool(String isEnteringSchool) {
        this.isEnteringSchool = isEnteringSchool;
    }

    public String getBase64Code() {
        return base64Code;
    }

    public void setBase64Code(String base64Code) {
        this.base64Code = base64Code;
    }

    public String getSynUrl() {
        return synUrl;
    }

    public void setSynUrl(String synUrl) {
        this.synUrl = synUrl;
    }

    public String getXxCode() {
        return xxCode;
    }

    public void setXxCode(String xxCode) {
        this.xxCode = xxCode;
    }
}
