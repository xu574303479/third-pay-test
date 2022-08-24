package com.pay.third.controller;

import com.pay.third.commons.system.SpringUtils;
import com.pay.third.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @author: Xu
 * @Date: 2022-08-24 12:02
 * @Version: 1.0.0
 */
@Validated    // 校验单个参数必须将@Validated加在类上
@Controller
@RequestMapping("/")
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    /**
     * 首页
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/index.html")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        String templateName = "index";
        try {
            // 查询首页数据
            RedisService redisService = (RedisService) SpringUtils.getBean("redisServiceImpl");
            RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBean("redisTemplate");
            Object obj = redisService.queryValue("system_params_value-RECHARGE_TOTAL", redisTemplate);
            System.out.println("obj = " + obj);

        } catch (Exception e) {
            LOG.error("首页异常,url:/index.html,错误信息:{}", e.getMessage(), e);
            return "forward:/common/500.html";
        }
        return templateName;
    }

}
