package com.gzedu.xlims.constants;

/**
 * @Function: Mybatis Sql脚本的ID名称
 * @ClassName: SqlId 
 * @date: 2016年4月17日 下午9:46:00 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
public interface SqlId {
	public String SQL_SELECT_COUNT = "selectCount";
	public String SQL_SELECT = "select";
	public String SQL_SELECT_BY_ID = "selectById";
	public String SQL_UPDATE_BY_ID = "updateById";
	public String SQL_UPDATE_BY_ID_SELECTIVE = "updateByIdSelective";
	public String SQL_DELETE = "delete";
	public String SQL_DELETE_BY_ID = "deleteById";
	public String SQL_INSERT = "insert";
}
