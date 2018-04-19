package com.gzedu.xlims.service.exam;

import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.exam.GjtExamPointNew;

public interface GjtExamPointNewService {

	/**
	 * 
	 * @param searchParams中key的格式为OPERATOR_FIELDNAME
	 * @param pageRequst
	 *            分页对象
	 * @return
	 */
	public Page<GjtExamPointNew> queryAll(Map<String, Object> searchParams, PageRequest pageRequst);

	public boolean existsCode(String code);

	GjtExamPointNew findByExamBatchCodeAndCode(String examBatchCode, String code);

	public void save(GjtExamPointNew entity) throws ServerException;
	
	boolean batchAddPoint(String examBatchId, List<String> pointIds) throws ServerException;
	
	/**
	 * 通用学习中心插入-1
	 * @param examPointId
	 * @return
	 */
	boolean insertExamPointStudyCenter(String examPointId);

	public void delete(List<String> entity);

	public GjtExamPointNew queryById(String id);

}
