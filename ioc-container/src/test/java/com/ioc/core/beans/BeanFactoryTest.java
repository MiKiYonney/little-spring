package com.ioc.core.beans;

import com.ioc.core.beans.context.ClassPathXmlApplicationContext;
import com.ioc.core.beans.factory.BeanFactory;
import com.ioc.core.beans.xml.XmlContext;
import com.ioc.logic.entity.Course;
import com.ioc.logic.entity.Student;
import org.junit.Test;

/**
 * Created by yonney.yang on 2015/6/26.
 */
public class BeanFactoryTest {
    private String config = "config.xml";

    @Test
    public void testBeanFactory(){
        BeanFactory beanFactory = new XmlContext(config);
        Student user = (Student) beanFactory.getBean("user");
        assert "juan.yang".equals(user.getUserName());
        assert user.getCourse() != null;
        assert user.getCourse().getMathScore() == 150;

        Course course = (Course) beanFactory.getBean("course");
        assert course != null;
    }


    @Test
    public void testXmlApplicationContext(){
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("beans-study.xml","beans-course.xml");
        Student user = (Student) beanFactory.getBean("user");
        assert "juan.yang".equals(user.getUserName());
        assert user.getCourse() != null;
        assert user.getCourse().getMathScore() == 150;

        Course course = (Course) beanFactory.getBean("course");
        assert course != null;
    }
}
