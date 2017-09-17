package cn.e3mall.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/*
 * 初始化spring容器,证实服务发布与tomcat没有关系
 */
public class TestPublish {
	@Test
	public void publishService() throws Exception{
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		//方式一:死循环
		while(true){
			Thread.sleep(1000);
		}
		//方式二:
		//等待键盘输入,在控制台敲
		//System.in.read();
	}
	@Test
	public void getCPU(){
		int cpuNums = Runtime.getRuntime().availableProcessors();
		System.out.println(cpuNums);
	}
}
