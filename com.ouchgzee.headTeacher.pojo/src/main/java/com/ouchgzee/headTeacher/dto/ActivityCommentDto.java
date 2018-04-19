/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.dto;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;

/**
 * 功能说明：
 * 
 * @author 王城
 * @Date 2016年7月20日
 * @version 1.0
 *
 */
public class ActivityCommentDto {
	/**
	 * 评语
	 */
	private String comments;
	/**
	 * 评论日期
	 */
	private Date commentDt;
	/**
	 * 评论人姓名
	 */
	private String name;
	/**
	 * 评论人头像
	 */
	private String avatar;

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setCOMMENTS(Clob comments) {
		try {
			this.comments = comments != null
					? comments.getSubString(1, (int) comments.length()) : null;
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
