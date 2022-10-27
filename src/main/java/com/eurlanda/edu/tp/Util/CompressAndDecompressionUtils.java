package com.eurlanda.edu.tp.Util;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.*;

/**
 * Created by test on 2018/5/7.
 */
public class CompressAndDecompressionUtils {

    static final int BUFFER = 8192;
    public static FileSystem hdfs;
    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CompressAndDecompressionUtils.class.getName());


    public static OutputStream compress(List<String> srcPaths, OutputStream outputStream) throws IOException {


        ZipOutputStream zipOut = null;
        try {
            //out = new FileOutputStream(dstFile);
            CheckedOutputStream cos = new CheckedOutputStream(outputStream, new CRC32());
            zipOut = new ZipOutputStream(cos);
            String baseDir = "";
            for (String srcPath : srcPaths) {
                File srcFile = new File(srcPath);
                if (!srcFile.exists()) {
                    log.error("该文件夹不存在" + srcPath);
                    //throw new FileNotFoundException(srcPath + "不存在！");
                    continue;
                }
                compress(srcFile, zipOut, baseDir);
            }
        } finally {

        }
        return zipOut;
    }


   /* public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        System.setProperty("hadoop.home.dir", "/usr/local/hadoop-2.6.0");
        String hdfsIpAndPort = "192.168.137.175:8020";
        configuration.set("fs.defaultFS","hdfs://" + hdfsIpAndPort);
        hdfs = hdfs.get(configuration);
        Integer repositoryId = 8;
        String path = "/udata/u" + repositoryId;
        String toPath = "C:\\uploadFiles";
        String src1 = "C:\\uploadFiles\\image\\1525503663812";
        String dsPath = "C:\\uploadFiles\\new.zip";
        List<String> srcs = new ArrayList<>();
        srcs.add(src1);
        FileOutputStream out = null;
        ZipOutputStream zipOut = null;
        try{

        List<OutputStream> outputStreams = compress(srcs,dsPath);
        out = (FileOutputStream)outputStreams.get(0);
        zipOut = (ZipOutputStream)outputStreams.get(1);
        String json = "[{\"gender\":\"male\",\"name\":\"jack\"},{\"gender\":\"male1\",\"name\":\"jack1\"},{\"gender\":\"male2\",\"name\":\"jack2\"}]";
        byte[] courseDataStrBytes = json.getBytes();
        ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(courseDataStrBytes);
        CompressAndDecompressionUtils.compressFileByInputStream(tInputStringStream,zipOut,"data.json");
        download(path,zipOut,"");
        }finally {
            if(zipOut != null){
                zipOut.close();
                out = null;
            }
            if(out != null){
                out.close();
            }
        }

    }*/


    public static void compress(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (file.isDirectory()) {
            compressDirectory(file, zipOut, baseDir);
        } else {
            compressFile(file, zipOut, baseDir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream zipOut, String baseDir) throws IOException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOut, baseDir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream zipOut, String baseDir) throws IOException {
        if (!file.exists()) {
            return;
        }

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(baseDir + file.getName());
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        } finally {
            if (null != bis) {
                bis.close();
            }
        }
    }


    /**
     * 在压缩文件流中加入新的文件流
     */
    public static void compressFileByInputStream(InputStream bis, ZipOutputStream zipOut, String fileName) throws IOException {
        if (bis == null) {
            return;
        }

        try {
            //bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(fileName);
            zipOut.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                zipOut.write(data, 0, count);
            }

        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        } finally{
            if (null != bis) {
                bis.close();
            }
        }
    }


    public static void decompress(String zipFile, String dstPath) throws IOException {
        File pathFile = new File(dstPath);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = null;
            OutputStream out = null;
            try {
                in = zip.getInputStream(entry);
                String outPath = (dstPath + "/" + zipEntryName).replaceAll("\\*", "/");
                ;
                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }

                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
            } finally {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }
            }
        }
    }




    public static void downloadFile(String srcPath,ZipOutputStream zipOut,String name) throws IOException {
        FSDataInputStream in = null;
        //FileOutputStream out = null;
        try {
            in = hdfs.open(new Path(srcPath));
            //IOUtils.copyBytes(in, out, 4096, false);
            compressFileByInputStream(in,zipOut,name);
        } finally {
            IOUtils.closeStream(in);
            //IOUtils.closeStream(out);
        }
    }

    public static void downloadFolder(String srcPath,ZipOutputStream zipOut) throws IOException {
        Path path = new Path(srcPath);
        if(!hdfs.exists(path)){
            log.error("hdfs路径不存在！path:" + path);
            return;
        }
        FileStatus[] srcFileStatus = hdfs.listStatus(path);
        Path[] srcFilePath = FileUtil.stat2Paths(srcFileStatus);
        if(srcFilePath.length == 0){
            //空的目录，创建文件夹
            ZipEntry entry = new ZipEntry(srcPath + "/");
            zipOut.putNextEntry(entry);
        }
        for (int i = 0; i < srcFilePath.length; i++) {
            Path newPath = srcFilePath[i];
            String srcFile = newPath.toString();
            int fileNamePosi = srcFile.lastIndexOf('/');
            String fileName = srcFile.substring(fileNamePosi + 1);
            download(srcPath + '/' + fileName,zipOut,srcPath + "/");
        }
    }

    public static void setHdfsConfig(Configuration config){
        try{
            hdfs = hdfs.get(config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void download(String srcPath,ZipOutputStream zipOut,String baseDir) throws IOException {
        Path path = new Path(srcPath);
        if (hdfs.isFile(path)) {
            downloadFile(srcPath,zipOut,baseDir + path.getName());
        } else {
            downloadFolder(srcPath,zipOut);
        }
    }

    public static boolean deleteFile(String delpath) throws Exception {
        try {

            File file = new File(delpath);
            if(!file.exists()){
                log.error("deleteFile该文件不存在!path：" + delpath);
                return true;
            }
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.getAbsoluteFile().delete();
                        System.out.println(delfile.getAbsolutePath() + "删除文件成功");
                    } else if (delfile.isDirectory()) {
                        deleteFile(delpath + "\\" + filelist[i]);
                    }
                }
                System.out.println(file.getAbsolutePath()+"删除成功");
                file.getAbsoluteFile().delete();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.info("deletefile() Exception:" + e.getMessage(),e);
        }
        return true;
    }

    public static void main(String[] args) throws Exception{
        String path = "C:\\uploadFiles\\cdatahdfsTmp\\tx_logo.png";
        String path1 = "C:\\uploadFiles\\cdatahdfsTmp";
        CompressAndDecompressionUtils.deleteFile(path1);
      /*  File file = new File(path);
        boolean flag = file.delete();
        System.out.println(111);*/
    }




}
