package com.convert.convert.config;

import com.convert.convert.Interceptor.CovertInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: xiongwei
 * @Date: 2020/8/19
 * @why：
 */
@Configuration
public class MyConfiguration implements WebMvcConfigurer {
    @Autowired
    private CovertInterceptor covertInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(covertInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/send")
                .excludePathPatterns("/manager")
                .excludePathPatterns("/convert/list")
                .excludePathPatterns("/del");
    }
}
