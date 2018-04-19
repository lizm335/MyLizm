package com.ouchgzee.study.web.vo;

import java.util.Date;

import com.gzedu.xlims.common.gzdec.framework.util.DateUtil;
import com.gzedu.xlims.pojo.graduation.GjtGraduationRegisterEdu;

/**
 * 毕业申请-主要学历经历
 * @author lyj
 * @time 2017年4月1日 
 * TODO
 */
public class EduLiveVo {
	
	// 学历id
	private String eduId;
	// 时间
	private String beginTime;
	private String endTime;
	// 学校
	private String school;
	// 学历
	private String degree;
	// 地区
	private String region; // 地区
	
	private String status;// 0：新增，1:更新，2:删除，默认为0
	
	public EduLiveVo(GjtGraduationRegisterEdu e ) {
		super();
		this.eduId = e.getEduId();
		this.beginTime = DateUtil.toString(e.getBeginTime(), "yyyy-MM");
		this.endTime = DateUtil.toString(e.getEndTime(), "yyyy-MM");;
		this.school = e.getSchool();
		this.degree = e.getDegree();
		this.region = e.getRegion();
	}
	
	public EduLiveVo() {
	}

	public String getEduId() {
		return eduId;
	}
	public void setEduId(String eduId) {
		this.eduId = eduId;
	}
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
