package com.gzedu.xlims.common.exception;


/**
 * @Function: 控制层异常信息
 * @ClassName: ControllerException 
 * @date: 2016年4月17日 下午10:02:17 
 *
 * @author  zhy
 * @version V2.5
 * @since   JDK 1.6
 */
public class ControllerException extends RuntimeException {

	/**
	 * @fields serialVersionUID 
	 */
	private static final long serialVersionUID = 4081863331832266720L;

	public ControllerException() {
		super();
	}

	public ControllerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ControllerException(String message) {
		super(message);
	}

	public ControllerException(Throwable cause) {
		super(cause);
	}

}
