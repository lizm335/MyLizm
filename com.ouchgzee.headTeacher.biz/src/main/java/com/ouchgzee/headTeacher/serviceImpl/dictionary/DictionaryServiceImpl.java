/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl.dictionary;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.ouchgzee.headTeacher.dao.base.BaseDao;
import com.ouchgzee.headTeacher.dao.dictionary.GjtDistrictDao;
import com.ouchgzee.headTeacher.dao.dictionary.TblSysDataDao;
import com.ouchgzee.headTeacher.pojo.BzrGjtDistrict;
import com.ouchgzee.headTeacher.pojo.BzrTblSysData;
import com.ouchgzee.headTeacher.service.dictionary.BzrDictionaryService;
import com.ouchgzee.headTeacher.serviceImpl.base.BaseServiceImpl;
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
@Deprecated @Service("bzrDictionaryServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class DictionaryServiceImpl extends BaseServiceImpl<BzrTblSysData> implements BzrDictionaryService {

    @Autowired
    private TblSysDataDao tblSysDataDao;

    @Autowired
    private GjtDistrictDao gjtDistrictDao;

    @Override
    protected BaseDao<BzrTblSysData, String> getBaseDao() {
        return tblSysDataDao;
    }

    @Override
    public List<BzrTblSysData> queryByTypeCode(String typeCode) {
        return tblSysDataDao.findByIsDeletedAndTypeCodeOrderByOrdNoAsc(Constants.BOOLEAN_NO, typeCode);
    }

    @Override
    public List<BzrTblSysData> queryByTypeName(String typeName) {
        return tblSysDataDao.findByIsDeletedAndTypeNameOrderByOrdNoAsc(Constants.BOOLEAN_NO, typeName);
    }

    @Override
    public List<BzrGjtDistrict> queryAreaBy(Map<String, Object> searchParams, Sort sort) {
        if (sort == null) {
            sort = new Sort(Sort.Direction.DESC, "createdDt");
        }
        Criteria<BzrGjtDistrict> spec = new Criteria();
//        spec.add(Restrictions.eq("isDeleted", Constants.BOOLEAN_NO, true));
        spec.addAll(Restrictions.parse(searchParams));
        return gjtDistrictDao.findAll(spec, sort);
    }

    @Override
    public List<Object[]> queryAllDistrict() {
        return gjtDistrictDao.findDistrictAll();
    }

}
