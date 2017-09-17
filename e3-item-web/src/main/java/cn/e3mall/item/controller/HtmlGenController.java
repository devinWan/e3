package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/*
 * 生成静态页面测试controller
 */
@Controller
public class HtmlGenController {
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@RequestMapping("/genhtml")
	@ResponseBody//另一个作用,不会跳转到逻辑视图
	public String genHtml() throws Exception{
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		Map data = new HashMap<>();
		//指定文件输出 的路径及文件名
		data.put("hello", 123456);
		Writer out = new FileWriter(new File("D:/tmp/freemarker/hello2.html"));
		//输出文件
		template.process(data, out);
		//关闭流
		out.close();
		return "ok";
	}
}
