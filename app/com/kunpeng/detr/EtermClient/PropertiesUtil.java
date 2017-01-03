package com.kunpeng.detr.EtermClient;

import java.io.*;
import java.util.Properties;

/**
 * Created by Administrator on 2016/4/14.
 */
public class PropertiesUtil {
    public static String defaultPath = "config.properties";
    public static Properties GetConfig(String filePath) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
        properties.load(inputStream);
        return properties;
    }
}
