package cn.mirrorming;

import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

/**
 * @author: mirrorming
 **/

public class FunctionDemo {
    public static void main(String[] args) {
        /**
         * 断言函数接口
         */
        Predicate<Integer> predicate = i -> i > 0;
        IntPredicate predicate2 = i -> i > 0;
        System.out.println(predicate.test(1));
        System.out.println(predicate2.test(-1));

        /**
         * 消费者函数接口
         */
        Consumer<String> consumer = c -> System.out.println(c);
        IntConsumer consumer2 = c -> System.out.println(c);
        consumer.accept("消费者函数接口");
        consumer2.accept(233);
    }
}