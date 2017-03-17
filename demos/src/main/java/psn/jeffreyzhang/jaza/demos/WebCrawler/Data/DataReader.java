package psn.jeffreyzhang.jaza.demos.WebCrawler.Data;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import psn.jeffreyzhang.jaza.demos.WebCrawler.MyData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据读取用
 */
public class DataReader {

    private static final Logger logger = LogManager.getLogger(DataReader.class);

    /**
     * 从指定的URL获取页面内容
     *
     * @param url      要获取数据的页面URL
     * @param cssQuery 要获取数据的页面元素CSSQuery
     * @return
     * @throws Exception
     */
    public static String getData(String url, String cssQuery) {
        String result = null;

        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(true);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setTimeout(5000);
        try {
            HtmlPage page = client.getPage(url);
            String pageStr = page.asXml();
            Document doc = Jsoup.parse(pageStr, url);
            Element element = doc.select(cssQuery).first();

            result = element.text();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * 按照URL获取表格中的数据。
     * 本函数对页面中包含在ID为“mk_type”的ul中，含有Data属性的li元素进行遍历并模拟点击，
     * 然后对ID为“stat_type”的ul中的li进行遍历并模拟点击，
     * 然后返回页面中ID为“dt_1”的表格中的数据。
     *
     * @param url 要获取数据的页面的URL
     * @return 获取到的数据，以表格头/每行格式输出。
     * @throws IOException
     */
    public static List<MyData> getData(String url) throws IOException, InterruptedException {
        List<MyData> result = new ArrayList<>();

        // 创建并设置Web浏览器对象
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(true);
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        client.getOptions().setCssEnabled(false);
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setTimeout(5000);
        HtmlPage page = client.getPage(url);
        String noConflict = getNoConflict(page);

        // 按页面内容获取
        String pageStr = page.asXml();
        Document doc = Jsoup.parse(pageStr, url);

        // 获取mk_type ul对象，遍历、点击并获取数据
        List<Element> tabs = doc.select("#mk_type").first().children();
        for (int idx = 0; idx < tabs.size(); idx++) {
            Element tab = tabs.get(idx);
            String attrData = tab.attr("data");

            if (!attrData.equals("")) {
                Document newDoc = doc;
                if (idx > 0) {
                    String jsCode = noConflict + "(\"[data=\'" + attrData + "\']\").first().click()";
                    page.executeJavaScript(jsCode);
                    newDoc = Jsoup.parse(page.asXml(), url);
                }

                // 如果找到“stat_type”元素，说明有二级ul，继续模拟点击
                List<Element> subTabs = newDoc.select("#stat_type");
                if (subTabs != null && subTabs.size() > 0) {
                    List<Element> subLis = subTabs.get(0).children();
                    for (int subIdx = 0; subIdx < subLis.size(); subIdx++) {
                        Element subLi = subLis.get(subIdx);
                        if (subIdx > 0) {
                            if (!subLi.attr("data").equals("")) {
                                String subJsCode = noConflict + "(\"[data=\'" + subLi.attr("data") + "\']\").first().click()";
                                page.executeJavaScript(subJsCode);
                            }
                        }

                        // 点击分页按钮
                        int pageIdx = 0;
                        DomNode nodeNextPage = null;
                        do {
                            result.add(getMyData(tab.ownText() + "-" + subLi.ownText(), page, pageIdx == 0));
                            pageIdx++;

                            String jsNextPage = noConflict + "(\"#PageNav a:contains('下一页')\").click()";
                            page.executeJavaScript(jsNextPage);

                            boolean loading = false;
                            int waited = 0;
                            do {
                                Thread.currentThread().sleep(100L);
                                waited++;

                                Pattern hidden = Pattern.compile("display\\s?:\\s?none");
                                List<DomElement> divs = page.getElementsByTagName("div");
                                for (DomElement div : divs) {
                                    DomElement child = div.getFirstElementChild();
                                    if (child != null && child.getTagName().equals("img") && child.getAttribute("src").contains("loading.gif")) {
                                        loading = !hidden.matcher(div.getAttribute("style")).find();
                                        break;
                                    }
                                }
                            } while (loading && waited < 100);

                            try {
                                nodeNextPage = page.getFirstByXPath("//a[contains(text(),'下一页') and not(@class='nolink')]");
                            } catch (Exception e) {
                                int retry = 0;
                                while (retry < 100) {
                                    Thread.currentThread().sleep(100L);
                                    retry++;
                                    try {
                                        nodeNextPage = page.getFirstByXPath("//a[contains(text(),'下一页') and not(@class='nolink')]");
                                        break;
                                    } catch (Exception ex) {
                                        if (retry == 99)
                                            logger.error("异常：" + ex);
                                    }
                                }
                            }

                            if (result.size() > 100 && result.size() % 100 == 0) {
                                int a = 0;
                            }
                        } while (nodeNextPage != null);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 从页面中获取数据
     *
     * @param name          数据名称
     * @param page          Html页面
     * @param includeHeader 是否包含表头
     * @return
     */
    private static MyData getMyData(String name, HtmlPage page, boolean includeHeader) throws InterruptedException {
        MyData data = null;

        DomElement table = page.getElementById("dt_1");
        boolean loading = true;
        int waited = 0;
        if (table != null) {
            do {
                Thread.currentThread().sleep(100L);
                List<HtmlElement> tbody = table.getElementsByTagName("tbody");
                if (tbody != null && tbody.size() > 0) {
                    DomElement tr = tbody.get(0).getFirstElementChild();
                    DomNodeList<HtmlElement> tds = tr.getElementsByTagName("td");
                    waited++;
                    loading = tds.size() < 2 && waited < 100;
                }
            } while (loading);

            if (waited < 100) {
                String subNewStr = page.asXml();
                Document subNewDoc = Jsoup.parse(subNewStr);
                Element tableNode = subNewDoc.select("#dt_1").first();

                String tableTxt = formatTable(tableNode, includeHeader);
                data = new MyData(name, tableTxt);
            }
        }
        return data;
    }

    /**
     * Table元素的内容以正确的字符串形式
     *
     * @param table         要获取数据的table
     * @param includeHeader 是否包含表头
     * @return
     */
    private static String formatTable(Element table, boolean includeHeader) {
        StringBuffer buffer = new StringBuffer();

        if (includeHeader) {
            // 获取表头内容。
            List<Element> tHeadTrs = table.select("thead>tr");
            List<Boolean> rowSpanList = new ArrayList<>();
            if (tHeadTrs.size() > 0) {
                Element tr1 = tHeadTrs.get(0);
                for (Element td : tr1.children()) {
                    buffer.append(td.text() + "\t");
                    String rowSpan = td.attr("rowspan").replace("'", "").replace("\"", "");
                    rowSpanList.add(!rowSpan.equals("") && !rowSpan.equals("0") && !rowSpan.equals("1"));
                }
                buffer.append("\r");
            }
            if (tHeadTrs.size() == 2) {
                List<Element> tr2tds = tHeadTrs.get(1).children();
                int tr2Idx = 0;
                for (int idx = 0; idx < rowSpanList.size(); idx++) {    // 根据表头第一行对应的位置是否有rowspan决定表头第二行的内容
                    buffer.append(rowSpanList.get(idx) ? "\t" : tr2tds.get(tr2Idx++).text() + "\t");
                }
                buffer.append("\r");
            }
        }

        // 获取表格内容
        List<Element> trs = table.select("tbody>tr");
        for (Element tr : trs) {
            for (Element td : tr.children()) {
                buffer.append(td.text() + "\t");
            }
            buffer.append("\r");
        }

        return buffer.toString();
    }

    /**
     * 按页面内容获取jQuery的noConflict，默认为“$”
     *
     * @param page 要获取的页面
     * @return 获取到的noConflict
     */
    private static String getNoConflict(HtmlPage page) {
        String noConflict = "$";
        Pattern noConflictPtn = Pattern.compile("var\\s+(\\w+)\\s*=\\s*\\$\\.noConflict\\(\\);");
        Matcher matcher = noConflictPtn.matcher(page.asXml());
        if (matcher.find()) {
            noConflict = matcher.group(1);
        }
        return noConflict;
    }
}
