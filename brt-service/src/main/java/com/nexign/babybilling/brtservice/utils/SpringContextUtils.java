package com.nexign.babybilling.brtservice.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils {

    private static ApplicationContext applicationContext;

    public void setApplicationContext(final ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /*
        Get a class bean from the application context
     */
    public static <T> T getBean(final Class clazz) {
        return (T) applicationContext.getBean(clazz);
    }

    /*
        Return the application context if necessary for anything else
     */
    public static ApplicationContext getContext() {
        return applicationContext;
    }
}
