package com.senpure.snowflake.controller;

import com.senpure.base.result.ResultMap;
import com.senpure.base.spring.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ErrorController
 *
 * @author senpure
 * @time 2019-03-12 14:44:22
 */
@ControllerAdvice
@Controller
public class ErrorController extends BaseController implements org.springframework.boot.web.servlet.error.ErrorController {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultMap error(HttpServletRequest request, Exception e) {
        logger.error(request.getMethod() + ": " + request.getRequestURI() + " 服务器未知错误", e);
        ResultMap result = ResultMap.dim();
        return wrapMessage(request, result);
    }

    @RequestMapping(value = "/error")
    @ResponseBody
    public ResultMap notFind(HttpServletRequest request) {
        String path = (String) request.getAttribute("javax.servlet.error.request_uri");
        logger.warn("{}:{} 服务器服务器未找到", request.getMethod(), path);
        return wrapMessage(request, ResultMap.notExist(), path);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
