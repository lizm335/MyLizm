package com.gzedu.xlims.serviceImpl;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.criterion.Criteria;
import com.gzedu.xlims.common.criterion.Restrictions;
import com.gzedu.xlims.dao.SysLogDao;
import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.SysLogEntity;
import com.gzedu.xlims.service.SysLogService;
import com.gzedu.xlims.serviceImpl.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 系统日志逻辑类<br>
 * 功能说明：
 *
 * @author 黄一飞 huangyifei@eenet.com
 * @Date 2017年07月24日
 * @version 2.5
 * @since JDK 1.7
 */
@Service
public class SysLogServiceImpl extends BaseServiceImpl<SysLogEntity> implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    @Override
    protected BaseDao<SysLogEntity, String> getBaseDao() {
        return this.sysLogDao;
    }

    @Override
    public Page<SysLogEntity> queryByPage(Map<String, Object> searchParams, PageRequest pageRequest) {
        if (pageRequest.getSort() == null) {
            pageRequest = new PageRequest(pageRequest.getPageNumber(), pageRequest.getPageSize(),
                    new Sort(Sort.Direction.DESC, "createDate"));
        }
        Criteria<SysLogEntity> spec = new Criteria();
        String platfromType = (String) searchParams.get("EQ_platfromType");
        if(StringUtils.isNotBlank(platfromType)) {
            spec.add(Restrictions.eq("platfromType", NumberUtils.toInt(platfromType), true));
        }
        String likeUsername = (String) searchParams.get("LIKE_username");
        if(StringUtils.isNotBlank(likeUsername)) {
            spec.add(Restrictions.or(Restrictions.like("username", likeUsername, true), Restrictions.like("operation", likeUsername, true)));
        }
        String method = (String) searchParams.get("LIKE_method");
        if(StringUtils.isNotBlank(method)) {
        	 spec.add(Restrictions.like("method", method, true));
        }
        String params = (String) searchParams.get("LIKE_params");
        if(StringUtils.isNotBlank(params)) {
            spec.add(Restrictions.like("params", params, true));
        }
        int time = NumberUtils.toInt((String) searchParams.get("GTE_time"));
        if(time == 30) {
            spec.add(Restrictions.gte("time", time*1000, true));
        } else if(time == 10) {
            spec.add(Restrictions.gte("time", time*1000, true));
            spec.add(Restrictions.lt("time", 30000, true));
        }
        // spec.addAll(Restrictions.parse(searchParams));
        return getBaseDao().findAll(spec, pageRequest);
    }

    @Override
    public boolean insert(SysLogEntity entity) {
        entity.setId(UUIDUtils.random());
        entity.setCreateDate(new Date());
        SysLogEntity info = sysLogDao.save(entity);
        return info != null;
    }
    
}
