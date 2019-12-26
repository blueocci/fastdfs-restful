package org.csource.fastdfs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
public class FastDFSFile {
    private String name;
    private byte[] Content;
    private String ext;
    private String author;
}
