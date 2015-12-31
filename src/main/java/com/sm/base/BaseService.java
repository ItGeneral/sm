package com.sm.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * ����������
 *
 * @author Administrator on 2015/11/16.
 */
public abstract class BaseService<T> {

    @Autowired
    private BaseDao realDao;
    protected BaseDao dao;

    private String namespace;

    public BaseService() {
        // �ҵ�ֱ�Ӽ̳�BaseService�����ȡ����
        Class targetClazz = getClass();
        while (true) {
            if (targetClazz.getSuperclass().equals(BaseService.class)) {
                break;
            }
            targetClazz = targetClazz.getSuperclass();
        }

        Class<T> clazz = (Class<T>) ((ParameterizedType) targetClazz.getGenericSuperclass()).getActualTypeArguments()[0];
        namespace = clazz.getSimpleName();

        // ��̬����dao��ʹ��dao������ҪsqlId
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
