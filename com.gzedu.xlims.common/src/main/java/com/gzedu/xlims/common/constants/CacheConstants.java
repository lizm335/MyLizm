package com.gzedu.xlims.common.constants;

/**
 * 缓存常量
 */
public class CacheConstants {

	/**
	 * 考试补考费缴费单号 type:hash 有效期: 30天不更新则删除
	 *
	 */
	public final static String EXAM_PAY_ORDER_NO = "exam_pay_order_no";
	/**
	 * 考点编码 GJT_EXAM_ADDRESS_NEW.EXAM_ADDRESS_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String EXAM_ADDRESS_CODE = "exam_ac";

	/**
	 * 考试批次编码 GJT_EXAM_BATCH_NEW.EXAM_BATCH_CODE type:hash 有效期: 30天不更新则删除
	 * 
	 */
	public final static String EXAM_BATCH_CODE = "exam_bc";

	/**
	 * 考试科目编码 GJT_EXAM_SUBJECT_NEW.SUBJECT_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String SUBJECT_CODE = "exam_sc";

	/**
	 * 毕业设计编码 GJT_GRADUATION_BATCH.BATCH_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String GRADUATION_BATCH_CODE = "graduation_bc";
	/**
	 * 毕业设计编码 GJT_GRADUATION_BATCH.BATCH_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String EXAM_POINT_CODE = "exam_kd";

	/**
	 * 库存操作批次编码 GJT_TEXTBOOK_STOCK_OPERA_BATCH.BATCH_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String TEXTBOOK_STOCK_OPERA_BATCH_CODE = "textbookStockOpera_bc";

	/**
	 * 教材发放表订单号 GJT_TEXTBOOK_DISTRIBUTE.ORDER_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String TEXTBOOK_DISTRIBUTE_ORDER_CODE = "textbookDistribute_oc";

	/**
	 * 论文计划编码 GJT_THESIS_PLAN.THESIS_PLAN_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String THESIS_PLAN_CODE = "thesisPlan_bc";

	/**
	 * 社会实践计划编码 GJT_PRACTICE_PLAN.PRACTICE_PLAN_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String PRACTICE_PLAN_CODE = "practicePlan_bc";
	/**
	 * 毕业计划编码 GJT_GRADUATION_PLAN.GRADUATION_PLAN_CODE type:hash 有效期: 30天不更新则删除
	 */
	public final static String GRADUATION_PLAN_CODE = "graduationPlan_bc";
	
	/**
	 * 防止不同类型订单号冲突，订单号前面加一个类型区分
	 * 功能说明：
	 * @author 黄一飞 huangyifei@eenet.com
	 * @Date 2018年4月10日
	 * @version 3.0
	 */
	public static enum OrderType {

		
		EXAM_PAY("1","补考费"),
		TEXTBOOK_DISTRIBUTE("2","教材费"),
		;
		
		private OrderType(String code, String name) {
			this.code = code;
			this.name = name;
		}

		private String code;
		
		private String name;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
}
