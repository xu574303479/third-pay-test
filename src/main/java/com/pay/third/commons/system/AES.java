package com.pay.third.commons.system;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 10:40
 * @Version: 1.0.0
 */
public class AES {
    public static String encrypt(String src, String password) throws Exception {
        password = MD5.getMD5Encode(password).toLowerCase().substring(16);
        byte[] key = password.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, skeySpec);
        byte[] decryptText = cipher.doFinal(src.getBytes("utf-8"));
        return CyptoUtil.byte2hex(decryptText);
    }

    public static String decrypt(String src, String password) throws Exception {
        password = MD5.getMD5Encode(password).toLowerCase().substring(16);
        byte[] key = password.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, skeySpec);
        byte[] hbyte = CyptoUtil.hex2byte(src);
        if (hbyte == null) {
            return null;
        } else {
            byte[] plainText = cipher.doFinal(hbyte);
            return new String(plainText, "utf-8");
        }
    }
}
