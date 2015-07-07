package com.ioc.core.beans.context;

import com.ioc.core.beans.AbstractBeanDefinitionReader;
import com.ioc.core.beans.entity.BeanDefinition;
import com.ioc.core.beans.factory.AbstractBeanFactory;
import com.ioc.core.beans.factory.SingletonBeanFactory;
import com.ioc.core.beans.xml.XmlBeanDefinitionReader;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/27.
 */
@Log4j
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    @Setter
    private String[] locations;

    public ClassPathXmlApplicationContext(String... locations) {
        this(new SingletonBeanFactory(), locations);
    }

    public ClassPathXmlApplicationContext(AbstractBeanFactory beanFactory, String... locations) {
        super(beanFactory);
        this.locations = locations;
        doLoadAndParse();
    }

    @Override
    protected void loadDefinition(AbstractBeanFactory beanFactory) {
        AbstractBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader();
        beanDefinitionReader.loadBeanDefinitions(this.locations);
        //将Definition注册到工厂中去
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionReader.getDefinitionRegister().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }

}
