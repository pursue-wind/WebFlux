package cn.mirrorming.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author: mirrorming
 * @create: 2019-06-30 11:00
 **/
@RestController
@Slf4j
public class TestController {
    /**
     * 传统方式
     *
     * @return
     */
    @GetMapping("block")
    public String getBlockDemo() {
        log.info("block start");
        String result = doSomeThing();
        log.info("block end");
        return result;
    }

    /**
     * Mono
     *
     * @return
     */
    @GetMapping("mono")
    private Mono<String> Mono() {
        log.info("Mono start");
        Mono<String> result = Mono.fromSupplier(() -> doSomeThing());
        log.info("Mono end");
        return result;
    }

    /**
     * Flux
     *
     * @return
     */
    @GetMapping(value = "flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    private Flux<String> flux() {
        log.info("flux start");
        Flux<String> result = Flux.fromStream(IntStream.range(1, 100).mapToObj(c -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Data: " + c;
        }));
        log.info("flux end");
        return result;
    }

    /**
     * doSomeThing
     *
     * @return
     */
    private String doSomeThing() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "ok";
    }

}