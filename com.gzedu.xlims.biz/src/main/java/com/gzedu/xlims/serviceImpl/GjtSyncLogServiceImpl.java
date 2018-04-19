package com.gzedu.xlims.serviceImpl;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.dao.GjtSyncLogDao;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.GjtSyncLog;
import com.gzedu.xlims.service.GjtSyncLogService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 同步数据出错日志逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年08月22日
 * @version 3.0
 * @since JDK 1.7
 */
@Service
public class GjtSyncLogServiceImpl extends BaseServiceImpl<GjtSyncLog> implements GjtSyncLogService {

    @Autowired
    private GjtSyncLogDao gjtSyncLogDao;

    @Override
    protected BaseDao<GjtSyncLog, String> getBaseDao() {
        return this.gjtSyncLogDao;
    }

    @Override
    public boolean insert(GjtSyncLog entity) {
        entity.setId(UUIDUtils.random());
        entity.setCreateDate(new Date());
        GjtSyncLog info = gjtSyncLogDao.save(entity);
        return info != null;
    }
    
}
