package com.pay.third.commons.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pay.third.commons.utils.Regx;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import static java.util.regex.Pattern.compile;

/**
 * 返回结果封装类
 *
 * @author Tang
 * @version 1.0.0
 * @date 2020/8/9 22:47
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> implements Serializable {
    private final static Logger LOG = LoggerFactory.getLogger(Result.class);

    private static final long serialVersionUID = 399045245588971419L;

    /**
     * 响应状态码
     */
    private Integer code;
    /**
     * 状态码描述
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;
    /**
     * 全局附加数据，字段、内容不定（如等级，经验的变化，可以作为全局的数据存在，区别于某次请求的具体data）
     */
    private Extra extra;
    /**
     * 地区
     */
    private Locale locale;

    /**
     * 成功状态
     *
     * @param <T>
     * @return
     */
    public static <T> Result ok() {
        Result<T> result = new Result<>();
        result.setCode(CodeEnum.SUCCESS.code);
        result.setMsg(CodeEnum.SUCCESS.msg);
        return result;
    }

    /**
     * 失败状态
     *
     * @param <T>
     * @return
     */
    public static <T> Result error() {
        Result<T> result = new Result<>();
        result.setCode(CodeEnum.FAILED.code);
        result.setMsg(CodeEnum.FAILED.msg);
        return result;
    }

    /**
     * 密码错误次数过多
     *
     * @param <T>
     * @return
     */
    public static <T> Result passwordError() {
        Result<T> result = new Result<>();
        result.setCode(CodeEnum.PASSWORDERROR.code);
        result.setMsg(CodeEnum.PASSWORDERROR.msg);
        return result;
    }

    /**
     * 未授权状态
     *
     * @param <T>
     * @return
     */
    public static <T> Result unauthorized() {
        Result<T> result = new Result<>();
        result.setCode(CodeEnum.UNAUTHORIZED.code);
        result.setMsg(CodeEnum.UNAUTHORIZED.msg);
        return result;
    }

    /**
     * 系统未知错误
     *
     * @param <T>
     * @return
     */
    public static <T> Result unknow() {
        Result<T> result = new Result<>();
        result.setCode(CodeEnum.UNKNOWN_ERROR.code);
        result.setMsg(CodeEnum.UNKNOWN_ERROR.msg);
        return result;
    }

    /**
     * 系统维护
     *
     * @param <T>
     * @return
     */
    public static <T> Result maintenance() {
        Result<T> result = new Result<>();
        result.setCode(CodeEnum.SYSTEM_MAINTENANCE.code);
        result.setMsg(CodeEnum.SYSTEM_MAINTENANCE.msg);
        return result;
    }

    // ================支持国际化的方法======================//

    /**
     * 成功状态
     *
     * @param <T>
     * @return
     */
    public static <T> Result ok(Locale locale) {
        Result<T> result = new Result<>();
        result.setLocale(locale);
        result.setCode(CodeEnum.SUCCESS.code);
        result.setMsg(setI18NMsg(result.code, locale));
        return result;
    }

    /**
     * 失败状态
     *
     * @param <T>
     * @return
     */
    public static <T> Result error(Locale locale) {
        Result<T> result = new Result<>();
        result.setLocale(locale);
        result.setCode(CodeEnum.FAILED.code);
        result.setMsg(setI18NMsg(result.code, locale));
        return result;
    }

    /**
     * 密码错误次数过多
     *
     * @param <T>
     * @return
     */
    public static <T> Result passwordError(Locale locale) {
        Result<T> result = new Result<>();
        result.setLocale(locale);
        result.setCode(CodeEnum.PASSWORDERROR.code);
        result.setMsg(setI18NMsg(result.code, locale));
        return result;
    }

    /**
     * 未授权状态
     *
     * @param <T>
     * @return
     */
    public static <T> Result unauthorized(Locale locale) {
        Result<T> result = new Result<>();
        result.setLocale(locale);
        result.setCode(CodeEnum.UNAUTHORIZED.code);
        result.setMsg(setI18NMsg(result.code, locale));
        return result;
    }

    /**
     * 系统未知错误
     *
     * @param <T>
     * @return
     */
    public static <T> Result unknow(Locale locale) {
        Result<T> result = new Result<>();
        result.setLocale(locale);
        result.setCode(CodeEnum.UNKNOWN_ERROR.code);
        result.setMsg(setI18NMsg(result.code, locale));
        return result;
    }

    /**
     * 系统维护中
     *
     * @param <T>
     * @return
     */
    public static <T> Result systemMaintenance(Locale locale) {
        Result<T> result = new Result<>();
        result.setLocale(locale);
        result.setCode(CodeEnum.SYSTEM_MAINTENANCE.code);
        result.setMsg(setI18NMsg(result.code, locale));
        return result;
    }

    // ================国际化方法======================//

    /**
     * 设置消息内容，带国际化
     *
     * @param subCode
     * @param locale
     * @return
     */
    private static String setI18NMsg(int subCode, Locale locale) {
        try {
            String msg = LangUtil.getInfoByKey("code_" + subCode, locale);
            if (StringUtils.isEmpty(msg)) {
                return "code:" + subCode;
            } else {
                return msg;
            }
        } catch (Exception e) {
            LOG.error("设置消息内容，带国际化，异常消息：{}", e.getMessage(), e);
        }

        return "Message code:" + subCode;
    }

    /**
     * 设置消息内容，带国际化
     *
     * @param subCode
     * @param locale
     * @return
     */
    private static String setI18NMsg(int subCode, String[] params, Locale locale) {
        try {
            String msg = LangUtil.getInfoByKey("code_" + subCode, locale);
            if (params.length > 0) {
                msg = fillStringByArgs(msg, params);
            }

            if (StringUtils.isEmpty(msg)) {
                return "code:" + subCode;
            } else {
                return msg;
            }
        } catch (Exception e) {
            LOG.error("设置消息内容，带国际化，异常消息：{}", e.getMessage(), e);
        }

        return "Message code:" + subCode;
    }


    /**
     * 获取资源文件msg并替换字符
     *
     * @param key
     * @param params
     * @param locale
     * @return
     */
    public static String getSourceMsg(String key, String[] params, Locale locale) {
        String msg = LangUtil.getInfoByKey(key, locale);
        if (params.length > 0) {
            msg = fillStringByArgs(msg, params);
        }
        return msg;
    }

    /**
     * 替换字符
     *
     * @param str
     * @param arr
     * @return
     */
    private static String fillStringByArgs(String str, String[] arr) {
        Matcher m = compile("\\{(\\d)\\}").matcher(str);
        while (m.find()) {
            str = str.replace(m.group(), arr[Integer.parseInt(m.group(1))]);
        }
        return str;
    }

    // ================链式设置方法======================//

    /**
     * 自定义信息
     *
     * @param msg
     * @return
     */
    public Result<T> msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    /**
     * 传入返回结果
     *
     * @param data
     * @return
     */
    public Result<T> data(T data) {
        this.setData(data);
        return this;
    }

    /**
     * 传入全局附加数据
     *
     * @param extra
     * @return
     */
    public Result<T> extra(Extra extra) {
        this.setExtra(extra);
        return this;
    }

    /**
     * 设置子状态码
     *
     * @param subCode
     * @return
     */
    public Result<T> subCode(Integer subCode) {
        this.setMsg(setI18NMsg(subCode, this.locale));
        return this;
    }

    /**
     * 设置子状态码
     *
     * @param subCode 一个或多个code,例如：12|13|14|15
     * @return
     */
    public Result<T> subCode(String subCode) {
        String msg = "";
        if (StringUtils.isNotBlank(subCode)) {
            List<String> codes = new ArrayList<>(Arrays.asList(subCode.split("\\|")));
            for (String code : codes) {
                // 是否是纯数字,纯数字代表自定义错误代码subCode
                // @NotEmpty(message = "75")   // 请输入手机号
                if (code.matches(Regx.REGX_COUNT)) {
                    msg += setI18NMsg(Integer.parseInt(code), this.locale) + ";";
                } else {
                    // @NotEmpty(message = "参数code不能为空") String code
                    msg += code + ";";
                }
            }
        }
        this.setMsg(msg);
        return this;
    }

    /**
     * 设置子状态码
     *
     * @param subCode
     * @param params
     * @return
     */
    public Result<T> subCode(Integer subCode, String[] params) {
        this.setMsg(setI18NMsg(subCode, params, this.locale));
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * 状态码枚举
     */
    public static enum CodeEnum {
        SUCCESS(1, "成功"),
        FAILED(2, "失败"),
        PASSWORDERROR(3, "登录密码错误次数已达上限，请使用短信验证码登录"),
        UNAUTHORIZED(401, "登录失效，请重新登录"),
        NOPERMISSION(403, "无权限访问"),
        UNKNOWN_ERROR(500, "系统开小差了，请重试"),
        SYSTEM_MAINTENANCE(1000, "系统维护中");

        private Integer code;
        private String msg;

        private CodeEnum(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    /**
     * 附加数据
     */
    public static class Extra implements Serializable {
        private static final long serialVersionUID = 5449408258377976375L;
        /**
         * 类型
         */
        private Integer type;
        /**
         * 描述
         */
        private String desc;

        public Extra() {

        }

        public Extra(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
