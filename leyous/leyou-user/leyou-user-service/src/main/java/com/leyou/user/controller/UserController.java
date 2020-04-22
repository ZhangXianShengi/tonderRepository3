package com.leyou.user.controller;

import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    /*
    * 校验用户名和手机号是否唯一
    *
    * */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,@PathVariable("type")Integer type){
        Boolean result = this.userService.checkUser(data,type);
        if (result== null){
            ResponseEntity.badRequest().build();//返回一个400
        }
        return ResponseEntity.ok(result);
    }
}
