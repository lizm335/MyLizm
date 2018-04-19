package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookStockApprovalRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookStockApproval;

@Repository
public class GjtTextbookStockApprovalDao {

	@Autowired
	private GjtTextbookStockApprovalRepository gjtTextbookStockApprovalRepository;
	
	public GjtTextbookStockApproval save(GjtTextbookStockApproval entity) {
		return gjtTextbookStockApprovalRepository.save(entity);
	}
	
	public List<GjtTextbookStockApproval> save(List<GjtTextbookStockApproval> entities) {
		return gjtTextbookStockApprovalRepository.save(entities);
	}

}
