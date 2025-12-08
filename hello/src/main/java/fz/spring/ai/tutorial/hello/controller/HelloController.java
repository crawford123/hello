package fz.spring.ai.tutorial.hello.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
public class HelloController {

    private ChatClient chatClient;

    private ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MICROSECONDS, new LinkedBlockingQueue<>(10));

    public HelloController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "input", defaultValue = "讲一个笑话") String input) {

        return chatClient.prompt(input).call().content();

    }


    @GetMapping(value = "/mock/stream", produces = "text/html;charset=UTF-8")
    public Flux<String> mockStream(){

        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();

        executor.submit(() -> {
            for (int i = 0; i < 100; i++) {

                sink.tryEmitNext(i + " ");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return sink.asFlux();
    }

    @GetMapping(value = "/hello/stream", produces = "text/html;charset=UTF-8")
    public Flux<String> helloStream(@RequestParam(value = "input", defaultValue = "讲一个笑话") String input){
        return chatClient.prompt(input).stream().content();
    }
}