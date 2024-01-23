package com.example.chess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * webSocket配置类
 *
 * @author lrf
 */

@Configuration
public class WebSocketConfig implements WebMvcConfigurer {
    
    /**
     * 服务器支持跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .exposedHeaders("Access-Control-Allow-Headers", "Access-Control-Allow-Methods", "Access-Control-Origin", "Access-Control-Max-Age", "X-Frame-Options")
                .allowCredentials(false)
                .maxAge(3600);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
