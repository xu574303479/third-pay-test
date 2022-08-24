package com.pay.third.commons.system;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring工具，用于直接获取spring管理的bean
 * 
 * @author Tang
 * @date 2018年9月16日 下午2:09:26
 * @version 1.0.0
 */
@Component
public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
		}
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 根据bean名称获取bean对象
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
}
