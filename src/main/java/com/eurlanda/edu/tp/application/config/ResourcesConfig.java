package com.eurlanda.edu.tp.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class ResourcesConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("/rs/**").addResourceLocations("classpath:/static/rs/");
//        registry.addResourceHandler("/index.html").addResourceLocations("classpath:/index.html");
    }
}