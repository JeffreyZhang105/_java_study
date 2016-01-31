package psn.jeffreyzhang.WebCrawler;


import psn.jeffreyzhang.WebCrawler.data.DataReader;
import psn.jeffreyzhang.WebCrawler.data.DataWriter;
import psn.jeffreyzhang.WebCrawler.model.MyData;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {
    private static final String CONFIG_FILE = "config.xml";

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            XMLConfiguration config = new XMLConfiguration(CONFIG_FILE);
            List<HierarchicalConfiguration> targets = config.configurationsAt("targets.target");
            for (HierarchicalConfiguration target : targets) {
                String url = target.getString("url");
                if (url != null && !url.isEmpty()) {
                    List<HierarchicalConfiguration> elements = target.configurationsAt("elements");
                    if (elements == null || elements.size() < 1) {
                        List<MyData> datas = DataReader.getData(url);
                        for (MyData data : datas) {
                            DataWriter.writeDataByDate(data);
                        }
                    } else {
                        for (HierarchicalConfiguration element : elements) {
                            String cssQuery = element.getString("element.cssQuery");
                            String data = DataReader.getData(url, cssQuery);
                            String name = target.getString("name") + "-" + element.getString("name");
                            DataWriter.writeDataByDate(new MyData(name, data));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("异常： ", e);
            System.exit(0);
        }
    }
}
