package com.ioc.core.beans;

import com.ioc.core.beans.entity.BeanDefinition;

import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/26.
 */
public interface BeanDefinitionParser {
    Map<String, Object> parse(Map<String, BeanDefinition> definitionMap);
}
