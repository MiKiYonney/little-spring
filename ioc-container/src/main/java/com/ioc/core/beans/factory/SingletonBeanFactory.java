package com.ioc.core.beans.factory;


import com.ioc.core.beans.entity.BeanDefinition;
import com.ioc.core.beans.entity.BeanRef;
import com.ioc.core.beans.entity.PropertyValue;
import com.ioc.core.beans.entity.PropertyValues;
import com.ioc.core.exceptions.BeanException;
import com.ioc.core.utils.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public class SingletonBeanFactory extends AbstractBeanFactory {

    @Override
    protected void parseProperties(Object bean, BeanDefinition definition) {
      /*  BeanDefinitionParser beanDefinitionParser = new XmlBeanDefinitionParser();
        beanDefinitionParser.parse(beanDefinitionMap);*/
        PropertyValues propertyValues = definition.getPropertyValues();
        Object fieldValue = null;
        for (PropertyValue property : propertyValues.getPropertyValues()) {
            String name = property.getName();
            Method method = ReflectUtils.getMethodForPro(definition.getBeanClazz(), name, "set");

            Object value = property.getValue();

            if (value != null && value instanceof BeanRef) {
                BeanRef beanReference = (BeanRef) value;
                fieldValue = getBean(beanReference.getName());
            }
            if (value != null && !(value instanceof BeanRef)) {
                fieldValue = ReflectUtils.getValueForProperty(method, value);
            }

            try {
                method.invoke(bean, fieldValue);
            } catch (IllegalAccessException e) {
                throw new BeanException("illegal access:" + definition.getClazz());
            } catch (InvocationTargetException e) {
                throw new BeanException("invoke target error:" + definition.getClazz());
            }
        }
    }
}
