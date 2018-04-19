/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.service.dictionary;

import com.gzedu.xlims.pojo.GjtDistrict;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.service.base.BaseService;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

/**
 * 数据字典业务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
public interface DictionaryService extends BaseService<TblSysData> {

    /**
     * 根据编码查询数据
     * @param typeCode
     * @return
     */
    List<TblSysData> queryByTypeCode(String typeCode);

    /**
     * 根据名称查询数据
     * @param typeName
     * @return
     */
    List<TblSysData> queryByTypeName(String typeName);

    /**
     * 根据条件查询区域信息
     *
     * @param searchParams
     * @param sort
     * @return
     */
    List<GjtDistrict> queryAreaBy(Map<String, Object> searchParams, Sort sort);

    /**
     * 按照编码号排序查询到所有的区域信息
     * @return
     */
    List<Object[]> queryAllDistrict();

}
