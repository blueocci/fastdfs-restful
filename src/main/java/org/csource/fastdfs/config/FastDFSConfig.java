package org.csource.fastdfs.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.csource.fastdfs.ClientGlobal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j @NoArgsConstructor @Data
@Configuration
@PropertySource("classpath:/${application.name}/fastdfs.properties")
public class FastDFSConfig {

    @Autowired
    Environment env;

    @Value("${fastdfs.tracker_http_host}")
    private String httpHost;

    FastDFSConfig fastDfsConfig(){
        try {
            String appName = env.getProperty("application.name");
            ClientGlobal.initByProperties("/"+appName+"/fdfs.properties");
        } catch (Exception e) {
            log.error("FastDFS Client Init Fail!", e);
        }
        FastDFSConfig ret = new FastDFSConfig();
        return ret;
    }
}
