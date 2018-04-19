package com.gzedu.xlims.service.cache;

public interface GjtExamArrangeCache {

	public Long arrangeLock(String examBatchCode, String examPointCode);
	
	public void clearArrangeLock(String examBatchCode, String examPointCode);
}
