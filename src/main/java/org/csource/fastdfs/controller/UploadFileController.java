package org.csource.fastdfs.controller;


import org.csource.fastdfs.model.DownloadFileResponse;
import org.csource.fastdfs.model.UploadFileModel;
import org.csource.fastdfs.service.FileManageImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;

@Slf4j
@RestController
@PropertySource("classpath:application.yaml")
@RequestMapping(value = "api")
public class UploadFileController {

    private FileManageImpl fileManage;

    private MessageSource messages;

    @Autowired
    public void setFileManage(FileManageImpl fileManage) {
        this.fileManage = fileManage;
    }

    @ApiOperation(value = "ppt上传", notes = "ppt上传")
    @RequestMapping(path = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileModel pptUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new UploadFileModel(500, "传入的文件为空！", "", 0, null, null, null);
        }
        try {
            String path = fileManage.saveFile(file);
            if (StringUtils.isNotEmpty(path)) {
                return new UploadFileModel(200, "上传成功", path, 0, file.getOriginalFilename(), null, null);
            } else {
                return new UploadFileModel(500, "上传失败，请重新上传！", "", 0, null, null, null);
            }
        } catch (Exception e) {
            log.error("upload file failed",e);
        }
        return new UploadFileModel(500, "", "", 0, null, null, null);
    }

    @ApiOperation(value = "下载文件", notes = "下载文件")
    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public DownloadFileResponse downloadPPT(@RequestParam(value = "url", required = true) String url,
                                            HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            OutputStream out = null;
            InputStream inputStream = fileManage.getFastDfsFileStream(url);
            int length = inputStream.available();
            //设置发送到客户端的响应内容类型
            response.setContentType("appication/powerpoint;charset=utf-8");
            String fileName = null;
            if(fileName == null || StringUtils.isEmpty(fileName)){
                fileName = "讲课ppt.ppt";
            }
            response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            response.addIntHeader("Content-Length", length);
            out = response.getOutputStream();
            IOUtils.copy(inputStream,out);
            if (inputStream != null) {
                inputStream.close();
            }
            if (out != null) {
                out.close();
            }
            return new DownloadFileResponse(200, "下载成功！");
        } catch (Exception e) {
            log.error("",e);
            return new DownloadFileResponse(500, "系统出错!请重试");
        }
    }

}
