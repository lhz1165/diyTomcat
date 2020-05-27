package com.lhz.diytomcat.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: lhz
 * @date: 2020/5/26
 * 这个浏览器会模拟发送 Http 协议的请求，
 * 并且获取完整的 Http 响应，通过这种方式，
 * 我们就可以更好的理解浏览器与服务器是如何通信
 *
 *
 * 1. getHttpBytes     返回二进制的 http 响应
 * 2. getHttpString    返回字符串的 http 响应
 * 3. getContentBytes  返回二进制的 http 响应内容 （可简单理解为去掉头的 html 部分）
 * 4. getContentString 返回字符串的 http 响应内容 （可简单理解为去掉头的 html 部分）
 * 5. 以上4个方法都增加了个 gzip 参数，可以获取压缩后的数据，便于后续学习 gzip 章节的理解和学习
 **/
public class MiniBrowser {


    public static byte[] getContentBytes(String url) {
        return null;
    }

    public static String getContentString(String url) {

    }

    public static String getContentString(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (null == result) {
            return null;
        }
        try {
            return new String(result,"utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 获取二进制的相应体
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = getHttpBytes(url,gzip);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        int pos = -1;
        for (int i = 0; i < response.length-doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);

            if(Arrays.equals(temp, doubleReturn)) {
                pos = i;
                break;
            }
        }
        if (-1 == pos) {
            return null;
        }

        pos += doubleReturn.length;

        byte[] result = Arrays.copyOfRange(response, pos, response.length);
        return result;
    }


    /**
     * 返回二进制的 http 响应头
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] getHttpBytes(String url, boolean gzip) {
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (-1 == port) {
                port = 80;
            }
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(),port);
            client.connect(inetSocketAddress,1000);
            //封装http请求头
            Map<String,String> requestHeaders = new HashMap<>();
            requestHeaders.put("Host", u.getHost()+":"+port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "lhz mini brower / java1.8");

            if (gzip) {
                requestHeaders.put("Accept-Encoding", "gzip");
            }
            String path = u.getPath();
            if (path.length() == 0) {
                path = "/";
            }
            //请求头
            String fristLine = "GET " + path + "HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(fristLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ": " + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }
            //封装好了 写出
            PrintWriter writer = new PrintWriter(client.getOutputStream());
            writer.println(httpRequestString);
            InputStream is = client.getInputStream();
            int buffer_size = 1024;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[buffer_size];
            while (true) {
                int length = is.read(buffer);
                if (-1 == length) {
                    break;
                }
            }
            int len = 0;
            while ((len=is.read(buffer)) != -1) {
                baos.write(buffer,0,len);
                if (len != buffer_size) {
                    break;
                }
            }
            result = baos.toByteArray();
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString().getBytes(StandardCharsets.UTF_8);
        }
        return result;
    }
}
