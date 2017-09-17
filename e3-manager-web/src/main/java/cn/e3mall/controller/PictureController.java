package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class PictureController {
	//加载配置文件中的key
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	@RequestMapping(value="/pic/upload",produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	//为了解决兼容性,返回string,@ResponseBody如果返回对象的话,就会转换为json
	//如果返回的是字符串,相当于直接response.getWriter.write(""),响应头为纯文本格式
	public String uploadFile(MultipartFile uploadFile){
		Map result = null;
		try {
			//把图片上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/client.conf");
			//取文件扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			//得到一个图片的地址和文件名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//补充为完整的url
			url = IMAGE_SERVER_URL+url;
			//封装到map中返回
			 result = new HashMap<>();
			result.put("error", 0);
			result.put("url",url);
		} catch (Exception e) {
			e.printStackTrace();
			 result = new HashMap<>();
			result.put("error", 1);
			result.put("message","图片上传失败");
		}
		return JsonUtils.objectToJson(result) ;
		//return result;
	}
}
