package com.gzedu.xlims.service.exam;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface GjtExamStudentArrangesService {

	public Map<String, Object> autoArrange(String examBatchCode, String examPointId);
	
	public HSSFWorkbook exportArrange(List<Map<String, Object>> list);
}
