/*
 * @(#)HttpUtils.java    Created on 2011-2-18
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.clj.panda.temp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.benhe.minax.entity.enums.TerminalType;
import com.benhe.todo.common.enums.TodoCode;
import com.benhe.todo.pc.helper.ApplicationConfigHelper;
import com.benhe.todo.pc.model.LoginUser;
import com.benhe.wolf.util.SecurityUtils;
import com.benhe.wolf.util.StringUtils;

/**
 * 提供访问HTTP服务的工具类.
 * 
 * @author huangwq
 * @version $Revision$, $Date$
 */
public abstract class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    // 默认连接超时时间: 30s
    private static final int DEFAULT_CONNECTION_TIMEOUT = 1000 * 20;  //unit:ms
    // 默认读取超时时间: 30s
    private static final int DEFAULT_READ_TIMEOUT = 1000 * 20;  //unit:ms

    private static final String DEFAULT_ENCODE = "UTF-8";

    public static String requestURL(String url, int connectionTimeout, int readTimeout) throws IOException {
        return requestURL(url, DEFAULT_ENCODE, connectionTimeout, readTimeout);
    }

    /**
     * 请求指定地址的服务.,返回网页内容，使用UTF-8编码
     * 
     * @param url
     *            URL地址
     * @return 请求服务返回的结果
     */
    public static String requestURL(String url) throws IOException {
        return requestURL(url, DEFAULT_ENCODE);
    }

    /**
     * 请求指定地址的服务.,返回网页内容
     * 
     * @param url
     * @param encoding
     *            编码
     * @return
     */
    public static String requestURL(String url, String encoding) throws IOException {
        return requestURL(url, encoding, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public static String requestURL(String url, String encoding, int connectionTimeout, int readTimeout)
            throws IOException {
        String result = null;
        BufferedReader reader = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            System.out.println(connection.getClass().getName());
            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding), 8 * 1024);
            String line = null;
            StringBuffer buffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
            result = buffer.toString();
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e1) {
                }
            }
        }
        return result;
    }

    /**
     * 下载文件，输出流到file里
     * 
     * @param downloadUrl
     * @param file
     * @return
     */
    public static void downloadURLToFile(String downloadUrl, File file) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists() || !parentFile.isDirectory()) {
                parentFile.mkdirs();
            }
            URLConnection connection = new URL(downloadUrl).openConnection();
            connection.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
            connection.setReadTimeout(1000 * 60 * 10);

            connection.connect();
            in = connection.getInputStream();
            out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bs = new byte[1024];
            int bytesReaded = 0;
            while ((bytesReaded = in.read(bs)) != -1) {
                out.write(bs, 0, bytesReaded);
            }
        }
        catch (Exception e) {
            file.delete();
            throw e;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    public static void getImage(String downloadUrl, File file) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        try {
            File parentFile = file.getParentFile();
            if (!parentFile.exists() || !parentFile.isDirectory()) {
                parentFile.mkdirs();
            }
            URLConnection connection = new URL(downloadUrl).openConnection();
            connection.setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT);
            connection.setReadTimeout(1000 * 60 * 10);

            connection.connect();
            in = connection.getInputStream();
            out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] bs = new byte[1024];
            int bytesReaded = 0;
            while ((bytesReaded = in.read(bs)) != -1) {
                out.write(bs, 0, bytesReaded);
            }
        }
        catch (Exception e) {
            file.delete();
            throw e;
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                }
                catch (IOException e) {
                }
            }
        }
    }

    public static String requestURLPost(String url, Map<String, String> paramsMap) throws IOException {
        return requestURLPost(url, paramsMap, DEFAULT_ENCODE, 12000, 12000);
    }

    public static String requestURLPost(String url, Map<String, String> paramsMap, String encoding,
            int connectionTimeout, int readTimeout) throws IOException {
        if (encoding == null) {
            encoding = DEFAULT_ENCODE;
        }
        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);// 参数用utf8格式传输
        // client.getParams().setParameter(HttpMethodParams., encoding);
        HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
        // 设置连接超时时间(单位毫秒)
        managerParams.setConnectionTimeout(connectionTimeout);
        // 设置读数据超时时间(单位毫秒)
        managerParams.setSoTimeout(readTimeout);
        PostMethod m = new PostMethod(url);
        // m.setRequestHeader("client-agent", header);
        for (Map.Entry<String, String> e : paramsMap.entrySet()) {
            m.addParameter(e.getKey(), e.getValue());
        }
        client.executeMethod(m);
        return m.getResponseBodyAsString();
    }

    public static PostMethod requestURLPostAndGetStreamNoToken(String url, Map<String, String> paramsMap)
            throws IOException {
        url = ApplicationConfigHelper.getApiUrl() + url;
        for (String key : paramsMap.keySet()) {
            // logger.info(key + "=" + paramsMap.get(key));
        }
        // logger.info("url===" + url);
        return requestURLPostAndGetStream(url, paramsMap, DEFAULT_ENCODE, 12000, 12000);
    }

    public static PostMethod requestURLPostAndGetStream(String url, Map<String, String> paramsMap) throws IOException {
        url = ApplicationConfigHelper.getApiUrl() + url;
        putToken(paramsMap);
        for (String key : paramsMap.keySet()) {
            // logger.info(key + "=" + paramsMap.get(key));
        }
        // logger.info("url===" + url);
        return requestURLPostAndGetStream(url, paramsMap, DEFAULT_ENCODE, 12000, 12000);
    }

    public static PostMethod requestURLPostAndGetStream(String url, Map<String, String> paramsMap, String encoding,
            int connectionTimeout, int readTimeout) throws IOException {
        if (encoding == null) {
            encoding = DEFAULT_ENCODE;
        }
        HttpClient client = new HttpClient();
        client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);// 参数用utf8格式传输
        // client.getParams().setParameter(HttpMethodParams., encoding);
        HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
        // 设置连接超时时间(单位毫秒)
        managerParams.setConnectionTimeout(connectionTimeout);
        // 设置读数据超时时间(单位毫秒)
        managerParams.setSoTimeout(readTimeout);
        PostMethod m = new PostMethod(url);
        // m.setRequestHeader("client-agent", header);
        for (Map.Entry<String, String> e : paramsMap.entrySet()) {
            m.addParameter(e.getKey(), e.getValue());
        }
        client.executeMethod(m);
        return m;
    }

    public static String uploadImage(String url, String imageKey, String imagePath, Map<String, String> paramsMap) {
        String result = null;
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", DEFAULT_ENCODE);
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            StringBuffer params = new StringBuffer();
            boolean isFirst = true;
            for (String key : paramsMap.keySet()) {
                if (isFirst) {
                    isFirst = false;
                }
                else {
                    params.append("&");
                }
                params.append(key + "=" + paramsMap.get(key));
            }
            params.append("&").append(imageKey).append("=");
            byte[] headParams = params.toString().getBytes();
            ds.write(headParams, 0, headParams.length);
            // 输出图片
            FileInputStream fStream = new FileInputStream(imagePath);
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
                /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            fStream.close();
            ds.flush();
            /* 取得Response内容 */
            InputStream is = connection.getInputStream();
            StringBuffer b = new StringBuffer();
            int ch;
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            result = b.toString().trim();
            is.close();
            ds.close();
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void putToken(Map<String, String> paramsMap) {
        List<String> list = new ArrayList<String>();
        paramsMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        paramsMap.put("terminalType", TerminalType.PC.getValue() + "");
        LoginUser user = LoginUser.getInstance();
        paramsMap.put("token", user.getLoginResp().getToken());
        if (!paramsMap.containsKey("employeeId")) {
            paramsMap.put("employeeId", user.getLoginResp().getId());
        }
        if (!paramsMap.containsKey("companyId")) {
            paramsMap.put("companyId", user.getLoginResp().getCompanyId());
        }
        for (String key : paramsMap.keySet()) {
            // logger.info("key==" + key + "--value==" + paramsMap.get(key));
            list.add(paramsMap.get(key));
        }
        Collections.sort(list);
        StringBuilder signatureBuilder = new StringBuilder(40);
        for (String p : list) {
            signatureBuilder.append(p);
        }
        // logger.info("signature-builder==" + signatureBuilder.toString());
        paramsMap.put("signature", SecurityUtils.encodeByMD5(signatureBuilder.toString()));
        paramsMap.remove("token");
    }

    public static JSONObject httpRequest(String pUrl, Map<String, String> paramsMap) {
        String url = ApplicationConfigHelper.getApiUrl() + pUrl;
        // logger.info(url);
        String httpResult = null;
        if (paramsMap == null) {
            paramsMap = new HashMap<String, String>();
        }
        // --------------------------以下内容用于http 验证------------------------------
        putToken(paramsMap);
        // --------------------------以上内容用于http 验证------------------------------
        try {
            httpResult = HttpUtils.requestURLPost(url, paramsMap);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(httpResult)) {
            // message = "网络连接异常";
            return null;
        }
        JSONObject json = JSON.parseObject(httpResult);
        if (!isHttpRequestSuccess(json)) {
            return null;
        }
        isHttpRequestSuccess(json);
        return json;
    }

    protected static boolean isHttpRequestSuccess(JSONObject json) throws JSONException {
        int code = json.getInteger("code");
        boolean isSuccess = false;
        if (TodoCode.SUCCESS.getValue() == code) {
            isSuccess = true;
        }
        long serverTime = json.getLong("serverTime");
        String message = TodoCode.valueOf(code).getName();
        logger.info("code:" + message);
        return isSuccess;
    }

    public static void main(String[] args) throws Exception {
        // Login login = new Login();
        // login.login("18258235633", "12345");
        // Map<String, String> paramsMap = new HashMap<String, String>();
        // paramsMap.put("phone", "18258235633");
        // paramsMap.put("mode", "1");
        // logger.info(ApplicationConfigHelper.getApiUrl() + "sms/sendVerifyCodeSms.htm");
        // paramsMap.clear();
        // // 登录
        // paramsMap.put("phone", "18258235633");
        // paramsMap.put("smsVerifyCode", "12345");

        HttpUtils.requestURL("http://www.baidu.com");
    }
}
