package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @program: ohhhh
 * @description: 测试
 * @author: wangnie
 * @create: 2022-10-13 20:59
 **/

@RequestMapping("/demo")
@RestController
public class DemoController {

    @GetMapping("/demo1")
    public String demo1() {
        return "demo1";
    }


    @GetMapping("/")
    public String uid(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UUID uid = (UUID) session.getAttribute("uid");
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);
        return uid.toString() + " : " + session.getId();
    }
}
