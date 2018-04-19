/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.study.web.vo;

import java.math.BigDecimal;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.pojo.GjtActivity;
import com.gzedu.xlims.pojo.dto.GjtActivityDto;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangeweiwen@eenet.com
 * @Date 2017年3月3日
 * @version 2.5
 *
 */
public class ClassActivityVO {
	String id = "";
	String activityAddress = "";// 活动地址
	String activityIntroduce = "";// 活动介绍
	String activityPicture = "";// 活动图片多图，用逗号隔开
	String activityTitle = "";// 活动标题
	String beginTime = "";// 活动开始时间
	BigDecimal ceilingNum = new BigDecimal(0);// 人数限制
	BigDecimal commentNum;// 评论总数
	String endTime = "";// 活动结束时间
	BigDecimal isFree = new BigDecimal(0);// 是否收费（1-免费,2-收费）
	BigDecimal chargeMoney = new BigDecimal(0);// 活动费用
	BigDecimal joinNum = new BigDecimal(0);// 报名人数
	String publicityPicture = "";// 活动图片
	int isEnter;// 学员是否报名（0-未报名,1-已报名）
	BigDecimal studentAuditStatus;// 学员报名以后是否审核通过(0-待审核,1-审核通过,2-审核不通过)

	public ClassActivityVO() {

	}

	public ClassActivityVO(GjtActivityDto gjtActivityDto) {
		super();
		this.id = gjtActivityDto.getID();
		this.activityAddress = gjtActivityDto.getACTIVITY_ADDRESS();
		this.activityIntroduce = gjtActivityDto.getACTIVITY_INTRODUCE();
		this.activityPicture = gjtActivityDto.getACTIVITY_PICTURE();
		this.activityTitle = gjtActivityDto.getACTIVITY_TITLE();
		this.beginTime = DateUtils.getStringToDate(gjtActivityDto.getBEGIN_TIME());
		this.ceilingNum = gjtActivityDto.getCEILING_NUM();
		this.commentNum = gjtActivityDto.getCOMMENT_NUM();
		this.endTime = DateUtils.getStringToDate(gjtActivityDto.getEND_TIME());
		this.isFree = gjtActivityDto.getIS_FREE();
		this.chargeMoney = gjtActivityDto.getCHARGE_MONEY();
		this.joinNum = gjtActivityDto.getJOIN_NUM();
		this.publicityPicture = gjtActivityDto.getPUBLICITY_PICTURE();
		this.isEnter = 1;
		this.studentAuditStatus = gjtActivityDto.getAUDIT_STATUS();
	}

	/**
	 * @param id
	 */
	public ClassActivityVO(GjtActivity item) {
		super();
		this.id = item.getId();
		this.activityAddress = item.getActivityAddress();
		this.activityIntroduce = item.getActivityIntroduce();
		this.activityPicture = item.getActivityPicture();
		this.activityTitle = item.getActivityTitle();
		this.beginTime = DateUtils.getStringToDate(item.getBeginTime());
		this.ceilingNum = item.getCeilingNum();
		this.commentNum = item.getCommentNum();
		this.endTime = DateUtils.getStringToDate(item.getEndTime());
		this.isFree = item.getIsFree();
		this.chargeMoney = item.getChargeMoney();
		this.joinNum = item.getJoinNum();
		this.publicityPicture = item.getPublicityPicture();
	}

	/**
	 * @param GjtActivity
	 */
	public ClassActivityVO(GjtActivity item, int isEnter, BigDecimal studentAuditStatus) {
		super();
		this.id = item.getId();
		this.activityAddress = item.getActivityAddress();
		this.activityIntroduce = item.getActivityIntroduce();
		this.activityPicture = item.getActivityPicture();
		this.activityTitle = item.getActivityTitle();
		this.beginTime = DateUtils.getStringToDate(item.getBeginTime());
		this.ceilingNum = item.getCeilingNum();
		this.commentNum = item.getCommentNum();
		this.endTime = DateUtils.getStringToDate(item.getEndTime());
		this.isFree = item.getIsFree();
		this.chargeMoney = item.getChargeMoney();
		this.joinNum = item.getJoinNum();
		this.publicityPicture = item.getPublicityPicture();
		this.isEnter = isEnter;
		this.studentAuditStatus = studentAuditStatus;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivityAddress() {
		return activityAddress;
	}

	public void setActivityAddress(String activityAddress) {
		this.activityAddress = activityAddress;
	}

	public String getActivityIntroduce() {
		return activityIntroduce;
	}

	public void setActivityIntroduce(String activityIntroduce) {
		this.activityIntroduce = activityIntroduce;
	}

	public String getActivityPicture() {
		return activityPicture;
	}

	public void setActivityPicture(String activityPicture) {
		this.activityPicture = activityPicture;
	}

	public String getActivityTitle() {
		return activityTitle;
	}

	public void setActivityTitle(String activityTitle) {
		this.activityTitle = activityTitle;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public BigDecimal getCeilingNum() {
		return ceilingNum;
	}

	public void setCeilingNum(BigDecimal ceilingNum) {
		this.ceilingNum = ceilingNum;
	}

	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(BigDecimal commentNum) {
		this.commentNum = commentNum;
	}

	public BigDecimal getIsFree() {
		return isFree;
	}

	public void setIsFree(BigDecimal isFree) {
		this.isFree = isFree;
	}

	public BigDecimal getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(BigDecimal joinNum) {
		this.joinNum = joinNum;
	}

	public String getPublicityPicture() {
		return publicityPicture;
	}

	public void setPublicityPicture(String publicityPicture) {
		this.publicityPicture = publicityPicture;
	}

	public int getIsEnter() {
		return isEnter;
	}

	public void setIsEnter(int isEnter) {
		this.isEnter = isEnter;
	}

	public BigDecimal getStudentAuditStatus() {
		return studentAuditStatus;
	}

	public void setStudentAuditStatus(BigDecimal studentAuditStatus) {
		this.studentAuditStatus = studentAuditStatus;
	}

}
