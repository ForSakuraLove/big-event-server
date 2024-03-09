package com.pactera.bigevent.controller;

import com.pactera.bigevent.common.entity.base.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @PostMapping
    public Result<String> uploadFile(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        String uuid = filename;
        if (filename != null) {
            uuid = UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
        }
        file.transferTo(new File("D:\\Project\\files" + uuid));
        return Result.success("https://big-event-gwd.oss-cn-beijing.aliyuncs.com/9bf1cf5b-1420-4c1b-91ad-e0f4631cbed4.png");
    }

}
