package com.lhz.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.lhz.diytomcat.catalina.Context;
import com.lhz.diytomcat.catalina.Host;
import com.lhz.diytomcat.http.Request;
import com.lhz.diytomcat.http.Response;
import com.lhz.diytomcat.util.Constant;
import com.lhz.diytomcat.util.ServerXMLUtil;
import com.lhz.diytomcat.util.ThreadPoolUtil;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.Struct;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: lhz
 * @date: 2020/5/25
 **/
public class Bootstrap {

    public static Map<String, Context> contextMap = new HashMap<>();


    public static void main(String[] args) {
        try {
            PropertyConfigurator.configure("src\\com\\lhz\\diytomcat\\log4j.properties");
            logJVM();

            scanContextsOnWebAppFolder();
            scanContextsOnXML();

            int port = 18080;

//            if(!NetUtil.isUsableLocalPort(port)) {
//                System.out.println(port +" 端口已经被占用了，排查并关闭本端口的办法请用：\r\nhttps://how2j.cn/k/tomcat/tomcat-portfix/545.html");
//                return;
//            }
            ServerSocket ss = new ServerSocket(port);

            while(true) {
                Socket s =  ss.accept();
                Host host = new Host();
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Request request = new Request(s,host);
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
                                String html = "Hello DIY Tomcat from how2j.cn";
                                //返回响应体写进流
                                response.getWriter().println(html);
                            } else {
                                String fileName = StrUtil.removePrefix(uri, "/");
                                File file = FileUtil.file(context.getDocBase(), fileName);

                                //返回响应体写进流
                                if (file.exists()) {
                                    String fileContent = FileUtil.readUtf8String(file);
                                    response.getWriter().println(fileContent);
                                } else {
                                    response.getWriter().println("File Not Found");
                                }
                                //处理响应头
                                handle200(s, response);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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
        Map<String,String> infos = new LinkedHashMap<>();
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
            LogFactory.get().debug(key+":\t\t" + infos.get(key));
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

    public static void scanContextsOnWebAppFolder() {
        File[] folders = Constant.webappFplader.listFiles();
        for (File folder : folders) {
            if (!folder.isDirectory()) {
                continue;
            }
            loadContext(folder);

        }
    }

    public static void scanContextsOnXML() {
        List<Context> context = ServerXMLUtil.getContext();
        for (Context c : context) {
            contextMap.put(c.getPath(), c);
        }
    }


    /**
     * 加载目录成为Context对象
     * @param folder
     */
    private static void loadContext(File folder) {
        //访问路径  /
        String path = folder.getName();
        if ("ROOT".equals(path)) {
            path = "/";
        } else {
            path = "/" + path;
        }
        //文件系统中的绝对路径
        String docBase = folder.getAbsolutePath();
        Context context = new Context(path,docBase);
        contextMap.put(context.getPath(), context);
    }
}
