package com.lhz.diytomcat.util;

import cn.hutool.core.io.FileUtil;
import com.lhz.diytomcat.catalina.Context;
import com.lhz.diytomcat.catalina.Engine;
import com.lhz.diytomcat.catalina.Host;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lhzlhz
 * @create 2020/6/8
 * 解析xml
 */
public class ServerXMLUtil {
	public static void main(String[] args) {
		System.out.println(getEngineDefaultHost());
	}

	public static List<Context> getContext() {
		ArrayList<Context> result = new ArrayList<>();
		//读取文件内容
		String xml = FileUtil.readUtf8String(Constant.serverXmlFile);
		//解析xml
		Document d = Jsoup.parse(xml);
		Elements es = d.select("Context");
		// <Context path="/b" docBase="D:/lhz/project/private/diytomcat/b" />
		for (Element e : es) {
			//path="/b"
			String path = e.attr("path");
			//docBase="D:/lhz/project/private/diytomcat/b"
			String docBase = e.attr("docBase");
			Context context = new Context(path, docBase);
			result.add(context);
		}
		return result;
	}


	public static String getEngineDefaultHost() {
		return getSomeoneName("Engine","defaultHost");
	}
	public static List<Host> getHosts(Engine engine) {
		List<Host> result = new ArrayList<>();
		Elements es = getElement("Host");
		for (Element e : es) {
			String name = e.attr("name");
			Host host = new Host(name,engine);
			result.add(host);
		}
		return result;
	}
	public static String getServiceName() {
		return getSomeoneName("Service","name");
	}


	/**
	 * 获取Element
	 * @param node
	 * @return
	 */
	public static Elements getElement(String node) {
		String xml = FileUtil.readUtf8String(Constant.serverXmlFile);
		Document d = Jsoup.parse(xml);
		return d.select(node);
	}

	/**
	 * 获取标签的第一个属性
	 * @param node
	 * @param attribute
	 * @return
	 */
	public static String getSomeoneName(String node,String attribute) {
		Element host = getElement(node).first();
		return host.attr(attribute);
	}
}
