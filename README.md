## Lambda

![Lambda](https://raw.githubusercontent.com/mirrormingzZ/WebFlux/master/webflux/src/main/resources/static/Lambda.png)

## Stream

![Lambda](https://raw.githubusercontent.com/mirrormingzZ/WebFlux/master/webflux/src/main/resources/static/Stream流编程-创建.png)

![Lambda](https://raw.githubusercontent.com/mirrormingzZ/WebFlux/master/webflux/src/main/resources/static/Stream流编程-中间操作.png)

![Lambda](https://raw.githubusercontent.com/mirrormingzZ/WebFlux/master/webflux/src/main/resources/static/Stream流编程-终止操作.png)

## WebFlux

> WebFlux响应式编程
### 接口
- http://127.0.0.1:8082/flux
- http://127.0.0.1:8082/block
- http://127.0.0.1:8082/mono
- html: http://127.0.0.1:8082/index.html

> 添加用户的json
```json
{
	"name":"mirror1",
	"age":18
}
```


- `get`: http://localhost:8082/user/stream/all
- `get`: http://localhost:8082/user/`id`
- `delete`: http://localhost:8082/user/`id`
- `post`: http://localhost:8082/user/ +`json`
- `put`: http://localhost:8082/user/`id`+`json`
- `get`: http://localhost:8082/user/age/20/30

**Router**
- `get`: http://localhost:8082/router/user/
- `post`: http://localhost:8082/router/user/ +`json`
- `delete`: http://localhost:8082/router/user/`id`

### 整合mongodb
1. 添加依赖

```java
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
</dependency>
```

2. 注解
```java
@EnableReactiveMongoRepositories
```

3. 定义对象
```java
@Document(collection = "user")
@Data
public class User{}
```

4. Repository

```java
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String>
```

5. CRUD
```java

@GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<User> streamGetAll() {
    return userRepository.findAll();
}

@GetMapping("/{id}")
public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
    return userRepository.findById(id)
            .map(user -> new ResponseEntity<User>(user, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

@DeleteMapping("/{id}")
public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
    return userRepository.findById(id)
            .flatMap(user -> userRepository.delete(user).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

@PutMapping("/{id}")
public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @Valid @RequestBody User user) {
    return userRepository.findById(id)
            .flatMap(oldUser -> {
                user.setId(id);
                return userRepository.save(user);
            }).map(oldUser -> new ResponseEntity<User>(oldUser, HttpStatus.OK))
            .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}
```

6. 参数校验
```java
@NotBlank
@Range(min = 10, max = 100)
@Valid
...
```
- Aop 异常处理切面
```java
public class CheckAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleBindException(WebExchangeBindException e) {
        return new ResponseEntity<String>(toStr(e), HttpStatus.BAD_REQUEST);
    }

    private String toStr(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }
}
```

7. 自定义校验逻辑

```java
private final static String[] INVALID_NAMES = {"admin", "mirror", "mirrorming"};

public static void checkName(String value) {
    Stream.of(INVALID_NAMES)
            .filter(name -> name.equalsIgnoreCase(value))
            .findAny().ifPresent(name -> {
        throw new CheckException("name", value);
    });
}
```
- 自定义异常
```java 
public class CheckException extends RuntimeException
```

- aop处理
```java
@ExceptionHandler(CheckException.class)
public ResponseEntity<String> handleCheckException(CheckException e) {
    return new ResponseEntity<String>(toStr(e), HttpStatus.BAD_REQUEST);
}

private String toStr(CheckException e) {
    return e.getFieldName() + "不能使用：" + e.getMessage();
}
```

### RouterFunction
- Handler
```java
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


public Mono<ServerResponse> getAllUser(ServerRequest request) {
    return ok()
            .contentType(APPLICATION_JSON_UTF8)
            .body(userRepository.findAll(), User.class);
}
public Mono<ServerResponse> createUser(ServerRequest request) {
    Mono<User> userMono = request.bodyToMono(User.class);
    return userMono.flatMap(user -> {
        CheckUtil.checkName(user.getName());
        return ok()
                .contentType(APPLICATION_JSON_UTF8)
                .body(userRepository.save(user), User.class);
    });
}
public Mono<ServerResponse> deleteUserById(ServerRequest request) {
    String id = request.pathVariable("id");
    return userRepository.findById(id)
            .flatMap(user -> userRepository.delete(user).then(ok().build()))
            .switchIfEmpty(notFound().build());
}
```
- Router
```java
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

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
```
- 校验异常处理
```java
@Component
@Order(-2)
public class ExceptionHandler implements WebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        String errMsg = toStr(throwable);
        DataBuffer dataBuffer = response.bufferFactory().wrap(errMsg.getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    private String toStr(Throwable throwable) {
        if (throwable instanceof CheckException) {
            CheckException e = (CheckException) throwable;
            return e.getFieldName() + ": invalid value -> " + e.getFieldValue();
        }
        else {
            throwable.printStackTrace();
            return throwable.toString();
        }
    }
}
```
