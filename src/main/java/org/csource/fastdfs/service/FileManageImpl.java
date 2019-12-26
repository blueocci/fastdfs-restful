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

/**
 * 文件上传服务层
 */
@Service
public class FileManageImpl {

    @Value("${tempWorkBasePath}")
    private String tempWorkPath;

    @Autowired
    FastDFSClient fastDFSClient;

    /**
     * 生成文件路径
     *
     * @param fileName
     * @return filePath
     */
    public String generateDirectory(String fileName) {
        fileName = StringSimpleUtils.handleFileName(fileName);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        Date date = new Date();
        String ImagReg = "^\\S+\\.gif|\\.GIF|\\.PNG|\\.png|\\.jpg|\\.JPG|\\.JPEG|\\.jpeg|\\.ppt|\\.pptx$";
        String apkReg = "^\\S+\\.apk$";
        String pptReg = "^\\S+\\.ppt|\\.pptx$";
        String videoReg = "^\\S+\\.mp4$";
        String classroomKey = "";
        boolean ImagMatcher = Pattern.compile(ImagReg).matcher(fileName).find();
        boolean apkMatcher = Pattern.compile(apkReg).matcher(fileName).find();
        boolean pptMatcher = Pattern.compile(pptReg).matcher(fileName).find();
        boolean videoMatcher = Pattern.compile(videoReg).matcher(fileName).find();
        if (StringUtils.isNotEmpty(fileName) && apkMatcher) {
            classroomKey = "apk";
        } else if (StringUtils.isNotEmpty(fileName) && pptMatcher) {
            classroomKey = "ppt";
        } else if (StringUtils.isNotEmpty(fileName) && ImagMatcher) {
            classroomKey = "image";
        } else if (StringUtils.isNotEmpty(fileName) && videoMatcher) {
            classroomKey = "video";
        } else {
            classroomKey = "other";
        }
        String key = File.separator + classroomKey + File.separator + sdf.format(date) + File.separator + sdfHour.format(date) + File.separator + fileName;
        return key;
    }

    public String getFileType(String fileName) {
        if (StringUtils.isNotEmpty(fileName) && fileName.contains(".")) {
            String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
            return suffix;
        }
        return "default";
    }

    public String getFileContentType(String fileSuffix) {
        //不同后缀对应的文件类型
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("pptx", "appication/powerpoint;charset=utf-8");
        typeMap.put("ppt", "appication/powerpoint;charset=utf-8");
        typeMap.put("mp4", "video/mpeg4;charset=utf-8");
        typeMap.put("png", "application/x-png;charset=utf-8");
        typeMap.put("PNG", "application/x-png;charset=utf-8");
        typeMap.put("jpg", "application/x-png;charset=utf-8");
        typeMap.put("JPG", "application/x-png;charset=utf-8");
        typeMap.put("jpeg", "application/x-png;charset=utf-8");
        typeMap.put("JPEG", "application/x-png;charset=utf-8");
        typeMap.put("apk", "application/vnd.android.package-archive;charset=utf-8");
        String objectPath = typeMap.get(fileSuffix);
        if (objectPath == null || "".equals(objectPath)) {
            return "application/octet-stream;charset=utf-8";
        }
        return objectPath;
    }


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

    public static String[] extractGroupNameAndFileName(String url) throws Exception {
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
