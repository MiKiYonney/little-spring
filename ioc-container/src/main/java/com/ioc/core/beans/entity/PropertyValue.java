package com.ioc.core.beans.entity;

/**
 * Created by yonney.yang on 2015/6/27.
 */

public class PropertyValue {
    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
