package com.nyist.config;

import com.nyist.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Administrator on 2018/9/7/007.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    public WebConfig(){
        super();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");

        super.addResourceHandlers(registry);
    }



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截规则：除了/index，/system/user/validate其他都拦截判断
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/tologin","/login","/getImage","/css/**","/js/**","/images/**","/node_modules/**","/sweetalert/**","/templates/**");
        super.addInterceptors(registry);
    }
}
