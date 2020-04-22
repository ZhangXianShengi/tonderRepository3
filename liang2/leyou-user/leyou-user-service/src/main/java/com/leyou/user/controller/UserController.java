package com.leyou.user.controller;

import com.leyou.user.bojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 手机号、用户名的唯一性校验
     * @param data 要校验的数据
     * @param type 要校验的数据类型：1，用户名；2，手机
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable("data")String data,@PathVariable("type")Integer type){
        Boolean boo = userService.checkUser(data,type);
        if (boo == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(boo);
    }

    /**
     * 生成验证码并保存到redis
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> verifyCode(@RequestParam("phone") String phone){
        this.userService.verifyCode(phone);
        return ResponseEntity.noContent().build();
    }

    /**
     * 注册用户接口
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code){
        this.userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password){
        User user = this.userService.queryUser(username,password);
        if (user == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}






















