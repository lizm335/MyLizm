package com.gzedu.xlims.service.model;

import java.util.List;

import com.gzedu.xlims.pojo.PriModelInfo;
import com.gzedu.xlims.service.base.BaseService;

public interface PriModelInfoService extends BaseService<PriModelInfo> {
	
	List<PriModelInfo> findAll();

}
