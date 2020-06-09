package com.lhz.diytomcat.catalina;

import cn.hutool.core.io.FileUtil;
import com.lhz.diytomcat.util.Constant;
import com.lhz.diytomcat.util.ServerXMLUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: lhz
 * @date: 2020/6/9
 **/
public class Host {


    private String name;
    private Map<String,Context> contextMap;

    public Host() {
        name = ServerXMLUtil.getHostName();
        contextMap = new HashMap<>();
        scanXml(contextMap);
        scanWebapp(contextMap);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }

    public void setContextMap(Map<String, Context> contextMap) {
        this.contextMap = contextMap;
    }

    private void scanXml(Map<String, Context> result) {
        List<Context> contexts = ServerXMLUtil.getContext();
        for (Context context : contexts) {
            result.put(context.getPath(), context);
        }
    }

    public void scanWebapp(Map<String, Context> result) {
        File[] webappFolders = Constant.webappFplader.listFiles();
        for (File folder : webappFolders) {
            String path = folder.getName();
            if ("ROOT".equals(path)) {
                path = "/";
            } else {
                path = "/" + path;
            }
            String docBase = folder.getAbsolutePath();
            Context context = new Context(path,docBase);
            result.put(path, context);
        }

    }
    public Context getContext(String path) {
        return contextMap.get(path);
    }




}
