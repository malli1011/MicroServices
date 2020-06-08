package com.spring.reactive.initialize;

import com.spring.reactive.document.Item;
import com.spring.reactive.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
@Profile("!test")// do not run this for test profile.
public class ItemDataInitializer implements CommandLineRunner {
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetUp();
    }

    public List<Item> data(){
        List<Item> items = List.of(new Item("123", "Samsung TV", 200.0),
                new Item(null, "Samsung Mobile", 150.0),
                new Item(null, "Beats", 200.0));
        return items;
    }

    private void initialDataSetUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe( item -> System.out.println("Item inserted from CommandLineRunner: "+ item));
    }
}
