/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtCertificateRecord;
import com.gzedu.xlims.service.base.BaseService;

/**
 * 功能说明：证书发放记录
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月31日
 * @version 3.0
 *
 */
public interface GjtCertificateRecordService extends BaseService<GjtCertificateRecord> {

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年8月31日 下午3:00:15
	 * @param orgId
	 * @param searchParams
	 * @param pageRequst
	 * @return
	 */
	Page<GjtCertificateRecord> queryAll(String orgId, Map<String, Object> searchParams, PageRequest pageRequst);

	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月1日 下午1:53:33
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	List<GjtCertificateRecord> queryAll(String orgId, Map<String, Object> searchParams);


	/**
	 * @author ouguohao@eenet.com
	 * @Date 2017年9月1日 下午4:46:03
	 * @param orgId
	 * @param searchParams
	 * @return
	 */
	long countAll(String orgId, Map<String, Object> searchParams);

	/**
	 * 根据条件查询数量
	 * @param searchParams
	 * @return
	 */
	long count(Map<String, Object> searchParams);
}
