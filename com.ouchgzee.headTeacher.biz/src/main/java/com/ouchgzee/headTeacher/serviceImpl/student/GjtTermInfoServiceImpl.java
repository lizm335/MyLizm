/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.comm.CommonDao;
import com.ouchgzee.headTeacher.dao.student.GjtTermInfoDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtTermInfo;
import com.ouchgzee.headTeacher.service.student.BzrGjtTermInfoService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;

/**
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月16日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrGjtTermInfoServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTermInfoServiceImpl extends BaseServiceImpl<BzrGjtTermInfo> implements BzrGjtTermInfoService {

	@Autowired
	private GjtTermInfoDao gjtTermInfoDao;
	@Autowired
	private CommonDao commonDao;

	@Override
	protected BaseDao<BzrGjtTermInfo, String> getBaseDao() {
		return gjtTermInfoDao;
	}

	@Override
	public String getStudentCurrentTerm(String studentId) {
		String currentTermId = gjtTermInfoDao.findByStudentCurrentTerm(studentId);
		return currentTermId;
	}

	@Override
	public Object[] countByStudentTerm(String studentId) {
		return gjtTermInfoDao.countByStudentTerm(studentId);
	}

	/**
	 * 根据班级id查询全部的期
	 *
	 * @param classId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findByClassTerms(String classId) {

		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("classId", classId);

		sql.append("  	SELECT");
		sql.append("  	DISTINCT t.TERM_ID,");
		sql.append("  	t.TERM_NAME");
		sql.append("  FROM");
		sql.append("  	gjt_term_info t");
		sql.append("  INNER JOIN gjt_student_term b ON");
		sql.append("  	t.term_id = b.term_id");
		sql.append("  LEFT JOIN GJT_CLASS_STUDENT gcs ON");
		sql.append("  	gcs.STUDENT_ID = b.STUDENT_ID");
		sql.append("  	AND gcs.IS_DELETED = 'N'");
		sql.append("  WHERE");
		sql.append("  	t.is_deleted = 'N'");
		sql.append("  	AND gcs.CLASS_ID =:classId");

		return commonDao.queryForMapListNative(sql.toString(), params);
	}
}
