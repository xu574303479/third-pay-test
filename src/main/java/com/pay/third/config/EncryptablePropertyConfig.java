package com.pay.third.config;

import com.pay.third.commons.configuration.DefaultConfiguration;
import com.pay.third.commons.system.AES;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置文件解密
 *
 * @author Tang
 * @version 1.0.0
 * @date 2021/3/30 23:31
 */
@Configuration
public class EncryptablePropertyConfig {
    private static final Logger LOG = LoggerFactory.getLogger(EncryptablePropertyConfig.class);

    /**
     * 加密属性值的前缀
     */
    public static final String ENCODED_PASSWORD_PREFIX = "ENC[";

    /**
     * 加密属性值的后缀
     */
    public static final String ENCODED_PASSWORD_SUFFIX = "]";

    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return new EncryptionPropertyResolver();
    }

    public static class EncryptionPropertyResolver
            implements EncryptablePropertyResolver {
        @Override
        public String resolvePropertyValue(String value) {
            if (StringUtils.isBlank(value)) {
                return value;
            }

            if (value.startsWith(ENCODED_PASSWORD_PREFIX) &&
                    value.endsWith(ENCODED_PASSWORD_SUFFIX)) {
                try {
                    value = value.substring(ENCODED_PASSWORD_PREFIX.length(),
                            value.length() - ENCODED_PASSWORD_SUFFIX.length());
                    return AES.decrypt(value, DefaultConfiguration.getAesPwd());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //不需要解密的值直接返回
            return value;
        }
    }
}
