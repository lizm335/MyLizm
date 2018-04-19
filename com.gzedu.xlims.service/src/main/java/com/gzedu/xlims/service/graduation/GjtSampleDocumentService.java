package com.gzedu.xlims.service.graduation;

import com.gzedu.xlims.pojo.graduation.GjtSampleDocument;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 示例文档业务逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月11日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtSampleDocumentService extends BaseService<GjtSampleDocument> {

	/**
	 * 随机获取自我鉴定示例文档下载路径，并更新下载次数
	 * @param xxId
	 * @param specialtyId
     * @return
     */
	String updateRandomGraduationDoc(String xxId, String specialtyId);

	/**
	 * 批量删除示例文档
	 * @param ids
	 * @return
     */
	boolean deleteInBatch(String[] ids);
}
