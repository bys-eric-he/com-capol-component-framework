package com.capol.component.biz.controller;

import com.capol.component.framework.core.CapolRabbitMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/send")
public class SendMessageController {

    @Autowired
    private CapolRabbitMQTemplate capolRabbitMQTemplate;

    @GetMapping("/message")
    public void send() {
        capolRabbitMQTemplate.send("2022091706360001", "BIZ-这是Controller发送过来的消息!!!!");
    }
}
