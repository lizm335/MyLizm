package com.gzedu.xlims.common.exception;

/**
 * @Function: Dao层异常信息
 * @ClassName: DaoException 
 * @date: 2016年4月17日 下午10:01:23 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
public class DaoException extends RuntimeException {

	/**
	 * @fields serialVersionUID 
	 */
	private static final long serialVersionUID = 8350049272861703406L;

	public DaoException() {
		super();
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
