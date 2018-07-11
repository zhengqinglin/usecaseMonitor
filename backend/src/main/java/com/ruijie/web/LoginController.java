package com.ruijie.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    /**
     *  验证账号和密码
     */
    @RequestMapping(value = "/login")
    public Object login(){
        return null;
    }
}
