package com.gzedu.xlims.pojo.message;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the GJT_MESSAGE_COMMENT_LIKE database table.
 * 
 */
@Entity
@Table(name = "GJT_MESSAGE_COMMENT_LIKE")
@NamedQuery(name = "GjtMessageCommentLike.findAll", query = "SELECT g FROM GjtMessageCommentLike g")
public class GjtMessageCommentLike implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name = "COMMENT_ID")
	private String commentId;

	@Column(name = "IS_LIKE")
	private String isLike;

	@Column(name = "USER_ID")
	private String userId;

	public GjtMessageCommentLike() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCommentId() {
		return this.commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getIsLike() {
		return this.isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}