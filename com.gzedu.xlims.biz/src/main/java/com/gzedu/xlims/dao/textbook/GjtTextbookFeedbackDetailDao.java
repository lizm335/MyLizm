package com.gzedu.xlims.dao.textbook;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.textbook.repository.GjtTextbookFeedbackDetailRepository;
import com.gzedu.xlims.pojo.textbook.GjtTextbookFeedbackDetail;

@Repository
public class GjtTextbookFeedbackDetailDao {

	@Autowired
	private GjtTextbookFeedbackDetailRepository gjtTextbookFeedbackDetailRepository;
	
	public void save(List<GjtTextbookFeedbackDetail> entities) {
		gjtTextbookFeedbackDetailRepository.save(entities);
	}

}
