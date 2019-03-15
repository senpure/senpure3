package com.senpure.snowflake.controller;

import com.senpure.base.result.ResultMap;
import com.senpure.base.spring.BaseController;
import com.senpure.snowflake.criteria.ServerCenterAndWorkCriteriaStr;
import com.senpure.snowflake.model.ServerCenterAndWork;
import com.senpure.snowflake.result.ServerCenterAndWorkRecordResult;
import com.senpure.snowflake.service.SnowflakeDispathService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * SnowflakeDispatchController
 *
 * @author senpure
 * @time 2019-03-12 11:40:26
 */
@Controller
public class SnowflakeDispatchController extends BaseController {


    @Autowired
    private SnowflakeDispathService dispathService;

    private String a = "a";
    private ErrorController errorController = new ErrorController();



    @GetMapping("snowflake/dispatch")
    @ResponseBody
    @ApiResponses(@ApiResponse(code = 200, message = "OK", response = ServerCenterAndWorkRecordResult.class))
    public ResultMap dispatch(HttpServletRequest request, @Valid @ModelAttribute("criteria") ServerCenterAndWorkCriteriaStr criteria, BindingResult validResult) {
        if (validResult.hasErrors()) {
            return incorrect(request, validResult);
        }

        ServerCenterAndWork serverCenterAndWork = dispathService.dispatch(criteria.getServerName(), criteria.getServerKey());
        if (serverCenterAndWork == null) {
            return wrapMessage(request, ResultMap.dim());
        }
        return ResultMap.success().put("serverCenterAndWork", serverCenterAndWork);
    }
}
