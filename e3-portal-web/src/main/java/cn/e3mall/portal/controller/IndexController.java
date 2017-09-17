package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/*
 * 首页展示
 * handler与controller的区别?controller是类,handler的只得方法
 */
@Controller
public class IndexController {
	//取出资源文件中的属性
	@Value("${CONTENT_LUNBO}")
	private Long CONTENT_LUNBO;
	@Autowired
	private ContentService contentService;
	
	//web.xml中配置的是.html  index.html后缀是可以省略的
	@RequestMapping("/index")
	public String showIndex(Model model){
		//查询内容列表
		List<TbContent> ad1List = contentService.findByCategoryId(CONTENT_LUNBO);
		//把结果传递给页面,需要用到Model
		model.addAttribute("ad1List", ad1List);
		return "index";
	}
	
}
