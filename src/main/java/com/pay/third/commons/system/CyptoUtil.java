package com.pay.third.commons.system;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 10:41
 * @Version: 1.0.0
 */
public class CyptoUtil {
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for(int i = 0; i < b.length; ++i) {
            stmp = Integer.toHexString(b[i] & 255);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        return hs.toUpperCase();
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        } else {
            int l = strhex.length();
            if (l % 2 == 1) {
                return null;
            } else {
                byte[] b = new byte[l / 2];

                for(int i = 0; i != l / 2; ++i) {
                    b[i] = (byte)Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
                }

                return b;
            }
        }
    }
}
