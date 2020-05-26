package com.lhz.diytomcat.util;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public static String getHttpBytes(String url, boolean gzip) {
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
            Map<String,String> requestHeaders = new HashMap<>();
            requestHeaders.put("Host", u.getHost()+":"+port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "lhz mini brower / java1.8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
