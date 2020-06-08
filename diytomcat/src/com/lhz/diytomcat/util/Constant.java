package com.lhz.diytomcat.util;



import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @author: lhz
 * @date: 2020/6/4
 **/
public class Constant {
    public static void main(String[] args) {
        System.out.println(SystemUtil.get("user.dir"));
    }
    public final static String response_head_202="HTTP/1.1 200 OK\r\n"+"Content-Type:{}\r\n\r\n";
    //D:\JavaSoft\Workspase\gitProject\diytomcat\webapps
    public final static File webappFplader = new File(SystemUtil.get("user.dir"), "webapps");
    //D:\JavaSoft\Workspase\gitProject\diytomcat\webapps\ROOT
    public final static File rootFolder = new File(webappFplader,"ROOT");

    //定位server.xml的常量
    public static final File confFolder = new File(SystemUtil.get("user.dir"),"conf");
    //D:\JavaSoft\Workspase\gitProject\diytomcat\conf\server.xml
    public static final File serverXmlFile = new File(confFolder, "server.xml");


}
