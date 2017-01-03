package com.kunpeng.detr.Socket;



/**
 * Created by Administrator on 2016/4/12.
 */
public class VerifyHelper {
    public static byte[] BuildVerify1(String userName, String passWord)
    {
        byte[] sendBytes = new byte[162];
        sendBytes[0] = 0x1;
        sendBytes[1] = (byte)0xA2;
       // byte[] userBytes = Encoding.default.getBytes(UserName);
        byte[] userBytes = userName.getBytes();

        for (int i = 0; i < userBytes.length; i++)
        {
            sendBytes[i + 2] = userBytes[i];
        }
        sendBytes[11] = 0x0;
        sendBytes[12] = 0x0;
        sendBytes[13] = 0x0;
        sendBytes[14] = 0x0;
        sendBytes[15] = 0x0;
        sendBytes[16] = 0x0;
        sendBytes[17] = 0x0;
        byte[] passBytes = passWord.getBytes();
        for (int i = 0; i < passBytes.length; i++)
        {
            sendBytes[i + 18] = passBytes[i];
        }
        for (int i = 0; i < 20; i++)
        {
            sendBytes[i + 29] = 0x0;
        }
        sendBytes[50] = 0x30;
        sendBytes[51] = 0x30;
        sendBytes[52] = 0x30;
        sendBytes[53] = 0x63;
        sendBytes[54] = 0x32;
        sendBytes[55] = 0x39;
        sendBytes[56] = 0x37;
        sendBytes[57] = 0x33;
        sendBytes[58] = 0x39;
        sendBytes[59] = 0x65;
        sendBytes[60] = 0x34;
        sendBytes[61] = 0x39;
        sendBytes[62] = 0x31;
        sendBytes[63] = 0x39;
        sendBytes[64] = 0x32;
        sendBytes[65] = 0x2E;
        sendBytes[66] = 0x31;
        sendBytes[67] = 0x36;
        sendBytes[68] = 0x38;
        sendBytes[69] = 0x2E;
        sendBytes[70] = 0x31;
        sendBytes[71] = 0x35;
        sendBytes[72] = 0x30;
        sendBytes[73] = 0x2E;
        sendBytes[74] = 0x31;
        sendBytes[75] = 0x32;
        sendBytes[76] = 0x38;
        sendBytes[77] = 0x33;
        sendBytes[78] = 0x36;
        sendBytes[79] = 0x30;
        sendBytes[80] = 0x30;
        sendBytes[81] = 0x33;
        sendBytes[82] = 0x31;
        sendBytes[83] = 0x30;
        sendBytes[84] = 0x0;
        sendBytes[85] = 0x30;
        sendBytes[86] = 0x30;
        sendBytes[87] = 0x30;
        sendBytes[88] = 0x30;
        sendBytes[89] = 0x30;
        sendBytes[90] = 0x30;
        return sendBytes;
    }

    public static byte[] BuildVerify2()
    {
        byte[] sendBytes = new byte[33];
        sendBytes[0] = 0x1;
        sendBytes[1] = (byte)0xFE;
        sendBytes[2] = 0x0;
        sendBytes[3] = 0x11;
        sendBytes[4] = 0x14;
        sendBytes[5] = 0x10;
        sendBytes[6] = 0x0;
        sendBytes[7] = 0x2;
        for (int i = 0; i < 8; i++)
        {
            sendBytes[i + 8] = 0x0;
        }
        return sendBytes;
    }

    public static byte[] BuildVerify3()
    {
        byte[] sendBytes = new byte[33];
        sendBytes[0] = 0x1;
        sendBytes[1] = (byte)0xFE;
        sendBytes[2] = 0x0;
        sendBytes[3] = 0x11;
        sendBytes[4] = 0x14;
        sendBytes[5] = 0x10;
        sendBytes[6] = 0x0;
        sendBytes[7] = 0x2;
        sendBytes[8] = 0xC;
        for (int i = 0; i < 7; i++)
        {
            sendBytes[i + 9] = 0x0;
        }
        sendBytes[17] = 0x1;
        sendBytes[18] = (byte)0xFE;
        sendBytes[19] = 0x0;
        sendBytes[20] = 0x11;
        sendBytes[21] = 0x14;
        sendBytes[22] = 0x10;
        sendBytes[23] = 0x0;
        sendBytes[24] = 0x2;
        sendBytes[25] = 0x29;
        for (int i = 0; i < 7; i++)
        {
            sendBytes[i + 26] = 0x0;
        }
        return sendBytes;
    }

    public static byte[] BuildVerify4()
    {
        byte[] sendBytes = new byte[67];
        sendBytes[0] = 0x1;
        sendBytes[1] = (byte)0xF9;
        sendBytes[2] = 0x0;
        sendBytes[3] = 0x44;
        sendBytes[4] = 0x0;
        sendBytes[5] = 0x1;
        sendBytes[6] = 0x1B;
        for (int i = 0; i < 60; i++)
        {
            sendBytes[i + 7] = 0x0;
        }
        return sendBytes;
    }

    public static byte[] BuildSendCmd(String OpCmd, Byte eKey)
    {
        //byte[] dataBytes = Encoding.Default.GetBytes(OpCmd);
        byte[] dataBytes = OpCmd.getBytes();
        int count = 19 + OpCmd.length();
        byte[] sendBytes = new byte[count + 1];
        sendBytes[0] = 0x1;
        sendBytes[1] = 0x0;
        sendBytes[2] = 0x0;
        sendBytes[3] = (byte)(count + 1);
        sendBytes[4] = 0x0;
        sendBytes[5] = 0x0;
        sendBytes[6] = 0x0;
        sendBytes[7] = 0x1;
        sendBytes[8] = eKey;
        sendBytes[9] = 0x51;
        sendBytes[10] = 0x70;
        sendBytes[11] = 0x2;
        sendBytes[12] = 0x1B;
        sendBytes[13] = 0xB;
        sendBytes[14] = 0x20;
        sendBytes[15] = 0x20;
        sendBytes[16] = 0x0;
        sendBytes[17] = 0xF;
        for (int i = 0; i < dataBytes.length; i++)
        {
            sendBytes[18 + i] = dataBytes[i];
        }
        sendBytes[count - 1] = 0x20;
        sendBytes[count] = 0x3;
        return sendBytes;
    }
}
