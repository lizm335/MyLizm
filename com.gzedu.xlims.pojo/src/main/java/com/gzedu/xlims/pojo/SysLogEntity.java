package com.gzedu.xlims.pojo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 系统日志
 *
 * @author huangyifei
 * @email huangyifei@eenet.com
 * @date 2017年07月24日
 */
@Entity
@Table(name = "SYS_LOG")
@NamedQuery(name = "SysLogEntity.findAll", query = "SELECT s FROM SysLogEntity s")
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;
	//用户名
	@Column(name = "USERNAME")
	private String username;
	//用户操作
	@Column(name = "OPERATION")
	private String operation;
	//请求方法
	@Column(name = "METHOD")
	private String method;
	//请求参数
	@Column(name = "PARAMS")
	private String params;
	//执行时长(毫秒)
	@Column(name = "TIME")
	private Long time;
	//错误信息
	@Column(name = "ERROR")
	private String error;
	//IP地址
	@Column(name = "IP")
	private String ip;
	//创建时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE")
	private Date createDate;
	// 平台类型：1、管理平台；2、辅导教师平台；3、班主任平台；4、督导教师平台；5、学习空间
	@Column(name = "PLATFROM_TYPE")
	private Integer platfromType;

	/**
	 * 设置：
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * 获取：用户名
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * 设置：用户操作
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	/**
	 * 获取：用户操作
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * 设置：请求方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * 获取：请求方法
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 设置：请求参数
	 */
	public void setParams(String params) {
		this.params = params;
	}
	/**
	 * 获取：请求参数
	 */
	public String getParams() {
		return params;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	/**
	 * 设置：IP地址
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * 获取：IP地址
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	public Integer getPlatfromType() {
		return platfromType;
	}

	public void setPlatfromType(Integer platfromType) {
		this.platfromType = platfromType;
	}
}
