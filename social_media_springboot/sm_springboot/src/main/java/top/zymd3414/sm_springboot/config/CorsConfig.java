package top.zymd3414.sm_springboot.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 匹配所有接口路径
                .allowedOriginPatterns("*")  // 允许所有来源（需Spring Boot 2.4+）
                .allowedMethods("*")        // 允许所有HTTP方法（GET/POST/PUT等）
                .allowedHeaders("*")        // 允许所有请求头
                .allowCredentials(true)     // 允许携带凭证（如Cookie）
                .maxAge(3600);             // 预检请求缓存时间（单位：秒）
    }
}
