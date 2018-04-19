package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviser;

public interface GjtGraduationAdviserDao extends JpaRepository<GjtGraduationAdviser, String>, JpaSpecificationExecutor<GjtGraduationAdviser> {

	@Modifying
	@Transactional
	@Query("delete from GjtGraduationAdviser a where a.gjtGraduationSpecialty.settingId = ?1")
	public void deleteBySettingId(String settingId);

}
