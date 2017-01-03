package com.kunpeng.detr.Socket;

import java.io.IOException;

/**
 * Created by Administrator on 2016/4/12.
 */
public interface ISocketClient {
    void Init(String Host, int Port) throws IOException;
    String Verify() throws IOException;
    String SendCmd(String OpCmd) throws IOException;
    void Close() throws IOException, InterruptedException;
}
