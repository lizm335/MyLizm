/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.eenet.com
 */
package com.gzedu.xlims.serviceImpl.graduation;

import java.util.List;
import java.util.Map;

import com.gzedu.xlims.common.StringUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gzedu.xlims.common.dao.DynamicSpecifications;
import com.gzedu.xlims.common.dao.SearchFilter;
import com.gzedu.xlims.dao.graduation.GjtPhotographAddressDao;
import com.gzedu.xlims.pojo.graduation.GjtPhotographAddress;
import com.gzedu.xlims.service.graduation.GjtPhotographAddressService;

/**
 * 功能说明：
 *
 * @author 张伟文 zhangeweiwen@eenet.com
 * @version 3.0
 * @Date 2018年3月28日
 */
@Service
public class GjtPhotographAddressServiceImpl implements GjtPhotographAddressService {

    @Autowired
    GjtPhotographAddressDao gjtPhotographAddressDao;


    @Override
    public Page<GjtPhotographAddress> queryPageInfo(Map<String, Object> searchParams, PageRequest pageRequst) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        PageRequest page = new PageRequest(pageRequst.getPageNumber(), pageRequst.getPageSize(),
                new Sort(Sort.Direction.DESC, "createdDt"));

        if (!StringUtils.isEmpty(searchParams.get("EQ_province"))) {
            List<String> pIds = gjtPhotographAddressDao.getIdByProvince(ObjectUtils.toString(searchParams.get("EQ_province")));
            if (StringUtils.isNotBlank(pIds)) {
                filters.put("district", new SearchFilter("district", SearchFilter.Operator.IN, pIds));
                searchParams.put("district", new SearchFilter("district", SearchFilter.Operator.IN, pIds));
                searchParams.remove("EQ_province");
                filters.remove("province");
                filters.remove("EQ_province");
            }
        }
        if (!StringUtils.isEmpty(searchParams.get("EQ_city"))) {
            List<String> pIds = gjtPhotographAddressDao.getIdByCity(ObjectUtils.toString(searchParams.get("EQ_city")));
            if (StringUtils.isNotBlank(pIds)) {
                filters.put("district", new SearchFilter("district", SearchFilter.Operator.IN, pIds));
                searchParams.put("district", new SearchFilter("district", SearchFilter.Operator.IN, pIds));
                searchParams.remove("EQ_city");
                filters.remove("city");
                filters.remove("EQ_city");
            }
        }


        Specification<GjtPhotographAddress> spec = DynamicSpecifications.bySearchFilter(filters.values(),
                GjtPhotographAddress.class);
        return gjtPhotographAddressDao.findAll(spec, page);
    }

    @Override
    public GjtPhotographAddress save(GjtPhotographAddress entity) {
        return gjtPhotographAddressDao.save(entity);
    }

    @Override
    public GjtPhotographAddress update(GjtPhotographAddress entity) {
        return gjtPhotographAddressDao.save(entity);
    }

    @Override
    public GjtPhotographAddress queryById(String id) {
        return gjtPhotographAddressDao.findOne(id);
    }

    @Override
    public int delete(String id) {
        return gjtPhotographAddressDao.deleteById(id);
    }

    @Override
    public Boolean updateEnabled(String id, String enabled) {
        int i = gjtPhotographAddressDao.updateEnabled(id, enabled);
        return i > 0 ? true : false;
    }

}
