package com.gzedu.xlims.web.common;

/**
 * 导入反馈信息
 * @author eenet09
 *
 */
public class ImportFeedback {

	private boolean successful;

	private String message;
	
	private int totalCount;
	
	private int successCount;
	
	private int failedCount;
	
	private String successFileName;
	
	private String failedFileName;
	
	private Object result;

	public ImportFeedback(){}
	
	public ImportFeedback(boolean successful, String message) {
		this.successful = successful;
		this.message = message;
	}

	public ImportFeedback(boolean successful, int totalCount, int successCount, int failedCount,
			String successFileName, String failedFileName) {
		this.successful = successful;
		this.totalCount = totalCount;
		this.successCount = successCount;
		this.failedCount = failedCount;
		this.successFileName = successFileName;
		this.failedFileName = failedFileName;
	}

	public ImportFeedback(boolean successful, String message, int totalCount, int successCount, int failedCount,
			String successFileName, String failedFileName) {
		this.successful = successful;
		this.message = message;
		this.totalCount = totalCount;
		this.successCount = successCount;
		this.failedCount = failedCount;
		this.successFileName = successFileName;
		this.failedFileName = failedFileName;
	}

	public boolean isSuccessful() {
		return successful;
	}

	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public int getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(int failedCount) {
		this.failedCount = failedCount;
	}

	public String getSuccessFileName() {
		return successFileName;
	}

	public void setSuccessFileName(String successFileName) {
		this.successFileName = successFileName;
	}

	public String getFailedFileName() {
		return failedFileName;
	}

	public void setFailedFileName(String failedFileName) {
		this.failedFileName = failedFileName;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
