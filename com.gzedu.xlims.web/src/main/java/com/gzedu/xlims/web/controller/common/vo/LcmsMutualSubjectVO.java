package com.gzedu.xlims.web.controller.common.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.pojo.GjtGrade;
import com.gzedu.xlims.pojo.GjtStudentInfo;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.pojo.LcmsMutualReply;
import com.gzedu.xlims.pojo.LcmsMutualSubject;
import com.gzedu.xlims.pojo.PriRoleInfo;

public class LcmsMutualSubjectVO {
	private String title;
	private String content;
	private List<String> imgUrls;
	private String id;
	private String createDt;
	private String studentXm;
	private String avatar;
	private List<LcmsMutualReplyVO> replyList;
	private String isComm;
	private String isState;
	private String zhuanfaId;
	private String chushiId;
	private int askCount;
	private String className;
	private String xh;
	private String pycc;
	private String tel;
	private String grade;
	private String semester;
	private String specialtyName;
	private String url;
	private String studentId;
	private String questionSourcePath;
	private String isSolve;// 标记问题是否已经解决 0否 1是
	private String isTranspond;// 是否转发 0否1是
	private String initialUserName;// 初始拿到问题者
	private String forwardUserName;// 被转发问题者，回答者
	private String timeOutDate;// 超时时间
	private String isMessage;// 是否发送短信催促过 N没有，Y 有
	private String oftenType;// 数据字典
	private String isTime;// 是否超过12小时
	private String orgName; // 学习中心
	private String eeUrl;// 老师对学员跳转EE
	private String singUrl;// h5链接
	private List<String> isCommendType;// 常见问题标签

	public LcmsMutualSubjectVO() {
	};

	/**
	 * 新的答疑
	 * 
	 * @param item
	 * @param gjtEmployeeInfoService
	 */
	public LcmsMutualSubjectVO(LcmsMutualSubject item, Map<String, String> pyccMap, GjtStudentInfo studentInfo) {
		super();
		this.id = item.getSubjectId();
		this.title = item.getSubjectTitle();
		this.content = item.getSubjectContent();
		this.isComm = item.getIsCommend();
		this.isState = item.getSubjectStatus();
		this.zhuanfaId = item.getForwardAccountId();
		this.chushiId = item.getInitialAccountId();
		this.askCount = item.getReplyCount().intValue();
		this.oftenType = item.getOftenType();

		String commendTypes = item.getIsCommendType();
		if (StringUtils.isNotBlank(commendTypes)) {
			String[] split = commendTypes.split(";");
			this.isCommendType = Arrays.asList(split);
		}

		// 图片集合
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(item.getResPath())) {
			String[] imgs = item.getResPath().split(",");
			for (String str : imgs) {
				imgList.add(str);
			}
		}
		this.imgUrls = imgList;
		this.questionSourcePath = item.getQuestionSourcePath();
		this.isSolve = item.getIsSolve();
		this.isTranspond = item.getIsTranspond();
		this.forwardUserName = item.getFeedbackContacts();

		if ("1".equals(item.getIsTranspond())) {
			this.initialUserName = item.getRemark();
			if (EmptyUtils.isNotEmpty(item.getTranspondDt())) {
				long time = item.getTranspondDt().getTime();
				long currTime = DateUtils.getDate().getTime();
				long times = (currTime - time) / 1000 / 60 / 60;
				if (times < 24) {
					this.timeOutDate = times + "小时";
				} else {
					this.timeOutDate = (times / 24) + "天";
				}
				if (times > 12) {// 超过12小时可以提醒
					this.isTime = "Y";
					this.isMessage = (item.getIsMessage() != null && item.getIsMessage()) ? "Y" : "N";
				} else {
					this.isTime = "N";
				}
			}
		}

		if (studentInfo != null) {
			this.studentId = studentInfo.getStudentId();
			this.avatar = studentInfo.getAvatar();
			this.studentXm = studentInfo.getXm();
			this.xh = studentInfo.getXh();
			this.pycc = pyccMap.get(studentInfo.getPycc());
			this.tel = studentInfo.getSjh();
			GjtGrade gjtGrade = studentInfo.getGjtGrade();
			if (gjtGrade != null) {
				this.grade = gjtGrade.getGjtYear().getName();
				this.semester = gjtGrade.getGradeName();
			}
			this.className = studentInfo.getUserclass();
			this.specialtyName = studentInfo.getGjtSpecialty() != null ? studentInfo.getGjtSpecialty().getZymc() : "";
			// this.url =
			// gjtUserAccountService.studentSimulation(gjtStudentInfo.getStudentId(),
			// gjtStudentInfo.getXh());
		}
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
			lmr.setPid(item.getSubjectId());
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
				lmr.setTeacherName(lcmsMutualReply.getCreatedUser().getRealName());
				PriRoleInfo priRoleInfo = lcmsMutualReply.getCreatedUser().getPriRoleInfo();
				if (priRoleInfo != null) {
					lmr.setTeacherType(priRoleInfo.getRoleName());
				} else {
					lmr.setTeacherType("不明身份");
				}
			}
			lmr.setTitle(lcmsMutualReply.getReplyTitle());
			replyList.add(lmr);
		}
		this.replyList = replyList;
	}

	// 老师对象
	public LcmsMutualSubjectVO(LcmsMutualSubject item) {
		super();
		this.id = item.getSubjectId();
		this.title = item.getSubjectTitle();
		this.content = item.getSubjectContent();
		this.isComm = item.getIsCommend();
		this.isState = item.getSubjectStatus();
		this.zhuanfaId = item.getForwardAccountId();
		this.chushiId = item.getInitialAccountId();
		this.askCount = item.getReplyCount().intValue();
		this.oftenType = item.getOftenType();

		// 图片集合
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(item.getResPath())) {
			String[] imgs = item.getResPath().split(",");
			for (String str : imgs) {
				imgList.add(str);
			}
		}
		this.imgUrls = imgList;

		this.questionSourcePath = item.getQuestionSourcePath();

		this.isSolve = item.getIsSolve();
		this.isTranspond = item.getIsTranspond();

		this.forwardUserName = item.getFeedbackContacts();
		if ("1".equals(item.getIsTranspond())) {
			this.initialUserName = item.getRemark();
			if (EmptyUtils.isNotEmpty(item.getTranspondDt())) {
				long time = item.getTranspondDt().getTime();
				long currTime = DateUtils.getDate().getTime();
				long times = (currTime - time) / 1000 / 60 / 60;
				if (times < 24) {
					this.timeOutDate = times + "小时";
				} else {
					this.timeOutDate = (times / 24) + "天";
				}
				if (times > 12) {// 超过12小时可以提醒
					this.isTime = "Y";
					this.isMessage = (item.getIsMessage() != null && item.getIsMessage()) ? "Y" : "N";
				} else {
					this.isTime = "N";
				}
			}
		}
		GjtUserAccount createAccount = item.getCreateAccount();
		if (item.getCreateAccount() != null) {
			this.className = "";
			this.studentId = "";
			this.avatar = "";
			this.studentXm = createAccount.getRealName();
			this.xh = createAccount.getLoginAccount();
			this.pycc = "";
			this.tel = createAccount.getTelephone();
			this.grade = "";
			this.semester = "";
			this.specialtyName = "";
		}
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
			lmr.setPid(item.getSubjectId());
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
				lmr.setTeacherName(lcmsMutualReply.getCreatedUser().getRealName());
				PriRoleInfo priRoleInfo = lcmsMutualReply.getCreatedUser().getPriRoleInfo();
				if (priRoleInfo != null) {
					lmr.setTeacherType(priRoleInfo.getRoleName());
				} else {
					lmr.setTeacherType("不明身份");
				}
			}
			lmr.setTitle(lcmsMutualReply.getReplyTitle());
			replyList.add(lmr);
		}
		this.replyList = replyList;
	}

	public String getSingUrl() {
		return singUrl;
	}

	public void setSingUrl(String singUrl) {
		this.singUrl = singUrl;
	}

	public String getQuestionSourcePath() {
		return questionSourcePath;
	}

	public void setQuestionSourcePath(String questionSourcePath) {
		this.questionSourcePath = questionSourcePath;
	}

	public String getIsSolve() {
		return isSolve;
	}

	public void setIsSolve(String isSolve) {
		this.isSolve = isSolve;
	}

	public String getIsTranspond() {
		return isTranspond;
	}

	public void setIsTranspond(String isTranspond) {
		this.isTranspond = isTranspond;
	}

	public String getInitialUserName() {
		return initialUserName;
	}

	public void setInitialUserName(String initialUserName) {
		this.initialUserName = initialUserName;
	}

	public String getForwardUserName() {
		return forwardUserName;
	}

	public void setForwardUserName(String forwardUserName) {
		this.forwardUserName = forwardUserName;
	}

	public String getTimeOutDate() {
		return timeOutDate;
	}

	public void setTimeOutDate(String timeOutDate) {
		this.timeOutDate = timeOutDate;
	}

	public String getIsMessage() {
		return isMessage;
	}

	public void setIsMessage(String isMessage) {
		this.isMessage = isMessage;
	}

	public String getOftenType() {
		return oftenType;
	}

	public void setOftenType(String oftenType) {
		this.oftenType = oftenType;
	}

	public String getIsTime() {
		return isTime;
	}

	public void setIsTime(String isTime) {
		this.isTime = isTime;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getEeUrl() {
		return eeUrl;
	}

	public void setEeUrl(String eeUrl) {
		this.eeUrl = eeUrl;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getPycc() {
		return pycc;
	}

	public void setPycc(String pycc) {
		this.pycc = pycc;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getSpecialtyName() {
		return specialtyName;
	}

	public void setSpecialtyName(String specialtyName) {
		this.specialtyName = specialtyName;
	}

	public int getAskCount() {
		return askCount;
	}

	public void setAskCount(int askCount) {
		this.askCount = askCount;
	}

	public String getZhuanfaId() {
		return zhuanfaId;
	}

	public void setZhuanfaId(String zhuanfaId) {
		this.zhuanfaId = zhuanfaId;
	}

	public String getChushiId() {
		return chushiId;
	}

	public void setChushiId(String chushiId) {
		this.chushiId = chushiId;
	}

	public String getIsState() {
		return isState;
	}

	public void setIsState(String isState) {
		this.isState = isState;
	}

	public String getIsComm() {
		return isComm;
	}

	public void setIsComm(String isComm) {
		this.isComm = isComm;
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

	public List<LcmsMutualReplyVO> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<LcmsMutualReplyVO> replyList) {
		this.replyList = replyList;
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

	public List<String> getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getIsCommendType() {
		return isCommendType;
	}

	public void setIsCommendType(List<String> isCommendType) {
		this.isCommendType = isCommendType;
	}

}
