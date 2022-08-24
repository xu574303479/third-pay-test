package com.pay.third.commons.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 10:27
 * @Version: 1.0.0
 */
public class RegxUtil {

    public static final String REGX_ID = "[1-9]{1}[0-9]*";
    public static final String REGX_8UUID = "[1-9]{1}[0-9]{7}";
    public static final String REGX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
    public static final String REGX_PHONE = "1[3-9][0-9]{9}";
    public static final String REGX_AMOUNT = "[0-9]*\\.?[0-9]{1,2}";
    public static final String REGX_DATE_1 = "(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)";
    public static final String REGX_DATE_2 = "(?:(?:(?:(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(\\/|-)(?:0?2\u0001(?:29)))|(?:(?:(?:1[6-9]|[2-9]\\d)?\\d{2})(\\/|-)(?:(?:(?:0?[13578]|1[02])\\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\\2(29|30))|(?:(?:0?[1-9])|(?:1[0-2]))\\2(?:0?[1-9]|1\\d|2[0-8])))))\\s(?:([0-1]\\d|2[0-3]):[0-5]\\d:[0-5]\\d)";
    public static final String REGX_AMOUNT4 = "[0-9]*\\.?[0-9]{1,4}";
    public static final String REGX_COUNT = "[0-9]*[1-9][0-9]*";
    public static final String REGX_NAME = "[一-龥]{0,}";
    public static final String REGX_IDCARD = "([0-9]{15})|([0-9]{17}[0-9xX]{1})";
    public static final Map<String, String> REGX_GLOBAL_PHONE = new HashMap();

    static {
        REGX_GLOBAL_PHONE.put("86", "(13|14|15|17|18|19)\\d{9}");
        REGX_GLOBAL_PHONE.put("852", "(9|6)\\d{7}");
        REGX_GLOBAL_PHONE.put("853", "(6)\\d{7}");
        REGX_GLOBAL_PHONE.put("886", "(9)\\d{8}");
        REGX_GLOBAL_PHONE.put("1", "\\d{10}");
        REGX_GLOBAL_PHONE.put("65", "(9|8)\\d{7}");
        REGX_GLOBAL_PHONE.put("84", "((9|09)\\d{8})|((1|01)\\d{9})");
        REGX_GLOBAL_PHONE.put("82", "(1|01)\\d{9}");
        REGX_GLOBAL_PHONE.put("60", "(1)\\d{8}");
        REGX_GLOBAL_PHONE.put("66", "(08|09|8|9)\\d{8}");
        REGX_GLOBAL_PHONE.put("63", "((9|09)\\d{9})|((2|02)\\d{7})");
        REGX_GLOBAL_PHONE.put("62", "(8)(\\d{10,12})");
    }
}
