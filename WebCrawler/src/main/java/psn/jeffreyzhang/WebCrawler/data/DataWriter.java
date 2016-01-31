package psn.jeffreyzhang.WebCrawler.data;

import psn.jeffreyzhang.WebCrawler.model.MyData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据写入文件
 */
public class DataWriter {

    private static final String FILE_EXT = ".txt";

    private static final String DATE_FORMAT = "yyyyMMdd";

    /**
     * 输出文本文件。格式：yyyyMMdd/数据名.txt
     *
     * @param data 要输出的数据
     * @throws IOException
     */
    public synchronized static void writeDataByDate(MyData data) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        String date = format.format(new Date());
        File directory = new File(date);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String outFileName = date + File.separator + data.name + FILE_EXT;
        FileWriter writer = new FileWriter(outFileName, true);
        writer.write(data.value);
        writer.close();
    }
}
