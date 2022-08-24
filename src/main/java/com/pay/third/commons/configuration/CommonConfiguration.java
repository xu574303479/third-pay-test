package com.pay.third.commons.configuration;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 静态文件配置
 * 
 * @author Tang
 * @date 2018年8月31日 下午6:26:01
 * @version 1.0.0
 */
@Data
public class CommonConfiguration {
	private final static Logger LOG = LoggerFactory.getLogger(CommonConfiguration.class);
	
	// AES密钥，64位，用于配置文件中的密码加密等
	private final static String AES_PWD = "b5qn#gNKLJQWDHALKSDOUIQWDJKLASJDEqvRegfWUEKx0&MEEdyCXxd&HZQvR6pol";
	
	// xml配置文件名
	private final static String XML_CONFIG_FILE = "config.xml";
	
	// ehcache配置文件名
	private final static String EHCACHE_CONFIGURATION_FILE = "ehcache_common.xml";

	// ehcache缓存名
	private final static String EHCACHE_CACHE_NAME = "COM_CONFIG_COLLECT";
	
	// 项目包名前缀
	private final static String PACKAGE_PREFIX = "com.pay.third";
	
	static {
		DefaultConfiguration.setAesPwd(AES_PWD);
		DefaultConfiguration.setXmlConfigFile(XML_CONFIG_FILE);
		DefaultConfiguration.setEhcacheConfigurationFile(EHCACHE_CONFIGURATION_FILE);
		DefaultConfiguration.setCacheName(EHCACHE_CACHE_NAME);
		DefaultConfiguration.setPackagePrefix(PACKAGE_PREFIX);
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("创建 CommonConfiguration，初始化项目参数设置成功!");
		}
	}
}
