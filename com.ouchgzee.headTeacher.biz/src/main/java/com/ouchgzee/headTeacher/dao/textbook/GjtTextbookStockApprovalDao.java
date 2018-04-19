package com.ouchgzee.headTeacher.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ouchgzee.headTeacher.dao.textbook.repository.GjtTextbookStockApprovalRepository;
import com.ouchgzee.headTeacher.pojo.textbook.BzrGjtTextbookStockApproval;

@Deprecated @Repository("bzrGjtTextbookStockApprovalDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class GjtTextbookStockApprovalDao {

	@Autowired
	private GjtTextbookStockApprovalRepository gjtTextbookStockApprovalRepository;
	
	public BzrGjtTextbookStockApproval save(BzrGjtTextbookStockApproval entity) {
		return gjtTextbookStockApprovalRepository.save(entity);
	}
	
	public List<BzrGjtTextbookStockApproval> save(List<BzrGjtTextbookStockApproval> entities) {
		return gjtTextbookStockApprovalRepository.save(entities);
	}

}
