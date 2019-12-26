package org.csource.fastdfs.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;

public class StringSimpleUtils {
    //String数组转为long数组
    public static long[] StringArrToLongArr(String[] arrs) {
        if (null == arrs || arrs.length == 0) {
            return null;
        }
        long[] ints = new long[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            ints[i] = Long.parseLong(arrs[i]);
        }
        return ints;
    }

    //String数组转为int数组
    public static int[] StringArrToIntArr(String[] arrs) {
        if (null == arrs || arrs.length == 0) {
            return null;
        }
        int[] ints = new int[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            ints[i] = Integer.parseInt(arrs[i]);
        }
        return ints;
    }

    public static String fetchFileNamePart(String url, String defaultName) {
        String fileName = defaultName;
        if (url != null) {
            int ind = url.lastIndexOf("/");
            if ((ind >= 0 && ind < url.length() - 5 && url.toLowerCase().endsWith(".ppt")) || (ind >= 0 && ind < url.length() - 6 && url.toLowerCase().endsWith(".pptx"))) {
                fileName = url.substring(url.lastIndexOf("/") + 1);
            }
        }
        return fileName;
    }

    public static String getFileName(String url, String defaultName) {
        String fileName = defaultName;
        if (StringUtils.isNotEmpty(url)) {
            File tempFile = new File(url.trim());
            fileName = tempFile.getName();
        }
        return fileName;
    }

    public static String handleFileName(String filename) {
        if (StringUtils.isNotEmpty(filename)) {
            int unixSep = filename.lastIndexOf('/');
            int winSep = filename.lastIndexOf('\\');
            int pos = (winSep > unixSep ? winSep : unixSep);
            if (pos != -1) {
                // Any sort of path separator found...
                filename = filename.substring(pos + 1);
            }
        }
        return filename;
    }
}
