package com.ioc.core.beans.context;

import com.ioc.core.beans.factory.AbstractBeanFactory;
import com.ioc.core.beans.factory.BeanFactory;

/**
 * Created by yonney.yang on 2015/6/27.
 */
public abstract class AbstractApplicationContext implements BeanFactory {
    private AbstractBeanFactory beanFactory;

    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void doLoadAndParse() {
        loadDefinition(beanFactory);
        preInitialSingletons();
    }

    private void preInitialSingletons() {
        beanFactory.preInitialSingletons();
    }

    protected abstract void loadDefinition(AbstractBeanFactory beanFactory);


    @Override
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }
}
