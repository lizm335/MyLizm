package com.gzedu.xlims.serviceImpl.transaction;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.gzedu.EmptyUtils;
import com.gzedu.xlims.dao.transaction.GjtExemptExamInstallDao;
import com.gzedu.xlims.dao.transaction.GjtExemptExamMaterialDao;
import com.gzedu.xlims.pojo.GjtExemptExamInstall;
import com.gzedu.xlims.pojo.GjtExemptExamMaterial;
import com.gzedu.xlims.pojo.GjtUserAccount;
import com.gzedu.xlims.service.transaction.GjtExemptExamMaterialService;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月24日
 * @version 2.5
 */
@Service
public class GjtExemptExamMaterialServiceImpl implements GjtExemptExamMaterialService{
	private static final Logger log = LoggerFactory.getLogger(GjtExemptExamMaterialServiceImpl.class);
	
	@Autowired
	private GjtExemptExamMaterialDao gjtExemptExamMaterialDao;
	
	@Autowired
	private GjtExemptExamInstallDao GjtExemptExamInstallDao;

	@Override
	@Transactional
	public int insertMaterial(GjtExemptExamMaterial item,String[] nj, String courseId, GjtUserAccount user,String[] materialName, String[] memo, String[] isOnlineExam) {		
		try {
			String installId=UUIDUtils.random();
			GjtExemptExamInstall install=new GjtExemptExamInstall();			
			install.setInstallId(installId);
			install.setCourseId(courseId);
			install.setIsDeleted("N");
			StringBuffer grade = new StringBuffer();
			StringBuffer name = new StringBuffer();
			if(EmptyUtils.isNotEmpty(nj)){				
				for(int j=0;j<nj.length;j++){
					String[] content=nj[j].split(",");					
					for(int k=0;k<content.length;k++){
						String str=content[k];
						String[] pp=str.split("_");
						String grade_id=pp[0];//学期ID
						String gradeName=pp[1];//学期名称
						grade.append(grade_id+",");
						name.append(gradeName+",");												
					}
				}
				install.setGradeId(grade.substring(0,grade.length()-1));
				install.setGradeName(name.substring(0,name.length()-1));
			}else{
				install.setGradeId("");//通用
			}
			install.setMaterial(String.valueOf(materialName.length));
			install.setXxId(user.getGjtOrg().getId());
			install.setStatus("0");//默认为已停用
			GjtExemptExamInstallDao.save(install);
			for(int i=0;i<materialName.length;i++){
				String id=UUIDUtils.random();
				String material_name=materialName[i];
				String remak=memo[i];
				String isOnline=isOnlineExam[i];
				item.setId(id);
				item.setInstallId(installId);
				item.setMaterialName(material_name);
				item.setMemo(remak);
				item.setIsOnlineExam(isOnline);
				gjtExemptExamMaterialDao.save(item);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);			
			return 0;
		}		
		return 1;
	}

	@Override
	public List<GjtExemptExamMaterial> findByInstallIdAndIsDeleted(String installId,String booleanNo) {
		return gjtExemptExamMaterialDao.findByInstallIdAndIsDeleted(installId,booleanNo);
	}

	@Override
	public void deletedMaterial(String installId) {
		gjtExemptExamMaterialDao.deletedMaterial(installId);		
	}

	@Override
	@Transactional
	public int insert(GjtExemptExamMaterial item, String installId, String[] materialName, String[] memo,String[] isOnlineExam) {
		log.info("免修免考设置： installId 【"+installId+"】");
		try {
			for(int i=0;i<materialName.length;i++){
				String id=UUIDUtils.random();
				String material_name=materialName[i];
				String remak=memo[i];
				String isOnline=isOnlineExam[i];
				item.setId(id);
				item.setInstallId(installId);
				item.setMaterialName(material_name);
				item.setMemo(remak);
				item.setIsOnlineExam(isOnline);
				gjtExemptExamMaterialDao.save(item);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);			
			return 0;
		}
		return 1;
	}
}
