/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.vo;

import java.util.List;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2018年2月28日
 * @version 3.0
 *
 */
public class GjtMessageCommentVO {
	private String userName;
	private String createDate;
	private String platfrom;
	private String content;
	private List<String> imgUrls;
	private long likeCount;
	private Boolean isLike;
	private List<GjtMessageCommentDetailsVO> commentDetailsList;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getPlatfrom() {
		return platfrom;
	}

	public void setPlatfrom(String platfrom) {
		this.platfrom = platfrom;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public long getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(long likeCount) {
		this.likeCount = likeCount;
	}

	public Boolean getIsLike() {
		return isLike;
	}

	public void setIsLike(Boolean isLike) {
		this.isLike = isLike;
	}

	public List<GjtMessageCommentDetailsVO> getCommentDetailsList() {
		return commentDetailsList;
	}

	public void setCommentDetailsList(List<GjtMessageCommentDetailsVO> commentDetailsList) {
		this.commentDetailsList = commentDetailsList;
	}

}
