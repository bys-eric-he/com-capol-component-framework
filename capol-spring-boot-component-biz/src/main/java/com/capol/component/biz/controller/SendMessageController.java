package com.capol.component.biz.controller;

import com.capol.component.framework.core.CapolRabbitMQTemplate;
import com.capol.component.framework.exception.ServiceException;
import com.capol.component.framework.exception.enums.GlobalErrorCodeConstants;
import com.capol.component.framework.idempotent.core.annotation.Idempotent;
import com.capol.component.framework.response.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/send")
public class SendMessageController {

    @Autowired
    private CapolRabbitMQTemplate capolRabbitMQTemplate;

    @Idempotent(timeout = 10)
    @GetMapping("/message")
    public CommonResult<String> send() {
        CommonResult result = new CommonResult();
        try {
            capolRabbitMQTemplate.send("2022091706360001", "BIZ-这是Controller发送过来的消息!!!!");
            result = CommonResult.success(null, "消息发送成功!");
        } catch (ServiceException serviceException) {
            result = CommonResult.error(GlobalErrorCodeConstants.LOCKED.getCode(), serviceException.getMessage());
        }
        return result;
    }
}
