package com.kunpeng.detr.Socket;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2015/12/15.
 */
public class DeepText {
    //处理成正常编码，没有考虑汉字问题
    public static String SrcOut(byte[] tts) throws UnsupportedEncodingException {
        StringBuilder ss1 = new StringBuilder();
        boolean bb1, bb2, BB3, BB4;
        int j = 0, k = 0, g = 0;
        int n1 = 0, n2 = 0;
        byte[] aa1 = new byte[5];
        aa1[0] = 0x20;
        aa1[1] = 0x21;
        aa1[2] = 0x22;
        aa1[3] = 0x23;
        aa1[4] = 0x24;
        byte[] aa2 = new byte[5];
        aa2[0] = 0x25;
        aa2[1] = 0x26;
        aa2[2] = 0x27;
        aa2[3] = 0x28;
        aa2[4] = 0x29;

        bb1 = bb2 = BB3 = BB4 = false;

        long h = tts.length;
        j = 0;
        while (j < h)
        {
            if (j > 7)
            {
                if (tts[j - 7] == 0x1B && tts[j - 6] == 0xB && tts[j - 3] == 0xF && tts[j - 2] == 0x1B)//半屏状态字符开始
                {
                    bb1 = BB3 = true;
                    g = 1;
                }
                if (tts[j - 5] == 0x1B && tts[j - 4] == 0xB && tts[j - 1] == 0xF && tts[j] != 0x1B)//全屏状态字符开始
                {
                    bb1 = BB3 = true;
                    g = 1;
                }
            }

            if (j + 3 < h)
            {
                if (tts[j + 1] == 0x1E && tts[j + 2] == 0x1B && tts[j + 3] == 0x62 && tts[j + 4] == 0x3) //半屏状态字符开始
                {
                    bb1 = BB3 = true;
                }
            }
            if (j + 7 < h)
            {
                if (tts[j + 1] == 0x1B && tts[j + 2] == 0x62 && tts[j + 6] == 0x62 && tts[j + 7] == 0x3) //全屏状态字符开始
                {
                    bb1 = BB3 = true;
                }
            }

            if (j + 2 < h)
            {
                if (tts[j] == 0x1B && tts[j + 1] == 0x62)
                {
                    bb1 = false;
                    k = j + 2;
                }
                if (j == k && j > 10 && BB3)
                {
                    bb1 = true;
                }
            }

            if (bb1)
            {
                if (j + 1 < h)
                {
                    if (tts[j] == 0x1B) //判断汉字开始
                    {
                        j++;
                        if (tts[j] == 0xE)
                        {
                            bb2 = true;
                            j++;
                        }
                        if (tts[j] == 0xF)
                        {
                            bb2 = false;
                            j++;
                        }
                    }

                }

                if (tts[j] == 0xD) //判断换行
                {
                    ss1.append("\r\n");
                    BB4 = true;
                    //j++;
                    g = 1;
                }

                if (g == 81) //'判断换行
                {
                    ss1.append("\r\n");
                    BB4 = true;
                    g = 1;
                }

                if (bb2) //汉字解码//暂时不实现
                {
                    Byte aa3;
                    while (n2 < 5)
                    {
                        if (tts[j] == aa2[n2])
                        {
                            aa3 = tts[j];
                            tts[j] = tts[j + 1];
                            tts[j + 1] = (byte)(aa3 + 0xA);
                            break;
                        }
                        n2++;
                    }
                    n2 = 0;
                    while (n1 < 5)
                    {
                        if (tts[j] == aa1[n1])
                        {
                            tts[j] = (byte)(tts[j] + 0xE);
                            break;
                        }
                        n1++;
                    }

                    if (tts[j] > 128)
                    {
                        tts[j] = 127;
                    }
                    if (tts[j + 1] > 128)
                    {
                        tts[j + 1] = 127;
                    }
                    //不知道为什么他编码小的需要+14
                    if (tts[j] <= 36)
                        tts[j] = (byte)(tts[j] + 14);
                    byte[] tbyte = new byte[2];
                    tbyte[0] = (byte)(tts[j] + 0x80);
                    tbyte[1] = (byte)(tts[j + 1] + 0x80);
                    String han = new String(tbyte, "GBK");
                    ss1.append(han);
                    j += 2;
                    g += 2;
                }

                if (!bb2) //英文解码
                {
                    //得是可见字符
                    if (tts[j] != 27 && tts[j] != 30)
                    {
                        byte[] tbyte = new byte[1];
                        tbyte[0] = tts[j];
                        String eng = new String(tbyte);
                        if (!eng.equals("b"))
                            ss1.append(eng);
                    }

                    j++; g++;
                }

            }
            else
            {
                j++; g++;
            }
        }

        return ss1.toString();
    }
}
