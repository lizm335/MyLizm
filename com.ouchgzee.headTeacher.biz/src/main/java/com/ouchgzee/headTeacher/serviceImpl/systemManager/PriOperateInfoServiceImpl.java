/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.serviceImpl.systemManager;

import com.gzedu.xlims.common.UUIDUtils;
import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.ouchgzee.headTeacher.dao.model.PriOperateInfoDao;
import com.ouchgzee.headTeacher.pojo.BzrPriOperateInfo;
import com.ouchgzee.headTeacher.service.systemManage.BzrPriOperateInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 功能说明：
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Deprecated @Service("bzrPriOperateInfoServiceImpl") // 为将来班主任的entity/dao/service模块合并到管理后台做准备，合并后尽可能的少在班主任上写代码，防止Spring容器name冲突，组件的name需加前缀bzr
public class PriOperateInfoServiceImpl implements BzrPriOperateInfoService {

    @Autowired
    PriOperateInfoDao priOperateInfoDao;

    @Override
    public Page<BzrPriOperateInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<BzrPriOperateInfo> spec = DynamicSpecifications.bySearchFilter(filters.values(),
                BzrPriOperateInfo.class);
        return priOperateInfoDao.findAll(spec, pageRequst);
    }

    @Override
    public BzrPriOperateInfo queryBy(String id) {
        return priOperateInfoDao.findOne(id);
    }

    @Override
    public void insert(BzrPriOperateInfo entity) {
        entity.setOperateId(UUIDUtils.random());
        entity.setCreatedDt(new Date());
        priOperateInfoDao.save(entity);
    }

    @Override
    public void delete(String id) {
        priOperateInfoDao.delete(id);
    }

    @Override
    public void delete(Iterable<String> ids) {
        priOperateInfoDao.delete(priOperateInfoDao.findAll(ids));
    }

    @Override
    public void update(BzrPriOperateInfo entity) {
        entity.setUpdatedDt(new Date());
        priOperateInfoDao.save(entity);
    }

    @Override
    public List<BzrPriOperateInfo> queryAll() {
        return priOperateInfoDao.findAll();
    }

    @Override
    public List<BzrPriOperateInfo> queryAll(Iterable<String> ids) {
        return (List<BzrPriOperateInfo>) priOperateInfoDao.findAll(ids);
    }

}
