package com.pay.third;

import com.pay.third.listener.ConfigurationListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ServletComponentScan("com.pay.third")
public class ThirdPayTestApplication {

    public static void main(String[] args) {
//        SpringApplication.run(ThirdPayTestApplication.class, args);
        SpringApplication application = new SpringApplication(ThirdPayTestApplication.class);
        application.addListeners(new ConfigurationListener());
        application.run(args);
    }

}
