package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;

public class YearCourseDto {

	private BigDecimal YEARID; // 学年度ID
	private int yearCode; // 学年度CODE
	// private Integer YEARID; // 学年度ID
	private String COURSEID;// 课程ID
	private String COURSENAME;// 课程名称
	private String YEARNAME;// 学年度名称
	private String COACHTEACH;// 辅导教师
	private Integer CLASSNUM;// 班级数
	private String SCHOOLNAME;// 学校名称

	/**
	 * @return the sCHOOLNAME
	 */
	public String getSCHOOLNAME() {
		return SCHOOLNAME;
	}

	/**
	 * @param sCHOOLNAME
	 *            the sCHOOLNAME to set
	 */
	public void setSCHOOLNAME(String sCHOOLNAME) {
		SCHOOLNAME = sCHOOLNAME;
	}

	public BigDecimal getYEARID() {
		return YEARID;
	}

	public void setYEARID(BigDecimal yEARID) {
		YEARID = yEARID;
	}

	/**
	 * @return the cOURSEID
	 */
	public String getCOURSEID() {
		return COURSEID;
	}

	/**
	 * @param cOURSEID
	 *            the cOURSEID to set
	 */
	public void setCOURSEID(String cOURSEID) {
		COURSEID = cOURSEID;
	}

	/**
	 * @return the cOURSENAME
	 */
	public String getCOURSENAME() {
		return COURSENAME;
	}

	/**
	 * @param cOURSENAME
	 *            the cOURSENAME to set
	 */
	public void setCOURSENAME(String cOURSENAME) {
		COURSENAME = cOURSENAME;
	}

	/**
	 * @return the yEARNAME
	 */
	public String getYEARNAME() {
		return YEARNAME;
	}

	/**
	 * @param yEARNAME
	 *            the yEARNAME to set
	 */
	public void setYEARNAME(String yEARNAME) {
		YEARNAME = yEARNAME;
	}

	/**
	 * @return the cOACHTEACH
	 */
	public String getCOACHTEACH() {
		return COACHTEACH;
	}

	/**
	 * @param cOACHTEACH
	 *            the cOACHTEACH to set
	 */
	public void setCOACHTEACH(String cOACHTEACH) {
		COACHTEACH = cOACHTEACH;
	}

	/**
	 * @return the cLASSNUM
	 */
	public Integer getCLASSNUM() {
		return CLASSNUM;
	}

	/**
	 * @param cLASSNUM
	 *            the cLASSNUM to set
	 */
	public void setCLASSNUM(Integer cLASSNUM) {
		CLASSNUM = cLASSNUM;
	}

	public int getYearCode() {
		return YEARID.intValue();
	}

	public void setYearCode(int yearCode) {
		this.yearCode = yearCode;
	}

}