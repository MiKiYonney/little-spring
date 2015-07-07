package com;

import com.entity.ActionMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yonney.yang on 2015/7/2.
 */
public abstract class AbstractReader implements DefinitionReader {
    private Map<String,ActionMapping> definitionRegister;

    public AbstractReader() {
        this.definitionRegister = new HashMap<String, ActionMapping>();
    }

    public Map<String, ActionMapping> getDefinitionRegister() {
        return definitionRegister;
    }

}
