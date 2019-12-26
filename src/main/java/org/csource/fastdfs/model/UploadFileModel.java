package org.csource.fastdfs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileModel {
    private int state;
    private String message;
    private String url;
    //文件大小
    private long fileSize;
    //文件名
    private String fileName;
    //文件版本
    private String version;
    //文件包名
    private String pkgName;
}
