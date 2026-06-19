package com.gym.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private final String uploadRoot;

    public StaticResourceConfig(@Value("${app.upload.root:data/uploads}") String uploadRoot) {
        this.uploadRoot = uploadRoot;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path absolute = Paths.get(uploadRoot).toAbsolutePath().normalize();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + absolute + "/");
    }
}