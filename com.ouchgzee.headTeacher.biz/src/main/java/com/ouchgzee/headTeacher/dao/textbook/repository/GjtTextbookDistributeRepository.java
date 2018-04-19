package com.ouchgzee.headTeacher.dao.textbook.repository;

import java.util.Collection;
import java.util.List;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import org.springframework.data.jpa.repository.Query;

import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookDistribute;
import org.springframework.stereotype.Repository;

@Deprecated @Repository("bzrGjtTextbookDistributeRepository") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtTextbookDistributeRepository extends BaseDao<BzrGjtTextbookDistribute, String> {
	
	public BzrGjtTextbookDistribute findByOrderCodeAndStatusAndIsDeleted(String orderCode, int status, String isDeleted);
	
	public List<BzrGjtTextbookDistribute> findByStudentIdAndIsDeletedAndStatusIn(String studentId, String isDeleted, Collection<Integer> statuses);
	
	@Query("select b from BzrGjtTextbookDistribute b inner join b.gjtStudentInfo s inner join s.gjtClassStudentList cs where b.gjtStudentInfo.xxId = ?1 and cs.isDeleted = 'N' and cs.gjtClassInfo.classId = ?2 and b.isDeleted = ?3 and b.status in (?4) ")
	public List<BzrGjtTextbookDistribute> findByIsDeletedAndStatusIn(String orgId, String classId, String isDeleted, Collection<Integer> statuses);

}
