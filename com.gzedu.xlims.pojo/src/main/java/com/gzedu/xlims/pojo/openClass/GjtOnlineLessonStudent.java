package com.gzedu.xlims.pojo.openClass;


import com.gzedu.xlims.pojo.GjtStudentInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by paul on 2017/8/23.
 */
@Entity
@Table(name = "GJT_ONLINE_LESSON_STUDENT")
@NamedQuery(name = "GjtOnlineLessonStudent.findAll", query = "SELECT gols FROM GjtOnlineLessonStudent gols ")
public class GjtOnlineLessonStudent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ONLINE_LESSON_STUDENT_ID")
    private String onlineLessonStudentId;

    @Column(name = "REC_ID")
    private String recId;

    @Column(name = "ONLINE_ID")
    private String onlineId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONLINE_ID" , insertable = false, updatable = false)
    private GjtOnlineLesson gjtOnlineLesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID" )
    private GjtStudentInfo gjtStudentInfo;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_BY")
    private String updatedby;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "REMARK")
    private String remark;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DT")
    private Date createdDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_DT")
    private Date updatedDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGIN_DT")
    private Date loginDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGOUT_DT")
    private Date logoutDt;

    public String getOnlineLessonStudentId() {
        return onlineLessonStudentId;
    }

    public void setOnlineLessonStudentId(String onlineLessonStudentId) {
        this.onlineLessonStudentId = onlineLessonStudentId;
    }

    public void setLoginDt(Date loginDt) {
        this.loginDt = loginDt;
    }

    public void setLogoutDt(Date logoutDt) {
        this.logoutDt = logoutDt;
    }

    public Date getLoginDt() {
        return loginDt;
    }

    public Date getLogoutDt() {
        return logoutDt;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String studentId) {
        this.recId = recId;
    }

    public String getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(String onlineId) {
        this.onlineId = onlineId;
    }

    public GjtOnlineLesson getGjtOnlineLesson() {
        return gjtOnlineLesson;
    }

    public void setGjtOnlineLesson(GjtOnlineLesson gjtOnlineLesson) {
        this.gjtOnlineLesson = gjtOnlineLesson;
    }

    public GjtStudentInfo getGjtStudentInfo() {
        return gjtStudentInfo;
    }

    public void setGjtStudentInfo(GjtStudentInfo gjtStudentInfo) {
        this.gjtStudentInfo = gjtStudentInfo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public Date getUpdatedDt() {
        return updatedDt;
    }

    public void setUpdatedDt(Date updatedDt) {
        this.updatedDt = updatedDt;
    }

    @Override
    public String toString() {
        return "GjtOnlineLessonStudent{" +
                "onlineLessonStudentId='" + onlineLessonStudentId + '\'' +
                ", recId='" + recId + '\'' +
                ", onlineId='" + onlineId + '\'' +
                ", gjtOnlineLesson=" + gjtOnlineLesson +
                ", gjtStudentInfo=" + gjtStudentInfo +
                ", createdBy='" + createdBy + '\'' +
                ", updatedby='" + updatedby + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", remark='" + remark + '\'' +
                ", createdDt=" + createdDt +
                ", updatedDt=" + updatedDt +
                ", loginDt=" + loginDt +
                ", logoutDt=" + logoutDt +
                '}';
    }
}
