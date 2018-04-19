package com.gzedu.xlims.serviceImpl.transaction;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.common.dao.SearchFilter.Operator;
import com.gzedu.xlims.dao.transaction.GjtExemptExamInstallDao;
import com.gzedu.xlims.dao.transaction.GjtExemptExamMaterialDao;
import com.gzedu.xlims.pojo.GjtExemptExamInstall;
import com.gzedu.xlims.service.transaction.GjtExemptExamInstallService;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月22日
 * @version 2.5
 */
@Service
public class GjtExemptExamInstallServiceImpl implements GjtExemptExamInstallService{
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamInstallServiceImpl.class);

	@Autowired
	private GjtExemptExamInstallDao gjtExemptExamInstallDao;
	
	@Autowired
	private GjtExemptExamMaterialDao gjtExemptExamMaterialDao;
	
	@Override
	public Page<GjtExemptExamInstall> queryAll(String xxId, Map<String, Object> searchParams,PageRequest pageRequst) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		filters.put("xxId",new SearchFilter("xxId", Operator.EQ, xxId));
		filters.put("isDeleted",new SearchFilter("isDeleted", Operator.EQ, "N"));
		Specification<GjtExemptExamInstall> spec = DynamicSpecifications.bySearchFilter(filters.values(),
				GjtExemptExamInstall.class);
		return gjtExemptExamInstallDao.findAll(spec, pageRequst);
	}

	@Override
	@Transactional
	public void insert(GjtExemptExamInstall install) {
		gjtExemptExamInstallDao.save(install);		
	}

	@Override
	public GjtExemptExamInstall findByInstallId(String installId) {		
		return gjtExemptExamInstallDao.findByInstallId(installId);
	}

	@Override
	public Boolean saveEntity(GjtExemptExamInstall install) {		
		GjtExemptExamInstall info=gjtExemptExamInstallDao.save(install);
		if(info==null){
			return false;
		}
		return true;
	}

	@Override
	public int deleteInstall(String installId) {
		try {
			gjtExemptExamMaterialDao.updateMaterial(installId);			
			gjtExemptExamInstallDao.updateInstall(installId);					
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return 0;
		}		
		return 1;
	}
	
	/**
	 * 查询免修免考信息
	 * @param xxId
	 * @param booleanNo
	 * @return
	 */
	@Override
	public List<GjtExemptExamInstall> findByXxIdAndIsDeletedAndStatus(String xxId, String booleanNo,String status) {	
		return gjtExemptExamInstallDao.findByXxIdAndIsDeletedAndStatus(xxId,booleanNo,status);
	}

	@Override
	public List<GjtExemptExamInstall> findByXxIdAndCourseIdAndIsDeleted(String xxId, String courseId, String booleanNo) {
		return gjtExemptExamInstallDao.findByXxIdAndCourseIdAndIsDeleted(xxId,courseId,booleanNo);
	}
}
