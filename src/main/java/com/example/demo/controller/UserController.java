package com.example.demo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import util.aes.AesUtil;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    // 注入用户Service
    @Resource
    private UserService userService;
    
    @ApiOperation("用户登录")
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String findUserByName(@RequestParam Map<String, Object> map,HttpSession session) {
        JSONObject paramObj=AesUtil.GetParam(map);
        String username = (String) paramObj.get("username");
        String password = (String) paramObj.get("password");
        Map<String, Object> userMap = userService.findUserByName(username,password);
        JSONObject jsonObject = JSONObject.fromObject(userMap);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
    
    @ApiOperation("更新用户登录密码")
    @RequestMapping(value="/saveNewPassWord",method=RequestMethod.POST)
    public String saveNewPassWord(@RequestParam Map<String, Object> map){
        JSONObject paramObj=AesUtil.GetParam(map);
        JSONObject obj = new JSONObject();
        //Map<String, Object> resultMap = new HashMap<String, Object>();
        String id = (String) paramObj.get("id");
        String oldPassword = (String) paramObj.get("oldPassword");
        String newPassword = (String) paramObj.get("newPassword");
        System.out.println("oldPassword");
        System.out.println(oldPassword);
        System.out.println("newPassword");
        System.out.println(newPassword);
        try{
            if(userService.findUserPwdById(id).equals(oldPassword)){
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("id", id);              
                paramMap.put("newPassword", newPassword);
                userService.saveNewPassWord(paramMap);
                //resultMap.put("status", "success");
                //resultMap.put("msg", "密码保存成功!");
                obj.put("result", "success");
                obj.put("reason", "密码保存成功!");
                //obj.put("resultMap", resultMap);
            } else{
                obj.put("result", "failure");
                obj.put("reason", "原密码错误");
            }
        } catch (Exception e) {
            obj.put("result", "failure");
            obj.put("reason", "修改密码失败");
        }
        JSONObject jsonObject = JSONObject.fromObject(obj);
        String enResult = AesUtil.enCodeByKey(jsonObject.toString());
        return enResult;
    }
}
