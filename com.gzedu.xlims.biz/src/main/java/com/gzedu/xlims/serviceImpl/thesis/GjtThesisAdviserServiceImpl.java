package com.gzedu.xlims.serviceImpl.thesis;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.thesis.GjtThesisAdviserDao;
import com.gzedu.xlims.service.thesis.GjtThesisAdviserService;

@Service
public class GjtThesisAdviserServiceImpl implements GjtThesisAdviserService {
	
	@Autowired
	private GjtThesisAdviserDao gjtThesisAdviserDao;

	@Override
	public void deleteByArrangeIdAndAdviserTypes(String arrangeId, Set<Integer> dviserTypes) {
		gjtThesisAdviserDao.deleteByArrangeIdAndAdviserTypes(arrangeId, dviserTypes);
	}

}
