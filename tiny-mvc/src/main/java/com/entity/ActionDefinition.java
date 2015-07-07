package com.entity;

/**
 * Created by yonney.yang on 2015/6/27.
 */

public class ActionDefinition {
    private String requestMapping;

    private String clazzName;

    private String methodName;

    private ResultDefinition result;

    public ActionDefinition(String requestMapping, String clazzName, String methodName) {
        this.requestMapping = requestMapping;
        this.clazzName = clazzName;
        this.methodName = methodName;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public String getClazzName() {
        return clazzName;
    }

    public String getMethodName() {
        return methodName;
    }

    public ResultDefinition getResult() {
        return result;
    }

    public void setResult(ResultDefinition result) {
        this.result = result;
    }
}
