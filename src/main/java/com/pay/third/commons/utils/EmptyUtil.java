package com.pay.third.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * 判断对象是否为空
 *
 * @author lsy
 */
public class EmptyUtil {


    /**
     * 判断对象为空
     *
     * @param obj 对象名
     * @return 是否为空
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
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

    /**
     * 判断对象不为空
     *
     * @param obj 对象名
     * @return 是否不为空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }


    /**
     * 判断指定实体类对象是否为空
     *
     * @param object
     * @return
     */
    public static boolean objIsEmpty(Object object) {
        boolean flag = false; //定义返回结果，默认为true

        Class clazz = object.getClass();
        String clazzName = clazz.getName();

//        if (DomainNameEnum.PAGINGLIST.getName().equals(clazzName)) {
//            PagingList result = (PagingList) object;
//            if (null == result || null == result.getResultList() || result.getResultList().size() < 1) {
//                flag = true;
//            }
//        } else {
//            flag = false;
//        }

        return flag;
    }


    /**
     * 判断对象是否为空，且对象的所有属性都为空
     * ps: boolean类型会有默认值false 判断结果不会为null 会影响判断结果
     * 序列化的默认值也会影响判断结果
     *
     * @param object
     * @return
     */
    public static boolean objCheckIsNull(Object object) {
        Class clazz = (Class) object.getClass(); // 得到类对象
        Field fields[] = clazz.getDeclaredFields(); // 得到所有属性
        boolean flag = true; //定义返回结果，默认为true
        for (Field field : fields) {
            field.setAccessible(true);
            Object fieldValue = null;
            try {
                fieldValue = field.get(object); //得到属性值
                Type fieldType = field.getGenericType();//得到属性类型
                String fieldName = field.getName(); // 得到属性名
                System.out.println("属性类型：" + fieldType + ",属性名：" + fieldName + ",属性值：" + fieldValue);
            } catch (IllegalArgumentException e) {

            } catch (IllegalAccessException e) {

            }
            if (fieldValue != null) {  //只要有一个属性值不为null 就返回false 表示对象不为null
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * @Description:
     * @Auther: Work-PC
     * @date: 2019/8/23 11:56
     * @Version: 1.0.0
     */
    public enum DomainNameEnum {
        PAGINGLIST(1, "com.bentongchain.system.PagingList"),       // 分页对象

        ;

        // 属性类型
        private int type;
        // 属性类型名称
        private String name;

        // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
        DomainNameEnum(int type, String name) {
            this.type = type;
            this.name = name;
        }

        /**
         * 根据索引获取变量名称
         *
         * @param type
         * @return
         */
        public static String getName(int type) {
            for (DomainNameEnum c : DomainNameEnum.values()) {
                if (c.getType() == type) {
                    return c.name;
                }
            }
            return null;
        }

        /**
         * 是否有该类型
         *
         * @param name
         * @return
         */
        public static boolean contains(String name) {
            for (DomainNameEnum c : DomainNameEnum.values()) {
                if (c.name.equals(name)) {
                    return true;
                }
            }

            return false;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}