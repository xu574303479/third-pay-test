package com.pay.third.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pay.third.commons.system.CustomException;
import com.pay.third.commons.system.LangUtil;
import com.pay.third.commons.system.RequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 公共工具类
 *
 * @author Tang
 * @version 1.0.0
 * @date 2018年9月4日 上午12:34:41
 */
public class CommonUtil {

    private final static Logger LOG = LoggerFactory.getLogger(CommonUtil.class);

    public static final int COIN_SCALE = 8;
    public static final int CNY_SCALE = 2;
    public static final int PRICE_SCALE = 2;
    public static final int SCALE_1 = 2;
    public static final int INITIAL_CAPACITY = 16;

    /**
     * 在控制台打印json字符串
     *
     * @param obj
     */
    public static void printJsonString(Object obj) {
        System.out.println(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue));
    }

    /**
     * 获取request中所有请求参数
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Map<String, String> getAllParams(HttpServletRequest request) throws IOException {
        // 获取所有参数名和参数
        String rType = request.getContentType();
        Map<String, String> params = new HashMap<>();
        if (rType != null && rType.toLowerCase().contains("json")) {
            // 处理json格式数据
            String requestJson = RequestWrapper.inputStream2String(request.getInputStream());
            Map<String, Object> jsonParams = JSON.parseObject(requestJson, Map.class);
            if (jsonParams != null && jsonParams.size() > 0) {
                for (Map.Entry<String, Object> entry : jsonParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    params.put(key, String.valueOf(value));
                }
            }
            // 处理form提交数据
        } else {
            // 获取所有请求参数
            Enumeration<String> parameterNames = request.getParameterNames();
            for (Enumeration e = parameterNames; e.hasMoreElements(); ) {
                String thisName = e.nextElement().toString();
                String thisValue = request.getParameter(thisName);
                params.put(thisName, thisValue);
            }
        }

        // 获取 @PathVariable 中的参数,保存到 params 中
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (isNotEmpty(pathVariables)) {
            for (String key : pathVariables.keySet()) {
                params.put(key, pathVariables.get(key));
            }
        }

        return params;
    }

    /**
     * 比较对象,数据大小
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return obj1大于obj2返回1
     * obj1等于obj2返回0
     * obj1小于obj2返回-1
     */
    public static int compareObj(Object obj1, Object obj2) throws Exception {
        if (ObjectUtils.isEmpty(obj1) || ObjectUtils.isEmpty(obj2)) {
            throw new Exception("obj cannot be zero");
        }

        Class clazz = obj1.getClass();
        String clazzName = clazz.getName();
        if ("java.lang.Integer".equals(clazzName)) {
            int rs1 = new Integer(String.valueOf(obj1)).intValue();
            int rs2 = new Integer(String.valueOf(obj2)).intValue();
            if (rs1 == rs2) {
                return 0;
            } else if (rs1 > rs2) {
                return 1;
            } else if (rs1 < rs2) {
                return -1;
            }
        } else if ("java.lang.Long".equals(clazzName)) {
            long rs1 = new Long(String.valueOf(obj1)).longValue();
            long rs2 = new Long(String.valueOf(obj2)).longValue();
            if (rs1 == rs2) {
                return 0;
            } else if (rs1 > rs2) {
                return 1;
            } else if (rs1 < rs2) {
                return -1;
            }
        } else if ("java.lang.Double".equals(clazzName) || "java.math.BigDecimal".equals(clazzName)) {
            BigDecimal rs1 = new BigDecimal(String.valueOf(obj1));
            BigDecimal rs2 = new BigDecimal(String.valueOf(obj2));
            return rs1.compareTo(rs2);
        }

        return -2;
    }

    /**
     * 1个或多个参数转数组
     *
     * @param params
     * @return
     */
    public static String[] getParams(Object... params) {
        List<String> list = new ArrayList<>();
        for (Object param : params) {
            list.add(String.valueOf(param));
        }
        String[] objects = list.toArray(new String[params.length]);
        return objects;
    }


    /**
     * 过滤内容中HTML标签和"\r\n,\t,\r,\n",获取中文内容
     *
     * @param htmlContent
     * @param isDelHtml   是否删除html标签
     * @return
     */
    public static String replaceHTMLContent(String htmlContent, Integer isDelHtml) {
        if (!StringUtils.isEmpty(htmlContent)) {
            // 过滤"\r\n,\t,\r,\n"
            Pattern p = compile("(\r\n|\r|\n|\n\r|\t)");
            Matcher m = p.matcher(htmlContent);
            htmlContent = m.replaceAll("");

            // 过滤HTML标签
            if (isDelHtml == 1) {
                htmlContent = htmlContent.replaceAll("<[.[^<]]*>", "").replace("&nbsp;", "");
            }
        }

        return htmlContent;
    }

    /**
     * 打乱列表实现方法
     *
     * @param list
     * @param <T>
     */
    public static <T> void disruptList(List<T> list) {
        // 打乱顺序
        Collections.shuffle(list);
    }

    /**
     * 通过两点的经纬度计算两点间的距离
     *
     * @param longitude0
     * @param latitude0
     * @param longitude1
     * @param latitude1
     * @return
     */
    public static float calculateLineDistance(double longitude0, double latitude0, double longitude1, double latitude1) {
        try {
            double var2 = longitude0;
            double var4 = latitude0;
            double var6 = longitude1;
            double var8 = latitude1;
            var2 *= 0.01745329251994329D;
            var4 *= 0.01745329251994329D;
            var6 *= 0.01745329251994329D;
            var8 *= 0.01745329251994329D;
            double var10 = Math.sin(var2);
            double var12 = Math.sin(var4);
            double var14 = Math.cos(var2);
            double var16 = Math.cos(var4);
            double var18 = Math.sin(var6);
            double var20 = Math.sin(var8);
            double var22 = Math.cos(var6);
            double var24 = Math.cos(var8);
            double[] var28 = new double[3];
            double[] var29 = new double[3];
            var28[0] = var16 * var14;
            var28[1] = var16 * var10;
            var28[2] = var12;
            var29[0] = var24 * var22;
            var29[1] = var24 * var18;
            var29[2] = var20;
            return (float) (Math.asin(Math.sqrt((var28[0] - var29[0]) * (var28[0] - var29[0]) + (var28[1] - var29[1]) * (var28[1] - var29[1]) + (var28[2] - var29[2]) * (var28[2] - var29[2])) / 2.0D) * 1.27420015798544E7D);
        } catch (Throwable var26) {
            var26.printStackTrace();
            return 0.0F;
        }
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if cannot be determined
     */
    public static Class getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Class getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---String
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static String getStringFromMap(String key, String def, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        if (StringUtils.isBlank(key)) {
            return "";
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : obj.toString();
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---String
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static String getStringFromSmap(String key, String def, Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        if (StringUtils.isBlank(key)) {
            return "";
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : obj.toString();
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---String
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static String getStringFromObjMap(String key, String def, Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        if (StringUtils.isBlank(key)) {
            return "";
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : obj.toString();
    }


    /**
     * 获取map中value值，如果为空或者null，则返回默认值---String
     *
     * @param key 查询参数key值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static String getStringFromMap(String key, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        if (StringUtils.isBlank(key)) {
            return "";
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? "" : String.valueOf(obj);
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---String
     *
     * @param key 查询参数key值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static String getStringFromObjMap(String key, Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        if (StringUtils.isBlank(key)) {
            return "";
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? "" : String.valueOf(obj);
    }


    /**
     * 获取map中value值，如果为空或者null，则返回默认值---int
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static Integer getIntFromMap(String key, int def, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return def;
        }
        if (StringUtils.isBlank(key)) {
            return def;
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : Integer.parseInt(obj.toString());
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---int
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static Integer getIntFromObjMap(String key, int def, Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return def;
        }
        if (StringUtils.isBlank(key)) {
            return def;
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : Integer.parseInt(obj.toString());
    }


    /**
     * 获取map中value值，如果为空或者null，则返回默认值---double
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static Double getDoubleFromMap(String key, double def, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return def;
        }
        if (StringUtils.isBlank(key)) {
            return def;
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : Double.parseDouble(obj.toString());
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---double
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static Double getDoubleFromObjMap(String key, double def, Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return def;
        }
        if (StringUtils.isBlank(key)) {
            return def;
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : Double.parseDouble(obj.toString());
    }


    /**
     * 获取map中value值，如果为空或者null，则返回默认值---long
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static Long getLongFromMap(String key, long def, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return def;
        }
        if (StringUtils.isBlank(key)) {
            return def;
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : Long.parseLong(obj.toString());
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---long
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static Long getLongFromObjMap(String key, long def, Map<Object, Object> map) {
        if (map == null || map.isEmpty()) {
            return def;
        }
        if (StringUtils.isBlank(key)) {
            return def;
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? def : Long.parseLong(obj.toString());
    }

    /**
     * 获取map中value值，如果为空或者null，则返回默认值---BigDecimal
     *
     * @param key 查询参数key值
     * @param def 默认值
     * @param map 结果集
     * @return defulat 默认值
     */
    public static BigDecimal getBigDecimalFromMap(String key, double def, Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return new BigDecimal(String.valueOf(def));
        }
        if (StringUtils.isBlank(key)) {
            return new BigDecimal(String.valueOf(def));
        }

        Object obj = map.get(key);

        return isEmpty(obj) ? new BigDecimal(def) : new BigDecimal(String.valueOf(obj));
    }

    /**
     * 字符串翻转
     *
     * @param str
     * @return
     */
    public static String reverseStr(String str) {
        return new StringBuffer(str).reverse().toString();
    }

    /**
     * 判断对象为空
     *
     * @param obj 对象名
     * @return 是否为空
     */
    public static boolean isEmpty(Object obj) {
        if (ObjectUtils.isEmpty(obj)) {
            return true;
        } else if ((obj instanceof String)) {
            return StringUtils.isBlank(String.valueOf(obj));
        } else if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else if ((obj instanceof Map)) {
            if (((Map) obj).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    /**
     * 获取参数值---String类型---无默认值
     *
     * @param obj
     * @return
     */
    public static String objToString(Object obj) {
        if (isEmpty(obj)) {
            return null;
        }
        return String.valueOf(obj).trim();
    }

    /**
     * 获取参数值---String类型
     *
     * @param obj
     * @param def 为null或者为""，def
     * @return
     */
    public static String objToString(Object obj, String def) {
        if (isEmpty(obj)) {
            return def;
        }
        return String.valueOf(obj).trim();
    }


    /**
     * 获取参数值---Integer 类型
     *
     * @param obj
     * @param def 为null或者为""，def
     * @return
     */
    public static Integer objToInteger(Object obj, Integer def) {
        if (isEmpty(obj)) {
            return def;
        }
        return Integer.valueOf(String.valueOf(obj).trim());
    }

    /**
     * 获取参数值---Double 类型
     *
     * @param obj
     * @param def 为null或者为""，则返回def
     * @return
     */
    public static Double objToDouble(Object obj, Double def) {
        if (isEmpty(obj)) {
            return def;
        }
        return Double.valueOf(String.valueOf(obj).trim());
    }

    /**
     * 获取参数值---Long 类型
     *
     * @param obj
     * @param def 为null或者为""，则返回def
     * @return
     */
    public static Long objToLong(Object obj, Long def) {
        if (isEmpty(obj)) {
            return def;
        }
        return Long.valueOf(String.valueOf(obj).trim());
    }

    /**
     * 获取参数值---BigDecimal 类型
     *
     * @param obj
     * @param def 为null或者为""，则返回def
     * @return
     */
    public static BigDecimal objToBigDecimal(Object obj, String def) {
        if (isEmpty(obj)) {
            return new BigDecimal("0");
        }
        return new BigDecimal(String.valueOf(obj).trim());
    }

    /*************************************** BigDecimal 运算开始 ******************************************************************/
    /**
     * double 取 整数
     *
     * @param value
     * @return
     */
    public static int doubleToInt(double value) {
        return new BigDecimal(String.valueOf(value)).intValue();
    }

    /**
     * 加法，保留小数
     *
     * @param num1 加数
     * @param num2 被加数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getAdd(double num1, double num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .add(new BigDecimal(String.valueOf(num2)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 加法，保留小数
     *
     * @param num1 加数
     * @param num2 被加数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     *             RoundingMode.CEILING：取右边最近的整数
     *             RoundingMode.DOWN：去掉小数部分取整，也就是正数取左边，负数取右边，相当于向原点靠近的方向取整
     *             RoundingMode.FLOOR：取左边最近的正数
     *             RoundingMode.HALF_DOWN:五舍六入，负数先取绝对值再五舍六入再负数
     *             RoundingMode.HALF_UP:四舍五入，负数原理同上
     *             RoundingMode.HALF_EVEN:这个比较绕，整数位若是奇数则四舍五入，若是偶数则五舍六入
     * @return
     */
    public static Double getAdd(Double num1, Double num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .add(new BigDecimal(String.valueOf(num2)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 加法，保留小数---BigDecimal类型
     *
     * @param num1 加数
     * @param num2 被加数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static BigDecimal getAdd(BigDecimal num1, BigDecimal num2, int sc, RoundingMode ro) {
        return num1.add(num2).setScale(sc, ro);
    }

    /**
     * 减法，保留小数
     *
     * @param num1 减数
     * @param num2 被减数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getSubtract(double num1, double num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .subtract(new BigDecimal(String.valueOf(num2)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 减法，保留小数
     *
     * @param num1 减数
     * @param num2 被减数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getSubtract(Double num1, Double num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .subtract(new BigDecimal(String.valueOf(num2)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 减法，保留小数---BigDecimal类型
     *
     * @param num1 减数
     * @param num2 被减数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static BigDecimal getSubtract(BigDecimal num1, BigDecimal num2, int sc, RoundingMode ro) {
        return num1.subtract(num2).setScale(sc, ro);
    }

    /**
     * 乘法(使用参数为float或double的BigDecimal创建对象会丢失精度。因此强烈建议不要使用参数为float或double的BigDecimal创建对象。最好都转化为字符串)
     * 保留小数，四舍五入
     *
     * @param num1 乘数1
     * @param num2 乘数2
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     *             RoundingMode.CEILING：取右边最近的整数
     *             RoundingMode.DOWN：去掉小数部分取整，也就是正数取左边，负数取右边，相当于向原点靠近的方向取整
     *             RoundingMode.FLOOR：取左边最近的正数
     *             RoundingMode.HALF_DOWN:五舍六入，负数先取绝对值再五舍六入再负数
     *             RoundingMode.HALF_UP:四舍五入，负数原理同上
     *             RoundingMode.HALF_EVEN:这个比较绕，整数位若是奇数则四舍五入，若是偶数则五舍六入
     * @return
     */
    public static Double getMultiply(double num1, double num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .multiply(new BigDecimal(String.valueOf(num2)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 乘法(使用参数为float或double的BigDecimal创建对象会丢失精度。因此强烈建议不要使用参数为float或double的BigDecimal创建对象。最好都转化为字符串)
     * 保留小数，四舍五入
     *
     * @param num1 乘数1
     * @param num2 乘数2
     * @param num3 乘数3
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getMultiply(double num1, double num2, double num3, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .multiply(new BigDecimal(String.valueOf(num2)))
                        .multiply(new BigDecimal(String.valueOf(num3)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 乘法(使用参数为float或double的BigDecimal创建对象会丢失精度。因此强烈建议不要使用参数为float或double的BigDecimal创建对象。最好都转化为字符串)
     * 保留小数，四舍五入
     *
     * @param num1 乘数1
     * @param num2 乘数2
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getMultiply(Double num1, Double num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .multiply(new BigDecimal(String.valueOf(num2)))
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * BigDecimal类型
     * <p>
     * 乘法(使用参数为float或double的BigDecimal创建对象会丢失精度。因此强烈建议不要使用参数为float或double的BigDecimal创建对象。最好都转化为字符串)
     * 保留小数，四舍五入
     *
     * @param num1 乘数1
     * @param num2 乘数2
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     *             RoundingMode.CEILING：取右边最近的整数
     *             RoundingMode.DOWN：去掉小数部分取整，也就是正数取左边，负数取右边，相当于向原点靠近的方向取整
     *             RoundingMode.FLOOR：取左边最近的正数
     *             RoundingMode.HALF_DOWN:五舍六入，负数先取绝对值再五舍六入再负数
     *             RoundingMode.HALF_UP:四舍五入，负数原理同上
     *             RoundingMode.HALF_EVEN:这个比较绕，整数位若是奇数则四舍五入，若是偶数则五舍六入
     * @return
     */
    public static BigDecimal getMultiply(BigDecimal num1, BigDecimal num2, int sc, RoundingMode ro) {
        return num1.multiply(num2).setScale(sc, ro);
    }

    /**
     * 乘法(使用参数为float或double的BigDecimal创建对象会丢失精度。因此强烈建议不要使用参数为float或double的BigDecimal创建对象。最好都转化为字符串)
     * 保留小数，四舍五入
     *
     * @param num1 乘数1
     * @param num2 乘数2
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getMultiply(Double num1, BigDecimal num2, int sc, RoundingMode ro) {
        return (
                new BigDecimal(String.valueOf(num1))
                        .multiply(num2)
                        .setScale(sc, ro)
        ).doubleValue();
    }

    /**
     * 除法，保留小数
     *
     * @param num1 除数
     * @param num2 被除数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static Double getDivide(double num1, double num2, int sc, RoundingMode ro) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(num2));

        return (bigDecimal1.divide(bigDecimal2, sc, ro)).doubleValue();
    }


    /**
     * 除法，保留小数
     *
     * @param num1 除数
     * @param num2 被除数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     *             RoundingMode.CEILING：取右边最近的整数
     *             RoundingMode.DOWN：去掉小数部分取整，也就是正数取左边，负数取右边，相当于向原点靠近的方向取整
     *             RoundingMode.FLOOR：取左边最近的正数
     *             RoundingMode.HALF_DOWN:五舍六入，负数先取绝对值再五舍六入再负数
     *             RoundingMode.HALF_UP:四舍五入，负数原理同上
     *             RoundingMode.HALF_EVEN:这个比较绕，整数位若是奇数则四舍五入，若是偶数则五舍六入
     * @return
     */
    public static Double getDivide(Double num1, Double num2, int sc, RoundingMode ro) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(num1));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(num2));

        return (bigDecimal1.divide(bigDecimal2, sc, ro)).doubleValue();
    }

    /**
     * 除法，保留小数---BigDecimal类型
     *
     * @param num1 除数
     * @param num2 被除数
     * @param sc   保留小数位数
     * @param ro   保留小数方式
     * @return
     */
    public static BigDecimal getDivide(BigDecimal num1, BigDecimal num2, int sc, RoundingMode ro) {
        return num1.divide(num2, sc, ro);
    }


    /**
     * 保留小数，四舍五入
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static Double getDoubleAmount(double amount, int sc) {
        return new BigDecimal(String.valueOf(amount)).setScale(sc, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 保留小数，四舍五入
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static Double getDoubleAmount(Double amount, int sc) {
        if (ObjectUtils.isEmpty(amount)) {
            return 0D;
        }
        return new BigDecimal(String.valueOf(amount)).setScale(sc, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 保留小数，四舍五入
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static Double getDoubleAmount(String amount, int sc) {
        if (ObjectUtils.isEmpty(amount)) {
            return Double.valueOf(0);
        }
        return new BigDecimal(amount).setScale(sc, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 保留小数，四舍五入---BigDecimal类型
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static BigDecimal getDoubleAmount(BigDecimal amount, int sc) {
        if (ObjectUtils.isEmpty(amount)) {
            return new BigDecimal("0");
        }
        return amount.setScale(sc, RoundingMode.HALF_UP);
    }

    /**
     * 保留小数，四舍五入---BigDecimal类型
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static BigDecimal getDoubleAmount(Object amount, int sc) {
        if (ObjectUtils.isEmpty(amount)) {
            return new BigDecimal("0");
        }
        return new BigDecimal(String.valueOf(amount)).setScale(sc, RoundingMode.HALF_UP);
    }

    /**
     * 保留小数，四舍五入---BigDecimal类型---并去掉末尾无效的0
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static BigDecimal getBigDecimalZeros(BigDecimal amount, int sc) {
        if (ObjectUtils.isEmpty(amount)) {
            return amount;
        }
        String s = amount.setScale(sc, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        return new BigDecimal(s);
    }

    /**
     * 保留小数，四舍五入
     *
     * @param amount
     * @return
     */
    public static Double formatRemain(Double amount) {
        if (ObjectUtils.isEmpty(amount)) {
            return Double.valueOf(0);
        }
        return new BigDecimal(amount).setScale(COIN_SCALE, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 保留小数，四舍五入
     *
     * @param amount
     * @return
     */
    public static Double formatRemainCny(Double amount) {
        if (ObjectUtils.isEmpty(amount)) {
            return Double.valueOf(0);
        }
        return new BigDecimal(amount).setScale(CNY_SCALE, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 余额格式化
     * 保留小数，四舍五入---BigDecimal类型---并去掉末尾无效的0
     *
     * @param amount
     * @return
     */
    public static BigDecimal getBigDecimalRemain(Double amount) {
        if (ObjectUtils.isEmpty(amount)) {
            return new BigDecimal("0");
        }
        String s = new BigDecimal(amount.toString()).setScale(COIN_SCALE, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        return new BigDecimal(s);
    }


    /**
     * 保留小数，四舍五入---BigDecimal类型---并去掉末尾无效的0
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static BigDecimal getBigDecimalZeros(Double amount, int sc) {
        if (ObjectUtils.isEmpty(amount)) {
            return new BigDecimal("0");
        }
        String s = new BigDecimal(amount.toString()).setScale(sc, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        return new BigDecimal(s);
    }

    /**
     * 保留小数，四舍五入---BigDecimal类型---并去掉末尾无效的0
     *
     * @param amount
     * @param sc     小数位数
     * @return
     */
    public static BigDecimal getBigDecimalZeros(double amount, int sc) {
        String s = new BigDecimal(String.valueOf(amount)).setScale(sc, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        return new BigDecimal(s);
    }


    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param amount 需要格式化的数字
     * @param scale  小数点后保留几位
     * @return
     */
    public static String getDoubleRetainAmount(double amount, int scale) {

        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }

        return new DecimalFormat(formatStr).format(amount);
    }


    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(BigDecimal v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }

    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(Double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }

    /**
     * 将double格式化为指定小数位的String，不足小数位用0补全
     *
     * @param v     需要格式化的数字
     * @param scale 小数点后保留几位
     * @return
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (scale == 0) {
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for (int i = 0; i < scale; i++) {
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }


    /**
     * 将数量转化为 n.1K n.1W
     *
     * @param quantity
     * @return
     */
    public static int getQuantity(int quantity) {
        if (quantity <= 0) {
            return 0;
        }
        return quantity;
    }


    /**
     * 将数量转化为 n.1K n.1W
     *
     * @param quantity
     * @return
     */
    public static String getQuantityDesc(int quantity) {
        if (quantity <= 0) {
            return "0";
        }
        String desc = String.valueOf(quantity);
        if (quantity > 10000 && quantity <= 100000) {
            double n = getDivide(quantity, 1000, SCALE_1, RoundingMode.DOWN);
            desc = n + "K";
        } else if (quantity > 100000 && quantity <= 100000000) {
            double n = getDivide(quantity, 10000, SCALE_1, RoundingMode.DOWN);
            desc = n + "W";
        } else if (quantity > 100000000) {
            double n = getDivide(quantity, 100000000, SCALE_1, RoundingMode.DOWN);
            desc = n + "亿";
        }
        return desc;
    }

    /**
     * 将数量转化为 n.1W  千万则转
     *
     * @param amount
     * @return
     */
    public static String getAmonutDescMillion(BigDecimal amount) {
        if (null == amount || amount.doubleValue() <= 0) {
            return "0";
        }
        String desc = getDoubleAmount(amount, 4).toString();

        return desc;
    }

    /**
     * 将数量转化为 n.1W  千万则转
     *
     * @param amount
     * @return
     */
    public static String getAmonutDescMillion(double amount) {
        if (amount <= 0) {
            return "0";
        }
        String desc = String.valueOf(getDoubleAmount(amount, 4));

        return desc;
    }

    /**
     * 将数量转化为 n.1K n.1W
     * //比较大小：
     *
     * @param amount
     * @return
     */
    public static String getAmonutDescNew(BigDecimal amount) {
        if (null == amount || amount.doubleValue() <= 0) {
            return "0";
        }

        BigDecimal b1 = new BigDecimal("1000");
        BigDecimal b2 = new BigDecimal("10000000");

        BigDecimal b3 = new BigDecimal("10000");

        String desc = roundByScale(amount, 4);
        if (compareTo(amount, b1) == 1 && compareTo(amount, b2) == -1) {
            BigDecimal n = getDivide(amount, b1, SCALE_1, RoundingMode.DOWN);
            desc = n.toString() + "K";
        } else if (compareTo(amount, b2) == 1) {
            BigDecimal n = getDivide(amount, b3, SCALE_1, RoundingMode.DOWN);
            desc = n.toString() + "W";
        }
        return desc;
    }

    /**
     * BigDecimal 比较大小
     * <p>
     * int a = bigdemical.compareTo(bigdemical2)
     * //a = -1,表示bigdemical小于bigdemical2；
     * //a = 0,表示bigdemical等于bigdemical2；
     * //a = 1,表示bigdemical大于bigdemical2；
     *
     * @param b1
     * @param b2
     * @return
     */
    public static int compareTo(BigDecimal b1, BigDecimal b2) {
        return b1.compareTo(b2);
    }

    /**
     * BigDecimal 与 double  比较大小
     * <p>
     * int a = bigdemical.compareTo(bigdemical2)
     * //a = -1,表示bigdemical小于bigdemical2；
     * //a = 0,表示bigdemical等于bigdemical2；
     * //a = 1,表示bigdemical大于bigdemical2；
     *
     * @param b1
     * @param b2
     * @return
     */
    public static int compareTo(BigDecimal b1, double b2) {
        return b1.compareTo(new BigDecimal(String.valueOf(b2)));
    }

    /**
     * double 与 double  比较大小
     * <p>
     * int a = bigdemical.compareTo(bigdemical2)
     * //a = -1,表示bigdemical小于bigdemical2；
     * //a = 0,表示bigdemical等于bigdemical2；
     * //a = 1,表示bigdemical大于bigdemical2；
     *
     * @param b1
     * @param b2
     * @return
     */
    public static int compareTo(double b1, double b2) {
        return new BigDecimal(String.valueOf(b1)).compareTo(new BigDecimal(String.valueOf(b2)));
    }


    /*************************************** BigDecimal 运算结束 ******************************************************************/

    /**
     * 处理转换T,P字符串
     *
     * @param amount
     */
    public static String formatCalcu(Double amount) {
        String str = "0 T";
        if (amount.doubleValue() > 0) {
            if (amount.doubleValue() <= 999) {
                str = amount + " T";
            } else {
                BigDecimal result = CommonUtil.getBigDecimalZeros(CommonUtil.getDivide(amount, 1024, 2, RoundingMode.HALF_UP), 2);
                str = result + " P";
            }
        }
        return str;
    }

    /**
     * 将图片路径截取为保存到数据库的路径
     *
     * @param image
     * @return
     */
    public static String getDataBaseImageUrl(String image) {
        if (isEmpty(image)) {
            return "";
        } else if (image.startsWith("http")) {
            String mid = image.substring(image.lastIndexOf("//") + 2, image.length());
            image = mid.substring(mid.indexOf("/") + 1, mid.length());
            //image = image.substring(image.lastIndexOf(".com/") + ".com/".length(), image.length());

        }
        if (image.contains("?")) {
            image = image.substring(0, image.indexOf("?"));
        }
        return image;
    }

    /**
     * 隐藏手机号中间4位
     *
     * @param phone
     * @return
     */
    public static String getHidePartPhone(String phone) {
        if (phone.matches(RegxUtil.REGX_PHONE)) {
            phone = phone.substring(0, 3) + "****" + phone.substring(7);
        }
        return phone;
    }

    /**
     * 昵称隐藏手机号
     *
     * @param nickName
     * @return
     */
    public static String getHideNickName(String nickName) {
        Pattern pattern = compile(RegxUtil.REGX_PHONE);
        // 创建匹配给定输入与此模式的匹配器。
        Matcher matcher = pattern.matcher(nickName);

        String phone = "";
        //查找字符串中是否有符合的子字符串
        while (matcher.find()) {
            phone = matcher.group();
        }
        if (!StringUtils.isEmpty(phone)) {
            nickName = nickName.replace(phone, getHidePartPhone(phone));
        }
        return nickName;
    }

    /**
     * 随机生成大写字母字符串
     *
     * @param count
     * @return
     */
    public static String getRandCode(int count) {

        //ASCII 65-90 A-Z 97-122 a-z
        List<Integer> list = new ArrayList<Integer>();

        for (int i = 65; i <= 90; i++) {

            list.add(i);
        }
        char[] chars = new char[list.size()];
        for (int i = 0; i < chars.length; i++) {
            int value = list.get(i).intValue();

            chars[i] = (char) value;


        }
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        int len = chars.length;
        for (int i = 0; i < count; ++i) {

            int index = r.nextInt(len);
            sb.append(chars[index]);

        }
        return sb.toString();
    }


    /**
     * 去掉BigDecimal后无用的零
     *
     * @param num
     * @return
     */
    public static BigDecimal subTrailingZeros(BigDecimal num) {
        String str = num.stripTrailingZeros().toPlainString();
        return new BigDecimal(str);
    }


    /**
     * 判断时间是否为当前时间
     *
     * @param time
     * @return
     */
    public static boolean isThisTime(String time) throws Exception {

        // 格式化时间
        String str_1 = DateFormatUtil.DATE_FORMAT_STR_YMDHMS;

        SimpleDateFormat sdf_1 = new SimpleDateFormat(str_1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date date = sdf_1.parse(time);

        String param = sdf.format(date);//参数时间

        String now = sdf.format(new Date());//当前时间

        if (param.equals(now)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 英文大写+数字随机组合字符串
     *
     * @return
     */
    public static String getCodeStr(int n) {
        if (n < 1) {
            throw new CustomException("parameter Must be greater than 0 ");
        }

        String REGX_CODE = "^(?![0-9]+$)(?![A-Za-z]+$)[0-9A-Z]{1,18}$";

        String str = "01234abcdefghjklm56789npqrstuvwxyz01234ABCDEFGH56789JKLMNPQRS56789TUVWXYZ01234";

        StringBuilder sb = new StringBuilder();

        char[] chars = str.toCharArray();

        String code;

        do {
            sb.setLength(0);
            for (int i = 0; i < n; i++) {
                int index = new Random().nextInt(chars.length);
                sb.append(chars[index]);
            }
            code = sb.toString();
        } while (!code.matches(REGX_CODE));

        return code;
    }


    /**
     * List<String>去重---无需new
     *
     * @param list
     * @return
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    /**
     * 去掉集合中的null值
     * <p>
     * Not showing null elements
     *
     * @param list
     * @return
     */
    public static void removeNull(List list) {
        // 去掉集合中的空元素
        list.removeAll(Collections.singleton(null));
    }

    /**
     * map转参数字符串
     *
     * @param params
     * @return
     */
    public static String mapToStrParam(Map<String, Object> params) {
        Iterator<String> keys = params.keySet().iterator();
        String str = "";

        while (true) {
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = params.get(key);
                if (value instanceof String[]) {
                    String[] vals = (String[]) value;

                    for (int i = 0; i < vals.length; ++i) {
                        str = str + key + "=" + vals[i] + "&";
                    }
                } else {
                    str = str + key + "=" + value + "&";
                }
            }

            if (str.endsWith("&")) {
                str = str.substring(0, str.length() - 1);
            }

            return str;
        }
    }


//    /**
//     * 校验是否是最新版本
//     *
//     * @param appVersion 当前版本号,判断是否兼容最新版本API接口
//     * @param deviceType
//     * @return
//     * @throws Exception
//     */
//    public static boolean getVersionCompare(String appVersion, String deviceType) throws Exception {
//        boolean isNew = false;
//        // 查询APP版本信息
//        ApiVersionService apiVersionService = (ApiVersionService) SpringUtils.getBean("apiVersionServiceImpl");
//
//        Map<String, Object> versionData = apiVersionService.queryAppVersion(deviceType);
//        if (!isEmpty(versionData)) {
//            String compatVersionCode = getStringFromMap("compatVersionCode", "1000", versionData);// 默认最大
//            int compare = compareVersion(appVersion, compatVersionCode);
//            if (compare >= 0) {     // 已经是最新版本
//                isNew = true;
//            }
//        }
//
//        return isNew;
//    }


    /**
     * 版本号比较
     *
     * @param v1
     * @param v2
     * @return 0代表相等，1代表左边大，-1代表右边大
     * 举例：
     * Utils.compareVersion("1.0.358_20180820090554","1.0.358_20180820090553")=1
     * <p>
     * Utils.compareVersion("1.0.360","1.0.358_20180820090553")=1
     * <p>
     * Utils.compareVersion("1.0.358.2","1.0.358_20180820090553")=-1
     * <p>
     * Utils.compareVersion("1.0.0.2","1.0.1")=-1
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
                && (diff = Long.parseLong(version1Array[index])
                - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 解析出url请求的路径，包括页面
     *
     * @param strURL url地址
     * @return url路径
     */
    public static String getUrlPath(String strURL) {
        if (strURL.indexOf("?") < 0) {
            return strURL;
        }
        String path = null;// 结果--初始值 为空
        String[] arrSplit = null;// 截取所需数据 仓库数组--初始值 为空

        strURL = strURL.trim().toLowerCase();//参数-删除 开始和结束 的 空格，如有大写 变成小写，没有不做改变

        arrSplit = strURL.split("[?]");//截取 参数 以问号作为参照物 把参数分成两半 --放入仓库数组
        if (strURL.length() > 0) {//如果参数的 位数 大于零
            if (arrSplit.length > 1) {//如果仓库数组 位数 大于1，就是有两个数据
                if (arrSplit[0] != null) {//如果仓库数组 的第一个 数据不为空
                    path = arrSplit[0];//赋值
                }
            }
        }

        return path;
    }

    /**
     * 从资源文件获取短信内容---模板类型
     *
     * @param type   业务类型
     * @param params 参数
     * @return
     * @throws Exception
     */
    public static String getMsg(int type, Map<String, String> params, Locale locale) throws Exception {
        String templateName = "sms_" + type;
        String message = LangUtil.getInfoByKey(templateName, locale);
        if (StringUtils.isEmpty(message)) {
            throw new Exception("短信模板不存在!");
        }

        Pattern pattern = compile("\\{\\S+}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String param = matcher.group();
            String key = param.replace("{", "").replace("}", "");
            String value = params.get(key);

            message = message.replace("{" + key + "}", value);
        }

        return message;
    }

    /**
     * 获取阿里云短信模板编号---通知短信
     *
     * @param type
     * @param locale
     * @return
     * @throws Exception
     */
    public static String getSmsNoticeCode(int type, Locale locale) throws Exception {
        String templateName = "sms_notice_code_" + type;
        if (isEmpty(locale)) {
            String lang = "zh";
            String value = "CN";
            locale = new Locale(lang, value);
        }
        return LangUtil.getInfoByKey(templateName, locale);
    }

    /**
     * 获取阿里云短信模板编号
     *
     * @param type
     * @param locale
     * @return
     * @throws Exception
     */
    public static String getSmsCode(int type, Locale locale) throws Exception {
        String templateName = "sms_code_" + type;
        return LangUtil.getInfoByKey(templateName, locale);
    }

    /**
     * 获取阿里云市场短信模板编号
     *
     * @param type
     * @param locale
     * @return
     * @throws Exception
     */
    public static String getAliyunSmsCode(int type, Locale locale) throws Exception {
        String templateName = "aliyun_sms_code_" + type;
        return LangUtil.getInfoByKey(templateName, locale);
    }

    /**
     * 获取指定HTML标签的指定属性的值
     *
     * @param source  要匹配的源文本
     * @param element 标签名称
     * @param attr    标签的属性名称
     * @return 属性值列表
     */
    public static List<String> match(String source, String element, String attr) {
        List<String> result = new ArrayList<String>();
        String reg = "<" + element + "[^<>]*?\\s" + attr + "=['\"]?(.*?)['\"]?\\s.*?>";
        Matcher m = Pattern.compile(reg).matcher(source);
        while (m.find()) {
            String r = m.group(1);
            result.add(r);
        }
        return result;
    }

    /**
     * 两个数之间取随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int i = random.nextInt(max) % (max - min + 1) + min;
        return i;
    }


    /**
     * 截取指定长度的字数
     *
     * @param text
     * @param num
     * @return
     */
    public static String cutTxt(String text, Integer num) {

        try {
            if (StringUtils.isEmpty(text) || num < 1) {
                return "";
            }

            int length = text.length();
            if (length > num) {
                text = text.substring(0, num) + "...";
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return text;
    }

    /**
     * 手机号隐藏中间四位
     *
     * @param phone
     * @return
     */
    public static String hidePartPhone(String phone) {
        try {
            phone = phone.substring(0, 3) + "****" + phone.substring(7);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return phone;
    }

    /**
     * 隐藏银行卡号
     *
     * @param account
     * @return
     */
    public static String hidePartBankAccount(String account) {
        try {
            int length = account.length();
            if (length < 4) {
                return account;
            }

            int n = length / 4;
            int m = length % 4;
            if (m == 0) {
                n = n - 1;
            }

            String temp = "";
            for (int i = 0; i < n; i++) {
                temp += "**** ";
            }

            temp += "<span>" + account.substring(length - 4) + "</span>";

            return temp;

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return account;
    }

    /**
     * 将图片路径截取为保存到数据库的路径(批量)
     *
     * @param path
     * @return
     */
    public static String getStringFromMultisUrl(String path) {


        String result = "";
        if (StringUtils.isEmpty(path)) {
            return result;
        }
        String[] split = path.split(",");
        int len = split.length;
        for (int i = 0; i < len; i++) {
            String url = split[i];
            String imageUrl = getDataBaseImageUrl(url);
            if (i != 0 || i != len - 1) {
                result += imageUrl + ",";
            } else {
                result += imageUrl;
            }


        }
        return result;
    }


    /**
     * 判断字符串是否是emoji表情符号
     *
     * @param text
     * @return
     */
    public static boolean getIsEmoji(String text) {
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int codePoint = Character.codePointAt(text, i);
            //使用charcode而不是直接用char是因为有些字符需要两个char来表示 lowsurragate  highsurrgate
            boolean b = isEmojiCharacter(codePoint);
            if (b) {
                return true;
            }
        }
        return false;
    }


    public static boolean isEmojiCharacter(int codePoint) {
        return (0x0080 <= codePoint && codePoint <= 0x02AF) ||
                (0x0300 <= codePoint && codePoint <= 0x03FF) ||
                (0x0600 <= codePoint && codePoint <= 0x06FF) ||
                (0x0C00 <= codePoint && codePoint <= 0x0C7F) ||
                (0x1DC0 <= codePoint && codePoint <= 0x1DFF) ||
                (0x1E00 <= codePoint && codePoint <= 0x1EFF) ||
                (0x2000 <= codePoint && codePoint <= 0x209F) ||
                (0x20D0 <= codePoint && codePoint <= 0x214F) ||
                (0x2190 <= codePoint && codePoint <= 0x23FF) ||
                (0x2460 <= codePoint && codePoint <= 0x25FF) ||
                (0x2600 <= codePoint && codePoint <= 0x27EF) ||
                (0x2900 <= codePoint && codePoint <= 0x29FF) ||
                (0x2B00 <= codePoint && codePoint <= 0x2BFF) ||
                (0x2C60 <= codePoint && codePoint <= 0x2C7F) ||
                (0x2E00 <= codePoint && codePoint <= 0x2E7F) ||
                (0xA490 <= codePoint && codePoint <= 0xA4CF) ||
                (0xE000 <= codePoint && codePoint <= 0xF8FF) ||
                (0xFE00 <= codePoint && codePoint <= 0xFE0F) ||
                (0xFE30 <= codePoint && codePoint <= 0xFE4F) ||
                (0x1F000 <= codePoint && codePoint <= 0x1F02F) ||
                (0x1F0A0 <= codePoint && codePoint <= 0x1F0FF) ||
                (0x1F100 <= codePoint && codePoint <= 0x1F64F) ||
                (0x1F680 <= codePoint && codePoint <= 0x1F6FF) ||
                (0x1F910 <= codePoint && codePoint <= 0x1F96B) ||
                (0x1F980 <= codePoint && codePoint <= 0x1F9E0);
    }

    /**
     * 获取客户端真实ip
     *
     * @param request
     */
    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteHost();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(0, ip.indexOf(",")).trim();
        }
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 判断数组是否包含某个字符串---循环遍历效率最高
     * <p>
     * 也可用 new ArrayList<>(Arrays.asList(arr)).contains(v)
     * 或   ArrayUtils.contains(arr, v)
     *
     * @param arr
     * @param v
     * @return
     */
    public static boolean arrHasString(String[] arr, String v) {
        for (String s : arr) {
            if (s.equals(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断集合是否包含某个字符串---循环遍历效率最高
     * <p>
     *
     * @param list
     * @param v
     * @return
     */
    public static boolean listHasString(List<String> list, String v) {
        for (String s : list) {
            if (s.equals(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断数组是否包含某个Integer
     *
     * @param arr
     * @param v
     * @return
     */
    public static boolean arrHasInteger(Integer[] arr, Integer v) {
        for (Integer s : arr) {
            if (s.equals(v)) {
                return true;
            }
        }
        return false;
    }

    public static boolean useList(String[] arr, String targetValue) {
        return new ArrayList<>(Arrays.asList(arr)).contains(targetValue);
    }

    /**
     * 获取全路径链接文件大小
     *
     * @param fileUrl
     * @return
     * @throws Exception
     */
    public static Integer getUrlFileSize(String fileUrl) throws Exception {
        Integer size;
        URL url = new URL(fileUrl);
        URLConnection conn = url.openConnection();
        size = conn.getContentLength();
        System.out.println("size = " + size);
        conn.getInputStream().close();

        return size;
    }


    /**
     * 过滤内容中HTML指定标签,获取中文内容
     *
     * @param htmlContent
     * @return
     */
    public static String getHtmlSplit(String htmlContent, String s) {
        if (isEmpty(s)) {
            return htmlContent;
        }

        ArrayList<String> splitList = new ArrayList<>(Arrays.asList(s.split(",")));
        if (splitList.contains("rnt")) {
            // 过滤"\r\n,\t,\r,\n"
            Pattern p = compile("(\r\n|\r|\n|\n\r|\t)");
            Matcher m = p.matcher(htmlContent);
            htmlContent = m.replaceAll("");
        }
        if (splitList.contains("a")) {
            htmlContent = htmlContent.replaceAll("<a[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</a>", "");
        }
        if (splitList.contains("img")) {
            htmlContent = htmlContent.replaceAll("<img[^>]*>", "");
        }
        if (splitList.contains("p")) {
            htmlContent = htmlContent.replaceAll("<p[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</p>", "");
        }
        if (splitList.contains("br")) {
            htmlContent = htmlContent.replaceAll("<br[^>]*>", "");
        }
        if (splitList.contains("div")) {
            htmlContent = htmlContent.replaceAll("<div[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</div>", "");
        }
        if (splitList.contains("iframe")) {
            htmlContent = htmlContent.replaceAll("<iframe[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</iframe>", "");
        }
        if (splitList.contains("span")) {
            htmlContent = htmlContent.replaceAll("<span[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</span>", "");
        }
        if (splitList.contains("h1")) {
            htmlContent = htmlContent.replaceAll("<h1[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</h1>", "");
        }
        if (splitList.contains("a")) {
            htmlContent = htmlContent.replaceAll("<h2[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</h2>", "");
        }
        if (splitList.contains("h3")) {
            htmlContent = htmlContent.replaceAll("<h3[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</h3>", "");
        }
        if (splitList.contains("h4")) {
            htmlContent = htmlContent.replaceAll("<h4[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</h4>", "");
        }
        if (splitList.contains("h5")) {
            htmlContent = htmlContent.replaceAll("<h5[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</h5>", "");
        }
        if (splitList.contains("font")) {
            htmlContent = htmlContent.replaceAll("<font[^>]*>", "");
            htmlContent = htmlContent.replaceAll("</font>", "");
        }
        if (splitList.contains("nbsp")) {
            htmlContent = htmlContent.replaceAll("&nbsp", "");
        }
        return htmlContent;
    }


    /**
     * 获取HTML内容中所有图片
     *
     * @param htmlStr
     * @return
     */
    public static Set<String> getImgStr(String htmlStr) {
        if (isEmpty(htmlStr)) {
            return null;
        }
        Set<String> pics = new HashSet<>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据 不懂的qq1023732997
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

}
