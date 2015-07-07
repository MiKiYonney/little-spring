package com.ioc.core.beans.factory;

/**
 * Created by yonney.yang on 2015/6/26.
 */
public interface BeanFactory {
    /**
     * 根据beanId获取bean的对象
     *
     * @param beanId
     * @return
     */
    public Object getBean(String beanId);
}
