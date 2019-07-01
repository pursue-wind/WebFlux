package cn.mirrorming.webflux.router;

import cn.mirrorming.webflux.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author: mirrorming
 * @create: 2019-07-01 10:56
 **/
@Configuration
public class AllRouters {
    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler handler) {
            return nest(path("/router/user"),
                route(GET("/"), handler::getAllUser)
                        .andRoute(POST("/").and(accept(MediaType.APPLICATION_JSON_UTF8)), handler::createUser)
                        .andRoute(DELETE("/{id}"), handler::deleteUserById)
        );
    }
}