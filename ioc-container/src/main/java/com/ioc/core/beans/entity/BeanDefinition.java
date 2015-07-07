package com.ioc.core.beans.entity;

import com.ioc.core.exceptions.BeanException;

/**
 * Created by yonney.yang on 2015/6/26.
 */

public class BeanDefinition {
    private String id;
    private String clazz;
    private Class beanClazz;
    private Object bean;
    private PropertyValues propertyValues = new PropertyValues();

    private boolean isLazyInit = false;

    public boolean isLazyInit() {
        return isLazyInit;
    }

    public void setLazyInit(boolean isLazyInit) {
        this.isLazyInit = isLazyInit;
    }

    public BeanDefinition() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
        try {
            this.beanClazz = Class.forName(clazz);
        } catch (ClassNotFoundException e) {
            throw new BeanException("can't find class:" + clazz);
        }
    }


    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Class getBeanClazz() {
        return beanClazz;
    }

    public void setBeanClazz(Class beanClazz) {
        this.beanClazz = beanClazz;
    }
}
