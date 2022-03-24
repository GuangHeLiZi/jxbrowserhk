package com.uohe;



import java.nio.charset.Charset;
/*
 * byte转全部类
 * 2021-3-17
 * by GuangHeLiZi
 * 更多请鉴
 * */
public class ByteToAll {
    public String byteToText(byte[] bytes){//转文本 为了统一全部采用UTF-8
        return new String(bytes, Charset.forName("UTF-8"));
    }

}
