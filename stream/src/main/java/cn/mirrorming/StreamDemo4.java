package cn.mirrorming;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 流 处理
 *
 * @author: mirrorming
 **/

public class StreamDemo4 {
    public static void main(String[] args) {
        String str = "my name is mirror";

        System.out.println("========================================================");

        //并行
        str.chars().parallel().forEach(c -> System.out.print((char) c));

        System.out.println("========================================================");

        //并行保证顺序
        str.chars().parallel().forEachOrdered(c -> System.out.print((char) c));

        System.out.println("========================================================");

        //collect
        List<String> list = Stream.of(str.split(" ")).collect(Collectors.toList());
        System.out.println(list);

        System.out.println("========================================================");

        //使用 reduce 拼接字符串
        Optional<String> reduce = Stream.of(str.split(" ")).reduce((x, y) -> x + "|" + y);
        System.out.println(reduce.orElse(""));

        //带初始化值的 reduce
        String reduce2 = Stream.of(str.split(" ")).reduce("", (x, y) -> x + "|" + y);
        System.out.println(reduce2);

        //计算单词的长度
        Integer length = Stream.of(str.split(" ")).map(s -> s.length()).reduce(0, (x, y) -> x + y);
        System.out.println(length);

        //max
        Optional<String> max = Stream.of(str.split(" ")).max((x, y) -> x.length() - y.length());
        System.out.println(max.get());

        //使用findFirst短路操作
        OptionalInt first = new Random().ints().findFirst();
        System.out.println(first.getAsInt());
    }
}