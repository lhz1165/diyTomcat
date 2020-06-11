package com.lhz.diytomcat;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import com.lhz.diytomcat.catalina.*;
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
        Server server = new Server();
        server.start();


    }





}
