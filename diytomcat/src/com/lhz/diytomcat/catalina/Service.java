package com.lhz.diytomcat.catalina;

import com.lhz.diytomcat.util.ServerXMLUtil;

/**
 * @author: lhz
 * @date: 2020/6/11
 **/
public class Service {
    private String name;
    private Engine engine;
    private Server server;

    public Service(Server server) {
        this.server = server;
        name = ServerXMLUtil.getServiceName();
        engine = new Engine(this);
    }

    public Engine getEngine() {
        return engine;
    }
}
