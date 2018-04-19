/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.dictionary;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.dao.dictionary.GjtDistrictDao;
import com.gzedu.xlims.dao.dictionary.TblSysDataDao;
import com.gzedu.xlims.pojo.GjtDistrict;
import com.gzedu.xlims.pojo.TblSysData;
import com.gzedu.xlims.service.dictionary.DictionaryService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
@Service
public class DictionaryServiceImpl extends BaseServiceImpl<TblSysData> implements DictionaryService {

    @Autowired
    private TblSysDataDao tblSysDataDao;

    @Autowired
    private GjtDistrictDao gjtDistrictDao;

    @Override
    protected BaseDao<TblSysData, String> getBaseDao() {
        return tblSysDataDao;
    }

    @Override
    public List<TblSysData> queryByTypeCode(String typeCode) {
        return tblSysDataDao.findByIsDeletedAndTypeCodeOrderByOrdNoAsc(Constants.BOOLEAN_NO, typeCode);
    }

    @Override
    public List<TblSysData> queryByTypeName(String typeName) {
        return tblSysDataDao.findByIsDeletedAndTypeNameOrderByOrdNoAsc(Constants.BOOLEAN_NO, typeName);
    }

    @Override
    public List<GjtDistrict> queryAreaBy(Map<String, Object> searchParams, Sort sort) {
        if (sort == null) {
            sort = new Sort(Sort.Direction.DESC, "createdDt");
        }
        Criteria<GjtDistrict> spec = new Criteria();
//        spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
        spec.addAll(Restrictions.parse(searchParams));
        return gjtDistrictDao.findAll(spec, sort);
    }

    @Override
    public List<Object[]> queryAllDistrict() {
        return gjtDistrictDao.findDistrictAll();
    }

}
