package top.zymd3414.sm_springboot.config;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

public class GetHttpSessionConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // 获取HttpSession对象
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        // 保存HttpSession
        if(httpSession == null) return;
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
