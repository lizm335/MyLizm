package com.gzedu.xlims.service.graduation;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.gzedu.xlims.pojo.graduation.GjtGraduationAdviserMsg;

/**
 * 毕业指导老师接收信息权限
 * @author eenet09
 *
 */
public interface GjtGraduationAdviserMsgService {
	
	public Page<GjtGraduationAdviserMsg> queryPage(Map<String, Object> searchParams, PageRequest pageRequst);
	
	public List<GjtGraduationAdviserMsg> queryAll(Map<String, Object> searchParams);
	
	public void insert(GjtGraduationAdviserMsg entity);
	
	public GjtGraduationAdviserMsg queryById(String id);
	
	public void delete(String id);

}
