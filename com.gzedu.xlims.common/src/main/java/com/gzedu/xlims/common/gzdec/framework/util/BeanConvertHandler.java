package com.gzedu.xlims.common.gzdec.framework.util;

/**
 * 转换Bean处理器
 *
 * @author lzj
 * 
 */
public interface BeanConvertHandler<T, K>
{

    /**
     * bean转换操作（对需要特别处理的属性进行处理）
     * 
     * @param soruce
     *            源bean
     * @param target
     *            目标bean
     *
     */
    void handle(T soruce, K target);
}
