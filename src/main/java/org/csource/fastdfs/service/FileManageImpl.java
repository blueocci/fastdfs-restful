package org.csource.fastdfs.service;

import org.csource.fastdfs.model.FastDFSFile;
import org.csource.fastdfs.utils.FastDFSClient;
import org.csource.fastdfs.utils.StringSimpleUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileManageImpl {

    @Autowired
    FastDFSClient fastDFSClient;

    public String saveFile(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath = {};
        String fileName = multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = null;
        InputStream inputStream = multipartFile.getInputStream();
        if (inputStream != null) {
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext, "");
        try {
            fileAbsolutePath = fastDFSClient.upload(file);  //upload to fastdfs
        } catch (Exception e) {
            System.out.println("upload file Exception!");
        }
        if (fileAbsolutePath == null) {
            System.out.println("upload file failed,please upload again!");
            return "";
        }
        String path = fastDFSClient.getTrackerUrl() + fileAbsolutePath[0] + "/" + fileAbsolutePath[1];
        return path;
    }

    private static String[] extractGroupNameAndFileName(String url) throws Exception {
        String[] ret = {"",""};
        Pattern p = Pattern.compile("http://[^/]+/([^/]+)/(.*)");
        Matcher m = p.matcher(url);
        if(m.matches()) {
            ret[0] = m.group(1);
            ret[1] = m.group(2);
        }else{
            throw new Exception("Not Matched");
        }
        return ret;
     }

    public InputStream getFastDfsFileStream(String url) throws Exception {
        //http://112.19.241.225:10080/group1/M00/00/02/Cn9ejF38P4GAVL0cAAEMnbAZnj402.pptx
        String[] ret = extractGroupNameAndFileName(url);
        return fastDFSClient.downFile(ret[0],ret[1]);
    }

}
