package com.pay.third.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description: 公共页面
 * @author: tjy-admin
 * @date: 2021/8/1 22:58
 * @Version: 1.0.0
 */
@Validated    // 校验单个参数必须将@Validated加在类上
@Controller
@RequestMapping("/common")
public class CommonController {

    private final static Logger LOG = LoggerFactory.getLogger(CommonController.class);


    /**
     * 系统维护页面
     *
     * @return
     */
    @RequestMapping(value = "/tenance.html")
    public String tenance() {
        return "error/tenance";
    }

    /**
     * 页面找不到
     *
     * @return
     */
    @RequestMapping(value = "/404.html")
    public String error404() {
        return "error/404";
    }

    /**
     * 无权限
     *
     * @return
     */
    @RequestMapping(value = "/401.html")
    public String error401() {
        return "error/404";
    }

    /**
     * 错误页面
     *
     * @return
     */
    @RequestMapping(value = "/500.html")
    public String error500() {
        return "error/500";
    }



}
