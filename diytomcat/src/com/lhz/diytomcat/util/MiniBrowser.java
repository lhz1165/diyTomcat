package com.lhz.diytomcat.util;

import java.io.*;
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
//    public static void main(String[] args) {
//        String url = "http://static.how2j.cn/diytomcat.html";
//        String httpString = getHttpString(url);
//        System.out.println(httpString);
//    }


    public static byte[] getContentBytes(String url) {
        return getContentBytes(url, false);
    }

    public static String sendRequestAndGetResponse(String url) {
        return sendRequestAndGetResponse(url, false);
    }

    public static String sendRequestAndGetResponse(String url, boolean gzip) {
        byte[] result = getContentBytes(url, gzip);
        if (null == result) {
            return null;
        }
        try {
            return new String(result, "utf-8").trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getHttpString(String url) {
        return new String(sendRequestAndGetResponseByte(url, false)).trim();
    }

    /**
     * 获取二进制的相应体
     *
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] getContentBytes(String url, boolean gzip) {
        byte[] response = sendRequestAndGetResponseByte(url, gzip);
        byte[] doubleReturn = "\r\n\r\n".getBytes();

        int pos = -1;
        for (int i = 0; i < response.length - doubleReturn.length; i++) {
            byte[] temp = Arrays.copyOfRange(response, i, i + doubleReturn.length);

            if (Arrays.equals(temp, doubleReturn)) {
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
     *
     * 首先发送请求然后
     * 返回二进制的 http 响应头
     *
     * @param url
     * @param gzip
     * @return
     */
    public static byte[] sendRequestAndGetResponseByte(String url, boolean gzip) {
        byte[] result = null;
        try {
            URL u = new URL(url);
            Socket client = new Socket();
            int port = u.getPort();
            if (-1 == port) {
                port = 80;
            }
            InetSocketAddress inetSocketAddress = new InetSocketAddress(u.getHost(), port);
            client.connect(inetSocketAddress, 1000);
            Map<String, String> requestHeaders = new HashMap<>();

            requestHeaders.put("Host", u.getHost() + ":" + port);
            requestHeaders.put("Accept", "text/html");
            requestHeaders.put("Connection", "close");
            requestHeaders.put("User-Agent", "how2j mini brower / java1.8");

            if (gzip) {

                requestHeaders.put("Accept-Encoding", "gzip");
            }

            String path = u.getPath();
            if (path.length() == 0) {

                path = "/";
            }

            String firstLine = "GET " + path + " HTTP/1.1\r\n";

            StringBuffer httpRequestString = new StringBuffer();
            httpRequestString.append(firstLine);
            Set<String> headers = requestHeaders.keySet();
            for (String header : headers) {
                String headerLine = header + ":" + requestHeaders.get(header) + "\r\n";
                httpRequestString.append(headerLine);
            }
            //把http请求写出去
            PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
            pWriter.println(httpRequestString);
            InputStream is = client.getInputStream();
            //读取响应
            result = readBytes(is);
            client.close();

        } catch (Exception e) {
            e.printStackTrace();
            result = e.toString().getBytes(StandardCharsets.UTF_8);
        }
        return result;
    }

    /**
     * 读取请求或者响应 然后把他们转化成字节数组
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] readBytes(InputStream is) throws IOException {
        int buffer_size = 1024;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[buffer_size];
        int len = 0;
        //从输入流
        while ((len = is.read(buffer)) != -1) {
            // 转化成buye[]
            baos.write(buffer, 0, len);
            if (len != buffer_size) {
                break;
            }
        }

        return baos.toByteArray();
    }
}
