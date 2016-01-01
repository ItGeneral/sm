package com.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;
import java.lang.reflect.Method;

/**
 * @Author by songjiuhua
 * createdAt  16/1/1
 * cglib代理类     方法拦截器代理
 */
public class MyCglibProxy implements MethodInterceptor {

    private Logger log = Logger.getLogger(MyCglibProxy.class);
    public Enhancer enhancer = new Enhancer();
    String name;

    public MyCglibProxy(String name) {
        this.name = name ;
    }
    /**
     * 根据class对象创建该对象的代理对象
     * 本质：动态创建了一个class对象的子类
     * @param cls
     * @return
     */
    public Object getDaoBean(Class cls){
        //设置父类
        enhancer.setSuperclass(cls);
        //设置回调
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        log.info("调用的方法：" + method.getName());
        //在该处写拦截逻辑
        if(name.equals("John")){
            System.out.println("John have no privalege");
            return null;
        }
        Object result = methodProxy.invokeSuper(object, objects);
        return result;
    }
}
