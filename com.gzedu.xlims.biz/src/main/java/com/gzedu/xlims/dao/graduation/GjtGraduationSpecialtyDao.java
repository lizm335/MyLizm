package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gzedu.xlims.pojo.graduation.GjtGraduationSpecialty;

public interface GjtGraduationSpecialtyDao extends JpaRepository<GjtGraduationSpecialty, String>, JpaSpecificationExecutor<GjtGraduationSpecialty> {
	
	@Query("SELECT s FROM GjtGraduationSpecialty s where s.isDeleted='N' and s.gjtGraduationBatch.batchId = ?1 and s.specialtyId = ?2 and s.trainingLevel = ?3")
	public GjtGraduationSpecialty queryOneBySpecialty(String batchId, String specialtyId, String trainingLevel);

}
