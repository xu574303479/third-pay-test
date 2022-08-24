package com.pay.third.listener;

import com.pay.third.commons.configuration.CommonConfiguration;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;


/**
 * 加载自定义配置信息
 *
 * @author Tang
 * @version 1.0.0
 * @date 2020/1/3 18:21
 */
public class ConfigurationListener implements ApplicationListener<ApplicationStartingEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        // 加载配置
        CommonConfiguration commonConfiguration = new CommonConfiguration();
    }
}
