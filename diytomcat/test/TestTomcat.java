import cn.hutool.core.util.StrUtil;
import com.lhz.diytomcat.util.MiniBrowser;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author: lhz
 * @date: 2020/6/2
 **/
public class TestTomcat {
    private static int port = 18080;
    private static String ip = "127.0.0.1";

//    @BeforeClass
//    public static void beforeClass() {
//        //所有测试开始前看diy tomcat 是否已经启动了
//        if (NetUtil.isUsableLocalPort(port)) {
//            System.err.println("请先启动 位于端口: " + port + " 的diy tomcat，否则无法进行单元测试");
//            System.exit(1);
//        } else {
//            System.out.println("检测到 diy tomcat已经启动，开始进行单元测试");
//        }
//    }

    @Test
    public void testHelloTomcat() {
        String html = getContentString("/");
        Assert.assertEquals(html, "Hello DIY Tomcat from lhz");
    }

    @Test
    public void testaHtml() {
        String html = getContentString("/a.html");
        System.out.println(html);
        //Assert.assertEquals(html,"Hello DIY Tomcat from a.html");
    }

    @Test
    public void testbIndex() {
        String html = getContentString("/b/index.html");
        Assert.assertEquals("hello world @b",html);
    }

    private String getContentString(String uri) {
        String url = StrUtil.format("http://{}:{}{}", ip, port, uri);
        String content = MiniBrowser.sendRequestAndGetResponse(url);
        return content;
    }
}