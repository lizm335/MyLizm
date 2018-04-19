package com.ouchgzee.study.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.gzedu.xlims.common.DateUtils;
import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.gzedu.xlims.pojo.GjtEmployeeInfo;
import com.gzedu.xlims.pojo.GjtFeedback;
import com.gzedu.xlims.pojo.status.EmployeeTypeEnum;
import com.gzedu.xlims.service.usermanage.GjtEmployeeInfoService;

public class GjtFeedbackVo {
	private String title;
	private String content;
	private List<String> imgUrls;
	private String id;
	private String pid;
	private String createDt;
	private String type;
	private String creatorRole;
	private List<GjtFeedbackVo> gjtFeedbackVoList;
	private String teacherName;
	private String teacherType;
	private String teacherPhotoUrl;

	public GjtFeedbackVo() {
	};

	public GjtFeedbackVo(String content, List<String> imgUrls, String id, String pid, String createDt, String type,
			String creatorRole, String teacherName, String teacherType, String photoUrl) {
		super();
		this.content = content;
		this.imgUrls = imgUrls;
		this.id = id;
		this.pid = pid;
		this.createDt = createDt;
		this.type = type;
		this.creatorRole = creatorRole;
		this.teacherName = teacherName;
		this.teacherType = teacherType;
		this.teacherPhotoUrl = photoUrl;
	}

	public GjtFeedbackVo(String content, List<String> imgUrls, String id, String pid, String createDt, String type,
			String creatorRole) {
		super();
		this.content = content;
		this.imgUrls = imgUrls;
		this.id = id;
		this.pid = pid;
		this.createDt = createDt;
		this.type = type;
		this.creatorRole = creatorRole;
	}

	public GjtFeedbackVo(GjtFeedback gjtFeedback, GjtEmployeeInfoService gjtEmployeeInfoService) {
		super();
		this.id = gjtFeedback.getId();
		this.title = gjtFeedback.getTitle();
		this.content = gjtFeedback.getContent();
		this.type = gjtFeedback.getType();
		this.creatorRole = gjtFeedback.getCreatorRole();

		// 图片集合
		List<String> imgList = new ArrayList<String>();
		if (StringUtils.isNotBlank(gjtFeedback.getImgUrls())) {
			String[] imgs = gjtFeedback.getImgUrls().split(",");
			for (String str : imgs) {
				imgList.add(str);
			}
		}
		this.imgUrls = imgList;

		if (gjtFeedback.getCreatedDt() != null) {
			this.createDt = DateUtils.getStringToDate(gjtFeedback.getCreatedDt());
		} else {
			this.createDt = "1970-01-01 00:00:00";
		}

		List<GjtFeedbackVo> gjtFeedbackVoListTemp = new ArrayList<GjtFeedbackVo>();
		for (GjtFeedback item : gjtFeedback.getGjtFeedbackList()) {
			String createDtStr = null;
			if (item.getCreatedDt() != null) {
				createDtStr = DateUtils.getStringToDate(item.getCreatedDt());
			}
			List<String> imgages = new ArrayList<String>();
			if (StringUtils.isNotBlank(item.getImgUrls())) {
				String[] imgs = item.getImgUrls().split(",");
				for (String str : imgs) {
					imgages.add(str);
				}
			}
			GjtFeedbackVo gjtFeedbackVo = null;
			String type2 = item.getType();
			if ("reply".equals(type2)) {// 老师
				String employeeId = item.getCreatedBy();
				GjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(employeeId);
				String employeeName = "本班老师";
				String employeeType = "班主任";
				String photoUrl = "";
				if (employeeInfo != null) {
					employeeName = employeeInfo.getXm();
					employeeType = EmployeeTypeEnum.getName(Integer.valueOf(employeeInfo.getEmployeeType()));
					photoUrl = employeeInfo.getZp();
				}
				gjtFeedbackVo = new GjtFeedbackVo(item.getContent(), imgages, item.getId(), item.getPid(), createDtStr,
						item.getType(), item.getCreatorRole(), employeeName, employeeType, photoUrl);
			} else {// 学生追问
				gjtFeedbackVo = new GjtFeedbackVo(item.getContent(), imgages, item.getId(), item.getPid(), createDtStr,
						item.getType(), item.getCreatorRole());
			}
			gjtFeedbackVoListTemp.add(gjtFeedbackVo);
		}
		this.gjtFeedbackVoList = gjtFeedbackVoListTemp;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeacherType() {
		return teacherType;
	}

	public void setTeacherType(String teacherType) {
		this.teacherType = teacherType;
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

	public List<GjtFeedbackVo> getGjtFeedbackVoList() {
		return gjtFeedbackVoList;
	}

	public void setGjtFeedbackVoList(List<GjtFeedbackVo> gjtFeedbackVoList) {
		this.gjtFeedbackVoList = gjtFeedbackVoList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreatorRole() {
		return creatorRole;
	}

	public void setCreatorRole(String creatorRole) {
		this.creatorRole = creatorRole;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTeacherPhotoUrl() {
		return teacherPhotoUrl;
	}

	public void setTeacherPhotoUrl(String teacherPhotoUrl) {
		this.teacherPhotoUrl = teacherPhotoUrl;
	}

}
