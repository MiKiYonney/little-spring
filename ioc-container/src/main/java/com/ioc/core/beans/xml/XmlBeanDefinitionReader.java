package com.ioc.core.beans.xml;

import com.ioc.core.beans.AbstractBeanDefinitionReader;
import com.ioc.core.beans.entity.BeanDefinition;
import com.ioc.core.beans.entity.BeanRef;
import com.ioc.core.beans.entity.PropertyValue;
import com.ioc.core.exceptions.BeanException;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

/**
 * Created by yonney.yang on 2015/6/26.
 */
@Log4j
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
    public static final String BEAN_ELEMENT = "bean";

    public static final String ID_ATTRIBUTE = "id";

    public static final String CLASS_ATTRIBUTE = "class";

    public static final String PROPERTY_ELEMENT = "property";

    public static final String REF_ATTRIBUTE = "ref";

    public static final String NAME_ATTRIBUTE = "name";

    public static final String VALUE_ATTRIBUTE = "value";

    public static final String LAZY_INIT_ATTRIBUTE = "lazy-init";

    public static final String BOOLEAN_TRUE = "true";
    @Override
    public void loadBeanDefinitions(String... locations) {
        List<BeanDefinition> beanDefinitions = Lists.newArrayList();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db;
        try {
            db = factory.newDocumentBuilder();
            for (String location : locations) {
                Document dom = db.parse(this.getClass().getResourceAsStream("/" + location));
                dom.normalize();
                Element root = dom.getDocumentElement();
                NodeList nodeList = root.getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    BeanDefinition beanDefinition = null;
                    if (node instanceof Element) {
                        Element beanElement = (Element) node;
                        //用户自定义标签+spring规范的标签
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            beanDefinition = new BeanDefinition();
                            parseDefaultElement(beanElement, beanDefinition);
                        }
                    }
                    if (beanDefinition != null) {
                        beanDefinitions.add(beanDefinition);
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            throw new BeanException("Parse Configuration Exception");
        } catch (SAXException e) {
            throw new BeanException("parse source error");
        } catch (IOException e) {
            throw new BeanException("could not resolve bean definition resource");
        }
    }

    private void parseDefaultElement(Element element, BeanDefinition beanDefinition) {
        if (BEAN_ELEMENT.equals(element.getNodeName())) {
            processBeanDefinition(element, beanDefinition);
        }
    }

    private void processBeanDefinition(Element element, BeanDefinition beanDefinition) {
        String id = element.getAttribute(ID_ATTRIBUTE);
        beanDefinition.setId(id);
        beanDefinition.setClazz(element.getAttribute(CLASS_ATTRIBUTE));
        if(element.hasAttribute(LAZY_INIT_ATTRIBUTE)){
            if(BOOLEAN_TRUE.equals(element.getAttribute(LAZY_INIT_ATTRIBUTE))){
                beanDefinition.setLazyInit(true);
            }
        }
        processChild(element, beanDefinition);
        getDefinitionRegister().put(id, beanDefinition);
    }

    private void processChild(Element element, BeanDefinition beanDefinition) {
        for (Node curNode = element.getFirstChild(); curNode != null; curNode = curNode.getNextSibling()) {
            processPropertyTag(beanDefinition, curNode);
            //todo 加入解析<constructor-arg>标签
        }
    }

    //解析<Property>标签
    private void processPropertyTag(BeanDefinition beanDefinition, Node curNode) {
        if (curNode.getNodeName().equals(PROPERTY_ELEMENT)) {
            if (curNode instanceof Element) {
                Element propertyEl = (Element) curNode;

                String name = propertyEl.getAttribute(NAME_ATTRIBUTE);
                String value = propertyEl.getAttribute(VALUE_ATTRIBUTE);
                if (value != null && value.length() > 0) {
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
                } else {
                    String ref = propertyEl.getAttribute(REF_ATTRIBUTE);
                    if (ref == null || ref.length() == 0) {
                        throw new IllegalArgumentException("Configuration problem: <property> element for property '"
                                + name + "' must specify a ref or value");
                    }
                    BeanRef beanReference = new BeanRef(ref);
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
                }
            }
        }
    }
}
