package top.zymd3414.sm_springboot.config;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServletAwareConfigurator extends ServerEndpointConfig.Configurator
        implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return context.getBean(endpointClass); // 从 Spring 容器获取 Bean
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        context = ctx;
    }
}
