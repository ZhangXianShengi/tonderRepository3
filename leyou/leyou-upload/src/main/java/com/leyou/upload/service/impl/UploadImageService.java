package com.leyou.upload.service.impl;

import com.leyou.upload.controller.UploadController;
import com.leyou.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadImageService implements UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    //规定文件传输类型，数组
    private static final List<String> suffixes = Arrays.asList("image/x-icon", "image/jpeg","image/gif");
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if(!suffixes.contains(type)){
                logger.info("参数不合法{}"+type);
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求");
                return null;
            }
            // 2、保存图片
            // 2.1、生成保存目录
            File dir = new File("D:\\image\\upload");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 2.2、保存图片
            file.transferTo(new File(dir, file.getOriginalFilename()));
            // 2.3、拼接图片地址
            String url = "http://image.leyou.com/upload/" + file.getOriginalFilename();
            return url;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
