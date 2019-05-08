package com.nyist.utils;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import java.io.*;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 利用jodconverter(基于OpenOffice服务)将文件(*.doc、*.docx、*.xls、*.ppt)转化为html格式或者pdf格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 *
 * @author yjclsx
 */

/**
 * 利用jodconverter(基于OpenOffice服务)将文件(*.doc、*.docx、*.xls、*.ppt)转化为html格式或者pdf格式，
 * 使用前请检查OpenOffice服务是否已经开启, OpenOffice进程名称：soffice.exe | soffice.bin
 *
 * @author yjclsx
 */
public class Doc2HtmlUtil {

    private static Doc2HtmlUtil doc2HtmlUtil;

    /**
     * 获取Doc2HtmlUtil实例
     */
    public static synchronized Doc2HtmlUtil getDoc2HtmlUtilInstance() {
        if (doc2HtmlUtil == null) {
            doc2HtmlUtil = new Doc2HtmlUtil();
        }
        return doc2HtmlUtil;
    }
    /**
     * 转换文件成pdf
     *
     * @param fromFileInputStream:
     * @throws IOException
     */
    public InputStream file2pdf(InputStream fromFileInputStream, String toFilePath,String type,String fileOtherName) throws IOException {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timesuffix = sdf.format(date);
        String docFileName = null;
        String htmFileName = null;
        if(".doc".equals(type)){
            docFileName = fileOtherName + timesuffix + ".doc";
            htmFileName = fileOtherName + timesuffix + ".pdf";
        }else if(".docx".equals(type)){
            docFileName = fileOtherName + timesuffix + ".docx";
            htmFileName = fileOtherName + timesuffix + ".pdf";
        }else if(".xls".equals(type)){
            docFileName = fileOtherName + timesuffix + ".xls";
            htmFileName = fileOtherName + timesuffix + ".pdf";
        }else if(".ppt".equals(type)){
            docFileName = fileOtherName + timesuffix + ".ppt";
            htmFileName = fileOtherName + timesuffix + ".pdf";
        }else{
            return null;
        }

        File htmlOutputFile = new File(toFilePath + File.separatorChar + htmFileName);
        File docInputFile = new File(toFilePath + File.separatorChar + docFileName);
        if (htmlOutputFile.exists())
            htmlOutputFile.delete();
        htmlOutputFile.createNewFile();
        if (docInputFile.exists())
            docInputFile.delete();
        docInputFile.createNewFile();
        /**
         * 由fromFileInputStream构建输入文件
         */
        try {
            OutputStream os = new FileOutputStream(docInputFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024 * 8];
            while ((bytesRead = fromFileInputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.close();
            fromFileInputStream.close();
        } catch (IOException e) {
        }

        OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
        try {
            connection.connect();
        } catch (ConnectException e) {
            System.err.println("文件转换出错，请检查OpenOffice服务是否启动。");
        }
        // convert
        DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
        converter.convert(docInputFile, htmlOutputFile);
        connection.disconnect();
        // 转换完之后删除word文件
        docInputFile.delete();
        InputStream ftpInputStream=new FileInputStream(htmlOutputFile);

        return ftpInputStream;
    }

/*    public static void main(String[] args) throws IOException {
        Doc2HtmlUtil coc2HtmlUtil = getDoc2HtmlUtilInstance ();
        File file = null;
        FileInputStream fileInputStream = null;

        file = new File("C:/Users/Administrator/Desktop/xxx.doc");
        fileInputStream = new FileInputStream(file);

        coc2HtmlUtil.file2pdf(fileInputStream, "E:/360","doc");

    }*/

}
