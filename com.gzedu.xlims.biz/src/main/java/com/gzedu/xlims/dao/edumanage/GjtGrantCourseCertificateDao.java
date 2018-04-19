package com.gzedu.xlims.dao.edumanage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.pojo.GjtGrantCourseCertificate;

public interface GjtGrantCourseCertificateDao extends JpaRepository<GjtGrantCourseCertificate, String>, JpaSpecificationExecutor<GjtGrantCourseCertificate> {

}
