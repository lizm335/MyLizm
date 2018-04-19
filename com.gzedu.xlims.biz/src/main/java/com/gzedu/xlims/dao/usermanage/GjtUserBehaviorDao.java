/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.dao.usermanage;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtUserBehavior;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 用户行为记录DAO类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月08日
 * @version 2.5
 * @since JDK 1.7
 */
public interface GjtUserBehaviorDao extends BaseDao<GjtUserBehavior, String> {

    /**
     * 获取用户有行为的时间
     * @param userId
     * @return
     */
    @Query(value = "SELECT * FROM (select distinct TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE from Gjt_User_Behavior T where T.USER_ID=?1) TEMP ORDER BY CREATE_DATE", nativeQuery = true)
    List<String> queryAllDate(String userId);

}
