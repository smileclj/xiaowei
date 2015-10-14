package com.clj.panda.util;

import com.alibaba.fastjson.JSON;
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
     *
     * @param url 请求地址
     * @param params 请求参数 json格式
     * @param encoding 编码
     * @param connectionTimeout 连接超时时间
     * @param readTimeout 响应超时时间
     * @param isPost 是否post提交
     * @return
     * @throws IOException
     */
    public static String request(String url, String params,String encoding, int connectionTimeout, int readTimeout,boolean isPost){
        String result = null;
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

            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),DEFAULT_ENCODE),true);
            out.print(params);

            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding), 8 * 1024);
            String line = null;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (IOException e) {
            logger.error("get请求失败",e);
        }
        finally {
            boolean success = true;
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException e) {
                    success = false;
                }
            }
            if(out != null){
                try{
                    out.close();
                }catch (Exception e){
                    success = false;
                }
            }
            if(!success){
                return null;
            }
        }
        return result;
    }

//    public static String get(String url, String params){
//        return get(url,params,DEFAULT_ENCODE,DEFAULT_CONNECTION_TIMEOUT,DEFAULT_READ_TIMEOUT);
//    }


}
