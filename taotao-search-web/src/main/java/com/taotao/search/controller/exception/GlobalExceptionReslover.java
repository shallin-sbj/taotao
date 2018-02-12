package com.taotao.search.controller.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 *
 * @author sucl
 */
public class GlobalExceptionReslover implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionReslover.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception e) {

        logger.info("into GlobalExceptionReslover ......");
        logger.debug("test handler type :" + handler.getClass());
        e.printStackTrace();  // 控制台打印报错信息
        logger.error("systen error!",e);  // 向日志文件中写入异常
        // 暂时错误信息
        ModelAndView model = new ModelAndView();
        model.addObject("message","sorry service exception.");
        model.setViewName("error/exception");
        return model;
    }
}
