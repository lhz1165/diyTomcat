package com.lhz.diytomcat.http;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @author: lhz
 * @date: 2020/6/4
 **/
public class Response {
    //用来存放html文本
    private StringWriter stringWriter;

    private PrintWriter writer;

    private String contentType;



    public Response() {
        this.stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        contentType = "text/html";
    }

    public String getContentType() {
        return contentType;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    /**
     * 请求体
     * @return
     * @throws UnsupportedEncodingException
     */
    public byte[] getBody() throws UnsupportedEncodingException {
        String content = stringWriter.toString();
        return content.getBytes(StandardCharsets.UTF_8);
    }
}
