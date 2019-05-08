package com.nyist.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/28/028.
 */
public class FileUtils {
    public static String  uploadFile(MultipartFile file, String filePath, String fileName) throws Exception {
        String pathName=filePath + fileName;
        File dest = new File(pathName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathName;
    }
}
