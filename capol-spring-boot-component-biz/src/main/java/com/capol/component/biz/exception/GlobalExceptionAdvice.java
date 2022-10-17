package com.capol.component.biz.exception;

import com.capol.component.framework.exception.ServiceException;
import com.capol.component.framework.response.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 系统异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult handlerException(HttpServletRequest request, Exception e) {
        log.error("发生未知异常, " + e.getMessage());
        return CommonResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 自定义服务异常
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public CommonResult handlerHttpException(HttpServletRequest request, ServiceException e) {
        log.error("服务异常, " + e.getMessage());
        return CommonResult.error(e.getCode(), e.getMessage());
    }

    /**
     * 预设全局参数绑定
     *
     * @param model
     */
    @ModelAttribute
    public void presetParam(Model model) {
        model.addAttribute("globalAttr", "这是全局异常参数绑定值");
    }
}
