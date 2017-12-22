package com.taotao.controller;

import com.taotao.common.utlis.JsonUtils;
import com.taotao.utls.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传解析器
 * @author sucl
 */
@Controller
public class PictureController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String  picUpload(MultipartFile uploadFile) {
        try {
            //1. 接收上传文件名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            //2. 取扩展名
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
            //3. 上传到图片服务器
            String url = IMAGE_SERVER_URL + fastDFSClient.uploadFile(uploadFile.getBytes(), extName);

            //4. 响应上传图片的url
            Map result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map result = new HashMap <>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }


    }
}
