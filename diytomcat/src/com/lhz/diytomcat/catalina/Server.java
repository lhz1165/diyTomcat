package com.lhz.diytomcat.catalina;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.lhz.diytomcat.http.Request;
import com.lhz.diytomcat.http.Response;
import com.lhz.diytomcat.util.Constant;
import com.lhz.diytomcat.util.ServerXMLUtil;
import com.lhz.diytomcat.util.ThreadPoolUtil;
import com.lhz.diytomcat.util.WebXMLUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: lhz
 * @date: 2020/6/11
 **/
public class Server {

    private Service service;

    public Server() {
        this.service = new Service(this);
    }

    public void start() {
        logJVM();
        init();
    }

    public void init() {
        try {
            PropertyConfigurator.configure("D:\\lhz\\project\\private\\diytomcat\\src\\log4j.properties");
            logJVM();

            int port = 18080;
            ServerSocket ss = new ServerSocket(port);

            while (true) {
                Socket s = ss.accept();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Request request = new Request(s, service);
                            Response response = new Response();

                            System.out.println("浏览器的输入信息： \r\n" + request.getRequestString());
                            System.out.println("uri:" + request.getUri());
                            System.out.println("---------------------");


                            String uri = request.getUri();
                            if (null == uri) {
                                return;
                            }

                            Context context = request.getContext();

                            if ("/".equals(uri)) {

                                uri = WebXMLUtil.getWebXml(request.getContext());
                            }
                            String fileName = StrUtil.removePrefix(uri, "/");
                            File file = FileUtil.file(context.getDocBase(), fileName);

                            //返回响应体写进流
                            if (file.exists()) {
                                String fileContent = FileUtil.readUtf8String(file);
                                response.getWriter().println(fileContent);
                            } else {
                                handle404(s, uri);
                                return;
                            }
                            //处理响应头
                            handle200(s, response);

                        } catch (Exception e) {
                            e.printStackTrace();
                            handle500(s, e);
                        } finally {
                            try {
                                if (!s.isClosed()) {
                                    s.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };

                ThreadPoolUtil.run(runnable);
            }
        } catch (IOException e) {
            LogFactory.get().error(e);
            e.printStackTrace();

        }
    }

    /**
     * 日志
     */
    private static void logJVM() {
        Map<String, String> infos = new LinkedHashMap<>();
        infos.put("Server version", "How2J DiyTomcat/1.0.1");
        infos.put("Server built", "2020-04-08 10:20:22");
        infos.put("Server number", "1.0.1");
        infos.put("OS Name\t", SystemUtil.get("os.name"));
        infos.put("OS Version", SystemUtil.get("os.version"));
        infos.put("Architecture", SystemUtil.get("os.arch"));
        infos.put("Java Home", SystemUtil.get("java.home"));
        infos.put("JVM Version", SystemUtil.get("java.runtime.version"));
        infos.put("JVM Vendor", SystemUtil.get("java.vm.specification.vendor"));

        Set<String> keys = infos.keySet();
        for (String key : keys) {
            LogFactory.get().debug(key + ":\t\t" + infos.get(key));
        }
    }

    private static void handle200(Socket s, Response response) throws IOException {
        //text/html
        String contentType = response.getContentType();
        String headText = Constant.response_head_202;

        //HTTP/1.1 200 OK\r\n"+"Content-Type:{text/html}\r\n\r\n
        headText = StrUtil.format(headText, contentType);

        byte[] head = headText.getBytes();

        byte[] body = response.getBody();

        byte[] responseBytes = new byte[head.length + body.length];

        //把响应头添加到字节数组取
        ArrayUtil.copy(head, 0, responseBytes, 0, head.length);
        ArrayUtil.copy(body, 0, responseBytes, head.length, body.length);

        OutputStream os = s.getOutputStream();

        //把响应写出去
        os.write(responseBytes);
        s.close();
    }

    protected void handle404(Socket s, String uri) throws IOException {
        OutputStream os = s.getOutputStream();
        String responseText = StrUtil.format(Constant.textFormat_404, uri, uri);
        responseText = Constant.response_head_404 + responseText;
        byte[] responseByte = responseText.getBytes(StandardCharsets.UTF_8);
        os.write(responseByte);
    }

    protected void handle500(Socket s, Exception e) {
        try {
            OutputStream os = s.getOutputStream();
            StackTraceElement stes[] = e.getStackTrace();
            StringBuffer sb = new StringBuffer();
            sb.append(e.toString());
            sb.append("\r\n");
            for (StackTraceElement ste : stes) {
                sb.append("\t");
                sb.append(ste.toString());
                sb.append("\r\n");
            }

            String msg = e.getMessage();

            if (null != msg && msg.length() > 20) {
                msg = msg.substring(0, 19);
            }
            /**
             *1  An exception occurred processing {java.lang.ArithmeticException: / by zero}
             *2  {java.lang.ArithmeticException: / by zero}
             * Stacktrace:{
             * java.lang.ArithmeticException: / by zero
             * 	com.lhz.diytomcat.catalina.Server$1.run(Server.java:57)
             * 	java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
             * 	java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
             * 	java.lang.Thread.run(Thread.java:748)
             *    }
             */
            String text = StrUtil.format(Constant.textFormat_500, msg, e.toString(), sb.toString());
            //String text = StrUtil.format(Constant.textFormat_500, msg,  sb.toString());
            text = Constant.response_head_500 + text;
            byte[] responseBytes = text.getBytes("utf-8");
            os.write(responseBytes);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


}
