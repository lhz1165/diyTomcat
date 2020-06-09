package com.lhz.diytomcat.util;

import cn.hutool.core.io.FileUtil;
import com.lhz.diytomcat.catalina.Context;
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
		System.out.println(getHostName());
		getContext();
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

	public static String getHostName() {
		String xml =FileUtil.readUtf8String(Constant.serverXmlFile);
		Document d = Jsoup.parse(xml);
		Elements es = d.select("Host");
		return es.first().attr("name");
	}
}
