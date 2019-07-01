package cn.mirrorming.webflux.repository;

import cn.mirrorming.webflux.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author: mirrorming
 * @create: 2019-06-30 12:54
 **/
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Flux<User> findByAgeBetween(int start, int end);

    @Query("{'age':{'$gte':20,'$lte':30}}")
    Flux<User> getByQuery();
}