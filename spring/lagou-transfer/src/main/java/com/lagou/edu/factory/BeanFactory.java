package com.lagou.edu.factory;


import com.lagou.edu.annotation.Autowired;
import com.lagou.edu.annotation.Service;
import com.lagou.edu.annotation.Transactional;
import org.reflections.Reflections;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 应癫
 * <p>
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory implements ServletContextListener {


    private static final Map<String, Object> map = new HashMap<>();  // 存储对象


    static {
        try {
            //扫描获取service注解对象集合
            Reflections reflections = new Reflections("com.lagou.edu");
            Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Service.class);
            for (Class<?> clazz : typesAnnotatedWith) {
                Object bean = clazz.newInstance();
                Service annotation = clazz.getAnnotation(Service.class);

                //对象ID在service注解有value时用value，没有时用类名
                if (annotation.value().length() == 0) {
                    //由于getName获取的是全限定类名，所以要分割去掉前面包名部分
                    String[] names = clazz.getName().split("\\.");
                    map.put(names[names.length - 1], bean);
                } else {
                    map.put(annotation.value(), bean);
                }
            }

            // 实例化完成之后维护对象的依赖关系Autowired，检查哪些对象需要传值进入
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Object obj = entry.getValue();
                Class aClass = obj.getClass();
                //获取所有的变量
                Field[] fields = aClass.getDeclaredFields();
                //遍历属性，若持有Autowired注解则注入
                for (Field field : fields) {
                    //判断是否是使用注解的参数
                    if (field.isAnnotationPresent(Autowired.class)) {
                        //根据字段名称查找 ，需要保证 声明Autowired的字段名称和声明Service的value中值一样
                        String name = field.getName();
                        //强制设置值
                        field.setAccessible(true);
                        field.set(obj, map.get(name));

                    }

                }

            }

            //  先完成注入，再扫描Transactional注解
            for (String beanName : map.keySet()) {
                Object obj = map.get(beanName);
                Class aClass = obj.getClass();
                //判断对象类是否持有Transactional注解，若有则修改对象为代理对象
                if (aClass.isAnnotationPresent(Transactional.class)) {
                    //获取代理工厂
                    ProxyFactory proxyFactory = (ProxyFactory) BeanFactory.getBean("proxyFactory");
                    Class[] face = aClass.getInterfaces();
                    //判断对象是否实现接口
                    if (face.length > 0) {
                        //实现使用JDK
                        obj = proxyFactory.getJdkProxy(obj);
                    } else {
                        //没实现使用CGLIB
                        obj = proxyFactory.getCglibProxy(obj);
                    }
                    map.put(beanName, obj);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static Object getBean(String id) {
        return map.get(id);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
