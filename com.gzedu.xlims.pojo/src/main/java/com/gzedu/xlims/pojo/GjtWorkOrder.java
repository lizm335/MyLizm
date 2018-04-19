package com.gzedu.xlims.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

/**
 * The persistent class for the GJT_WORK_ORDER database table. 工单管理表
 */
@Entity
@Table(name = "GJT_WORK_ORDER")
@NamedQuery(name = "GjtWorkOrder.findAll", query = "SELECT g FROM GjtWorkOrder g")
public class GjtWorkOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	// 废弃一对多
	// @Column(name = "ASSIGN_PERSON", insertable = false, updatable = false)
	// 指定用户
	// private String assignPerson;
	//
	// @OneToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "ASSIGN_PERSON")
	// @NotFound(action = NotFoundAction.IGNORE)
	// private GjtUserAccount gjtUserAccount;

	@Column(name = "CREATED_BY", insertable = false, updatable = false)
	private String createdBy;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATED_BY")
	@NotFound(action = NotFoundAction.IGNORE)
	private GjtUserAccount createUser;

	private String content;// 工单内容

	@Temporal(TemporalType.DATE) // 指定完成时间
	@Column(name = "DEMAND_DATE")
	private Date demandDate;

	@Column(name = "FILE_NAME") // 附件名称
	private String fileName;

	@Column(name = "FILE_URL") // 附件地址
	private String fileUrl;

	@Column(name = "IS_STATE") // 任务完成状态 0-待完成，1-完成，2-关闭
	private String isState;

	private String priority;// 优先权 0-一般，1-优先，2-紧急

	private String title;// 工单标题

	@Column(name = "WORK_ORDER_TYPE") // 工单类型
	private String workOrderType;

	@OneToMany(mappedBy = "gjtWorkOrder", fetch = FetchType.LAZY) // 讨论内容
	@NotFound(action = NotFoundAction.IGNORE)
	@OrderBy(value = "CREATED_DT")
	@Where(clause = "IS_DELETED='N'")
	private List<GjtWorkOrderComment> gjtWorkOrderCommentList;

	@OneToMany(mappedBy = "gjtWorkOrder", fetch = FetchType.LAZY) // 抄送对象
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtWorkOrderUser> gjtWorkOrderUserList;

	@OneToMany(mappedBy = "gjtWorkOrder", fetch = FetchType.LAZY) // 抄送对象
	@NotFound(action = NotFoundAction.IGNORE)
	private List<GjtWorkOrderAssignPerson> gjtWorkOrderAssignPersonList;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDt;

	@Column(name = "IS_DELETED")
	private String isDeleted;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDt;

	private BigDecimal version;

	public GjtWorkOrder() {
	}

	public List<GjtWorkOrderAssignPerson> getGjtWorkOrderAssignPersonList() {
		return gjtWorkOrderAssignPersonList;
	}

	public void setGjtWorkOrderAssignPersonList(List<GjtWorkOrderAssignPerson> gjtWorkOrderAssignPersonList) {
		this.gjtWorkOrderAssignPersonList = gjtWorkOrderAssignPersonList;
	}

	public GjtUserAccount getCreateUser() {
		return createUser;
	}

	public void setCreateUser(GjtUserAccount createUser) {
		this.createUser = createUser;
	}

	public List<GjtWorkOrderComment> getGjtWorkOrderCommentList() {
		return gjtWorkOrderCommentList;
	}

	public void setGjtWorkOrderCommentList(List<GjtWorkOrderComment> gjtWorkOrderCommentList) {
		this.gjtWorkOrderCommentList = gjtWorkOrderCommentList;
	}

	public List<GjtWorkOrderUser> getGjtWorkOrderUserList() {
		return gjtWorkOrderUserList;
	}

	public void setGjtWorkOrderUserList(List<GjtWorkOrderUser> gjtWorkOrderUserList) {
		this.gjtWorkOrderUserList = gjtWorkOrderUserList;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Date getDemandDate() {
		return this.demandDate;
	}

	public void setDemandDate(Date demandDate) {
		this.demandDate = demandDate;
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

	public String getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getIsState() {
		return this.isState;
	}

	public void setIsState(String isState) {
		this.isState = isState;
	}

	public String getPriority() {
		return this.priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getWorkOrderType() {
		return this.workOrderType;
	}

	public void setWorkOrderType(String workOrderType) {
		this.workOrderType = workOrderType;
	}

}