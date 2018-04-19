package com.gzedu.xlims.dao.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.exam.GjtExamRoomNew;

public interface GjtExamRoomNewRepository
		extends JpaRepository<GjtExamRoomNew, String>, JpaSpecificationExecutor<GjtExamRoomNew> {

}
