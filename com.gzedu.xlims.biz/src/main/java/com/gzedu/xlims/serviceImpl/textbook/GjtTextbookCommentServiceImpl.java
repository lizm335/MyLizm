package com.gzedu.xlims.serviceImpl.textbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.textbook.GjtTextbookCommentDao;
import com.gzedu.xlims.pojo.textbook.GjtTextbookComment;
import com.gzedu.xlims.service.textbook.GjtTextbookCommentService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;

@Service
public class GjtTextbookCommentServiceImpl extends BaseServiceImpl<GjtTextbookComment> implements GjtTextbookCommentService {
	
	private static final Log log = LogFactory.getLog(GjtTextbookCommentServiceImpl.class);

	@Autowired
	private GjtTextbookCommentDao gjtTextbookCommentDao;
	@Override
	protected BaseDao<GjtTextbookComment, String> getBaseDao() {
		return gjtTextbookCommentDao;
	}


}
