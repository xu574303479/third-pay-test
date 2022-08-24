package com.pay.third.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置文件
 *
 * @author Tang
 * @version 1.0.0
 * @date 2020/1/9 23:25
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * 连接池配置
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.redis.jedis")
    public JedisPoolConfig jedisPoolConfig() {
        return new JedisPoolConfig();
    }
    //----------------------------------公共缓存
    @Bean
    public RedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(0);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisClientConfiguration
                = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        jedisClientConfiguration.poolConfig(jedisPoolConfig); // 设置连接池

        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // Hash value序列化

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate sredisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // 配置redisTemplate
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    //---------------------------会员缓存
    @Bean
    public RedisConnectionFactory mredisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(1);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisClientConfiguration
                = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        jedisClientConfiguration.poolConfig(jedisPoolConfig); // 设置连接池

        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> mredisTemplate(RedisConnectionFactory mredisConnectionFactory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(mredisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // Hash value序列化

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate msredisTemplate(RedisConnectionFactory mredisConnectionFactory) {
        // 配置redisTemplate
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(mredisConnectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }


    //---------------------------CSRF缓存
    @Bean
    public RedisConnectionFactory credisConnectionFactory(JedisPoolConfig jedisPoolConfig){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration ();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(2);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jedisClientConfiguration
                = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder)JedisClientConfiguration.builder();
        jedisClientConfiguration.poolConfig(jedisPoolConfig); // 设置连接池

        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());

        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> credisTemplate(RedisConnectionFactory credisConnectionFactory) {
        // 配置redisTemplate
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(credisConnectionFactory);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer); // key序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value序列化
        redisTemplate.setHashKeySerializer(stringSerializer); // Hash key序列化
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer()); // Hash value序列化

        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate csredisTemplate(RedisConnectionFactory credisConnectionFactory) {
        // 配置redisTemplate
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(credisConnectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
