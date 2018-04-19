
/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.ouchgzee.headTeacher.service.systemManage;

import com.ouchgzee.headTeacher.pojo.BzrPriOperateInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 功能说明： 操作
 *
 * @author 李明 liming@eenet.com
 * @Date 2016年5月16日
 * @version 2.5
 *
 */
@Deprecated public interface BzrPriOperateInfoService {

    List<BzrPriOperateInfo> queryAll();

    List<BzrPriOperateInfo> queryAll(Iterable<String> ids);

    Page<BzrPriOperateInfo> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

    BzrPriOperateInfo queryBy(String id);

    void insert(BzrPriOperateInfo entity);

    void update(BzrPriOperateInfo entity);

    void delete(String id);

    void delete(Iterable<String> ids);
}
