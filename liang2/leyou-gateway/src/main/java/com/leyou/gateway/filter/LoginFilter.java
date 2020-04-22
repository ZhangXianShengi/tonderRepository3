package com.leyou.gateway.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.gateway.properites.FilterProperties;
import com.leyou.gateway.properites.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        //先初始化上下文对象
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取URL
        String url = request.getRequestURI();
        // 判断白名单
        // 遍历允许访问的路径
        for (String path : this.filterProperties.getAllowPaths()) {
            if (url.contains(path)){
                return false;
            }
        }
        return true;
    }
    @Override
    public Object run() throws ZuulException {
        //先初始化上下文对象
        RequestContext context = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = context.getRequest();
        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());
        //解析token
        try {
            JwtUtils.getInfoFromToken(token,this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
            logger.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e );
        }
        return null;
    }
}

