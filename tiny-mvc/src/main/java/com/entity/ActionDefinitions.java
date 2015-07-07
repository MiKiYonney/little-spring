package com.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public class ActionDefinitions {
    private final List<ActionDefinition> actionDefinitionList = new ArrayList<ActionDefinition>();

    public ActionDefinitions() {
    }

    public void addActionDefinition(ActionDefinition actionDefinition){
        actionDefinitionList.add(actionDefinition);
    }

    public List<ActionDefinition> getActionDefinitionList() {
        return actionDefinitionList;
    }
}
