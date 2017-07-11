package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	//创建一个logger对象
	private static Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.debug("随便测试的日志信息。。。。。。");
		logger.info("系统发生异常，已经进入全局异常处理器");
		
		//写日志文件
		logger.error("系统发生异常", ex);
		//控制台打印错误消息
		ex.printStackTrace();
		//发邮件、发短信
		//Jmail:可以查找相关的资料
		//展示错误页面
		ModelAndView modelAndView = new ModelAndView();
		//设置返回逻辑视图
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
