package com.leyou.upload.service;

import com.leyou.upload.controller.UploadController;
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
public class UploadService {
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    // 支持的文件类型
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");
    public String upload(MultipartFile file)  {
        //获取上传文件的原始名称
        String originalFilename = file.getOriginalFilename();

        try {
            //获取文件的媒体名称1)校验文件类型
            String contentType = file.getContentType();
            if (!suffixes.contains(contentType)) {
                logger.info("上传失败，文件类型不匹配：{}"+originalFilename);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                logger.info("上传失败，文件内容不符合要求：{}"+originalFilename);
                return null;
            }
            // 2.2、保存图片
            file.transferTo(new File("D:\\Users\\image\\"+originalFilename));
            return "http://image.leyou.com/"+originalFilename;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("服务器累了，请稍后重试"+originalFilename);
        }
        return null;
    }
}
