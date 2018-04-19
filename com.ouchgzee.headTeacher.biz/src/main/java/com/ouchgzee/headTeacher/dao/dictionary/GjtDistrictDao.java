/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.dao.dictionary;

import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtDistrict;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 全国区域信息操作类<br>
 * 功能说明：
 * 
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月8日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Repository("bzrGjtDistrictDao") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public interface GjtDistrictDao extends BaseDao<BzrGjtDistrict, String> {

    /**
     * 获取所有区域信息
     *
     * @return
     */
    @Query(value = "SELECT t.ID CODE,t.NAME FROM GJT_DISTRICT t ORDER BY t.ID", nativeQuery = true)
    List<Object[]> findDistrictAll();

}
