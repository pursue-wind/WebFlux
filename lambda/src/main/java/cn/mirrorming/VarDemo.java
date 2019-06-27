package cn.mirrorming;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 变量引用
 *
 * @author: mirrorming
 **/

public class VarDemo {

    public static void main(String[] args) {
        String str = "mirror";
//        str = null;//不能修改，Java是值传递
        Consumer<String> stringConsumer = s -> System.out.println(s + str);
        stringConsumer.accept("233");


        List<String> list = new ArrayList<>();
//        list.clear();//不能修改，Java是值传递
        Consumer<String> consumer = s -> System.out.println(s + list);
        consumer.accept("233");
    }

}