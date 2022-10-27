package com.eurlanda.edu.tp.application.config;

import com.eurlanda.edu.tp.interceptors.MyInterceptors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{

    @Bean
    MyInterceptors myInterceptors(){
        return new MyInterceptors();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptors()).addPathPatterns("/**")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/startEx/**")
                .excludePathPatterns("/record/*/logout")
                //.excludePathPatterns("/auth/updatePassword")
                .excludePathPatterns("/record/**")
                .excludePathPatterns("/previewImage/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/common/hotCourses/all")
                .excludePathPatterns("/common/image/preview")
                .excludePathPatterns("/share/downloadShareFile")
                .excludePathPatterns("/teacher/class/export")
                 .excludePathPatterns("/common/course/*/experiments")
                .excludePathPatterns("/doc/downloadDocFile")
                .excludePathPatterns("/dmdata/**")
                .excludePathPatterns("/doc/previewProjectImage")
                .excludePathPatterns("/doc/previewDocImage")
                .excludePathPatterns("/webSocket/**");
    }
}
