/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

import com.gzedu.xlims.common.GsonUtils;

import java.math.BigDecimal;

/**
 * 学员综合信息DTO<br>
 * 功能说明：<br>
 *      GJT_TEMP_STUDENT_SYNTHESIZE表下的SYNTHESIZE_INFO字段信息<br/>
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2018年02月25日
 * @version 3.0
 */
public class SynthesizeInfoDto {

    /**
     * 学籍状态 1-正常 2-审核不通过 3-待注册
     */
    private Integer rollStatus;

    /**
     * 缴费状态 1-正常 2-待缴费 3-有欠费
     */
    private Integer paymentStatus;

    /**
     * 教材状态 1-正常 2-有未发教材
     */
    private Integer textbookStatus;

    /**
     * 学习状态 1-正常 2-有落后课程
     */
    private Integer studyStatus;

    /**
     * 考勤状态 1-正常 2-3天内未学习 3-3天以上未学习 4-7天以上未学习 5-从未学习
     */
    private Integer clockingStatus;

    private String isOnline; // 是否在线 Y-在线 N-离线

    private String device; // 在线设备

    private Integer loginTimes; // 登录次数

    private Integer lastLoginTime; // 离线时长

    /**
     * 考试状态 1-正常 2-异常，预约范围内，需督促 3-异常，预约已过期，漏报考 4-异常，已约满，需下次再约
     */
    private Integer examStatus;

    /**
     * 论文状态 1-正常 2-到期未完成 3-到期未申请
     */
    private Integer thesisStatus;

    /**
     * 实战状态 1-正常 2-到期未完成 3-到期未申请
     */
    private Integer practiceStatus;

    /**
     * 毕业状态 1-正常 2-毕业异常，到期未满足 3-毕业异常，过期未申请
     */
    private Integer graduationStatus;

    /**
     * 链接状态 1-正常 2-未安装APP 3-未使用PC 4-离线（7天以上未学习） 5-未绑定公众号
     */
    private Integer linkStatus;

    private Integer appLastLoginTime; // APP离线时长

    private String appIsOnline; // APP是否在线 Y-在线 N-离线

    private Integer pcLastLoginTime; // PC离线时长

    private String pcIsOnline; // PC是否在线 Y-在线 N-离线

    private Integer isBandingWx; // 是否绑定微信公众号 1-已绑定 0-未绑定

    /**
     * 校验最终的状态
     */
    public int validationGetStatus() {
        return rollStatus != null && rollStatus == 1 &&
                paymentStatus != null && paymentStatus == 1 &&
                textbookStatus != null && textbookStatus == 1 &&
                studyStatus != null && studyStatus == 1 &&
                clockingStatus != null && clockingStatus == 1 &&
                examStatus != null && examStatus == 1 &&
                thesisStatus != null && thesisStatus == 1 &&
                practiceStatus != null && practiceStatus == 1 &&
                graduationStatus != null && graduationStatus == 1 &&
                linkStatus != null && linkStatus == 1
                ? 1 : 2;
    }

    public Integer getRollStatus() {
        return rollStatus;
    }

    public void setRollStatus(Integer rollStatus) {
        this.rollStatus = rollStatus;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getTextbookStatus() {
        return textbookStatus;
    }

    public void setTextbookStatus(Integer textbookStatus) {
        this.textbookStatus = textbookStatus;
    }

    public Integer getStudyStatus() {
        return studyStatus;
    }

    public void setStudyStatus(Integer studyStatus) {
        this.studyStatus = studyStatus;
    }

    public Integer getClockingStatus() {
        return clockingStatus;
    }

    public void setClockingStatus(Integer clockingStatus) {
        this.clockingStatus = clockingStatus;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Integer getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Integer lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(Integer examStatus) {
        this.examStatus = examStatus;
    }

    public Integer getThesisStatus() {
        return thesisStatus;
    }

    public void setThesisStatus(Integer thesisStatus) {
        this.thesisStatus = thesisStatus;
    }

    public Integer getPracticeStatus() {
        return practiceStatus;
    }

    public void setPracticeStatus(Integer practiceStatus) {
        this.practiceStatus = practiceStatus;
    }

    public Integer getGraduationStatus() {
        return graduationStatus;
    }

    public void setGraduationStatus(Integer graduationStatus) {
        this.graduationStatus = graduationStatus;
    }

    public Integer getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(Integer linkStatus) {
        this.linkStatus = linkStatus;
    }

    public Integer getAppLastLoginTime() {
        return appLastLoginTime;
    }

    public void setAppLastLoginTime(Integer appLastLoginTime) {
        this.appLastLoginTime = appLastLoginTime;
    }

    public String getAppIsOnline() {
        return appIsOnline;
    }

    public void setAppIsOnline(String appIsOnline) {
        this.appIsOnline = appIsOnline;
    }

    public Integer getPcLastLoginTime() {
        return pcLastLoginTime;
    }

    public void setPcLastLoginTime(Integer pcLastLoginTime) {
        this.pcLastLoginTime = pcLastLoginTime;
    }

    public String getPcIsOnline() {
        return pcIsOnline;
    }

    public void setPcIsOnline(String pcIsOnline) {
        this.pcIsOnline = pcIsOnline;
    }

    public Integer getIsBandingWx() {
        return isBandingWx;
    }

    public void setIsBandingWx(Integer isBandingWx) {
        this.isBandingWx = isBandingWx;
    }

    /**
     * demo
     * @param args
     */
    public static void main(String[] args ) {
        SynthesizeInfoDto dto = new SynthesizeInfoDto();
        dto.setRollStatus(1);
        dto.setPaymentStatus(1);
        dto.setTextbookStatus(1);
        dto.setStudyStatus(1);
        dto.setClockingStatus(1);
        dto.setExamStatus(1);
        dto.setThesisStatus(1);
        dto.setPracticeStatus(1);
        dto.setGraduationStatus(1);
        dto.setLinkStatus(2);
        dto.setIsOnline("N");
        System.out.println(dto.validationGetStatus());
        System.out.println(GsonUtils.toJson(dto));
        System.out.println(GsonUtils.toBean(GsonUtils.toJson(dto), SynthesizeInfoDto.class));
    }
}
