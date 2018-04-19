/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.feedback;

import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.gzdec.framework.util.StringUtils;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.feedback.GjtFeedbackDao;
import com.ouchgzee.headTeacher.daoImpl.FeedbackDaoImpl;
import com.ouchgzee.headTeacher.dto.FeedbackSolvedDto;
import com.ouchgzee.headTeacher.dto.FeedbackUnsolvedDto;
import com.ouchgzee.headTeacher.pojo.BzrGjtEmployeeInfo;
import com.ouchgzee.headTeacher.pojo.BzrGjtFeedback;
import com.ouchgzee.headTeacher.pojo.status.EmployeeTypeEnum;
import com.ouchgzee.headTeacher.service.employee.BzrGjtEmployeeInfoService;
import com.ouchgzee.headTeacher.service.feedback.BzrGjtFeedbackService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 留言反馈业务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年5月31日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtFeedbackServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtFeedbackServiceImpl extends BaseServiceImpl<BzrGjtFeedback> implements BzrGjtFeedbackService {

	private static final String String = null;

	private static Logger log = LoggerFactory.getLogger(GjtFeedbackServiceImpl.class);

	@Autowired
	private GjtFeedbackDao gjtFeedbackDao;

	@Autowired
	private FeedbackDaoImpl feedbackDao;

	@Autowired
	BzrGjtEmployeeInfoService gjtEmployeeInfoService;

	@Override
	protected BaseDao<BzrGjtFeedback, String> getBaseDao() {
		return gjtFeedbackDao;
	}

	// 未解决的明细列表
	@Override
	public Page<FeedbackUnsolvedDto> queryUnsolvedFeedBackByBzrIdPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.created_dt"));
		}
		Page<Map> result = feedbackDao.findUnsolvedFeedBackByPage(searchParams, pageRequest);

		List<FeedbackUnsolvedDto> unsolvedList = new ArrayList();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();
			String id = (String) info.get("ID");
			String studentXm = (String) info.get("STUDENTXM");
			FeedbackUnsolvedDto unsolvedDto = new FeedbackUnsolvedDto();
			unsolvedDto.setId(id);
			unsolvedDto.setTitle((String) info.get("TITLE"));
			unsolvedDto.setStudentXm(studentXm);
			unsolvedDto.setAvatar((String) info.get("AVATAR"));
			unsolvedDto.setCreatedDt((Date) info.get("CREATED_DT"));
			Clob content = (Clob) info.get("CONTENT");
			String imgurl = (String) info.get("IMGURLS");

			List<String> pictures = null;
			if (StringUtils.isNotBlank(imgurl)) {
				pictures = new ArrayList<String>();// 学生提问图片
				String[] imgurls = imgurl.split(",");
				for (String picture : imgurls) {
					pictures.add(picture);
				}
			}
			unsolvedDto.setPictures(pictures);

			List<BzrGjtFeedback> list = gjtFeedbackDao.findByPidOrderByCreatedDtAsc(id);// 老师回复，以及自身的追问
			if (list != null && list.size() > 0) {
				List<FeedbackSolvedDto> gjtFeedbackList = new LinkedList<FeedbackSolvedDto>();
				for (BzrGjtFeedback gjtFeedback : list) {
					FeedbackSolvedDto item = new FeedbackSolvedDto();
					if ("reply".equals(gjtFeedback.getType())) {
						BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(gjtFeedback.getCreatedBy());
						if (employeeInfo != null) {
							item.setAnswerEmployeeId(gjtFeedback.getCreatedBy());
							item.setAnswerEmployeeXm(employeeInfo.getXm());
							item.setEmployeeType(
									EmployeeTypeEnum.getName(Integer.valueOf(employeeInfo.getEmployeeType())));
						}
						String teacherimgurl = gjtFeedback.getImgUrls();
						List<String> teacherPictures = null;
						if (StringUtils.isNotBlank(teacherimgurl)) {
							teacherPictures = new ArrayList<String>();// 老师回复图片
							String[] imgurls = teacherimgurl.split(",");
							for (String picture : imgurls) {
								teacherPictures.add(picture);
							}
						}
						item.setTeacherPictures(teacherPictures);
					}

					item.setType(gjtFeedback.getType());
					item.setStudentXm(studentXm);
					item.setCreatedDt(gjtFeedback.getCreatedDt());
					item.setContent(gjtFeedback.getContent());
					gjtFeedbackList.add(item);
				}
				unsolvedDto.setGjtFeedbackList(gjtFeedbackList);
			}
			try {
				unsolvedDto.setContent(content != null ? content.getSubString(1, (int) content.length()) : null);
			} catch (SQLException e) {
				log.error("类型转化异常", e);
			}

			unsolvedList.add(unsolvedDto);
		}
		return new PageImpl(unsolvedList, pageRequest, result.getTotalElements());
	}

	// 已解决的明细列表
	@Override
	public Page<FeedbackSolvedDto> querySolvedFeedBackByPage(Map<String, Object> searchParams,
			PageRequest pageRequest) {
		if (pageRequest.getSort() == null) {
			pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
					new Sort(Sort.Direction.DESC, "t.created_dt"));
		}
		Page<Map> result = feedbackDao.findSolvedFeedBackByPage(searchParams, pageRequest);

		List<FeedbackSolvedDto> solvedList = new ArrayList<FeedbackSolvedDto>();
		for (Iterator<Map> iter = result.iterator(); iter.hasNext();) {
			Map info = iter.next();
			FeedbackSolvedDto solvedDto = new FeedbackSolvedDto();
			solvedDto.setId((String) info.get("ID"));
			solvedDto.setTitle((String) info.get("TITLE"));
			solvedDto.setStudentXm((String) info.get("STUDENTXM"));
			solvedDto.setAvatar((String) info.get("AVATAR"));
			solvedDto.setCreatedDt((Date) info.get("CREATED_DT"));
			solvedDto.setAnswerDealDt((Date) info.get("ANSWERDEALDT"));
			Clob content = (Clob) info.get("CONTENT");
			Clob answerContent = (Clob) info.get("ANSWERCONTENT");
			String studentimgurl = (String) info.get("STUDENTIMGURLS");
			String employeeId = (String) info.get("EMPLOYEEID");

			List<String> studentPictures = null;
			if (StringUtils.isNotBlank(studentimgurl)) {
				studentPictures = new ArrayList<String>();// 学生提问图片
				String[] imgurls = studentimgurl.split(",");
				for (String picture : imgurls) {
					studentPictures.add(picture);
				}
			}
			solvedDto.setStudentPictures(studentPictures);

			String teacherimgurl = (String) info.get("TEACHERIMGURLS");

			List<String> teacherPictures = null;
			if (StringUtils.isNotBlank(teacherimgurl)) {
				teacherPictures = new ArrayList<String>();// 老师回复图片
				String[] imgurls = teacherimgurl.split(",");
				for (String picture : imgurls) {
					teacherPictures.add(picture);
				}
			}
			solvedDto.setTeacherPictures(teacherPictures);

			try {
				solvedDto.setContent(content != null ? content.getSubString(1, (int) content.length()) : null);
				solvedDto.setAnswerContent(
						answerContent != null ? answerContent.getSubString(1, (int) answerContent.length()) : null);
			} catch (SQLException e) {
				log.error("类型转化异常", e);
			}

			BzrGjtEmployeeInfo employeeInfo = gjtEmployeeInfoService.queryById(employeeId);
			if (employeeInfo != null) {
				solvedDto.setAnswerEmployeeId(employeeId);
				solvedDto.setAnswerEmployeeXm(employeeInfo.getXm());
				solvedDto.setEmployeeType(EmployeeTypeEnum.getName(Integer.valueOf(employeeInfo.getEmployeeType())));
			}
			solvedList.add(solvedDto);
		}
		return new PageImpl(solvedList, pageRequest, result.getTotalElements());
	}

	@Override
	public long countUnsolvedFeedBack(Map<String, Object> searchParams) {
		return feedbackDao.countUnsolvedFeedBack(searchParams);
	}

	@Override
	public long countSolvedFeedBack(Map<String, Object> searchParams) {
		return feedbackDao.countSolvedFeedBack(searchParams);
	}

	@Override
	public boolean shareTeacher(String id, String shareEmployeeId, String updatedBy) {
		// gjtFeedbackDao.shareTeacher(id, shareEmployeeId);
		return true;
	}

	@Override
	public boolean insert(BzrGjtFeedback entity) {
		BzrGjtFeedback parentFb = queryById(entity.getPid());
		if (parentFb != null) {
			entity.setTags(parentFb.getTags());
			BzrGjtFeedback info = gjtFeedbackDao.save(entity);
			// 父留言修改为已处理
			gjtFeedbackDao.updateAnswer(entity.getPid());
			return info != null;
		}
		return false;
	}

}
