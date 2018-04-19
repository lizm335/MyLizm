package com.gzedu.xlims.dao.practice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.practice.repository.GjtPracticeAdviserRepository;
import com.gzedu.xlims.pojo.practice.GjtPracticeAdviser;

@Repository
public class GjtPracticeAdviserDao {
	
	@Autowired
	private GjtPracticeAdviserRepository gjtPracticeAdviserRepository;
	
	public void deleteByArrangeId(String arrangeId) {
		gjtPracticeAdviserRepository.deleteByArrangeId(arrangeId);
	}
	
	public void save(List<GjtPracticeAdviser> entities) {
		gjtPracticeAdviserRepository.save(entities);
	}

}
