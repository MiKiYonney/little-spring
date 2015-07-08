package com.entity;

/**
 * Created by yonney.yang on 2015/7/2.
 */
public class ActionMapping {
    private String nameSpace;
    private ActionDefinitions actionDefinitions = new ActionDefinitions();

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public ActionDefinitions getActionDefinitions() {
        return actionDefinitions;
    }

    public void setActionDefinitions(ActionDefinitions actionDefinitions) {
        this.actionDefinitions = actionDefinitions;
    }
}
