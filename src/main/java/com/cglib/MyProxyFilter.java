package com.cglib;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @Author by songjiuhua
 * createdAt  16/1/1
 * 代理过滤器,过滤哪些方法可以试用，哪些不可以
 */
public class MyProxyFilter implements CallbackFilter {
    private String methodName;

    public MyProxyFilter (String methodName){
        this.methodName = methodName;
    }

    //methodName方法谁都有权限调用
    @Override
    public int accept(Method method) {
        if(!this.methodName.equalsIgnoreCase(method.getName()))
            return 0;
        return 1;
    }
}
