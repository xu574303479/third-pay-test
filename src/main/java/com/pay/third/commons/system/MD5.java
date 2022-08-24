package com.pay.third.commons.system;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 10:41
 * @Version: 1.0.0
 */
public class MD5 {
    private static MessageDigest md = null;
    private static boolean inited = false;

    public static String getMD5Encode(String src) throws Exception {
        if (src == null) {
            return null;
        } else {
            src = src + "b&J%OI1iuo*88oZd";
            return encrypt(src);
        }
    }

    public static String encrypt(String src) throws Exception {
        if (src == null) {
            return null;
        } else if (!inited) {
            throw new Exception("MD5 算法实例初始化错误！");
        } else {
            byte[] temp = null;
            synchronized (md) {
                temp = md.digest(src.getBytes());
            }

            return CyptoUtil.byte2hex(temp).toLowerCase();
        }
    }

    public static String encrypt(String src, String charset) throws Exception {
        if (src == null) {
            return null;
        } else if (!inited) {
            throw new Exception("MD5 算法实例初始化错误！");
        } else {
            byte[] temp = null;
            synchronized (md) {
                temp = md.digest(src.getBytes(charset));
            }

            return CyptoUtil.byte2hex(temp).toLowerCase();
        }
    }

    static {
        try {
            md = MessageDigest.getInstance("MD5");
            inited = true;
        } catch (NoSuchAlgorithmException var1) {
            inited = false;
        }

    }
}
