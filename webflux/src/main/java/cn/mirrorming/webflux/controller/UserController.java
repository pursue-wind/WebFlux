package cn.mirrorming.webflux.controller;

import cn.mirrorming.webflux.domain.User;
import cn.mirrorming.webflux.repository.UserRepository;
import cn.mirrorming.webflux.utils.CheckUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * @author: mirrorming
 * @create: 2019-06-30 12:59
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 以数组形式一次性返回数据
     *
     * @return
     */
    @GetMapping("/")
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * 以SSE形式多次返回数据
     *
     * @return
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return userRepository.findAll();
    }

    /**
     * 根据ID查找用户 存在返回用户信息, 不存在返回404
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable String id) {
        return userRepository.findById(id)
                //要操作数据，并返回一个Mono，这时候使用flatMap
                .map(user -> new ResponseEntity<User>(user, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 新增数据
     *
     * @param user
     * @return
     */
    @PostMapping("/")
    public Mono<User> saveUser(@Valid @RequestBody User user) {
        //Spring data jpa 里面，新增和修改都是save，有id 是修改，无 id 是新增
        user.setId(null);
        CheckUtil.checkName(user.getName());
        return userRepository.save(user);
    }

    /**
     * 根据id删除用户 存在的时候返回200, 不存在返回404
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable String id) {
        //deleteById没有返回值，不能判断数据是否存在
        return userRepository.findById(id)
                //要操作数据，并返回一个Mono，这时候使用flatMap
                .flatMap(user -> userRepository.delete(user).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 修改数据 存在的时候返回200 和修改后的数据, 不存在的时候返回404
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable String id, @Valid @RequestBody User user) {
        return userRepository.findById(id)
                //要操作数据，并返回一个Mono，这时候使用flatMap
                .flatMap(oldUser -> {
                    user.setId(id);
                    return userRepository.save(user);
                }).map(oldUser -> new ResponseEntity<User>(oldUser, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据年龄查找用户
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findUserByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return userRepository.findByAgeBetween(start, end);
    }

    /**
     * 根据年龄查找用户stream
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindUserByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return userRepository.findByAgeBetween(start, end);
    }

    /**
     * 得到年龄为20-30用户
     *
     * @return
     */
    @GetMapping("/age/custom")
    public Flux<User> findUserByQuery() {
        return userRepository.getByQuery();
    }
}