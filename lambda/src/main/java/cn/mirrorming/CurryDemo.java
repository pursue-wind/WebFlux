package cn.mirrorming;

import java.util.function.Function;

/**
 * 级联表达式
 * 柯里化：把多个参数的函数转换成只有一个参数的函数
 * 柯里化目的：把函数标准化
 * 高阶函数：返回函数的函数
 *
 * @author: mirrorming
 **/

public class CurryDemo {
    public static void main(String[] args) {
        Function<Integer, Function<Integer, Integer>> function = x -> y -> x + y;
        Integer result = function.apply(1).apply(2);
        System.out.println(result);

        Function<Integer, Function<Integer, Function<Integer, Integer>>> function2 = x -> y -> z -> x + y + z;
        Integer result2 = function2.apply(3).apply(4).apply(5);
        System.out.println(result2);


        int[] nums = {2, 3, 4};
        Function f = function2;
        for (int i : nums) {
            if (f instanceof Function) {
                Object obj = f.apply(i);
                if (obj instanceof Function) {
                    f = (Function) obj;
                } else {
                    System.out.println("调用结束，结果：" + obj);
                }
            }
        }
    }
}