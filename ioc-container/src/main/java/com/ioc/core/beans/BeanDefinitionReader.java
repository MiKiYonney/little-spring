package com.ioc.core.beans;

/**
 * Created by yonney.yang on 2015/6/26.
 */
public interface BeanDefinitionReader {
    public void loadBeanDefinitions(String... locations);
}
