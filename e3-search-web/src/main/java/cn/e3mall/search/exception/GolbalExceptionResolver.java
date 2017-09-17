package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/*
 * 全局异常处理器
 */
public class GolbalExceptionResolver implements HandlerExceptionResolver{
	//注意使用slf4j中的类和方法
	private static final Logger logger = LoggerFactory.getLogger(GolbalExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		//打印控制台
		ex.printStackTrace();
		//写日志
		logger.debug("测试输出的日志...");
		logger.info("系统发生异常了");
		logger.error("系统发生异常", ex);
		//发邮件.发短信
		//使用jmail工具包,发送短信使用第三方的webservice
		//显示错误页面
		ModelAndView mv = new ModelAndView();
		mv.setViewName("error/exception");
		return mv;
	}
	
}
