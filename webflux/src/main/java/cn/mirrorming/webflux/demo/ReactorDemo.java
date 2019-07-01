package cn.mirrorming.webflux.demo;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

/**
 * @author: mirrorming
 * @create: 2019-06-30 11:50
 **/

public class ReactorDemo {
    public static void main(String[] args) {
        /**
         *  reactor = stream + reactive stream
         *  Mono 0-1 个元素
         *  Flux 0-N 个元素
         */
        String[] strs = {"1", "2", "3", "4"};


        // 订阅者
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                // 保存订阅关系，需要用它来给发布者响应
                this.subscription = subscription;
                // 请求一个数据
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                // 接收到一个数据，处理
                System.out.println("接收到数据" + item);
                // 处理完调用request再请求一个数据
                this.subscription.request(1);
                // 或者 已经达到目标，调用 cancel 告诉发布者不再接收数据
//                this.subscription.cancel();
            }

            @Override
            public void onError(Throwable throwable) {
                // 出现了异常（比如处理数据时出现了异常）
                throwable.printStackTrace();
                // 我们可以告诉发布者，后面不接受数据了
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                // 数据全部处理完了
                System.out.println("处理完了");
            }
        };

        //stream
        Flux.fromArray(strs).map(s -> Integer.parseInt(s))
                //reactive stream
                .subscribe(subscriber);
    }
}