package com.gzedu.xlims.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 用户行为记录实体类
 *
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年08月08日
 */
@Entity
@Table(name = "GJT_USER_BEHAVIOR")
@NamedQuery(name = "GjtUserBehavior.findAll", query = "SELECT s FROM GjtUserBehavior s")
public class GjtUserBehavior implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;
	//用户ID
	@Column(name = "USER_ID")
	private String userId;
	//用户行为
	@Column(name = "OPERATION")
	private String operation;
	//行为描述
	@Column(name = "REMARK")
	private String remark;
	// 平台类型：1、管理平台；2、辅导教师平台；3、班主任平台；4、督导教师平台；5、个人中心
	@Column(name = "PLATFROM_TYPE")
	private Integer platfromType;
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPlatfromType() {
		return platfromType;
	}

	public void setPlatfromType(Integer platfromType) {
		this.platfromType = platfromType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
