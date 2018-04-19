/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

import java.util.Date;

/**
 * 学院选课信息DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年06月01日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentCourseDto {

    private static final long serialVersionUID = 1L;

    private String studentId; // 学生ID

    private String accountId; // 账号ID

    private String xm; // 姓名

    private String xh;// 学号

    private String xbm; // 性别码

    private String certificatetype; // 证件类型

    private String sfzh;// 身份证号

    private String sjh;// 手机号

    private String yearName; // 年级名称

    private String gradeName; // 学期名称

    private String specialtyName; // 专业名称

    private String pycc;// 培养层次

    private String avatar; // 头像地址

    private Date takeCourseCreatedDt; // 选课时间

    private String kch; // 课程号

    private String kcmc; // 课程名称

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getTakeCourseCreatedDt() {
        return takeCourseCreatedDt;
    }

    public void setTakeCourseCreatedDt(Date takeCourseCreatedDt) {
        this.takeCourseCreatedDt = takeCourseCreatedDt;
    }

    public String getKch() {
        return kch;
    }

    public void setKch(String kch) {
        this.kch = kch;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

}
