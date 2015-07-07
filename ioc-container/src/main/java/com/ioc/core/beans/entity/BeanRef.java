package com.ioc.core.beans.entity;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public class BeanRef {
    private String name;

    private Object bean;

    public BeanRef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
