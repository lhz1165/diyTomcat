package com.lhz.diytomcat.util;

import cn.hutool.core.io.FileUtil;
import com.lhz.diytomcat.catalina.Context;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;


/**
 * @author: lhz
 * @date: 2020/6/16
 * 解析欢迎文件
 **/
public class WebXMLUtil {
    public static String getWebXml(Context context) {
        String webXmlFile = FileUtil.readUtf8String(Constant.webXmlFile);
        Document document = Jsoup.parse(webXmlFile);
        Elements elements = document.select("welcome-file");
        for (Element element : elements) {
            String welcomeFileName = element.text();
            File file = new File(context.getDocBase(), welcomeFileName);
            if (file.exists()) {
                return file.getName();
            }
        }
        return "index.html";
    }

}
