package com.ouchgzee.headTeacher.service;

@Deprecated public interface BzrCodeGeneratorService {

	/**
	 * 根据类型与当前日期生成唯一编码
	 * @param type	
	 * @return 编码格式 年月日+当天自增序号, 例如: 201609200001
	 */
	public String codeGenerator(String type);
	
	public long incrRoundIndex(String examBatchCode);
}
