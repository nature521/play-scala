package com.kunpeng.detr.EtermClient;



import com.kunpeng.detr.Socket.ISocketClient;
import com.kunpeng.detr.Socket.SimpleSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;


/**
 * Created by Administrator on 2016/7/20.
 */
public class EtermClient  {

    public static final Logger LOGGER = LoggerFactory.getLogger(EtermClient.class);

    public enum ConnectionMode
    {
        PerQuery,   //每次重新连接
        KeepAlive   //保持连接
    }
    public enum EtermLoginMode
    {
        Basic,
        AuthData
    };
    public EtermLoginMode loginMode = EtermLoginMode.Basic;
    public final static  String EMPTY = "";
    public String authData;

    private String host;
    private int port;
    private String userName ;
    private String passWord;
    String ret = EMPTY;

    public ISocketClient SClinet;

    private static Object _lock = new Object();

    // 连接策略
    public ConnectionMode connMode = ConnectionMode.PerQuery;
    // 超时时间
    public int waitTimeOut = 5000;
    // 是否通过验证
    public boolean isVerified = false;
  //  private XMLClinet xmlClinet;

    public void PreInit() throws IOException, InterruptedException {
        while(SClinet == null) {
            try {
                SClinet = new SimpleSocketClient(userName, passWord, waitTimeOut);
                SClinet.Init(host, port);
                SClinet.Verify();
            }catch (Exception ex){
                Thread.sleep(1000);
                System.out.println("fail to connect av, retry again!");
            }
        }
    }

    //从配置获取账号信息，
    public EtermClient(int taskNumber) throws IOException {
        loginMode = EtermLoginMode.Basic;
        Properties config = PropertiesUtil.GetConfig(PropertiesUtil.defaultPath);
        this.host = config.getProperty("AV_HOST" + taskNumber);
        this.port = Integer.parseInt(config.getProperty("AV_PORT" + taskNumber));
        this.userName = config.getProperty("AV_USERNAME" + taskNumber);
        this.passWord = config.getProperty("AV_PASSWORD" + taskNumber);

        System.out.println("use user:[" + this.userName + "] to run this job!");

      //  this.xmlClinet = new XMLClinet();
    }

    //从配置获取账号信息，这里有些问题。配置找不到
    public EtermClient() throws IOException {
        loginMode = EtermLoginMode.Basic;
        Properties config = PropertiesUtil.GetConfig(PropertiesUtil.defaultPath);
        this.host = config.getProperty("AV_HOST");
        this.port = Integer.parseInt(config.getProperty("AV_PORT" ));
        this.userName = config.getProperty("AV_USERNAME");
        this.passWord = config.getProperty("AV_PASSWORD");

        System.out.println("use user:[" + this.userName + "] to run this job!");

        //  this.xmlClinet = new XMLClinet();
    }

    public EtermClient(String host, int port, String userName,String passWord) throws IOException {
        loginMode = EtermLoginMode.Basic;
       // Properties config = PropertiesUtil.GetConfig(PropertiesUtil.defaultPath);
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
        System.out.println("use user:[" + this.userName + "] to run this job!");

        //  this.xmlClinet = new XMLClinet();
    }

    //验证文件登陆
    public EtermClient(String host, int port, String authData)
    {
        loginMode = EtermLoginMode.AuthData;

        this.host = host;
        this.port = port;
        this.authData = authData;
    }

    //发送基本命令
    public String SendRawCmd(String opCmd) throws IOException, InterruptedException {
        PreInit();
        try {
            ret = SClinet.SendCmd(opCmd);
        }catch (Exception ex)
        {
            ret = null;
            //Socket连接失败
        }
        SClinet = null;
        return ret;
    }
    //重连
    public void ReLink(int remainCount) throws IOException, InterruptedException {
        if(SClinet != null)
        {
            SClinet.Close();
        }
        SClinet = null;
        isVerified = false;
        LOGGER.warn("重新连接");
    }
    //得到并转换为实体类
   /* public List<AVHResult> GetAVHResult(String opCmd) throws Exception {
        PreInit();
        //return DetrResultParser.ToBean(opCmd);
        return DetrResultParser.ToBean(opCmd);
    }*/
    //得到之后只需存储到数据库；
    public String Verify() throws IOException, InterruptedException {
        isVerified = true;
        PreInit();
        return SClinet.Verify();
    }
    public String GetVersion()
    {
        return "v1.1.1";
    }
}
