package com.pay.third.commons.system;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 10:17
 * @Version: 1.0.0
 */
public class LangUtil {
    public LangUtil() {
    }

    public static String getInfoByKey(String key, Locale locale) {
        String value;
        if (locale == null) {
            String lang = "en";
            value = "US";
            locale = new Locale(lang, value);
        }

        ResourceBundle rb = ResourceBundle.getBundle("messages", locale);
        value = rb.getString(key);
        return value;
    }
}
