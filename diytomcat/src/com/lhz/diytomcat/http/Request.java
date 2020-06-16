package com.lhz.diytomcat.http;

import cn.hutool.core.util.StrUtil;
import com.lhz.diytomcat.Bootstrap;
import com.lhz.diytomcat.catalina.Context;
import com.lhz.diytomcat.catalina.Engine;
import com.lhz.diytomcat.catalina.Host;
import com.lhz.diytomcat.catalina.Service;
import com.lhz.diytomcat.util.MiniBrowser;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author: lhz
 * @date: 2020/6/2
 * 请求对象
 **/
public class Request {

    private String requestString;
    private String uri;
    private Socket socket;
    private Context context;
    private Service service;
    public Request(Socket socket,Service service) throws IOException {
        this.service = service;
        this.socket = socket;
        //把请求解析成字符串  http的请求头样式 GET /b/index.html HTTP1.1 ····
        this.requestString=parseHttpRequest();
        if (StrUtil.isEmpty(requestString)) {
            return;
        }
        //把请求解析成uri 字符串 /b/index.html
        this.uri=parseUri();
        //  context(/b,d:/.../b)
        this.context=parseContext();
        //  修正uri  /b/index.html----->/index.html
        if (!"/".equals(context.getPath())) {
            uri = StrUtil.removePrefix(uri, context.getPath());
        }
    }

    private String parseHttpRequest() throws IOException {
        InputStream is = this.socket.getInputStream();
        //读取请求构建成字符串 这里接受的是MiniBrowser
        //PrintWriter pWriter = new PrintWriter(client.getOutputStream(), true);
        byte[] bytes = MiniBrowser.readBytes(is);
        return new String(bytes, "utf-8");
    }

    private String parseUri() {
        String temp;
        temp = StrUtil.subBetween(requestString, " ", " ");
        if (!StrUtil.contains(temp, '?')) {
            return temp;
        }
        temp = StrUtil.subBefore(temp, '?', false);
        return temp;
    }

    public String getUri() {
        return uri;
    }

    public String getRequestString(){
        return requestString;
    }
    public Context getContext() {
        return context;
    }

    /**
     * 增加解析Context 的方法，
     * 通过获取uri 中的信息来得到 path.
     * 然后根据这个 path 来获取 Context 对象。
     * 如果获取不到，比如 /b/a.html, 对应的 path 是 /b, 是没有对应 Context 的，那么就获取 "/” 对应的 ROOT Context。
     */
    private Context parseContext() {

        Context context = service.getEngine().getDefaultHost().getContext(uri);
        if (null != context) {
            return context;
        }
        //   /b/a.html  --->path=b
        String path = StrUtil.subBetween(uri, "/", "/");
        if (null == path) {
            path = "/";
        } else {
            path = "/" + path;
        }
        context = service.getEngine().getDefaultHost().getContext(path);
        if (null == context) {
            context = service.getEngine().getDefaultHost().getContext("/");
        }
        return context;
    }

}