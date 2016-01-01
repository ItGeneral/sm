package com.cglib;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

/**
 * @Author by songjiuhua
 * createdAt  16/1/1
 * 创建一个工厂生成实例
 */
public class UserServiceFactory {
    private static UserService userService = new UserService();

    private UserServiceFactory(){}

    //生成一个实例
    public static UserService getInstance(){
        return userService;
    }

    //生成一个动态代理实例
    public static UserService getProxyInstance(MyCglibProxy myCglibProxy){
        Enhancer enhancer = new Enhancer();
        //进行代理
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(myCglibProxy);
        return (UserService) enhancer.create();
    }

    //生成一个过滤方法的代理实例
    public static UserService getProxyInstanceByFilter(MyCglibProxy myCglibProxy){
        Enhancer en = new Enhancer();
        en.setSuperclass(UserService.class);
        //如果调用MyProxyFilter中设置的方法（query）就使用NoOp.INSTANCE进行拦截。
        en.setCallbacks(new Callback[]{myCglibProxy, NoOp.INSTANCE});
        en.setCallbackFilter(new MyProxyFilter());
        return (UserService) en.create();
    }
}
