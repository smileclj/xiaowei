package com.clj.panda.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
     * @param params 请求参数
     * @param encoding 编码
     * @param connectionTimeout 连接超时时间
     * @param readTimeout 响应超时时间
     * @return
     * @throws IOException
     */
    public static String get(String url, Map<String,Object> params,String encoding, int connectionTimeout, int readTimeout)
            throws IOException {
        String result = null;
        BufferedReader reader = null;
        try {
            URLConnection connection = new URL(url).openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

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
}
