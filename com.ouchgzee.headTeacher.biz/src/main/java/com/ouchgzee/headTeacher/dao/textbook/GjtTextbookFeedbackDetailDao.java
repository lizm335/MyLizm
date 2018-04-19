package com.ouchgzee.headTeacher.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookFeedbackDetailRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookFeedbackDetail;

@Deprecated @Repository("bzrGjtTextbookFeedbackDetailDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookFeedbackDetailDao {

	@Autowired
	private GjtTextbookFeedbackDetailRepository gjtTextbookFeedbackDetailRepository;
	
	public void save(List<BzrGjtTextbookFeedbackDetail> entities) {
		gjtTextbookFeedbackDetailRepository.save(entities);
	}

}
