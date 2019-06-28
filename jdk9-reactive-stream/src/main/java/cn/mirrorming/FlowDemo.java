package cn.mirrorming;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/**
 * @author: mirrorming
 * @create: 2019-06-28 13:52
 **/

public class FlowDemo {
    public static void main(String[] args) throws InterruptedException {
        // 1. 定义发布者，发布的数据类型是Integer
        // 直接使用jdk自带的SubmissionPublisher, 它实现了 Publisher 接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        // 2. 定义订阅者
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
        // 3. 发布者和订阅者建立订阅关系
        publisher.subscribe(subscriber);

        // 4. 生产数据，并发布 - 这里忽略发布过程
        int data = 233;
        // submit 是一个阻塞方法，如果在 onNext 里面的添加 Sleep 的方法，会让这边生产慢下来
        publisher.submit(data);
        publisher.submit(123);
        publisher.submit(234);

        // 5. 结束后，关闭发布者，应该放 finally 或者 使用 try-resource 确保关闭
        publisher.close();

        // 主线程延迟停止，否则数据没有消费就退出
        Thread.currentThread().join(1000);
    }
}