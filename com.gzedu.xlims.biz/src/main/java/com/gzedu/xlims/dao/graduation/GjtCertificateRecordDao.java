/**
 * Copyright(c) 2013 版权所有：广州远程教育中心 www.969300.com
 */
package com.gzedu.xlims.dao.graduation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gzedu.xlims.dao.base.BaseDao;
import com.gzedu.xlims.pojo.graduation.GjtCertificateRecord;

/**
 * 功能说明：证书发放记录
 * 
 * @author ouguohao@eenet.com
 * @Date 2017年8月31日
 * @version 3.0
 *
 */
public interface GjtCertificateRecordDao
		extends BaseDao<GjtCertificateRecord, String>, JpaRepository<GjtCertificateRecord, String>, JpaSpecificationExecutor<GjtCertificateRecord> {

	GjtCertificateRecord findByStudentIdAndCertificateType(String studentId, int certificateType );
}
