package com.gzedu.xlims.pojo.openClass;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by paul on 2017/8/23.
 * 直播表
 */
@Entity
@Table(name = "GJT_ONLINE_LESSON")
@NamedQuery(name = "GjtOnlineLesson.findAll", query = "SELECT gol FROM GjtOnlineLesson gol")
public class GjtOnlineLesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ONLINE_ID")
    private String onlineId;

    @Column(name = "STU_TOKEN")
    private String stuToken;
    
    @Column(name = "ONLINE_NAME")
    private String onlineName;

    @Column(name = "ONLINE_DESC")
    private String onlineDesc;

    @Column(name = "TCH_TOKEN")
    private String tchToken;

    @Column(name = "STU_CLIENT_TOKEN")
    private String stuClientToken;

    @Column(name = "ASSISTANT_TOKEN")
    private String assistantToken;

    @Column(name = "ROOM_NUMBER")
    private String roomNumber;

    @Column(name = "TCH_URL")
    private String tchUrl;

    @Column(name = "STU_URL")
    private String stuUrl;

    @Column(name = "SDK_ID")
    private String sdkId;

    @Column(name = "VIDEO_URL")
    private String videoUrl;

    @Column(name = "VIDEO_TOKEN")
    private String videoToken;

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
    @Column(name = "START_DT")
    private Date startDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DT")
    private Date endDt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "INVALID_DT")
    private Date invalidDt;

    public String getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(String onlineId) {
        this.onlineId = onlineId;
    }

    public String getOnlineName() {
        return onlineName;
    }

    public void setOnlineName(String onlineName) {
        this.onlineName = onlineName;
    }

    public String getOnlineDesc() {
        return onlineDesc;
    }

    public void setOnlineDesc(String onlineDesc) {
        this.onlineDesc = onlineDesc;
    }

    public String getTchToken() {
        return tchToken;
    }

    public void setTchToken(String tchToken) {
        this.tchToken = tchToken;
    }

    public String getStuClientToken() {
        return stuClientToken;
    }

    public void setStuClientToken(String stuClientToken) {
        this.stuClientToken = stuClientToken;
    }

    public String getAssistantToken() {
        return assistantToken;
    }

    public void setAssistantToken(String assistantToken) {
        this.assistantToken = assistantToken;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getTchUrl() {
        return tchUrl;
    }

    public void setTchUrl(String tchUrl) {
        this.tchUrl = tchUrl;
    }

    public String getStuUrl() {
        return stuUrl;
    }

    public void setStuUrl(String stuUrl) {
        this.stuUrl = stuUrl;
    }

    public String getSdkId() {
        return sdkId;
    }

    public void setSdkId(String sdkId) {
        this.sdkId = sdkId;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoToken() {
        return videoToken;
    }

    public void setVideoToken(String videoToken) {
        this.videoToken = videoToken;
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

    public Date getStartDt() {
        return startDt;
    }

    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getEndDt() {
        return endDt;
    }

    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }

    public Date getInvalidDt() {
        return invalidDt;
    }

    public void setInvalidDt(Date invalidDt) {
        this.invalidDt = invalidDt;
    }

    public void setStuToken(String stuToken) {
        this.stuToken = stuToken;
    }

    public String getStuToken() {
        return stuToken;
    }

    @Override
    public String toString() {
        return "GjtOnlineLesson{" +
                "onlineId='" + onlineId + '\'' +
                ", stuToken='" + stuToken + '\'' +
                ", onlineName='" + onlineName + '\'' +
                ", onlineDesc='" + onlineDesc + '\'' +
                ", tchToken='" + tchToken + '\'' +
                ", stuClientToken='" + stuClientToken + '\'' +
                ", assistantToken='" + assistantToken + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", tchUrl='" + tchUrl + '\'' +
                ", stuUrl='" + stuUrl + '\'' +
                ", sdkId='" + sdkId + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", videoToken='" + videoToken + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedby='" + updatedby + '\'' +
                ", isDeleted='" + isDeleted + '\'' +
                ", remark='" + remark + '\'' +
                ", createdDt=" + createdDt +
                ", updatedDt=" + updatedDt +
                ", startDt=" + startDt +
                ", endDt=" + endDt +
                ", invalidDt=" + invalidDt +
                '}';
    }
}
