package cn.mirrorming;

/**
 * @author: mirrorming
 **/
@FunctionalInterface
//只能有一个抽象方法
interface FunctionalInterfaceDemo {
    int retInt(int num);

    default int add(int x, int y) {
        return x + y;
    }
}

public class LambdaDemo {
    public static void main(String[] args) {

        FunctionalInterfaceDemo demo = (i) -> i * 2;
        System.out.println(demo.add(1, 2));
        System.out.println(demo.retInt(3));

        FunctionalInterfaceDemo demo2 = i -> i * 2;

        FunctionalInterfaceDemo demo3 = (int i) -> i * 2;

        FunctionalInterfaceDemo demo4 = (i) -> {
            System.out.println("其他逻辑 ==");
            return i * 2;
        };
    }
}
