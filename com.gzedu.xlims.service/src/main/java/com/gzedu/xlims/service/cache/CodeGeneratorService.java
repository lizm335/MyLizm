package com.gzedu.xlims.service.cache;

public interface CodeGeneratorService {

	/**
	 * 根据类型与当前日期生成唯一编码
	 * @param type
	 * @return 编码格式 年月日+当天自增序号, 例如: 201609200001，默认生成12位
	 */
	public String codeGenerator(String type);

	/**
	 * 根据类型与当前日期生成唯一编码
	 * @param type
	 * @param digit 生成多少位数
	 * @return 编码格式 年月日+当天自增序号, 例如: 201609200001
	 */
	public String codeGenerator(String type, int digit);
	
	public long incrRoundIndex(String examBatchCode);
}
