package com.pay.third.commons;

import com.pay.third.ParentTest;
import com.pay.third.commons.configuration.DefaultConfiguration;
import com.pay.third.commons.system.AES;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommonsTest extends ParentTest {

    @Test
    public void test() throws Exception {
        String encrypt1 = AES.encrypt("root", DefaultConfiguration.getAesPwd());
        String encrypt2 = AES.encrypt("123456", DefaultConfiguration.getAesPwd());
        System.out.println("encrypt1 = " + encrypt1);
        System.out.println("encrypt2 = " + encrypt2);
    }

}
