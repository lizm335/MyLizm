package com.gzedu.xlims.service.transaction;

import java.util.List;

import com.gzedu.xlims.pojo.GjtExemptExamMaterial;
import com.gzedu.xlims.pojo.GjtUserAccount;

/**
 * 功能说明：
 * @author 卢林林   lulinlin@eenet.com
 * @Date 2018年3月24日
 * @version 2.5
 */
public interface GjtExemptExamMaterialService {

	int insert(GjtExemptExamMaterial item, String installId, String[] materialName, String[] memo, String[] isOnlineExam);

	int insertMaterial(GjtExemptExamMaterial item, String[] nj, String courseId, GjtUserAccount user, String[] materialName, String[] memo, String[] isOnlineExam);

	List<GjtExemptExamMaterial> findByInstallIdAndIsDeleted(String installId,String booleanNo);

	void deletedMaterial(String  installId);

}
