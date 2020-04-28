package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import com.example.demo.service.UserService;

import java.util.Map;
import net.sf.json.JSONObject;
import util.aes.AesUtil;

@RestController
@RequestMapping(value = "/h1")
public class HelloCotroller {

    // 注入用户Service
    @Resource
    private UserService userService;

    @RequestMapping(value = "/hello")
    public String index() {
        System.out.println("123456");
        return "hello worldzhangheng!";
    }
    
    @RequestMapping(value="/hello1")
    public String  index1(){
        Map<String, Object> userMap = this.userService.findUserByName("admin", "e10adc3949ba59abbe56e057f20f883e");   
        JSONObject jsonObject = JSONObject.fromObject(userMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }

}