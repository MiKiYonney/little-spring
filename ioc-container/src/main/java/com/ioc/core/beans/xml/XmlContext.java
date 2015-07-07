package com.ioc.core.beans.xml;

import com.ioc.core.beans.factory.BeanFactory;
import com.ioc.core.exceptions.BeanException;
import com.ioc.core.utils.ReflectUtils;
import com.google.common.collect.Maps;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/25.
 */
@Log4j
public class XmlContext implements BeanFactory {
    private final Map<String, Object> beans = Maps.newHashMap();
    private Document document;
    private Element root;

    @Setter
    private String configName;

    @Setter
    private URL url;

    public XmlContext(String configName) {
        this.configName = configName;
        this.url = this.getClass().getResource("/" + configName);
        init();
    }

    public XmlContext(URL url) {
        this.url = url;
        init();
    }

    @SneakyThrows
    public void init() {
        SAXReader reader = new SAXReader();
        document = reader.read(url);
        root = document.getRootElement();
        for (Iterator i = root.elementIterator("bean"); i.hasNext(); ) {
            Element beanElement = (Element) i.next();
            beans.put(beanElement.attributeValue("id"), initBean(beanElement));
        }
    }

    @SneakyThrows
    private Object initBean(Element beanElement) {
        if (beanElement == null) {
            throw new BeanException("bean element is null,can't initialize");
        }
        //1.最简单的配置 <bean id="iocTest" class="com.dianping.test.IocTest">
        Class clazz = Class.forName(beanElement.attributeValue("class"));
        Object clazzInstance = clazz.newInstance();

        for (Iterator j = beanElement.elementIterator("property"); j.hasNext(); ) {
            Element propertyElement = (Element) j.next();
            String name = propertyElement.attributeValue("name");

            Method[] methods = clazz.getMethods();
            Method method = null;
            for (int k = 0; k < methods.length; k++) {
                if (methods[k].getName().equalsIgnoreCase("set" + name)) {
                    method = methods[k];
                }
            }
            if (method == null) {
                throw new BeanException("Can't Find Set Method For:" + name);
            }
            //2.给Bean属性设置值
            /*Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);*/

            Object fieldValue = null;
            //value为property的子元素
            for (Iterator v = propertyElement.elementIterator("value"); v.hasNext(); ) {
                Element valueElement = (Element) v.next();

                Class<?>[] types = method.getParameterTypes();
                fieldValue = ReflectUtils.getInstanceForName(types[0].getName(), valueElement.getText());
                break;
            }
            //value为属性
            String value = propertyElement.attributeValue("value");
            if (value != null) {
                Class<?>[] types = method.getParameterTypes();
                fieldValue = ReflectUtils.getInstanceForName(types[0].getName(), value);
            }

            //3.给Bean注入对象
            String ref = propertyElement.attributeValue("ref");
            if (ref != null) {
                if (beans.containsKey(ref)) {
                    fieldValue = beans.get(ref);
                } else {
                    List<Element> refElementList = document.selectNodes("//bean[@id='" + ref + "']");
                    if (CollectionUtils.isNotEmpty(refElementList)) {
                        fieldValue = initBean(refElementList.get(0));
                    } else {
                        throw new BeanException("Can't Find Bean:" + ref);
                    }
                }
            }
            // field.set(clazzInstance, fieldValue);
            method.invoke(clazzInstance, fieldValue);
        }
        return clazzInstance;
    }

    @Override
    public Object getBean(String beanId) {
        return beans.get(beanId);
    }

    public static void main(String[] args) {
        SAXReader reader = new SAXReader();
        try {
            Document document1 = reader.read(XmlContext.class.getResource("/" + "config.xml"));
            Element root = document1.getRootElement();
            List list = document1.selectNodes("//bean[@id='ref']");
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }
}
