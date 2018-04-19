package com.ouchgzee.study.web.vo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.PriRoleInfo;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;

public class LcmsMutualSubjectVO {
	private final static Logger log = LoggerFactory.getLogger(LcmsMutualSubjectVO.class);

	private String title;
	private String content;
	private List<String> imgUrls;
	private String id;
	private String createDt;
	private String studentXm;
	private String avatar;
	private String isAnswer;// 是否解答
	private String isSolve;// 是否解决
	private String oftenType;// 常见问题类型
	private long gjtFeedbackVoCount;
	private String teacherXm;
	private String soleType;// 提问者：0学生，1老师

	private List<LcmsMutualReplyVO> gjtFeedbackVoList;

	public LcmsMutualSubjectVO() {
	};

	/**
	 * student新的答疑
	 * 
	 * @param item
	 * @param gjtEmployeeInfoService
	 */
	public LcmsMutualSubjectVO(LcmsMutualSubject item, GjtEmployeeInfoService emService) {
		super();
		this.id = item.getSubjectId();
		this.title = item.getSubjectTitle();
		this.content = item.getSubjectContent();
		this.isAnswer = item.getSubjectStatus();
		this.isSolve = item.getIsSolve();
		this.oftenType = item.getOftenType();
		this.soleType = item.getSoleType();
		// 图片集合
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(item.getResPath())) {
			String[] imgs = item.getResPath().split(",");
			for (String str : imgs) {
				imgList.add(str);
			}
		}
		this.imgUrls = imgList;
		if (item.getCreateAccount().getGjtStudentInfo() != null) {
			this.avatar = item.getCreateAccount().getGjtStudentInfo().getAvatar();
			this.studentXm = item.getCreateAccount().getRealName();
		}
		this.teacherXm = "";
		if (item.getCreatedDt() != null) {
			this.createDt = DateUtils.getStringToDate(item.getCreatedDt());
		} else {
			this.createDt = "1970-01-01 00:00:00";
		}

		List<LcmsMutualReplyVO> replyList = new ArrayList<LcmsMutualReplyVO>();
		List<LcmsMutualReply> gjtLcmsMutualReply = item.getGjtLcmsMutualReply();
		for (LcmsMutualReply lcmsMutualReply : gjtLcmsMutualReply) {
			LcmsMutualReplyVO lmr = new LcmsMutualReplyVO();
			lmr.setId(lcmsMutualReply.getReplyId());
			lmr.setPid(lcmsMutualReply.getParentId());
			lmr.setContent(lcmsMutualReply.getReplyContent());
			if (lcmsMutualReply.getCreatedDt() != null) {
				lmr.setCreateDt(DateUtils.getStringToDate(lcmsMutualReply.getCreatedDt()));
			} else {
				lmr.setCreateDt("1970-01-01 00:00:00");
			}

			List<String> imgages = new ArrayList<String>();
			if (StringUtils.isNotBlank(lcmsMutualReply.getResPath())) {
				String[] imgs = lcmsMutualReply.getResPath().split(",");
				for (String str : imgs) {
					imgages.add(str);
				}
			}
			lmr.setImgUrls(imgages);
			if ("stud".equals(lcmsMutualReply.getUserType())) {
				lmr.setStudentXm(lcmsMutualReply.getCreatedUser().getRealName());
				lmr.setType("ask");
			} else {
				lmr.setType("reply");
				GjtUserAccount createdUser = lcmsMutualReply.getCreatedUser();
				String employee = lcmsMutualReply.getUserId();
				log.info("-------------->>employeeId:" + employee);
				GjtEmployeeInfo employeeInfo = null;
				if (StringUtils.isNotBlank(employee)) {
					employeeInfo = emService.queryById(employee);
				}
				if (employeeInfo != null) {
					lmr.setTeacherName(employeeInfo.getXm());
				} else {
					lmr.setTeacherName(createdUser.getRealName());
				}

				PriRoleInfo priRoleInfo = createdUser.getPriRoleInfo();
				if (priRoleInfo != null) {
					lmr.setTeacherType(priRoleInfo.getRoleName());
				} else {
					lmr.setTeacherType("不明身份");
				}
			}
			lmr.setTitle(lcmsMutualReply.getReplyTitle());
			replyList.add(lmr);
		}
		this.gjtFeedbackVoList = replyList;
		this.gjtFeedbackVoCount = replyList.size();
	}

	public LcmsMutualSubjectVO(LcmsMutualSubject item, String OftenType) {
		super();
		this.id = item.getSubjectId();
		this.title = item.getSubjectTitle();
		this.content = item.getSubjectContent();
		this.isAnswer = item.getSubjectStatus();
		this.isSolve = item.getIsSolve();
		this.oftenType = item.getOftenType();
		this.soleType = item.getSoleType();
		// 图片集合
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(item.getResPath())) {
			String[] imgs = item.getResPath().split(",");
			for (String str : imgs) {
				imgList.add(str);
			}
		}
		this.imgUrls = imgList;
		this.avatar = "";
		this.studentXm = "";
		this.teacherXm = "";
		if (item.getCreatedDt() != null) {
			this.createDt = DateUtils.getStringToDate(item.getCreatedDt());
		} else {
			this.createDt = "1970-01-01 00:00:00";
		}

		List<LcmsMutualReplyVO> replyList = new ArrayList<LcmsMutualReplyVO>();
		List<LcmsMutualReply> gjtLcmsMutualReply = item.getGjtLcmsMutualReply();
		for (LcmsMutualReply lcmsMutualReply : gjtLcmsMutualReply) {
			LcmsMutualReplyVO lmr = new LcmsMutualReplyVO();
			lmr.setId(lcmsMutualReply.getReplyId());
			lmr.setPid(lcmsMutualReply.getParentId());
			lmr.setContent(lcmsMutualReply.getReplyContent());
			if (lcmsMutualReply.getCreatedDt() != null) {
				lmr.setCreateDt(DateUtils.getStringToDate(lcmsMutualReply.getCreatedDt()));
			} else {
				lmr.setCreateDt("1970-01-01 00:00:00");
			}

			List<String> imgages = new ArrayList<String>();
			if (StringUtils.isNotBlank(lcmsMutualReply.getResPath())) {
				String[] imgs = lcmsMutualReply.getResPath().split(",");
				for (String str : imgs) {
					imgages.add(str);
				}
			}
			lmr.setImgUrls(imgages);
			if ("stud".equals(lcmsMutualReply.getUserType())) {
				lmr.setStudentXm(lcmsMutualReply.getCreatedUser().getRealName());
				lmr.setType("ask");
			} else {
				lmr.setType("reply");
				lmr.setTeacherName("");
				lmr.setTeacherType("");
			}
			lmr.setTitle(lcmsMutualReply.getReplyTitle());
			replyList.add(lmr);
		}
		this.gjtFeedbackVoList = replyList;
		this.gjtFeedbackVoCount = replyList.size();

	}

	public LcmsMutualSubjectVO(LcmsMutualSubject item) {
		super();
		this.id = item.getSubjectId();
		this.title = item.getSubjectTitle();
		this.content = item.getSubjectContent();
		this.isAnswer = item.getSubjectStatus();
		this.isSolve = item.getIsSolve();
		this.oftenType = item.getOftenType();
		this.soleType = item.getSoleType();
		// 图片集合
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(item.getResPath())) {
			String[] imgs = item.getResPath().split(",");
			for (String str : imgs) {
				imgList.add(str);
			}
		}
		this.imgUrls = imgList;
		this.avatar = "";
		this.teacherXm = item.getCreateAccount().getRealName();
		this.studentXm = "";
		if (item.getCreatedDt() != null) {
			this.createDt = DateUtils.getStringToDate(item.getCreatedDt());
		} else {
			this.createDt = "1970-01-01 00:00:00";
		}

		List<LcmsMutualReplyVO> replyList = new ArrayList<LcmsMutualReplyVO>();
		List<LcmsMutualReply> gjtLcmsMutualReply = item.getGjtLcmsMutualReply();
		for (LcmsMutualReply lcmsMutualReply : gjtLcmsMutualReply) {
			LcmsMutualReplyVO lmr = new LcmsMutualReplyVO();
			lmr.setId(lcmsMutualReply.getReplyId());
			lmr.setPid(lcmsMutualReply.getParentId());
			lmr.setContent(lcmsMutualReply.getReplyContent());
			if (lcmsMutualReply.getCreatedDt() != null) {
				lmr.setCreateDt(DateUtils.getStringToDate(lcmsMutualReply.getCreatedDt()));
			} else {
				lmr.setCreateDt("1970-01-01 00:00:00");
			}

			List<String> imgages = new ArrayList<String>();
			if (StringUtils.isNotBlank(lcmsMutualReply.getResPath())) {
				String[] imgs = lcmsMutualReply.getResPath().split(",");
				for (String str : imgs) {
					imgages.add(str);
				}
			}
			lmr.setImgUrls(imgages);
			if ("stud".equals(lcmsMutualReply.getUserType())) {
				GjtUserAccount createdUser = lcmsMutualReply.getCreatedUser();
				lmr.setTeacherName(createdUser.getRealName());
				PriRoleInfo priRoleInfo = createdUser.getPriRoleInfo();
				if (priRoleInfo != null) {
					lmr.setTeacherType(priRoleInfo.getRoleName());
				} else {
					lmr.setTeacherType("不明身份");
				}
				lmr.setType("ask");
			} else {
				lmr.setType("reply");
				GjtUserAccount createdUser = lcmsMutualReply.getCreatedUser();
				lmr.setAdminName(createdUser.getRealName());
				PriRoleInfo priRoleInfo = createdUser.getPriRoleInfo();
				if (priRoleInfo != null) {
					lmr.setAdminType(priRoleInfo.getRoleName());
				} else {
					lmr.setAdminType("不明身份");
				}
			}
			lmr.setTitle(lcmsMutualReply.getReplyTitle());
			replyList.add(lmr);
		}
		this.gjtFeedbackVoList = replyList;
		this.gjtFeedbackVoCount = replyList.size();
	}

	public String getSoleType() {
		return soleType;
	}

	public void setSoleType(String soleType) {
		this.soleType = soleType;
	}

	public String getTeacherXm() {
		return teacherXm;
	}

	public void setTeacherXm(String teacherXm) {
		this.teacherXm = teacherXm;
	}

	public long getGjtFeedbackVoCount() {
		return gjtFeedbackVoCount;
	}

	public void setGjtFeedbackVoCount(long gjtFeedbackVoCount) {
		this.gjtFeedbackVoCount = gjtFeedbackVoCount;
	}

	public String getOftenType() {
		return oftenType;
	}

	public void setOftenType(String oftenType) {
		this.oftenType = oftenType;
	}

	public String getIsSolve() {
		return isSolve;
	}

	public void setIsSolve(String isSolve) {
		this.isSolve = isSolve;
	}

	public String getIsAnswer() {
		return isAnswer;
	}

	public void setIsAnswer(String isAnswer) {
		this.isAnswer = isAnswer;
	}

	public String getStudentXm() {
		return studentXm;
	}

	public void setStudentXm(String studentXm) {
		this.studentXm = studentXm;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<LcmsMutualReplyVO> getGjtFeedbackVoList() {
		return gjtFeedbackVoList;
	}

	public void setGjtFeedbackVoList(List<LcmsMutualReplyVO> gjtFeedbackVoList) {
		this.gjtFeedbackVoList = gjtFeedbackVoList;
	}

	public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
