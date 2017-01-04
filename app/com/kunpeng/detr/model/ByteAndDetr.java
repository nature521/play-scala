package com.kunpeng.detr.model;

import com.kunpeng.detr.entity.DetrResultJV;

import java.util.*;

/**
 * Created by Administrator on 2017/1/4.
 */
public class ByteAndDetr {
    private byte[] bytes;
    private List<DetrResultJV> detrResultList;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public List<DetrResultJV> getDetrResultList() {
        return detrResultList;
    }

    public void setDetrResultList(List<DetrResultJV> detrResultList) {
        this.detrResultList = detrResultList;
    }
}
