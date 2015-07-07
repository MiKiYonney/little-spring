package com.entity;

/**
 * Created by yonney.yang on 2015/7/2.
 */
public class ActionMapping {
    private String nameSpace;
    private ActionDefinitions propertyValues = new ActionDefinitions();

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public ActionDefinitions getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(ActionDefinitions propertyValues) {
        this.propertyValues = propertyValues;
    }
}
