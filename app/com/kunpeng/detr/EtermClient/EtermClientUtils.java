package com.kunpeng.detr.EtermClient;

import com.kunpeng.detr.EtermClient.EtermClient;
import com.kunpeng.detr.EtermClient.PropertiesUtil;


import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/7/27.
 */
public class EtermClientUtils {

    //得到一个EtermClient，各个参数在配置文件中获取，获取路径为PropertiesUtil.defaultPath；
    public static EtermClient getEtermClient() throws IOException {
        String configPath = PropertiesUtil.defaultPath;
        /*Properties properties = PropertiesUtil.GetConfig(configPath);
        String post = properties.getProperty("post");
        int port = Integer.parseInt(properties.getProperty("port"));
        String userName = properties.getProperty("userName");
        String password = properties.getProperty("password");*/
        EtermClient etermClient = new EtermClient("116.213.132.7", 350, "detr", "www2730826");
        return etermClient;
    }
}
