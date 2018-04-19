/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtSampleDocument;

/**
 * 示例文档操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月11日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtSampleDocumentDao extends BaseDao<GjtSampleDocument, String> {

//    /**
//     * 逻辑删除示例文档
//     * @param id
//     * @return
//     */
//    @Modifying
//    @Transactional
//    @Query("UPDATE GjtSampleDocument t SET t.isDeleted='Y' WHERE t.documentId=?1")
//    int deleteById(String id);

}
