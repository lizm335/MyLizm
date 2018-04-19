package com.gzedu.xlims.service.recruitmanage;

import com.gzedu.xlims.pojo.GjtEnrollBatchNew;
import com.gzedu.xlims.pojo.GjtStudyEnrollNum;
/**
 * 
 * 功能说明：
 * 
 * @author lulinlin@eenet.com
 * @Date 2016年11月20日
 *
 */
public interface GjtStudyEnrollNumService {

	GjtStudyEnrollNum queryByIdOrEnrId(String enrollBatchId, String xxzxId);
	
	public int update(GjtStudyEnrollNum entity);
	
	public void insert(GjtStudyEnrollNum entity);
	
	/**
	 * 假删除（单个）
	 * @param ids
	 */
	public void delete(String id);
}
