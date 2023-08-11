package com.cattle.lihao.config;

import com.cattle.lihao.interceptor.LihaoInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置类，用于过滤前端请求，判断token与登录token
 *
 * @author niujie
 * @date 2023/4/30 14:45
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LihaoInterceptor lihaoInterceptor() {
        return new LihaoInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截所有请求
        // 对获取token和登录放行
        registry.addInterceptor(lihaoInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/cattle/lihao/token/getToken", "/cattle/lihao/user/login", "/cattle/lihao/user/loginOut");
    }
}
