/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

/**
 * 学员综合信息DTO<br>
 * 功能说明：<br>
 *      GJT_TEMP_STUDENT_SYNTHESIZE表下的SYNTHESIZE_INFO字段信息<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年02月25日
 * @version 3.0
 */
public class StudentSynthesizeInfoDto extends SynthesizeInfoDto {

    public StudentSynthesizeInfoDto() {
        super();
    }

    private String studentId;

    private String zp;

    private String xm;

    private String xh;

    private String sjh;

    private String userType;

    private String xjzt;

    private String yearName;

    private String gradeName;

    private String pycc;

    private String zymc;

    private String scName;

    /**
     * 最终状态 1-正常 2-异常
     */
    private Integer status;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getZp() {
        return zp;
    }

    public void setZp(String zp) {
        this.zp = zp;
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

    public String getSjh() {
        return sjh;
    }

    public void setSjh(String sjh) {
        this.sjh = sjh;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getXjzt() {
        return xjzt;
    }

    public void setXjzt(String xjzt) {
        this.xjzt = xjzt;
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

    public String getPycc() {
        return pycc;
    }

    public void setPycc(String pycc) {
        this.pycc = pycc;
    }

    public String getZymc() {
        return zymc;
    }

    public void setZymc(String zymc) {
        this.zymc = zymc;
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
