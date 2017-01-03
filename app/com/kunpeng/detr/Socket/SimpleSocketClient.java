package com.kunpeng.detr.Socket;

import com.kunpeng.detr.Socket.ISocketClient;
import com.kunpeng.detr.Socket.VerifyHelper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;


/**
 * Created by Administrator on 2016/4/12.
 */
public class SimpleSocketClient implements ISocketClient {
    private Socket socket;
    private Byte eKey;
    private final int Buffersize = 32 * 1024;
    private byte[] recvBuffer = new byte[Buffersize];
    public int status = 0;
    private static Character ETX = (char)3;

    private String host;
    private int port;
    private String userName;
    private String passWord;
    private int waitTimeOut = 5000;


    public SimpleSocketClient(String userName, String passWord, int timeout)
    {
        this.userName = userName;
        this.passWord = passWord;
        this.waitTimeOut = timeout;
    }

    public void Init(String host, int port) throws IOException {
        this.host = host;
        this.port = port;

        //socket = new Socket(AddressFamily.InterNetwork, SockcetType.Stream, ProtocolType.tcp);
        socket = new Socket();
        socket.connect(new InetSocketAddress(host, port));
        socket.setSoTimeout(waitTimeOut);
    }

    public String Verify() throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        //valid 1
        {
            byte[] sendBytes = VerifyHelper.BuildVerify1(userName, passWord);
            out.write(sendBytes, 0, sendBytes.length);
            //socket.send(sendBytes);
            byte[] recvBytes = new byte[Buffersize];
           // socket.Receive(recvBytes);
            in.read(recvBytes);
            eKey = recvBytes[8];
        }
        //valid 2
        {
            byte[] sendBytes = VerifyHelper.BuildVerify2();
            //socket.Send(sendBytes);
            out.write(sendBytes, 0, sendBytes.length);
            byte[] recvBytes = new byte[Buffersize];
            in.read(recvBytes);
           // socket.Receive(recvBytes);
        }
        //valid 3
        {
            byte[] sendBytes = VerifyHelper.BuildVerify3();
            //socket.Send(sendBytes);
            out.write(sendBytes, 0, sendBytes.length);
            byte[] recvBytes = new byte[Buffersize];
           // socket.Receive(recvBytes);
            in.read(recvBytes);
        }
        //valid 4
        {
            byte[] sendBytes = VerifyHelper.BuildVerify4();
           // socket.Send(sendBytes);
            out.write(sendBytes, 0, sendBytes.length);
            byte[] recvBytes = new byte[Buffersize];
            //socket.Receive(recvBytes);
            in.read(recvBytes);
            String DeepSource = DeepText.SrcOut(recvBytes);
            return TryToGetDeepSource(DeepSource);
        }
    }

    //发布报文
    public String SendCmd(String OpCmd) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        OpCmd = OpCmd.trim();
        byte[] sendBytes = VerifyHelper.BuildSendCmd(OpCmd, eKey);
       // byte[] sendByres = new SocketHelper().sendCmd(OpCmd, host, port);
       // socket.Send(sendBytes);
        out.write(sendBytes, 0, sendBytes.length);
        byte[] recvBytes = new byte[Buffersize];
        //socket.Blocking = true;
        ServerSocketChannel.open().configureBlocking(true);
        //socket.Receive(recvBytes);
        in.read(recvBytes);

        String DeepSource = DeepText.SrcOut(recvBytes);
        return TryToGetDeepSource(DeepSource);
    }

    //尝试提取字符串
    private String TryToGetDeepSource(String DeepSource){
        int test = DeepSource.indexOf(ETX);
        String source = "";
        if (test != -1)
        {
            try
            {
                source = DeepSource.substring(0, test);
            }
            catch(Exception ex)
            {
                // Nothing
            }
        }
        return source;
    }

    public void Close() throws IOException, InterruptedException {
        if (socket != null && socket.isConnected())
        {
           // socket.shutdown(SocketShutdown.Both);
            socket.shutdownInput();
            socket.shutdownOutput();
            //System.Threading.Thread.Sleep(100);
            Thread.sleep(100);
            socket.close();
        }
    }
}

