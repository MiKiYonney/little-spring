package com.reader;

import com.AbstractReader;
import com.entity.ActionMapping;
import com.entity.ActionDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonney.yang on 2015/7/2.
 */
public class XmlDefinitionReader extends AbstractReader {
    private final static String MVC_CONFIG_ElEMENT = "mvc-config";

    private final static String ACTION_MAPPING_ELEMENT = "action-mapping";

    private final static String NAMESPACE_ATTRIBUTE = "namespace";

    private final static String ACTION_ELEMENT = "action";

    private final static String REQUEST_MAPPING_ATTRIBUTE = "requestMapping";

    private final static String CLASS_ATTRIBUTE = "class";

    private final static String METHOD_ATTRIBUTE = "method";

    private final static String RESULT_ELEMENT ="result";

    private final static String TYPE_ATTRIBUTE ="type";


    @Override
    public void loadDefinitions(String location) {
        List<ActionMapping> actionMapDefinition = new ArrayList<ActionMapping>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db;
        try {
            db = factory.newDocumentBuilder();

            Document dom = db.parse(this.getClass().getResourceAsStream("/" + location));
            dom.normalize();
            Element root = dom.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                ActionMapping actionMapping = null;
                if (node instanceof Element) {
                    Element curElement = (Element) node;
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        actionMapping = new ActionMapping();
                        doLoadDefinition(curElement, actionMapping);
                    }
                }
                if (actionMapping != null) {
                    actionMapDefinition.add(actionMapping);
                }
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Parse Configuration Exception");
        } catch (SAXException e) {
            throw new RuntimeException("parse source error");
        } catch (IOException e) {
            throw new RuntimeException("could not resolve bean definition resource");
        }
    }

    private void doLoadDefinition(Element curElement, ActionMapping actionMapping) {
        if (ACTION_MAPPING_ELEMENT.equals(curElement.getNodeName())) {
            String nameSpace = curElement.getAttribute(NAMESPACE_ATTRIBUTE);
            actionMapping.setNameSpace(nameSpace);
            parseActionTag(curElement, actionMapping);
            getDefinitionRegister().put(nameSpace,actionMapping);
        }
    }

    private void parseActionTag(Element curElement, ActionMapping actionMapping) {
        if (curElement.getNodeName().equals(ACTION_ELEMENT)) {

            String requestMapping = curElement.getAttribute(REQUEST_MAPPING_ATTRIBUTE);
            String clazz = curElement.getAttribute(CLASS_ATTRIBUTE);
            String method = curElement.getAttribute(METHOD_ATTRIBUTE);

            ActionDefinition actionDefinition = new ActionDefinition(requestMapping, clazz, method);

            if(curElement.hasAttribute(RESULT_ELEMENT)){
                parseResultTag(curElement,actionDefinition);
            }
            actionMapping.getPropertyValues().addActionDefinition(actionDefinition);
        }
    }

    private void parseResultTag(Element curElement, ActionDefinition actionDefinition) {

    }
}
