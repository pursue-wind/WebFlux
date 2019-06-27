package cn.mirrorming;

import java.util.stream.IntStream;

/**
 * @author: mirrorming
 **/

public class MinDemo {
    public static void main(String[] args) {
        int num[] = {1, 2, 3, 4, 5, 6, -1, -2, -3, -4, -5, -6, 0};

        /**
         * 命令式编程求最小值
         */
        int min = Integer.MAX_VALUE;
        for (int i : num)
            if (i < min) min = i;

        System.out.println(min);

        /**
         * 函数式编程求最小值
         */
        int minLambda = IntStream.of(num).min().getAsInt();
        System.out.println(minLambda);

        //并行处理
        int minLambdaParallel = IntStream.of(num).parallel().min().getAsInt();
        System.out.println(minLambdaParallel);
    }
}