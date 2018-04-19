package com.gzedu.xlims.service.recruitmanage;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtEnrollBatch;

/**
 * 
 * 功能说明：
 * 
 * @author 张伟文 zhangweiwen@eenet.com
 * @Date 2016年6月21日
 * @version 2.5
 * @since JDK1.7
 *
 */
public interface GjtEnrollBatchService {
	public Page<GjtEnrollBatch> queryAll(final String schoolId, Map<String, Object> searchParams,
			PageRequest pageRequst);

	public GjtEnrollBatch queryBy(String id);

	public void delete(Iterable<String> ids);

	public void delete(String id);

	public void insert(GjtEnrollBatch entity);

	public void update(GjtEnrollBatch entity);
}
