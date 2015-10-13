/*
 * @(#)SpringExceptionInterceptor.java    Created on 2015年1月19日
 * Copyright (c) 2015 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package com.clj.panda.web.interceptors;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;


/**
 * 不必在Controller中对异常进行处理，抛出即可，由此异常解析器统一控制。<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 * */
public class PandaExceptionInterceptor extends ExceptionHandlerExceptionResolver {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String defaultErrorView = "404";

    public String getDefaultErrorView() {
        return defaultErrorView;
    }

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response,
            HandlerMethod handlerMethod, Exception e) {
        String callback = request.getParameter("callback");
        if (handlerMethod == null) {
            return null;
        }
        Method method = handlerMethod.getMethod();
        if (method == null) {
            return null;
        }
//        if (StringUtils.isNotBlank(callback)) {
//            return new ModelAndView(defaultErrorView);
//        }
//        try {
//            response.setContentType("application/json; charset=utf-8");
//            PrintWriter writer = response.getWriter();
//            Result result = new Result();
//            if (e instanceof TodoException) {
//                logger.error(e.getMessage());
//                result.setCode(((TodoException) e).getErrorCode());
//                result.setResult(e.getCause()==null?e.getMessage():e.getCause().getMessage());
//            }
//            else if (e instanceof org.springframework.dao.DuplicateKeyException) {
//                logger.error(e.getMessage());
//                result.setCode(TodoCode.ERROR_REPEAT);
//            }
//            else if (e.getCause() instanceof java.io.WriteAbortedException) {
//                logger.error(e.getMessage());
//                result.setCode(TodoCode.INPUT_ERROR);
//            }
//            else if (e.getCause() instanceof IllegalStateException) {
//                logger.error(e.getMessage());
//                result.setCode(TodoCode.INPUT_ERROR);
//            }
//            else if (e.getCause() instanceof java.net.ConnectException) {
//                logger.error(e.getMessage());
//                result.setCode(TodoCode.ACTION_NOT_EXIST);
//            }
//            else {
//                e.printStackTrace();
//                result.setCode(TodoCode.UNKNOW);
//            }
//            writer.write(JSON.toJSONString(result));
//            return new ModelAndView();
//        }
//        catch (IOException ioe) {
//            logger.info("exception io error");
//        }
//        finally {
//            try {
//                response.getWriter().close();
//            }
//            catch (IOException e1) {
//                logger.info("exception close error");
//            }
//        }
        return new ModelAndView(defaultErrorView);
    }
}
