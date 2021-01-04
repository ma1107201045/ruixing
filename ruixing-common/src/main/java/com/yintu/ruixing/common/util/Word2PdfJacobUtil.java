package com.yintu.ruixing.common.util;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * @Author Mr.liu
 * @Date 2021/1/4 10:50
 * @Version 1.0
 * 需求:
 */
public class Word2PdfJacobUtil {

        /* 转PDF格式值 */
        private static final int wdFormatPDF = 17;
        /**
         * Word文档转换
         *
         * @param inputFile
         * @param pdfFile
         */
        public static boolean word2PDF(String inputFile, String pdfFile) {
            ComThread.InitMTA(true);
            long start = System.currentTimeMillis();
            ActiveXComponent app = null;
            Dispatch doc = null;
            try {
                app = new ActiveXComponent("Word.Application");// 创建一个word对象
                app.setProperty("Visible", new Variant(false)); // 不可见打开word
                app.setProperty("AutomationSecurity", new Variant(3)); // 禁用宏
                Dispatch docs = app.getProperty("Documents").toDispatch();// 获取文挡属性

                System.out.println("打开文档 >>> " + inputFile);
                // Object[]第三个参数是表示“是否只读方式打开”
                // 调用Documents对象中Open方法打开文档，并返回打开的文档对象Document
                doc = Dispatch.call(docs, "Open", inputFile, false, true).toDispatch();
                System.out.println("转换文档 [" + inputFile + "] >>> [" + pdfFile + "]");
                // 调用Document对象的SaveAs方法，将文档保存为pdf格式
                // word保存为pdf格式宏，值为17
                Dispatch.call(doc, "SaveAs", pdfFile, wdFormatPDF);// word保存为pdf格式宏，值为17

                long end = System.currentTimeMillis();

                System.out.println("用时：" + (end - start) + "ms.");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("========Error:文档转换失败：" + e.getMessage());
            } finally {
                Dispatch.call(doc, "Close", false);
                System.out.println("关闭文档");
                if (app != null)
                     app.invoke("Quit", new Variant[] {});
                    //app.invoke("Quit",new Variant(17));
                // 如果没有这句话,winword.exe进程将不会关闭
                ComThread.Release();
                ComThread.quitMainSTA();
            }
            return false;
        }





    public static void main(String[] arg){
        String docPath = "D:\\jar\\日兴软件档案管理使用手册(1).docx";
        String pdfPath = "D:\\jar\\日兴软件档案管理使用手册(1).pdf";
        boolean res = Word2PdfJacobUtil.word2PDF(docPath, pdfPath);
        System.out.println(res);
    }

}
