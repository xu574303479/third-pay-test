package com.pay.third.commons.utils;


/**
 * 正则表达式
 *
 * @author Tang
 * @version 1.0.0
 * @date 2018年12月2日 上午12:11:01
 */
public class Regx extends RegxUtil {

    /**
     * 商品编号正则表达式
     */
    public final static String REGX_GOODS_SERIAL = "[1-9]{1}[0-9]{5}";

    /**
     * 数量正则表达式
     */
    public final static String REGX_COUNT = "[1-9]{1}[0-9]*";

    /**
     * 价格正则表达式
     */
    public final static String REGX_PRICE = "\\d\\.\\d*|[1-9]\\d*|\\d*\\.\\d*|\\d";

    /**
     * 生日正则表达式 birthday
     */
    public final static String REGX_BIRTHDAY = "\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}";

    /**
     * ip的正则表达式
     */
    public final static String REGX_IP = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,d2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

    /**
     * ip范围的正则表达式
     */
    public final static String REGX_IP_RANGE = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}/((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})";

    /**
     * 检验登录密码
     */
    public final static String REGX_PASSWORD = "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)\\S{6,18}$";

    /**
     * 校验支付密码
     */
    public final static String REGX_PAY_PASSWORD = "^\\d{6}$";

    /**
     * 邀请码正则表达式
     */
    public final static String REGX_CODE = "^(?![0-9]+$)(?![A-Z]+$)[0-9A-Z]{6}$";

}
