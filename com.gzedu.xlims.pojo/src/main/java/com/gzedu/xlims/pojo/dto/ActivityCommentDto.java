/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年2月28日
 * @version 2.5
 *
 */
public class ActivityCommentDto {

	private String comments;// 评语

	@Temporal(TemporalType.TIMESTAMP) // 评论日期
	private Date commentDt;

	private String name;// 评论人姓名

	private String avatar;// 评论人头像

	public String getComments() {
		return comments;
	}

	private String commentDate;

	private BigDecimal ROWNUM_;

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

	public String getCommentDate() {
		return commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setCOMMENTS(Clob comments) {
		try {
			this.comments = comments != null ? comments.getSubString(1, (int) comments.length()) : null;
		} catch (SQLException e) {
		}
	}

	/**
	 * @return the commentDt
	 */
	public Date getCommentDt() {
		return commentDt;
	}

	/**
	 * @param commentDt
	 *            the commentDt to set
	 */
	public void setCOMMENT_DT(Date commentDt) {
		this.commentDt = commentDt;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setXM(String name) {
		this.name = name;
	}

	/**
	 * @return the avatar
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * @param avatar
	 *            the avatar to set
	 */
	public void setAVATAR(String avatar) {
		this.avatar = avatar;
	}

}
