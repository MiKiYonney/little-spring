package com.ioc.core.beans;

import com.ioc.core.beans.entity.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    private Map<String, BeanDefinition> definitionRegister;


    protected AbstractBeanDefinitionReader() {
        this.definitionRegister = new HashMap<String, BeanDefinition>();
    }

    public Map<String, BeanDefinition> getDefinitionRegister() {
        return definitionRegister;
    }
}
