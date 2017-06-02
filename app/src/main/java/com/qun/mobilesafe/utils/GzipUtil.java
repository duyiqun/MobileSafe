package com.qun.mobilesafe.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {

    // 压缩
    public static void zip(File sourceFile, File targetFile) {
        // GzipOutPutStream 进行压缩操作即可
        FileInputStream fis = null;
        GZIPOutputStream gos = null;
        try {
            fis = new FileInputStream(sourceFile);
            gos = new GZIPOutputStream(new FileOutputStream(targetFile));
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                gos.write(buffer, 0, len);
            }
            gos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIOs(fis, gos);
        }
    }

    // 解压
    public static void unZip(File sourceFile, File targetFile) {
        // GzipInputStream 解压即可
        GZIPInputStream gis = null;
        FileOutputStream fos = null;
        try {
            gis = new GZIPInputStream(new FileInputStream(sourceFile));
            fos = new FileOutputStream(targetFile);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIOs(gis, fos);
        }
    }

    // 解压
    public static void unZip(InputStream inputStream, File targetFile) {
        // GzipInputStream 解压即可
        GZIPInputStream gis = null;
        FileOutputStream fos = null;
        try {
            gis = new GZIPInputStream(inputStream);
            fos = new FileOutputStream(targetFile);
            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = gis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIOs(gis, fos);
        }
    }

    public static void closeIOs(Closeable... ios) {
        for (int i = 0; i < ios.length; i++) {
            Closeable closeable = ios[i];
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
