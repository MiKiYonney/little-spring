package com.ioc.core.beans.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>();

    public PropertyValues() {
    }

    public void addPropertyValue(PropertyValue pv) {
        //TODO:可对重复propertyName进行判断
        this.propertyValueList.add(pv);
    }

    public List<PropertyValue> getPropertyValues() {
        return this.propertyValueList;
    }

}
