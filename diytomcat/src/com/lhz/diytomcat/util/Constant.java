package com.lhz.diytomcat.util;



import cn.hutool.system.SystemUtil;

import java.io.File;

/**
 * @author: lhz
 * @date: 2020/6/4
 **/
public class Constant {
    public final static String response_head_202="HTTP/1.1 200 OK\r\n"+"Content-Type:{}\r\n\r\n";
    public final static File webappFplader = new File(SystemUtil.get("user.dir"), "webapps");
    public final static File rootFolder = new File(webappFplader,"ROOT");

}
