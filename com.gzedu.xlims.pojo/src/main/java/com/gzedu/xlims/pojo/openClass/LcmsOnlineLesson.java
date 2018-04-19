package com.gzedu.xlims.pojo.openClass;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;



/**
 * The persistent class for the LCMS_ONLINE_LESSON database table.
 * 
 */
@Entity
@Table(name = "LCMS_ONLINE_LESSON")
@NamedQuery(name="LcmsOnlineLesson.findAll", query="SELECT l FROM LcmsOnlineLesson l")
public class LcmsOnlineLesson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String assistanttoken;

	private String clientjoin;

	private String code;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	private String description;

	private String duration;

	@Column(name = "IMG_URL")
	private String imgUrl;// 直播封面图

	@Column(name = "ACTIVITY_CONTENT")
	private String activityContent;// app活动宣传图

	@Temporal(TemporalType.TIMESTAMP)
	private Date invaliddate;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	private String loginname;

	private String maxattendees;

	private String message;

	@Column(name="NUMBER_")
	private String number;

	@Column(name = "ONLINETUTOR_DESC")
	private String onlinetutorDesc;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ONLINETUTOR_FINISH")
	private Date onlinetutorFinish;

	@Column(name="ONLINETUTOR_ID")
	private String onlinetutorId;

	@Column(name = "ONLINETUTOR_NAME")
	private String onlinetutorName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ONLINETUTOR_START")
	private Date onlinetutorStart;

	@Column(name = "ONLINETUTOR_TYPE")
	private String onlinetutorType;

	@Column(name="PASSWORD_")
	private String password;

	private String realtime;

	private String scene;

	private String scheduleinfo;

	@Column(name="SDK_ID")
	private String sdkId;

	private String sec;

	private String speakerinfo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;

	private String studentclienttoken;

	private String studentjoinurl;

	private String studenttoken;

	private String subject;

	private String teacherjoinurl;

	private String teachertoken;

	private String uicolor;

	private String uimode;

	private String uivideo;

	private String uiwindow;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private String upgrade;

	private String videojoinurl;

	private String videotoken;

	private String webjoin;

	private int lessonType;// 0-课程直播 1-活动直播
	
	private String onlinetutorLabel;// 直播标签

	@OneToMany
	@JoinColumn(name = "ONLINETUTOR_ID", referencedColumnName = "ONLINETUTOR_ID")
	@Where(clause = "IS_DELETED='N'")
	private List<LcmsOnlineObject> lcmsOnlineObjects;

	public LcmsOnlineLesson() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAssistanttoken() {
		return this.assistanttoken;
	}

	public void setAssistanttoken(String assistanttoken) {
		this.assistanttoken = assistanttoken;
	}

	public String getClientjoin() {
		return this.clientjoin;
	}

	public void setClientjoin(String clientjoin) {
		this.clientjoin = clientjoin;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDuration() {
		return this.duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Date getInvaliddate() {
		return this.invaliddate;
	}

	public void setInvaliddate(Date invaliddate) {
		this.invaliddate = invaliddate;
	}

	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getMaxattendees() {
		return this.maxattendees;
	}

	public void setMaxattendees(String maxattendees) {
		this.maxattendees = maxattendees;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getOnlinetutorId() {
		return this.onlinetutorId;
	}

	public void setOnlinetutorId(String onlinetutorId) {
		this.onlinetutorId = onlinetutorId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealtime() {
		return this.realtime;
	}

	public void setRealtime(String realtime) {
		this.realtime = realtime;
	}

	public String getScene() {
		return this.scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getScheduleinfo() {
		return this.scheduleinfo;
	}

	public void setScheduleinfo(String scheduleinfo) {
		this.scheduleinfo = scheduleinfo;
	}

	public String getSdkId() {
		return this.sdkId;
	}

	public void setSdkId(String sdkId) {
		this.sdkId = sdkId;
	}

	public String getSec() {
		return this.sec;
	}

	public void setSec(String sec) {
		this.sec = sec;
	}

	public String getSpeakerinfo() {
		return this.speakerinfo;
	}

	public void setSpeakerinfo(String speakerinfo) {
		this.speakerinfo = speakerinfo;
	}

	public Date getStartdate() {
		return this.startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public String getStudentclienttoken() {
		return this.studentclienttoken;
	}

	public void setStudentclienttoken(String studentclienttoken) {
		this.studentclienttoken = studentclienttoken;
	}

	public String getStudentjoinurl() {
		return this.studentjoinurl;
	}

	public void setStudentjoinurl(String studentjoinurl) {
		this.studentjoinurl = studentjoinurl;
	}

	public String getStudenttoken() {
		return this.studenttoken;
	}

	public void setStudenttoken(String studenttoken) {
		this.studenttoken = studenttoken;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTeacherjoinurl() {
		return this.teacherjoinurl;
	}

	public void setTeacherjoinurl(String teacherjoinurl) {
		this.teacherjoinurl = teacherjoinurl;
	}

	public String getTeachertoken() {
		return this.teachertoken;
	}

	public void setTeachertoken(String teachertoken) {
		this.teachertoken = teachertoken;
	}

	public String getUicolor() {
		return this.uicolor;
	}

	public void setUicolor(String uicolor) {
		this.uicolor = uicolor;
	}

	public String getUimode() {
		return this.uimode;
	}

	public void setUimode(String uimode) {
		this.uimode = uimode;
	}

	public String getUivideo() {
		return this.uivideo;
	}

	public void setUivideo(String uivideo) {
		this.uivideo = uivideo;
	}

	public String getUiwindow() {
		return this.uiwindow;
	}

	public void setUiwindow(String uiwindow) {
		this.uiwindow = uiwindow;
	}

	public String getUpgrade() {
		return this.upgrade;
	}

	public void setUpgrade(String upgrade) {
		this.upgrade = upgrade;
	}

	public String getVideojoinurl() {
		return this.videojoinurl;
	}

	public void setVideojoinurl(String videojoinurl) {
		this.videojoinurl = videojoinurl;
	}

	public String getVideotoken() {
		return this.videotoken;
	}

	public void setVideotoken(String videotoken) {
		this.videotoken = videotoken;
	}

	public String getWebjoin() {
		return this.webjoin;
	}

	public void setWebjoin(String webjoin) {
		this.webjoin = webjoin;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getActivityContent() {
		return activityContent;
	}

	public void setActivityContent(String activityContent) {
		this.activityContent = activityContent;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOnlinetutorDesc() {
		return onlinetutorDesc;
	}

	public void setOnlinetutorDesc(String onlinetutorDesc) {
		this.onlinetutorDesc = onlinetutorDesc;
	}

	public Date getOnlinetutorFinish() {
		return onlinetutorFinish;
	}

	public void setOnlinetutorFinish(Date onlinetutorFinish) {
		this.onlinetutorFinish = onlinetutorFinish;
	}

	public String getOnlinetutorName() {
		return onlinetutorName;
	}

	public void setOnlinetutorName(String onlinetutorName) {
		this.onlinetutorName = onlinetutorName;
	}

	public Date getOnlinetutorStart() {
		return onlinetutorStart;
	}

	public void setOnlinetutorStart(Date onlinetutorStart) {
		this.onlinetutorStart = onlinetutorStart;
	}

	public String getOnlinetutorType() {
		return onlinetutorType;
	}

	public void setOnlinetutorType(String onlinetutorType) {
		this.onlinetutorType = onlinetutorType;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public int getLessonType() {
		return lessonType;
	}

	public void setLessonType(int lessonType) {
		this.lessonType = lessonType;
	}

	public List<LcmsOnlineObject> getLcmsOnlineObjects() {
		return lcmsOnlineObjects;
	}

	public void setLcmsOnlineObjects(List<LcmsOnlineObject> lcmsOnlineObjects) {
		this.lcmsOnlineObjects = lcmsOnlineObjects;
	}

	public String getOnlinetutorLabel() {
		return onlinetutorLabel;
	}

	public void setOnlinetutorLabel(String onlinetutorLabel) {
		this.onlinetutorLabel = onlinetutorLabel;
	}

}