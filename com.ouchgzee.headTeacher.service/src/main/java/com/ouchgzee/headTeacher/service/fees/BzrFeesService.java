package com.ouchgzee.headTeacher.service.fees;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 缴费管理信息
 *
 */
@Deprecated public interface BzrFeesService {
	
	public Page getFeeslist(Map searchParams, PageRequest pageRequst);
}
