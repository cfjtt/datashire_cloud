package com.eurlanda.edu.tp.socket.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by test on 2018/5/11.
 */
public class StringUtils {

    private static final Logger logger = LoggerFactory.getLogger(StringUtils.class);
    public static byte[] copyOfRange(byte[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0)
            throw new IllegalArgumentException(from + " > " + to);
        byte[] copy = new byte[newLength];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, newLength));
        return copy;
    }

    public static byte[] copyOfRange2(byte[] original, int from, int length) {
        System.out.println("参数length为：" + length);
        byte[] copy = new byte[length];
        System.arraycopy(original, from, copy, 0,
                Math.min(original.length - from, length));
        return copy;
    }

    public static String bytes2Str(byte[] data) {
        return bytes2Str(data, "UTF-8");
    }

    public static byte[] str2Bytes(String data) {
        return str2Bytes(data, "UTF-8");
    }

    /**
     * 转换字符串到协议字节数组格式
     *
     * @param data 字符串
     * @return 字节数组格式
     * */
    public static final byte[] str2Bytes(String data, String charsetName) {
        logger.trace("convertStringToProtocolDataBytes:" + data);
        try { // 获得字符串编码方式（针对协议数据段）
            return data==null?null:data.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
            return data==null?null:data.getBytes();
        }
    }


    /**
     * 转换协议字节数组为字符串
     *
     * @param data 协议字节数组
     * @return 字符串形式
     * */
    public static final String bytes2Str(byte[] data, String charsetName) {
        try { // 获得字符串编码方式（针对协议数据段）
            return new String(data, 0, data.length, charsetName);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException", e);
            return new String(data);
        }
    }
}
