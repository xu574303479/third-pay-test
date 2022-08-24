package com.pay.third.commons.system;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * @Description: 通过重写HttpServletRequestWrapper把request的保存下来，然后通过过滤器保存下来的request在填充进去
 * 解决 RequestBody 只能读取1遍请求数据流的问题
 * @author: My-PC
 * @Date: 2021-04-12 13:10
 * @Version: 1.0.0
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    /**
     * 存储body数据的容器
     */
    private byte[] body;

    public RequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        // 将body数据存储起来
        String bodyStr = getBodyString(request);

        // 添加Header公共参数
        bodyStr = this.addHeaderParams(bodyStr, request);
        if (StringUtils.isNotBlank(bodyStr)) {
            body = bodyStr.getBytes(Charset.defaultCharset());
        }
    }

    /**
     * 添加Header公共参数
     *
     * @param bodyStr 参数json字符串
     * @param request
     * @return
     */
    private String addHeaderParams(String bodyStr, HttpServletRequest request) {
        if (StringUtils.isBlank(bodyStr)) {
            return null;
        }
        JSONObject jsonParams = JSON.parseObject(bodyStr);
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            jsonParams.put("token", token);
        } else {
            jsonParams.put("token", "");
        }

        return jsonParams.toString(SerializerFeature.WriteMapNullValue);
    }


    /**
     * 获取请求Body
     *
     * @param request request
     * @return String
     */
    public String getBodyString(final ServletRequest request) {
        try {
            return inputStream2String(request.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求Body
     *
     * @return String
     */
    public String getBodyString() {
        final InputStream inputStream = new ByteArrayInputStream(body);

        return inputStream2String(inputStream);
    }

    /**
     * 将inputStream里的数据读取出来并转换成字符串
     *
     * @param inputStream inputStream
     * @return String
     */
    public static String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }
}
