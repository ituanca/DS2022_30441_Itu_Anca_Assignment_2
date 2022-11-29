package ro.tuc.ds2022.webSockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/mywebsockets").setAllowedOrigins("mydomain.com").withSockJS();  //withSockJS() lets our WebSockets work even if the WebSocket PROTOCOL IS NOT SUPPORTED BY AN INTERNET BROWSER
    }  // setAllowedOrigins() need to be called to permit the client and server-side to communicate even though they use different domains

    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic/", "/queue/");  // 2 destination prefixes defined
        config.setApplicationDestinationPrefixes("/app"); // prefix used to filter destinations handled by methods annotated with @MessageMapping
    }

}
