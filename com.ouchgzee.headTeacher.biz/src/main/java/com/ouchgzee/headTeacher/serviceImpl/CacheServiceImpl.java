/**
 * Copyright(c) 2016 版权所有：广州远程教育中心 www.eenet.com
 */
package com.ouchgzee.headTeacher.serviceImpl;

import com.gzedu.xlims.common.Constants;
import com.gzedu.xlims.common.StringUtils;
import com.ouchgzee.headTeacher.dao.dictionary.TblSysDataDao;
import com.ouchgzee.headTeacher.dictionary.Dictionary;
import com.ouchgzee.headTeacher.pojo.BzrTblSysData;
import com.ouchgzee.headTeacher.service.BzrCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存服务接口<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2016年6月7日
 * @version 2.5
 * @since JDK 1.7
 */
@Deprecated @Service("bzrCacheServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class CacheServiceImpl implements BzrCacheService {

    @Autowired
    private TblSysDataDao tblSysDataDao;

    @Override
    public Object getCached(String key) {
        return null;
    }

    @Override
    public List<Value> getCachedDictionary(String key) {
        List<Value> vlist = Dictionary.get(key);
        if(vlist == null) {
            List<BzrTblSysData> dataList = tblSysDataDao.findByIsDeletedAndTypeCodeOrderByOrdNoAsc(Constants.BOOLEAN_NO, key);
            vlist = new ArrayList();
            for (BzrTblSysData data : dataList) {
                BzrCacheService.Value v = new BzrCacheService.Value(data.getCode(), data.getName());
                vlist.add(v);
            }
            // 添加到缓存中
            Dictionary.add(key, vlist);
        }
        return vlist;
    }

    @Override
    public Map<String, String> getCachedDictionaryMap(String key) {
        Map<String, String> vmap = new HashMap();
        for (Value v : getCachedDictionary(key)) {
            vmap.put(v.getCode(), v.getName());
        }
        return vmap;
    }

    @Override
    public String getCachedDictionaryName(String key, String code) {
        String name = null;
        if(StringUtils.isNotBlank(code)) {
            List<BzrCacheService.Value> valueList = this.getCachedDictionary(key);
            for (BzrCacheService.Value v : valueList) {
                if(v.getCode().equals(code)) {
                    name = v.getName();
                    break;
                }
            }
        }
        return name;
    }

    @Override
    public boolean updateCached(String key, Object value) {
        return false;
    }

    @Override
    public boolean deleteCached(String key) {
        return false;
    }
}
