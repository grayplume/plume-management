package com.plume.management.config;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.plume.management.exception.LoginFailedException;
import com.plume.management.pojo.User;
import com.plume.management.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object){
        String token = request.getHeader("token");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod handlerMethod)) {
            return true;
        } else {
            AuthAccess authAccess = handlerMethod.getMethodAnnotation(AuthAccess.class);
            if (authAccess != null){
                return true;
            }
        }
        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new LoginFailedException("无token,请重新登录");
        }

        // 获取信息
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e) {
            throw new LoginFailedException("token验证失败");
        }
        // 查询数据库
        User user = userService.getById(userId);
        if (user == null) {
            throw new LoginFailedException("用户不存在请重新登录");
        }

        //验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new LoginFailedException("验证失败,请重新登录");
        }
        return true;
    }

}
