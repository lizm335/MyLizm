/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.study.web.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.pojo.GjtCourse;
import com.gzedu.xlims.pojo.GjtTeachPlan;
import com.gzedu.xlims.pojo.status.DistributeStatusEnum;
import com.gzedu.xlims.pojo.textbook.GjtTextbook;
import com.gzedu.xlims.pojo.textbook.GjtTextbookDistributeDetail;

/**
 * 功能说明：
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年4月25日
 * @version 3.0
 *
 */
public class TearmVO {
	private String termName;

	private float totalPrice;

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}

	public List<CourseVO> getCourses() {
		return new ArrayList<TearmVO.CourseVO>(courseMap.values());
	}

	private Map<String, CourseVO> courseMap = new HashMap<String, CourseVO>();

	public void addCourse(GjtTeachPlan plan, Map<String, GjtTextbookDistributeDetail> statusMap,
			boolean isSign) {
		GjtCourse gjtCourse = plan.getGjtCourse();
		CourseVO courseVO;
		if (courseMap.containsKey(gjtCourse.getCourseId())) {
			courseVO = courseMap.get(gjtCourse.getCourseId());
		} else {
			courseVO = new CourseVO();
			courseVO.setCourseName(gjtCourse.getKcmc());
			courseMap.put(gjtCourse.getCourseId(), courseVO);
		}
		courseVO.addTextbook(plan.getGjtTextbookList(), statusMap, isSign);
		for (GjtTextbook tb : plan.getGjtTextbookList()) {
			totalPrice += tb.getPrice();
		}

	}

	public class CourseVO {
		private String courseName;
		private List<TextbookVO> textbooks;

		public String getCourseName() {
			return courseName;
		}

		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}

		public List<TextbookVO> getTextbooks() {
			return textbooks;
		}

		public void addTextbook(List<GjtTextbook> gjtTextbooks, Map<String, GjtTextbookDistributeDetail> statusMap,
				boolean isSign) {
			if (textbooks == null)
				textbooks = new ArrayList<TextbookVO>();
			for (GjtTextbook tb : gjtTextbooks) {
				TextbookVO tbvo = new TextbookVO(tb, statusMap, isSign);
				this.textbooks.add(tbvo);
			}

		}

	}

	public class TextbookVO {
		/**
		 * @param tb
		 */
		public TextbookVO(GjtTextbook tb, Map<String, GjtTextbookDistributeDetail> statusMap, boolean isSign) {
			this.textbookName = tb.getTextbookName();
			this.textbookType = tb.getTextbookType();
			this.price = tb.getPrice();
			if (statusMap.get(tb.getTextbookId()) != null) {
				this.distributeId = statusMap.get(tb.getTextbookId()).getGjtTextbookDistribute().getDistributeId();
				this.status = statusMap.get(tb.getTextbookId()).getStatus();
			} else if (isSign) {
				status = 3;
			}

		}

		private String textbookName;
		private Integer textbookType;
		private Float price;
		private String distributeId;
		private int status;

		public String getTextbookName() {
			return textbookName;
		}

		public Integer getTextbookType() {
			return textbookType;
		}

		public Float getPrice() {
			return price;
		}

		public String getDistributeId() {
			return distributeId;
		}

		public void setDistributeId(String distributeId) {
			this.distributeId = distributeId;
		}

		public Integer getStatus() {
			return status;
		}

		public String getStatusDesc() {
			return DistributeStatusEnum.getName(this.status);
		}
	}
}
