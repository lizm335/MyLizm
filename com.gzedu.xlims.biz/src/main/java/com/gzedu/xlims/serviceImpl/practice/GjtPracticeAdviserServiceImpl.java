package com.gzedu.xlims.serviceImpl.practice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.practice.GjtPracticeAdviserDao;
import com.gzedu.xlims.service.practice.GjtPracticeAdviserService;

@Service
public class GjtPracticeAdviserServiceImpl implements GjtPracticeAdviserService {
	
	@Autowired
	private GjtPracticeAdviserDao gjtPracticeAdviserDao;

	@Override
	public void deleteByArrangeId(String arrangeId) {
		gjtPracticeAdviserDao.deleteByArrangeId(arrangeId);
	}

}
