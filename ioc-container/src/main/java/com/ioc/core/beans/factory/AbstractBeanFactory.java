package com.ioc.core.beans.factory;

import com.ioc.core.beans.entity.BeanDefinition;
import com.ioc.core.exceptions.BeanException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public abstract class AbstractBeanFactory implements BeanFactory {
    protected Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();
    private final List<String> beanDefinitionNames = new ArrayList<String>();

    public void preInitialSingletons() {
        for (String beanName : beanDefinitionNames) {
            //如果lazyInit为true时，不预先加载
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (!beanDefinition.isLazyInit()) {
                getBean(beanName,beanDefinition);
            }
        }
    }

    private Object getBean(String name, BeanDefinition beanDefinition) {
        if (beanDefinition == null) {
            throw new IllegalArgumentException("No bean named " + name + " is defined");
        }
        Object bean = beanDefinition.getBean();
        if (bean == null) {
            bean = doCreateBean(beanDefinition);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        //todo 有参数时
        Object bean = createBeanInstance(beanDefinition);
        beanDefinition.setBean(bean);
        parseProperties(bean, beanDefinition);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        try {
            return beanDefinition.getBeanClazz().newInstance();
        } catch (InstantiationException e) {
            throw new BeanException("InstantiationException");
        } catch (IllegalAccessException e) {
            throw new BeanException("IllegalAccessException");
        }
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }

    protected abstract void parseProperties(Object bean, BeanDefinition beanDefinition);

    @Override
    public Object getBean(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        return getBean(name, beanDefinition);
    }
}
