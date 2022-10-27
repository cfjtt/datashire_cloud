package com.eurlanda.edu.tp.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer{
    /**
     * 注册stomp的端点
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        //在网页上通过http://ip:host/webSocket来和webSocket进行连接
        stompEndpointRegistry.addEndpoint("/webSocket").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置信息代理
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // queue是发送给指定用户，/topic是进行全局广播
        registry.enableSimpleBroker("/topic");
        //全局消息的前缀
        registry.setApplicationDestinationPrefixes("/app");
        // 点对点使用的订阅方式  (发送消息给指定的用户   地址为/user/queue/)  使用SimpMessageingTempldate.convertAndSendToUser()来发送给指定用户
        //registry.setUserDestinationPrefix("/user/");
    }
}
