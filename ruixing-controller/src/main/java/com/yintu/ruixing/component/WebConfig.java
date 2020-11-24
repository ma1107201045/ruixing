package com.yintu.ruixing.component;

import com.yintu.ruixing.common.util.OSInfoUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author:mlf
 * @date:2020/6/19 17:23
 */
@Component
public class WebConfig implements WebMvcConfigurer {

    public final String VIRTUAL_PATH = "/files/**";

    public final String WINDOW_PATH = "C:\\data\\ruixing\\files\\";

    public final String LINUX_PATH = "/data/ruixing/files/";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(VIRTUAL_PATH).addResourceLocations("file:" + (OSInfoUtil.isWindows() ? WINDOW_PATH : LINUX_PATH));
        registry.addResourceHandler("/backups/**").addResourceLocations("file:" + (OSInfoUtil.isWindows() ? "C:\\data\\ruixing\\backups\\" : "/data/ruixing/backups/"));

    }
}

