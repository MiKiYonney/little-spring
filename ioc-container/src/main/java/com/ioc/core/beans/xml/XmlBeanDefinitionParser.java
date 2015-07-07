package com.ioc.core.beans.xml;

import com.ioc.core.beans.BeanDefinitionParser;
import com.ioc.core.beans.entity.BeanDefinition;
import com.ioc.core.beans.entity.BeanRef;
import com.ioc.core.beans.entity.PropertyValue;
import com.ioc.core.beans.entity.PropertyValues;
import com.ioc.core.exceptions.BeanException;
import com.ioc.core.utils.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/26.
 */
public class XmlBeanDefinitionParser implements BeanDefinitionParser {
    private final Map<String, Object> beanContainer = new HashMap<String, Object>();

    @Override
    public Map<String, Object> parse(Map<String, BeanDefinition> definitionMap) {
        for (BeanDefinition definition : definitionMap.values()) {
            //非lazy-init
            beanContainer.put(definition.getId(), initBeans(definition, definitionMap));
        }
        return beanContainer;
    }

    private Object initBeans(BeanDefinition definition, Map<String, BeanDefinition> definitionMap) {
        Class clazz;
        Object clazzInstance;
        try {
            clazz = Class.forName(definition.getClazz());
            clazzInstance = clazz.newInstance();
            parseForProperty(definition, definitionMap, clazz, clazzInstance);
        } catch (ClassNotFoundException e) {
            throw new BeanException("can't find class:" + definition.getClazz());
        } catch (InstantiationException e) {
            throw new BeanException("reflect error:" + definition.getClazz());
        } catch (IllegalAccessException e) {
            throw new BeanException("illegal access:" + definition.getClazz());
        } catch (InvocationTargetException e) {
            throw new BeanException("invoke target error:" + definition.getClazz());
        }
        return clazzInstance;
    }

    private void parseForProperty(BeanDefinition definition, Map<String, BeanDefinition> definitionMap, Class clazz, Object clazzInstance) throws IllegalAccessException, InvocationTargetException {
        PropertyValues propertyValues = definition.getPropertyValues();
        List<PropertyValue> propertyValueList = propertyValues.getPropertyValues();
        Object fieldValue = null;
        for (PropertyValue property : propertyValueList) {
            String name = property.getName();
            Method method = ReflectUtils.getMethodForPro(clazz, name, "set");

            Object value = property.getValue();
            if (value != null && !(value instanceof BeanRef)) {
                fieldValue = ReflectUtils.getValueForProperty(method, value);
            }

            if (value != null && value instanceof BeanRef) {
                if (beanContainer.containsKey(name)) {
                    fieldValue = beanContainer.get(name);
                } else {
                    BeanDefinition refDefinition = definitionMap.get(name);
                    if (refDefinition != null) {
                        fieldValue = initBeans(refDefinition, definitionMap);
                    } else {
                        throw new BeanException("Can't Find Bean:" + name);
                    }
                }
            }
            method.invoke(clazzInstance, fieldValue);
        }
    }
}
