package pe.andeslink.andestrack.realtime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.config.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final String allowedOrigin;
    private final JwtDecoder jwtDecoder;
    public WebSocketConfig(@Value("$" + "{app.cors.allowed-origin}") String allowedOrigin, JwtDecoder jwtDecoder) {
        this.allowedOrigin=allowedOrigin; this.jwtDecoder=jwtDecoder;
    }
    @Override public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins(allowedOrigin);
    }
    @Override public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setUserDestinationPrefix("/user");
    }
    @Override public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor=MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor==null) return message;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String header=accessor.getFirstNativeHeader("Authorization");
                    if (header==null || !header.startsWith("Bearer "))
                        throw new AccessDeniedException("STOMP CONNECT requires a bearer token");
                    Jwt jwt=jwtDecoder.decode(header.substring(7));
                    List<SimpleGrantedAuthority> authorities=jwt.getClaimAsStringList("roles").stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_"+role)).toList();
                    accessor.setUser(new UsernamePasswordAuthenticationToken(jwt.getSubject(), header.substring(7), authorities));
                }
                if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    var user=accessor.getUser();
                    String destination=accessor.getDestination();
                    if (user==null || destination==null) throw new AccessDeniedException("Authenticated subscription required");
                    Authentication authenticated=(user instanceof Authentication a) ? a : null;
                    boolean allowed=authenticated!=null && (destination.startsWith("/topic/control/")
                            ? authenticated.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ROLE_COORDINATOR"))
                            : destination.equals("/user/queue/tracking")
                            ? authenticated.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"))
                            : destination.equals("/user/queue/driver")
                            ? authenticated.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_DRIVER"))
                            : false);
                    if (!allowed) throw new AccessDeniedException("Subscription is not allowed for this role");
                }
                return message;
            }
        });
    }
}
