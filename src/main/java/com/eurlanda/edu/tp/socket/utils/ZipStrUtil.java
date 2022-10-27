package com.eurlanda.edu.tp.socket.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by test on 2018/5/11.
 */
public class ZipStrUtil {

    /**
     * 解压的字节
     * @param strs 带解压的字节
     * @return 返回解压后的字符串
     * @throws IOException
     */
    public static byte[] unCompress(byte[] strs) throws IOException {
        if (null == strs || strs.length <= 0) {
            return null;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲区数组
        ByteArrayInputStream in = new ByteArrayInputStream(strs);
        // 使用默认缓冲区大小创建新的输入流
        GZIPInputStream gzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n = 0;
        while ((n = gzip.read(buffer)) >= 0) {// 将未压缩数据读入字节数组
            // 将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte数组输出流
            out.write(buffer, 0, n);
        }
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toByteArray();
    }


    /**
     * 字节的压缩
     * @param lens 带压缩的字节
     * @return 返回压缩后的字节
     * @throws IOException
     */
    public static byte[] compress(byte[] lens) throws IOException {
        if (null == lens || lens.length <= 0) {
            return null;
        }
        // 创建一个新的 byte 数组输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 使用默认缓冲区大小创建新的输出流
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        // 将 b.length 个字节写入此输出流
        gzip.write(lens);
        gzip.close();
        // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
        return out.toByteArray();
    }
}
