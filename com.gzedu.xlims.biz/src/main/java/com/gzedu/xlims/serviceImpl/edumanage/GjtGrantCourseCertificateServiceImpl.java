package com.gzedu.xlims.serviceImpl.edumanage;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.dao.edumanage.GjtGrantCourseCertificateDao;
import com.gzedu.xlims.pojo.GjtGrantCourseCertificate;
import com.gzedu.xlims.service.edumanage.GjtGrantCourseCertificateService;

@Service
public class GjtGrantCourseCertificateServiceImpl implements GjtGrantCourseCertificateService {
	@Autowired
	private GjtGrantCourseCertificateDao gjtGrantCourseCertificateDao;
	
	public void remove(Collection<GjtGrantCourseCertificate> entities) {
		gjtGrantCourseCertificateDao.deleteInBatch(entities);
	}
	
}
