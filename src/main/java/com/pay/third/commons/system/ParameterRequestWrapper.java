package com.pay.third.commons.system;

import com.pay.third.commons.configuration.DefaultConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * 增加token
 *
 * @author Tangshenghao
 * @version 1.0.0
 * @date 2019年9月3日 下午2:09:22
 */
public class ParameterRequestWrapper extends HttpServletRequestWrapper {
    private final static Logger LOG = LoggerFactory.getLogger(ParameterRequestWrapper.class);
    private Map<String, String[]> params = new HashMap<String, String[]>();

    public ParameterRequestWrapper(HttpServletRequest request) {
        super(request);
        // 解密
        this.decrypt(request);
        // 添加Header公共参数
        this.addHeaderParams(request);
    }

    /**
     * 解密参数
     *
     * @param request
     */
    private void decrypt(HttpServletRequest request) {
        Map<String, String[]> p = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : p.entrySet()) {
            String key = entry.getKey();
            if ("aes".equals(key)) {
                String value = entry.getValue()[0];
                // 解密
                try {
                    String str = AES.decrypt(value, DefaultConfiguration.getAesPwd());

                    String[] tempParams = str.split("&");
                    for (String s : tempParams) {
                        String k = s.substring(0, s.indexOf("="));
                        String v = s.substring(s.indexOf("=") + 1);

                        // 已经存在，则添加进去
                        if (this.params.containsKey(k)) {
                            String[] vs = this.params.get(k);
                            List<String> list = new ArrayList<String>(Arrays.asList(vs));
                            list.add(v);
                            String[] newArray = new String[list.size()];

                            this.params.put(k, list.toArray(newArray));
                        } else {
                            this.params.put(k, new String[]{v});
                        }
                    }

                } catch (Exception e) {
                    LOG.error("解密参数，异常消息：{}", e.getMessage(), e);
                }
            } else {
                this.params.put(key, entry.getValue());
            }
        }


    }

    public void addHeaderParams(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            this.params.put("token", new String[]{token});
        } else {
            this.params.put("token", new String[]{""});
        }
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Vector<String>(params.keySet()).elements();
    }

    @Override
    public String getParameter(String name) {//重写getParameter，代表参数从当前类中的map获取
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return this.params;
    }

    @Override
    public String[] getParameterValues(String name) {
        return this.params.get(name);
    }
}
