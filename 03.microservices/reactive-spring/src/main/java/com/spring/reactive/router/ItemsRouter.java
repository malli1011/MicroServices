package com.spring.reactive.router;

import com.spring.reactive.handler.ItemHandler;
import com.spring.reactive.handler.SampleHandlerFunction;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(ItemHandler handlerFunction){
        return RouterFunctions.route(GET("/v1/fun/items").and(accept(MediaType.APPLICATION_JSON)),handlerFunction::getAllItems)
                .andRoute(GET("/v1/fun/item/{id}").and(accept(MediaType.APPLICATION_JSON)),handlerFunction::getItem)
                .andRoute(POST("/v1/fun/item").and(accept(MediaType.APPLICATION_JSON)),handlerFunction::createItem)
                .andRoute(DELETE("/v1/fun/item/{id}").and(accept(MediaType.APPLICATION_JSON)),handlerFunction::deleteItem);

    }
}
