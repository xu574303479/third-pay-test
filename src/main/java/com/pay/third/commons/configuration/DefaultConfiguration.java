package com.pay.third.commons.configuration;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 10:43
 * @Version: 1.0.0
 */
public class DefaultConfiguration {
    private static String aesPwd = "!&#123rasdfasdsdfqasfqi23ruqwedsdszxqeruqperu*2022";
    private static String xmlConfigFile = "config.xml";
    private static String txtConfigFile = "config.txt";
    private static String propertiesConfigFile = "config.properties";
    private static String ehcacheConfigurationFile = "ehcache_common.xml";
    private static String cacheName = "COM_CONFIG_COLLECT";
    private static String packagePrefix = "com.";
    private static String googleAuthSeed = "CB!kjwqdpouiq@vC^$WSMb8bA!QCccKNK#Db71f^ni@%^o&zISrA5MR$3nB#EVbScY7";

    public DefaultConfiguration() {
    }

    public static String getAesPwd() {
        return aesPwd;
    }

    public static void setAesPwd(String aesPwd) {
        DefaultConfiguration.aesPwd = aesPwd;
    }

    public static String getXmlConfigFile() {
        return xmlConfigFile;
    }

    public static void setXmlConfigFile(String xmlConfigFile) {
        DefaultConfiguration.xmlConfigFile = xmlConfigFile;
    }

    public static String getTxtConfigFile() {
        return txtConfigFile;
    }

    public static void setTxtConfigFile(String txtConfigFile) {
        DefaultConfiguration.txtConfigFile = txtConfigFile;
    }

    public static String getPropertiesConfigFile() {
        return propertiesConfigFile;
    }

    public static void setPropertiesConfigFile(String propertiesConfigFile) {
        DefaultConfiguration.propertiesConfigFile = propertiesConfigFile;
    }

    public static String getEhcacheConfigurationFile() {
        return ehcacheConfigurationFile;
    }

    public static void setEhcacheConfigurationFile(String ehcacheConfigurationFile) {
        DefaultConfiguration.ehcacheConfigurationFile = ehcacheConfigurationFile;
    }

    public static String getCacheName() {
        return cacheName;
    }

    public static void setCacheName(String cacheName) {
        DefaultConfiguration.cacheName = cacheName;
    }

    public static String getPackagePrefix() {
        return packagePrefix;
    }

    public static void setPackagePrefix(String packagePrefix) {
        DefaultConfiguration.packagePrefix = packagePrefix;
    }

    public static String getGoogleAuthSeed() {
        return googleAuthSeed;
    }

    public static void setGoogleAuthSeed(String googleAuthSeed) {
        DefaultConfiguration.googleAuthSeed = googleAuthSeed;
    }
}
