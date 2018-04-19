package com.gzedu.xlims.pojo.openClass;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gzedu.xlims.common.DateUtil;

/**
 * 
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年11月28日
 * @version 3.0
 *
 */
public class LessonVO extends LcmsOnlineLesson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BigDecimal allNum;

	private BigDecimal joinNum;

	private BigDecimal ROWNUM_;

	public BigDecimal getAllNum() {
		return allNum;
	}

	public void setAllNum(BigDecimal allNum) {
		this.allNum = allNum;
	}

	public BigDecimal getJoinNum() {
		return joinNum;
	}

	public void setJoinNum(BigDecimal joinNum) {
		this.joinNum = joinNum;
	}

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal rOWNUM_) {
		ROWNUM_ = rOWNUM_;
	}

	public long getDays() {
		return DateUtil.diffDays(new Date(), getOnlinetutorFinish());
	}

	public int getStatus() {
		int status = 0;
		long currentTime = System.currentTimeMillis();
		if (getOnlinetutorStart().getTime() > currentTime) {// 未开始
			status = 0;
		} else if (getOnlinetutorFinish().getTime() < currentTime) {// 已结束
			status = 2;
		} else {// 直播中
			status = 1;
		}
		return status;
	}

	/**
	 * 
	 * @author ouguohao@eenet.com
	 * @Date 2017年11月28日 下午4:33:56
	 * @param type
	 *            直播对象类型 1 课程班学员 2 课程班 3 期课程 4 年级 5 专业 6 院校 7教学班学员 8培养层次 9课程
	 * @return
	 */
	public Set<String> getRangeIds(String type) {
		Set<String> set = new HashSet<String>();
		List<LcmsOnlineObject> objects = getLcmsOnlineObjects();
		if (objects == null) {
			return set;
		}
		for (LcmsOnlineObject o : objects) {
			if (type.equals(o.getObjectType())) {
				set.add(o.getObjectId());
			}
		}
		return set;

	}

}
