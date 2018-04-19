/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年6月17日
 * @version 2.5
 *
 */
public class LcmsMutualSubjectDto {
	String subject_id; // 答疑ID
	String subject_title;// 答疑标题
	String subject_content;// 答疑内容
	String class_id;// 班级ID
	String termcourse_id;// 期课程ID
	String is_commend;// 是否常见问题 N和Y
	String res_path;// 图片地址 英文逗号隔开
	@Temporal(TemporalType.TIMESTAMP)
	Date created_dt;// 创建时间
	String create_account_id;// 创建人ID gjt_user_account Id
	String forward_account_id;// 指定回复人ID gjt_user_account Id
	String initial_account_id;// 初始指定人ID gjt_user_account Id
	String isdeleted;// 是否删除
	BigDecimal reply_count;// 回复总数
	String studentId;// 兼容旧平台的数据
	String employeeId;// 兼容旧平台的数据
	String user_type;// 兼容用户类型 stud和tchr

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

	public BigDecimal getReply_count() {
		return reply_count;
	}

	public void setReply_count(BigDecimal reply_count) {
		this.reply_count = reply_count;
	}

	public String getSubject_id() {
		return subject_id;
	}

	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}

	public String getSubject_title() {
		return subject_title;
	}

	public void setSubject_title(String subject_title) {
		this.subject_title = subject_title;
	}

	public String getSubject_content() {
		return subject_content;
	}

	public void setSubject_content(String subject_content) {
		this.subject_content = subject_content;
	}

	public String getClass_id() {
		return class_id;
	}

	public void setClass_id(String class_id) {
		this.class_id = class_id;
	}

	public String getTermcourse_id() {
		return termcourse_id;
	}

	public void setTermcourse_id(String termcourse_id) {
		this.termcourse_id = termcourse_id;
	}

	public String getIs_commend() {
		return is_commend;
	}

	public void setIs_commend(String is_commend) {
		this.is_commend = is_commend;
	}

	public String getRes_path() {
		return res_path;
	}

	public void setRes_path(String res_path) {
		this.res_path = res_path;
	}

	public Date getCreated_dt() {
		return created_dt;
	}

	public void setCreated_dt(Date created_dt) {
		this.created_dt = created_dt;
	}

	public String getCreate_account_id() {
		return create_account_id;
	}

	public void setCreate_account_id(String create_account_id) {
		this.create_account_id = create_account_id;
	}

	public String getForward_account_id() {
		return forward_account_id;
	}

	public void setForward_account_id(String forward_account_id) {
		this.forward_account_id = forward_account_id;
	}

	public String getInitial_account_id() {
		return initial_account_id;
	}

	public void setInitial_account_id(String initial_account_id) {
		this.initial_account_id = initial_account_id;
	}

	public String getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(String isdeleted) {
		this.isdeleted = isdeleted;
	}

}
