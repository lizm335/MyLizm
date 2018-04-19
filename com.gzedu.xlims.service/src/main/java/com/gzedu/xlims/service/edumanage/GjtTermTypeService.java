package com.gzedu.xlims.service.edumanage;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.GjtTermType;

/**
 * 
 * 功能说明：学期管理
 * 
 * @author 李明 liming@eenet.com
 * @Date 2016年6月17日
 * @version 2.5
 *
 */
public interface GjtTermTypeService {
	public Page<GjtTermType> queryAll(final String schoolId, Map<String, Object> searchParams, PageRequest pageRequst);

	public GjtTermType queryBy(String id);

	public void delete(Iterable<String> ids);

	public void delete(String id);

	public void insert(GjtTermType entity);

	public void update(GjtTermType entity);
}
