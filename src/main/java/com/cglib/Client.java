package com.cglib;

/**
 * @Author by songjiuhua
 * createdAt  16/1/1
 * 创建一个客户端，调用CRUD
 */
public class Client {

    public static void main(String[] args){
        //不使用代理
        /*UserService userService = UserServiceFactory.getInstance();
        doMethod(userService);*/
        //使用动态代理，只有boss才有操作的权限，John没有任何权限
        /*UserService userService1 = UserServiceFactory.getProxyInstance(new MyCglibProxy("boss"));
        userService1.insert();
        UserService userService2 = UserServiceFactory.getProxyInstance(new MyCglibProxy("John"));
        userService2.insert();*/

        //使用过滤方法,query方法所有人都有权限，其他方法没有
        UserService userService3 = UserServiceFactory.getProxyInstanceByFilter(new MyCglibProxy("John"));
        userService3.query();
        userService3.insert();
    }


    public static void doMethod(UserService userService){
        userService.delete();
        userService.insert();
        userService.query();
        userService.update();
    }

}
