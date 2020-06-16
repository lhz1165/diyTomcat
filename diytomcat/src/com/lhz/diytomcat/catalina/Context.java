package com.lhz.diytomcat.catalina;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.log.LogFactory;

/**
 * @author lhzlhz
 * @create 2020/6/7
 */
public class Context {
	//访问的路径 相对路径 比如 /
	private String path;
	//文件系统中的位置  绝对路径 D:\lhz\project\private\diytomcat\webapps\ROOT
	private String docBase;

	public Context(String path, String docBase) {
		TimeInterval timeInterval = DateUtil.timer();
		this.path = path;
		this.docBase = docBase;
		LogFactory.get().info("Deploying web application directory {}", this.docBase);
		LogFactory.get().info("Deployment of web application directory {} has finished in {} ms", this.docBase,timeInterval.intervalMs());
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getDocBase() {
		return docBase;
	}

	public void setDocBase(String docBase) {
		this.docBase = docBase;
	}

}
