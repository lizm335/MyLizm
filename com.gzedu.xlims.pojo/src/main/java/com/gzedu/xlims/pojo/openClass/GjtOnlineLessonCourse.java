package com.gzedu.xlims.pojo.openClass;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtTermCourseinfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by paul on 2017/8/23.
 */
@Entity
@Table(name = "GJT_ONLINE_LESSON_COURSE")
@NamedQuery(name = "GjtOnlineLessonCourse.findAll", query = "SELECT golc FROM GjtOnlineLessonCourse golc ")
public class GjtOnlineLessonCourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ONLINE_LESSON_COURSE_ID")
    private String onlineLessonCourseId;

    @Column(name = "ONLINE_ID")
    private String onlineId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ONLINE_ID" , insertable = false, updatable = false)
    private GjtOnlineLesson gjtOnlineLesson;



    @Column(name = "TERMCOURSE_ID")
    private String termCourseId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERMCOURSE_ID" , insertable = false, updatable = false)
    private GjtTermCourseinfo gjtTermCourseinfo;


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

    public void setTermCourseId(String termCourseId) {
        this.termCourseId = termCourseId;
    }

    public String getTermCourseId() {
        return termCourseId;
    }

    public String getOnlineLessonCourseId() {
        return onlineLessonCourseId;
    }

    public void setOnlineLessonCourseId(String onlineLessonCourseId) {
        this.onlineLessonCourseId = onlineLessonCourseId;
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


    public void setGjtTermCourseinfo(GjtTermCourseinfo gjtTermCourseinfo) {
        this.gjtTermCourseinfo = gjtTermCourseinfo;
    }

    public GjtTermCourseinfo getGjtTermCourseinfo() {
        return gjtTermCourseinfo;
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
        return "GjtOnlineLessonCourse{" +
                "onlineLessonCourseId='" + onlineLessonCourseId + '\'' +
                ", onlineId='" + onlineId + '\'' +
                ", gjtOnlineLesson=" + gjtOnlineLesson +
                ", termCourseId='" + termCourseId + '\'' +
                ", gjtTermCourseinfo=" + gjtTermCourseinfo +
                ", createdBy='" + createdBy + '\'' +
                ", updatedby='" + updatedby + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", remark='" + remark + '\'' +
                ", createdDt=" + createdDt +
                ", updatedDt=" + updatedDt +
                '}';
    }
}
