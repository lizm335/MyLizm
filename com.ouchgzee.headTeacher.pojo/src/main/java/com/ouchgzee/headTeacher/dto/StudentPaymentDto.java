/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dto;

import java.util.Date;
import java.util.Map;

/**
 * 缴费信息DTO<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月26日
 * @version 2.5
 * @since JDK 1.7
 */
public class StudentPaymentDto {

    private String studentId;

    private String loginAccount;

    private String xm;

    private String sfzh;

    private String sjh;

    /**
     * 学习状态 1-正常学习 0-停止学习
     */
    private String learningState;

    /**
     * 逾期次数
     */
    private Integer overdueCount;

    /**
     * 欠费金额
     */
    private Double debtAmount;

    /**
     * 当前应缴金额
     */
    private Double currentRecAmt;

    /**
     * 最迟缴费时间
     */
    private Date currentRecDate;

    /**
     * 当前缴费状态 1-已缴费 2-未开始 3-欠费
     */
    private Integer currentStatus;

    /**
     * 缴费详情
     */
    private Map paymentDetail;

    /**
     * @return the studentId
     */
    public String getStudentId() {
        return studentId;
    }

    /**
     * @param studentId
     *            the studentId to set
     */
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    /**
     * @return the xm
     */
    public String getXm() {
        return xm;
    }

    /**
     * @param xm
     *            the xm to set
     */
    public void setXm(String xm) {
        this.xm = xm;
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

    public Integer getOverdueCount() {
        return overdueCount;
    }

    public void setOverdueCount(Integer overdueCount) {
        this.overdueCount = overdueCount;
    }

    public Double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(Double debtAmount) {
        this.debtAmount = debtAmount;
    }

    public Double getCurrentRecAmt() {
        return currentRecAmt;
    }

    public void setCurrentRecAmt(Double currentRecAmt) {
        this.currentRecAmt = currentRecAmt;
    }

    public Date getCurrentRecDate() {
        return currentRecDate;
    }

    public void setCurrentRecDate(Date currentRecDate) {
        this.currentRecDate = currentRecDate;
    }

    public Integer getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Integer currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getLearningState() {
        return learningState;
    }

    public void setLearningState(String learningState) {
        this.learningState = learningState;
    }

    public Map getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(Map paymentDetail) {
        this.paymentDetail = paymentDetail;
    }
}
