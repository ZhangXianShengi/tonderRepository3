package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Service
public class UploadService {

    private static final List<String> CONTENY_TYPE = Arrays.asList("image/jpeg","image/x-icon","image/gif");

    //日志打印
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
    @Autowired
    FastFileStorageClient storageClient;
    public String uploadImage(MultipartFile file) {
        //获取上传文件的原始名称
        String originalFilename = file.getOriginalFilename();

        try {
            //检验上传的文件类型
            //获取文件的媒体类型
            String contentType = file.getContentType();
            if (!CONTENY_TYPE.contains(contentType)){
                LOGGER.error("文件类型不合法：｛｝"+originalFilename);
                return null;
            }

            //效验上传文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                LOGGER.info("文件内容不合法：｛｝"+originalFilename);
                return null;
            }

            //保存到服务器
            //file.transferTo(new File("C:\\Users\\liang\\Pictures\\image\\"+originalFilename));

           //return "http://image.leyou.com/"+originalFilename;

            //通过storagecLient把图片上传到fastDFS
            String ext = StringUtils.substringAfterLast(originalFilename, ".");//后缀名
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), ext, null);
            //返回url，进行回显
            return "http://image.leyou.com/"+storePath.getFullPath();//完整路径
        } catch (IOException e) {
            LOGGER.info("服务器累了，请待会儿再试!");
            e.printStackTrace();
        }
        return null;
    }
}












































