package com.example.scholar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("img/avatar/**").addResourceLocations(
                "file:"+PathConfig.path+System.getProperty("file.separator")+"img" +System.getProperty("file.separator")+"avatar"+System.getProperty("file.separator")
        );


    }
}
