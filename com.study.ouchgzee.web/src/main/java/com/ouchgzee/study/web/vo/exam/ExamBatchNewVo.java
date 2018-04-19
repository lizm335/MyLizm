package com.ouchgzee.study.web.vo.exam;

import java.util.Date;

/**
 * 考试计划VO
 * 功能说明：
 *      时间都精确到yyyy-MM-dd HH:mm
 * @author huangyifei@eenet.com
 * @Date 2018年03月22日
 * @version 3.0
 */
public class ExamBatchNewVo {

    private String examBatchId;

    private String examBatchCode;

    private String name;

    private Date bookEnd;  //预约结束时间

    private Date bookSt;  //预约开始时间

    private int isBook; // 是否预约中

    private Date onlineSt;  //网考、大作业开始时间

    private Date onlineEnd;  //网考、大作业结束时间

    private int isOnline; // 是否网考、大作业中

    private Date provinceOnlineSt;  //省网考开始时间

    private Date provinceOnlineEnd;  //省网考结束时间

    private int isProvinceOnline; // 是否省网考中

    private Date paperSt;  //笔考开始时间

    private Date paperEnd;  //笔考结束时间

    private int isPaper; // 是否笔考中

    private Date machineSt;  //机考开始时间

    private Date machineEnd;  //机考结束时间

    private int isMachine; // 是否机考中

    private Date shapeEnd;  //形考任务登记截止时间

    private Date recordEnd;  //成绩登记结束时间

    private Date thesisEnd;  //论文截止时间

    private Date reportEnd;  //报告截止时间

    private Date bktkBookEnd;  // 本科统考预约结束时间

    private Date bktkBookSt;  // 本科统考预约开始时间

    private Date xwyyBookEnd;  // 学位英语报考结束时间

    private Date xwyyBookSt;  // 学位英语报考开始时间

    private Date bktkExamEnd;  // 本科统考考试结束时间

    private Date bktkExamSt;  // 本科统考考试开始时间

    private Date xwyyExamSt;  // 学位英语考试开始时间

    private Date xwyyExamEnd;  // 学位英语考试结束时间

    private int status; // 状态 0-未安排 1-预约中 2-考试中 3-登记中 4-已登记

    public String getExamBatchId() {
        return examBatchId;
    }

    public void setExamBatchId(String examBatchId) {
        this.examBatchId = examBatchId;
    }

    public String getExamBatchCode() {
        return examBatchCode;
    }

    public void setExamBatchCode(String examBatchCode) {
        this.examBatchCode = examBatchCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBookEnd() {
        return bookEnd;
    }

    public void setBookEnd(Date bookEnd) {
        this.bookEnd = bookEnd;
    }

    public Date getBookSt() {
        return bookSt;
    }

    public void setBookSt(Date bookSt) {
        this.bookSt = bookSt;
    }

    public Date getOnlineEnd() {
        return onlineEnd;
    }

    public void setOnlineEnd(Date onlineEnd) {
        this.onlineEnd = onlineEnd;
    }

    public Date getOnlineSt() {
        return onlineSt;
    }

    public void setOnlineSt(Date onlineSt) {
        this.onlineSt = onlineSt;
    }

    public Date getProvinceOnlineSt() {
        return provinceOnlineSt;
    }

    public void setProvinceOnlineSt(Date provinceOnlineSt) {
        this.provinceOnlineSt = provinceOnlineSt;
    }

    public Date getProvinceOnlineEnd() {
        return provinceOnlineEnd;
    }

    public void setProvinceOnlineEnd(Date provinceOnlineEnd) {
        this.provinceOnlineEnd = provinceOnlineEnd;
    }

    public Date getPaperSt() {
        return paperSt;
    }

    public void setPaperSt(Date paperSt) {
        this.paperSt = paperSt;
    }

    public Date getPaperEnd() {
        return paperEnd;
    }

    public void setPaperEnd(Date paperEnd) {
        this.paperEnd = paperEnd;
    }

    public Date getMachineSt() {
        return machineSt;
    }

    public void setMachineSt(Date machineSt) {
        this.machineSt = machineSt;
    }

    public Date getMachineEnd() {
        return machineEnd;
    }

    public void setMachineEnd(Date machineEnd) {
        this.machineEnd = machineEnd;
    }

    public Date getShapeEnd() {
        return shapeEnd;
    }

    public void setShapeEnd(Date shapeEnd) {
        this.shapeEnd = shapeEnd;
    }

    public Date getThesisEnd() {
        return thesisEnd;
    }

    public void setThesisEnd(Date thesisEnd) {
        this.thesisEnd = thesisEnd;
    }

    public Date getReportEnd() {
        return reportEnd;
    }

    public void setReportEnd(Date reportEnd) {
        this.reportEnd = reportEnd;
    }

    public Date getRecordEnd() {
        return recordEnd;
    }

    public void setRecordEnd(Date recordEnd) {
        this.recordEnd = recordEnd;
    }

    public Date getBktkBookEnd() {
        return bktkBookEnd;
    }

    public void setBktkBookEnd(Date bktkBookEnd) {
        this.bktkBookEnd = bktkBookEnd;
    }

    public Date getBktkBookSt() {
        return bktkBookSt;
    }

    public void setBktkBookSt(Date bktkBookSt) {
        this.bktkBookSt = bktkBookSt;
    }

    public Date getXwyyBookEnd() {
        return xwyyBookEnd;
    }

    public void setXwyyBookEnd(Date xwyyBookEnd) {
        this.xwyyBookEnd = xwyyBookEnd;
    }

    public Date getXwyyBookSt() {
        return xwyyBookSt;
    }

    public void setXwyyBookSt(Date xwyyBookSt) {
        this.xwyyBookSt = xwyyBookSt;
    }

    public Date getBktkExamEnd() {
        return bktkExamEnd;
    }

    public void setBktkExamEnd(Date bktkExamEnd) {
        this.bktkExamEnd = bktkExamEnd;
    }

    public Date getBktkExamSt() {
        return bktkExamSt;
    }

    public void setBktkExamSt(Date bktkExamSt) {
        this.bktkExamSt = bktkExamSt;
    }

    public Date getXwyyExamEnd() {
        return xwyyExamEnd;
    }

    public void setXwyyExamEnd(Date xwyyExamEnd) {
        this.xwyyExamEnd = xwyyExamEnd;
    }

    public Date getXwyyExamSt() {
        return xwyyExamSt;
    }

    public void setXwyyExamSt(Date xwyyExamSt) {
        this.xwyyExamSt = xwyyExamSt;
    }

    public int getIsBook() {
        Date now = new Date();
        if(this.bookSt != null && this.bookEnd != null) {
            return now.after(this.bookSt) && now.before(this.bookEnd) ? 1 : 0;
        }
        return 0;
    }

    public int getIsOnline() {
        Date now = new Date();
        if(this.onlineSt != null && this.onlineEnd != null) {
            return now.after(this.onlineSt) && now.before(this.onlineEnd) ? 1 : 0;
        }
        return 0;
    }

    public int getIsProvinceOnline() {
        Date now = new Date();
        if(this.provinceOnlineSt != null && this.provinceOnlineEnd != null) {
            return now.after(this.provinceOnlineSt) && now.before(this.provinceOnlineEnd) ? 1 : 0;
        }
        return 0;
    }

    public int getIsPaper() {
        Date now = new Date();
        if(this.paperSt != null && this.paperEnd != null) {
            return now.after(this.paperSt) && now.before(this.paperEnd) ? 1 : 0;
        }
        return 0;
    }

    public int getIsMachine() {
        Date now = new Date();
        if(this.machineSt != null && this.machineEnd != null) {
            return now.after(this.machineSt) && now.before(this.machineEnd) ? 1 : 0;
        }
        return 0;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
