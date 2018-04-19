/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.pojo.dto;

import java.math.BigDecimal;

/**
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.cn
 * @Date 2017年1月12日
 * @version 1.0
 *
 */
public class ExamScoreManage {
	private String XH; // 学号
	private String GRADE_NAME;// 年级名称
	private String SJH;// 手机号
	private String XM;// 姓名
	private String REC_ID;// 选课ID
	private String PYCC;// 培养层次
	private String KCMC;// 课程名称
	private String TERM_TYPE_CODE;// 学期名称
	private String SCORE_STATE;// 通过状态，0通过，1不通过，2未知
	private String ZYMC;// 专业名称
	private BigDecimal STUDY_RATIO;// 课程学习比率 NVL(gtp.kcxxbz, 70)
									// 教学计划有写，没有的话就固定70%
	private BigDecimal EXAM_RATIO;// 课程考试比率NVL(gtp.kcksbz, 30) kcksbz
									// 教学计划有写，没有的话就固定30%
	// 等同于下面的EXAM_SCORE1终结性成绩
	private BigDecimal EXAM_SCORE;// 考试成绩 NVL(grr.exam_score1, gsss.exam_score)
	private BigDecimal COURSE_SCHEDULE;// 学习成绩 NVL(grr.exam_score, GSSS.SCORE)
	private BigDecimal TOTAL_POINTS;// 总成绩
	// private String EXAM_SCORE1;// 终结性成绩
	// private String EXAM_SCORE2;// 总成绩
	// private String IS_RESERVE;// N 未预约 Y已预约
	// private String IS_OVER;// 0 否 1 是 2 免修 3 免修免考
	// private String IS_RESERVE_BOOK;// 预约教材
	private BigDecimal EXAM_TYPE;// 考试方式
	private String STUDENT_ID;// 学生ID

	public BigDecimal getEXAM_TYPE() {
		return EXAM_TYPE;
	}

	public void setEXAM_TYPE(BigDecimal eXAM_TYPE) {
		EXAM_TYPE = eXAM_TYPE;
	}

	public String getTERM_TYPE_CODE() {
		return TERM_TYPE_CODE;
	}

	public void setTERM_TYPE_CODE(String tERM_TYPE_CODE) {
		String str = "第" + tERM_TYPE_CODE + "学期";
		TERM_TYPE_CODE = str;
	}

	public BigDecimal getSTUDY_RATIO() {
		return STUDY_RATIO;
	}

	public void setSTUDY_RATIO(BigDecimal sTUDY_RATIO) {
		STUDY_RATIO = sTUDY_RATIO;
	}

	public BigDecimal getEXAM_RATIO() {
		return EXAM_RATIO;
	}

	public void setEXAM_RATIO(BigDecimal eXAM_RATIO) {
		EXAM_RATIO = eXAM_RATIO;
	}

	public String getSTUDENT_ID() {
		return STUDENT_ID;
	}

	public void setSTUDENT_ID(String sTUDENT_ID) {
		STUDENT_ID = sTUDENT_ID;
	}

	private BigDecimal ROWNUM_;

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

	public String getXH() {
		return XH;
	}

	public void setXH(String xH) {
		XH = xH;
	}

	public String getGRADE_NAME() {
		return GRADE_NAME;
	}

	public void setGRADE_NAME(String gRADE_NAME) {
		GRADE_NAME = gRADE_NAME;
	}

	public String getSJH() {
		return SJH;
	}

	public void setSJH(String sJH) {
		SJH = sJH;
	}

	public String getXM() {
		return XM;
	}

	public void setXM(String xM) {
		XM = xM;
	}

	public String getREC_ID() {
		return REC_ID;
	}

	public void setREC_ID(String rEC_ID) {
		REC_ID = rEC_ID;
	}

	public String getPYCC() {
		return PYCC;
	}

	public void setPYCC(String pYCC) {
		PYCC = pYCC;
	}

	public String getKCMC() {
		return KCMC;
	}

	public void setKCMC(String kCMC) {
		KCMC = kCMC;
	}

	public String getSCORE_STATE() {
		return SCORE_STATE;
	}

	public void setSCORE_STATE(String sCORE_STATE) {
		SCORE_STATE = sCORE_STATE;
	}

	public String getZYMC() {
		return ZYMC;
	}

	public void setZYMC(String zYMC) {
		ZYMC = zYMC;
	}

	public BigDecimal getEXAM_SCORE() {
		return EXAM_SCORE;
	}

	public void setEXAM_SCORE(BigDecimal eXAM_SCORE) {
		EXAM_SCORE = eXAM_SCORE;
	}

	public BigDecimal getCOURSE_SCHEDULE() {
		return COURSE_SCHEDULE;
	}

	public void setCOURSE_SCHEDULE(BigDecimal cOURSE_SCHEDULE) {
		COURSE_SCHEDULE = cOURSE_SCHEDULE;
	}

	public BigDecimal getTOTAL_POINTS() {
		return TOTAL_POINTS;
	}

	public void setTOTAL_POINTS(BigDecimal tOTAL_POINTS) {
		TOTAL_POINTS = tOTAL_POINTS;
	}

}
