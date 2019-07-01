package cn.mirrorming.webflux.exception;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * @author: mirrorming
 * @create: 2019-07-01 11:32
 **/
@Component
@Order(-2)
public class ExceptionHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        //设置响应头
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        //设置返回类型
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        //异常信息
        String errMsg = toStr(throwable);

        DataBuffer dataBuffer = response.bufferFactory().wrap(errMsg.getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    private String toStr(Throwable throwable) {
        //已知异常
        if (throwable instanceof CheckException) {
            CheckException e = (CheckException) throwable;
            return e.getFieldName() + ": invalid value -> " + e.getFieldValue();
        }
        //未知异常打印堆栈，方便定位
        else {
            throwable.printStackTrace();
            return throwable.toString();
        }
    }
}