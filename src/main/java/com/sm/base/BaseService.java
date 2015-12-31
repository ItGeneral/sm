package com.sm.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * 方法描述：
 *
 * @author Administrator on 2015/11/16.
 */
public abstract class BaseService<T> {

    @Autowired
    private BaseDao realDao;
    protected BaseDao dao;

    private String namespace;

    public BaseService() {
        // 找到直接继承BaseService的类获取泛型
        Class targetClazz = getClass();
        while (true) {
            if (targetClazz.getSuperclass().equals(BaseService.class)) {
                break;
            }
            targetClazz = targetClazz.getSuperclass();
        }

        Class<T> clazz = (Class<T>) ((ParameterizedType) targetClazz.getGenericSuperclass()).getActualTypeArguments()[0];
        namespace = clazz.getSimpleName();

        // 动态代理dao，使用dao不在需要sqlId
        Enhancer e = new Enhancer();
        e.setSuperclass(BaseDao.class);
        e.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args,
                                    MethodProxy proxy) throws Throwable {
                Object retValFromSuper = null;
                try {
                    args[0] = sqlId((String) args[0]);
                    retValFromSuper = method.invoke(realDao, args);
                } catch (Throwable t) {
                    throw t.fillInStackTrace();
                }
                return retValFromSuper;
            }
        });
        dao = (BaseDao) e.create();
    }

    private String sqlId(String sqlId) {
        return new StringBuffer(namespace).append(".").append(sqlId).toString();
    }
}
