package com.eurlanda.edu.tp.pdf;

/*import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;*/
import org.apache.log4j.Logger;

import java.io.File;

/**
 * ppt转换pdf
 */
public class ppt2pdfUtil {

    static Logger log = Logger.getLogger(ppt2pdfUtil.class.getName());
    /**
     * ppt转换pdf
     * @param inputFile
     * @param targetFile
     * @return
     */
    public static boolean ppt2pdf(String inputFile,String targetFile){
        /*log.info("开始转换PPT");
        long start = System.currentTimeMillis();
        ActiveXComponent app = null;
        try {
            app = new ActiveXComponent("Powerpoint.Application");
            Dispatch presentations = app.getProperty("Presentations").toDispatch();
            Dispatch presentation = Dispatch.call(presentations,//
                    "Open",
                    inputFile,// FileName
                    true,// ReadOnly
                    true,// Untitled 指定文件是否有标题。
                    false // WithWindow 指定文件是否可见。
            ).toDispatch();

            File tofile = new File(targetFile);
            if (tofile.exists()) {
                tofile.delete();
            }
            Dispatch.call(presentation,//
                    "SaveAs", //
                    targetFile, // FileName
                    32);

            Dispatch.call(presentation, "Close");
            long end = System.currentTimeMillis();
            System.out.println("转换完成..用时：" + (end - start) + "ms.");
        } catch (Exception e) {
            log.error("========Error:文档转换失败：" + e.getMessage());
            return false;
        } finally {
            if (app != null) app.invoke("Quit");
        }*/
        return true;
    }
}
