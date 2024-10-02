package com.example.scholar.config.handler;

import com.example.scholar.config.annotation.TokenToUser;
import com.example.scholar.dao.UserMapper;
import com.example.scholar.dao.UserTokenMapper;
import com.example.scholar.domain.User;
import com.example.scholar.domain.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserTokenMapper userTokenMapper;
    @Autowired
    private UserMapper userMapper;
    /*
    选择当前容器支持解析的方法、注解
    @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToUser.class);
    }
    /*
    处理参数
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if(parameter.getParameterAnnotation(TokenToUser.class) != null){
            String token = webRequest.getHeader("Authorization");
            // 获取token
            if(token != null){
                UserToken userToken = userTokenMapper.selectByToken(token);
                if(userToken == null || userToken.getExpiretime().getTime() <= System.currentTimeMillis()){
                    // 用户token不存在或者已经过期
                    throw new IllegalArgumentException("Invalid or expired token");
                }
                User user = userMapper.selectUserById(userToken.getUserid());
                if(user == null ){
                    // 用户不存在
                    throw new IllegalArgumentException("User not found");
                }
                return user;
            } else {
                throw new IllegalArgumentException("Token not found in request");
            }
        }
        return null;
    }
}
