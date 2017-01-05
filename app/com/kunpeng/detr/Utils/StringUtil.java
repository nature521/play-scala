package com.kunpeng.detr.Utils;

import com.google.common.base.Splitter;
import com.kunpeng.detr.model.EtermConfig;

import java.util.*;

/**
 * Created by Administrator on 2017/1/5.
 */
public  class StringUtil {
    public static EtermConfig parseConfig(String configStr){
        EtermConfig config = new EtermConfig();
        List<String> list = Splitter.on(":").trimResults().splitToList(configStr);
        config.setPost(list.get(0));
        config.setPort(Integer.valueOf(list.get(1)));
        config.setUserName(list.get(2));
        config.setPassword(list.get(3));
        return config;
    }
}
