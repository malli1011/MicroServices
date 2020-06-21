package com.spring.reactive.handler;

import com.spring.reactive.document.Item;
import com.spring.reactive.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemHandler {

    static Mono<ServerResponse> notfound = ServerResponse.notFound().build();

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getItem(ServerRequest request) {
        String id = request.pathVariable("id");

        Mono<Item> itemMono = itemReactiveRepository.findById(id);
        return itemMono.flatMap(item ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(item)).switchIfEmpty(notfound));

    }

    public Mono<ServerResponse> createItem(ServerRequest request) {
        Mono<Item> itemTobeInserted = request.bodyToMono(Item.class);
        return itemTobeInserted.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.save(item), Item.class));
    }

    public Mono<ServerResponse> deleteItem(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Void> deleteItem = itemReactiveRepository.deleteById(id);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(deleteItem, Void.class);

    }

    public Mono<ServerResponse> getAllItemsWithException(ServerRequest serverRequest){
        throw new RuntimeException("Runtime Exception occurred");
    }

}
