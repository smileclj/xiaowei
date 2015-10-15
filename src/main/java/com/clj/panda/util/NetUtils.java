package com.clj.panda.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by lao on 2015/9/29.
 */
public class NetUtils {
    private static Logger logger = LoggerFactory.getLogger(NetUtils.class);

    // 默认连接超时时间: 30s
    private static final int DEFAULT_CONNECTION_TIMEOUT = 1000 * 6;
    // 默认读取超时时间: 30s
    private static final int DEFAULT_READ_TIMEOUT = 1000 * 6;
    // 默认编码
    private static final String DEFAULT_ENCODE = "UTF-8";

    /**
     * post请求
     *
     * @param url               请求地址
     * @param params            请求参数 a=1&b=1格式
     * @param encoding          编码
     * @param connectionTimeout 连接超时时间
     * @param readTimeout       响应超时时间
     * @return
     * @throws IOException
     */
    public static String post(String url, String params, String encoding, int connectionTimeout, int readTimeout) {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), encoding));
            out.print(params);
            out.flush();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding), 8 * 1024);
            String line = null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            logger.error("POST请求失败", e);
        } finally {
            boolean success = true;
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    success = false;
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    success = false;
                }
            }
            if (!success) {
                return null;
            }
        }
        return result;
    }

    public static String post(String url, String params){
        return post(url,params,DEFAULT_ENCODE,DEFAULT_CONNECTION_TIMEOUT,DEFAULT_READ_TIMEOUT);
    }

    public static String post(String url,Map<String,Object> params){
        return post(url, _transfer(params),DEFAULT_ENCODE,DEFAULT_CONNECTION_TIMEOUT,DEFAULT_READ_TIMEOUT);
    }

    /**
     * 内部方法 拼接参数
     * @param params
     * @return
     */
    private static String _transfer(Map<String,Object> params){
        StringBuilder joinParams = new StringBuilder();
        for(Map.Entry<String,Object> entry:params.entrySet()){
            joinParams.append("&").append(entry.getKey()).append("="+entry.getValue());
        }
        return joinParams.toString().substring(1);
    }

    /**
     * get请求
     *
     * @param url               请求地址
     * @param params            请求参数 a=1&b=1格式
     * @param encoding          编码
     * @param connectionTimeout 连接超时时间
     * @param readTimeout       响应超时时间
     * @return
     */
    public static String get(String url, String params, String encoding, int connectionTimeout, int readTimeout) {
        String result = "";
        BufferedReader in = null;

        if (params != null && !params.equals("")) {
            url = url + "?" + params;
        }

        try {
            URLConnection connection = new URL(url).openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.setConnectTimeout(connectionTimeout);
            connection.setReadTimeout(readTimeout);
            // 建立实际的连接
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding), 8 * 1024);
            String line = null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            logger.error("GET请求失败", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }

    public static String get(String url, String params){
        return get(url,params,DEFAULT_ENCODE,DEFAULT_CONNECTION_TIMEOUT,DEFAULT_READ_TIMEOUT);
    }

    public static String get(String url, Map<String,Object> params){
        return get(url,_transfer(params),DEFAULT_ENCODE,DEFAULT_CONNECTION_TIMEOUT,DEFAULT_READ_TIMEOUT);
    }

    /**
     *
     * @param url http://www.a.com?a=1&b=1
     * @return
     */
    public static String get(String url){
        return get(url, null, DEFAULT_ENCODE, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT);
    }

    public static void main(String[] args) {
        String url = "http://localhost:8888/test/testJson.htm";
        System.out.println(NetUtils.get(url,"id=1"));
        System.out.println(NetUtils.post(url,"id=1"));
    }
}
