/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.graduation;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtDegreeCollege;
import org.springframework.data.jpa.repository.Query;

/**
 * 学位院校操作类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年11月21日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtDegreeCollegeDao extends BaseDao<GjtDegreeCollege, String> {

    @Query(value = "select * from (select * from GJT_DEGREE_COLLEGE t where  t.ORG_ID=?1) where rownum=1", nativeQuery = true)
    GjtDegreeCollege queryByOrgID(String orgId);
}
