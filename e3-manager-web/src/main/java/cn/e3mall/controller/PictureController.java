package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import cn.e3mall.common.utils.FastDFSClient;

@Controller
public class PictureController {

	@Value("${imageserver.url}")
	private String imageServerUrl;

	@RequestMapping("/pic/upload")
	@ResponseBody
	public Map uploadPicture(MultipartFile uploadFile) {
		
		try {
			//获取文件扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//创建一个FastDFSClient(工具，用于上传文件)
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//执行上传处理
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//拼接返回的url和ip地址，拼接成完整的url
			String url = imageServerUrl+path;
			//5、返回map
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			//5、返回map
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return result;

		}

	}
}
