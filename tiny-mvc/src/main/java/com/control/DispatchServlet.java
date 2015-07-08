package com.control;


import com.AbstractReader;
import com.entity.ActionDefinition;
import com.entity.ActionDefinitions;
import com.entity.ActionMapping;
import com.reader.XmlDefinitionReader;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by yonney.yang on 2015/6/30.
 */
@Log4j
public class DispatchServlet extends HttpServlet {
    private static String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";
    private Map<String, ActionMapping> context;

    @Override
    public void init() throws ServletException {
        //log.info("initializing servlet");
        String contextConfigLocation = this.getInitParameter(CONTEXT_CONFIG_LOCATION);

        AbstractReader definitionReader = new XmlDefinitionReader();
        definitionReader.loadDefinitions(this.getServletContext().getRealPath(contextConfigLocation));

        context = definitionReader.getDefinitionRegister();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doDispatch(req, resp);
    }


    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        //todo 请求路径解析优化
        String path = req.getRequestURI();
        try {
            URL url = new URL(path);
        } catch (MalformedURLException e) {

        }
        String[] paths = path.split("/");
        if(paths.length < 2) return;
        String nameSpace = paths[paths.length - 2];
        String pathPrefix = path.substring(path.lastIndexOf("/"));

        ActionMapping actionMapping = context.get(nameSpace);
        ActionDefinitions actionDefinitions = actionMapping.getActionDefinitions();

        ActionDefinition actionDefinition = null;
        for (ActionDefinition actionDef : actionDefinitions.getActionDefinitionList()) {
            if (pathPrefix.equals(actionDef.getRequestMapping())) {
                actionDefinition = actionDef;
                break;
            }
        }
        //Handler
        handlerRequest(req, resp, actionDefinition);

    }

    private void handlerRequest(HttpServletRequest req, HttpServletResponse resp, ActionDefinition actionDefinition) {
        String clazzName = actionDefinition.getClazzName();
        String methodName = actionDefinition.getMethodName();
        PrintWriter writer = null;
        try {
            Class clazz = Class.forName(clazzName);
            Object instance = clazz.newInstance();
            Method m = clazz.getMethod(methodName);
            Object result = m.invoke(instance);
            writer = resp.getWriter();
            writer.write((String) result);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class not found");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("NoSuchMethodException");
        } catch (InstantiationException e) {
            throw new RuntimeException("InstantiationException");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("IllegalAccessException");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("InvocationTargetException");
        } catch (IOException e) {
            throw new RuntimeException("IOException");
        }finally {
            if(writer != null){
                writer.close();
            }
        }
    }
}
