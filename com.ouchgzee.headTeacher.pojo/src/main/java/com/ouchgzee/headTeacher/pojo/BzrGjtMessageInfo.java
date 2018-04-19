package com.ouchgzee.headTeacher.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * 消息信息实体类<br>
 * The persistent class for the GJT_MESSAGE_INFO database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_INFO")
// @NamedQuery(name = "GjtMessageInfo.findAll", query = "SELECT g FROM GjtMessageInfo g")
@Deprecated public class BzrGjtMessageInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MESSAGE_ID")
	private String messageId;

	@Column(name = "PUT_USER")
	private String putUser;

	@OneToOne
	@JoinColumn(name = "PUT_USER", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private BzrGjtUserAccount gjtUserAccount;

	private String attachment;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT", insertable = false, updatable = false)
	private Date createdDt;

	private String degree;

	@Column(name = "FILE_NAME")
	private String fileName;

	@Column(name = "FILE_URL")
	private String fileUrl;

	@Lob
	@Column(name = "GET_USER")
	private String getUser;

	@Column(name = "GET_USER_METHOD")
	private String getUserMethod;

	@Column(name = "GET_USER_ROLE")
	private String getUserRole;// 接收者角色

	@Lob
	@Column(name = "INFO_CONTENT")
	private String infoContent;

	@Column(name = "INFO_THEME")
	private String infoTheme;

	@Column(name = "INFO_TOOL")
	private String infoTool;

	/**
	 * 消息类型 1-系统消息 2-教务通知 11-班级公告 12-考试通知 13-学习提醒
	 */
	@Column(name = "INFO_TYPE")
	private String infoType;

	@Column(name = "IS_DELETED", insertable = false)
	private String isDeleted;

	@Column(name = "IS_ENABLED", insertable = false)
	private String isEnabled;

	private String memo;

	@Column(name = "UPDATED_BY", insertable = false)
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT", insertable = false)
	private Date updatedDt;

	@Column(name = "VERSION", insertable = false)
	private BigDecimal version;

	@Column(name = "XX_ID")
	private String xxId;

	@Column(name = "CLASS_ID")
	private String classId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EFFECTIVE_TIME")
	private Date effectiveTime;

	// bi-directional many-to-one association to GjtMessageBox
	@OneToMany(mappedBy = "gjtMessageInfo", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private List<BzrGjtMessageBox> gjtMessageBoxList;

	/**
	 * 临时数据：是否阅读
	 */
	@Transient
	private Boolean colIsNew;

	@Transient
	private Boolean isStick;

	/**
	 * 临时数据：接收总人数
	 */
	@Transient
	private Long colReceiveUserNum;

	/**
	 * 临时数据：阅读人数
	 */
	@Transient
	private Long colReadUserNum;

	/**
	 * 查询条件：发布开始时间，格式yyyy-MM-dd
	 */
	@Transient
	private String condCreatedDtBegin;

	/**
	 * 查询条件：发布结束时间，格式yyyy-MM-dd
	 */
	@Transient
	private String condCreatedDtEnd;

	@Transient
	private String employeeId;

	public BzrGjtMessageInfo() {
	}

	public Boolean getIsStick() {
		return isStick;
	}

	public void setIsStick(Boolean isStick) {
		this.isStick = isStick;
	}

	public Date getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(Date effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getMessageId() {
		return this.messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getGetUser() {
		return this.getUser;
	}

	public void setGetUser(String getUser) {
		this.getUser = getUser;
	}

	public String getGetUserMethod() {
		return this.getUserMethod;
	}

	public void setGetUserMethod(String getUserMethod) {
		this.getUserMethod = getUserMethod;
	}

	public String getGetUserRole() {
		return this.getUserRole;
	}

	public void setGetUserRole(String getUserRole) {
		this.getUserRole = getUserRole;
	}

	public String getInfoContent() {
		return this.infoContent;
	}

	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}

	public String getInfoTheme() {
		return this.infoTheme;
	}

	public void setInfoTheme(String infoTheme) {
		this.infoTheme = infoTheme;
	}

	public String getInfoTool() {
		return this.infoTool;
	}

	public void setInfoTool(String infoTool) {
		this.infoTool = infoTool;
	}

	public String getInfoType() {
		return this.infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsEnabled() {
		return this.isEnabled;
	}

	public void setIsEnabled(String isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

	public BigDecimal getVersion() {
		return this.version;
	}

	public void setVersion(BigDecimal version) {
		this.version = version;
	}

	public String getXxId() {
		return this.xxId;
	}

	public void setXxId(String xxId) {
		this.xxId = xxId;
	}

	public List<BzrGjtMessageBox> getGjtMessageBoxList() {
		return this.gjtMessageBoxList;
	}

	public void setGjtMessageBoxList(List<BzrGjtMessageBox> gjtMessageBoxList) {
		this.gjtMessageBoxList = gjtMessageBoxList;
	}

	public BzrGjtMessageBox addGjtMessageBoxList(BzrGjtMessageBox gjtMessageBoxList) {
		getGjtMessageBoxList().add(gjtMessageBoxList);
		gjtMessageBoxList.setGjtMessageInfo(this);

		return gjtMessageBoxList;
	}

	public BzrGjtMessageBox removeGjtMessageBoxList(BzrGjtMessageBox gjtMessageBoxList) {
		getGjtMessageBoxList().remove(gjtMessageBoxList);
		gjtMessageBoxList.setGjtMessageInfo(null);

		return gjtMessageBoxList;
	}

	/**
	 * @return the gjtUserAccount
	 */
	public BzrGjtUserAccount getGjtUserAccount() {
		return gjtUserAccount;
	}

	/**
	 * @param gjtUserAccount
	 *            the gjtUserAccount to set
	 */
	public void setGjtUserAccount(BzrGjtUserAccount gjtUserAccount) {
		this.gjtUserAccount = gjtUserAccount;
	}

	/**
	 * get 临时数据：是否阅读
	 *
	 * @return the colIsNew
	 */
	public Boolean getColIsNew() {
		return colIsNew;
	}

	/**
	 * set 临时数据：是否阅读
	 *
	 * @return the colIsNew
	 */
	public void setColIsNew(Boolean colIsNew) {
		this.colIsNew = colIsNew;
	}

	/**
	 * get 临时数据：接收总人数
	 * 
	 * @return the colReceiveUserNum
	 */
	public Long getColReceiveUserNum() {
		return colReceiveUserNum;
	}

	/**
	 * set 临时数据：接收总人数
	 * 
	 * @param colReceiveUserNum
	 *            the colReceiveUserNum to set
	 */
	public void setColReceiveUserNum(Long colReceiveUserNum) {
		this.colReceiveUserNum = colReceiveUserNum;
	}

	/**
	 * get 临时数据：阅读人数
	 * 
	 * @return the colReadUserNum
	 */
	public Long getColReadUserNum() {
		return colReadUserNum;
	}

	/**
	 * set 临时数据：阅读人数
	 * 
	 * @param colReadUserNum
	 *            the colReadUserNum to set
	 */
	public void setColReadUserNum(Long colReadUserNum) {
		this.colReadUserNum = colReadUserNum;
	}

	/**
	 * get 查询条件：发布开始时间，格式yyyy-MM-dd
	 * 
	 * @return the condCreatedDtBegin
	 */
	public String getCondCreatedDtBegin() {
		return condCreatedDtBegin;
	}

	/**
	 * set 查询条件：发布开始时间，格式yyyy-MM-dd
	 * 
	 * @param condCreatedDtBegin
	 *            the condCreatedDtBegin to set
	 */
	public void setCondCreatedDtBegin(String condCreatedDtBegin) {
		this.condCreatedDtBegin = condCreatedDtBegin;
	}

	/**
	 * get 查询条件：发布结束时间，格式yyyy-MM-dd
	 * 
	 * @return the condCreatedDtEnd
	 */
	public String getCondCreatedDtEnd() {
		return condCreatedDtEnd;
	}

	/**
	 * set 查询条件：发布结束时间，格式yyyy-MM-dd
	 * 
	 * @param condCreatedDtEnd
	 *            the condCreatedDtEnd to set
	 */
	public void setCondCreatedDtEnd(String condCreatedDtEnd) {
		this.condCreatedDtEnd = condCreatedDtEnd;
	}

	public String getPutUser() {
		return putUser;
	}

	public void setPutUser(String putUser) {
		this.putUser = putUser;
	}

}