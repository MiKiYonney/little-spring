package com.ioc.core.utils;

import com.ioc.core.exceptions.BeanException;
import lombok.SneakyThrows;

import java.lang.reflect.Method;

/**
 * Created by yonney.yang on 2015/6/26.
 */
public class ReflectUtils {
    @SneakyThrows
    public static Object getInstanceForName(String name, Object value) {
        Class clazz = nameToClass(name);

        return clazz.getConstructor(String.class).newInstance(value);

    }

    private static Class nameToClass(String name) {
        Class clazz;
        if (name.equals("int")) {
            clazz = Integer.class;
        } else if (name.equals("char")) {
            clazz = Character.class;
        } else if (name.equals("boolean")) {
            clazz = Boolean.class;
        } else if (name.equals("short")) {
            clazz = Short.class;
        } else if (name.equals("long")) {
            clazz = Long.class;
        } else if (name.equals("float")) {
            clazz = Float.class;
        } else if (name.equals("double")) {
            clazz = Double.class;
        } else if (name.equals("byte")) {
            clazz = Byte.class;
        } else {
            try {
                clazz = Class.forName(name);
            } catch (ClassNotFoundException e) {
                throw new BeanException("class not found for name" + name);
            }
        }
        return clazz;
    }

    public static Method getMethodForPro(Class clazz, String name, String setOrGet) {
        Method[] methods = clazz.getMethods();
        Method method = null;
        for (int k = 0; k < methods.length; k++) {
            if (methods[k].getName().equalsIgnoreCase(setOrGet + name)) {
                method = methods[k];
            }
        }
        if (method == null) {
            throw new BeanException("Can't Find Set Method For:" + name);
        }
        return method;
    }

    public static Object getValueForProperty(Method method, Object value) {
        Class<?>[] types = method.getParameterTypes();
        return getInstanceForName(types[0].getName(), value);
    }
}
