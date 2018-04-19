package com.gzedu.xlims.common.gzdec.framework.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;


/**
 * Bean转换工具类
 *
 * @author lzj
 * 
 */
public final class BeanConvertUtils
{

    private BeanConvertUtils() {
        super();
    }


    /**
     * 转换bean
     * 
     * @param source
     *            源对象
     * @param targetClass
     *            目标对象类
     * @param handler
     *            对象转换处理器
     * @return 转换后的bean
     *
     */
    public static <K, T> K convert(T source, Class<K> targetClass, BeanConvertHandler<T, K> handler) {
        K target = null;

        if (source != null) {
            try {
                // 初始化bean
                target = targetClass.newInstance();

                // 简单的直接拷贝
                BeanUtils.copyProperties(source, target);

                // 如果存在自定义处理器则这些
                if (handler != null) {
                    handler.handle(source, target);
                }
            } catch (BeansException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        return target;
    }


    /**
     * 转换bean
     * 
     * @param source
     *            源对象
     * @param targetClass
     *            目标对象类
     * @return 转换后的bean
     *
     */
    public static <K, T> K convert(T source, Class<K> targetClass) {
        return convert(source, targetClass, null);
    }


    /**
     * 转换bean列表
     * 
     * @param sources
     *            源对象列表
     * @param targetClass
     *            目标对象类
     * @param handler
     *            转换处理对象
     * @return 转换后的targetClass 对应 bean列表
     * 
     */
    public static <K, T> List<K> convertList(List<T> sources, Class<K> targetClass, BeanConvertHandler<T, K> handler) {
        List<K> targets = null;

        if (sources != null) {
            targets = new ArrayList<K>(sources.size());

            // 循环转换
            K target = null;
            for (T source : sources) {
                target = convert(source, targetClass, handler);
                if (target != null) {
                    targets.add(target);
                }
            }
        }


        return targets;
    }


    /**
     * 转换bean列表
     * 
     * @param sources
     *            源对象列表
     * @param targetClass
     *            目标对象类
     * @return 转换后的targetClass 对应 bean列表
     * 
     */
    public static <K, T> List<K> convertList(List<T> sources, Class<K> targetClass) {
        return convertList(sources, targetClass, null);
    }

}

