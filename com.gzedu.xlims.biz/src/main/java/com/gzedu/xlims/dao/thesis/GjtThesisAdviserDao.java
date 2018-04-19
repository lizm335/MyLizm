package com.gzedu.xlims.dao.thesis;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.gzedu.xlims.dao.thesis.repository.GjtThesisAdviserRepository;
import com.gzedu.xlims.pojo.thesis.GjtThesisAdviser;

@Repository
public class GjtThesisAdviserDao {

	@Autowired
	private GjtThesisAdviserRepository gjtThesisAdviserRepository;
	
	public void deleteByArrangeIdAndAdviserTypes(String arrangeId, Set<Integer> dviserTypes) {
		gjtThesisAdviserRepository.deleteByArrangeIdAndAdviserTypes(arrangeId, dviserTypes);
	}
	
	public void save(List<GjtThesisAdviser> entities) {
		gjtThesisAdviserRepository.save(entities);
	}

}
