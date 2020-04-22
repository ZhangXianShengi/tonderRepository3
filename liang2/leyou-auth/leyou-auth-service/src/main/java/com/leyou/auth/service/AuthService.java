package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.bojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String login(String username, String password) {
        //1.远程调用用户中心查询用户接口
        User user = this.userClient.queryUser(username, password);

        //2.判断用户是否为空，用户为空就不需要查询了
        if (user == null){
            return null;
        }

        //初始化一个userInFo
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());

        //3.如果用户不为空就生成jwt类型的token
        try {
            return JwtUtils.generateToken(userInfo,this.jwtProperties.getPrivateKey(),this.jwtProperties.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析用户token
     * @param token
     * @return
     */
    public UserInfo verify(String token) {
        try {
            return JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

























