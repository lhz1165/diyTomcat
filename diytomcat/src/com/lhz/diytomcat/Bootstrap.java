package com.lhz.diytomcat;

import cn.hutool.core.util.NetUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author: lhz
 * @date: 2020/5/25
 **/
public class Bootstrap {
    public static void main(String[] args)  {
        try {
            int port = 18080;
            if (!NetUtil.isUsableLocalPort(port)) {
                System.out.println(port+" 端口已经被占用了，排查并关闭本端口");
                return;
            }
            //监听端口
            ServerSocket ss = new ServerSocket(port);
            while (true) {
                //建立连接
                Socket s = ss.accept();
                //获取输入流,浏览器的输入
                InputStream is = s.getInputStream();
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                is.read(buffer);
                String requestStr = new String(buffer, StandardCharsets.UTF_8);
                System.out.println("浏览器的输入信息： \r\n" + requestStr);

                OutputStream os = s.getOutputStream();
                String responseHead = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html\r\n\r\n";
                String responseString = "Hello DIY Tomcat from lhz";
                responseString = responseHead + responseString;
                os.write(responseString.getBytes());
                os.flush();
                s.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
