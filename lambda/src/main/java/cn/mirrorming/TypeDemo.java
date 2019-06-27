package cn.mirrorming;

/**
 * @author: mirrorming
 **/
@FunctionalInterface
interface Math {
    int add(int x, int y);
}

@FunctionalInterface
interface Math2 {
    int sub(int x, int y);
}

public class TypeDemo {
    public static void main(String[] args) {
        //变量类型定义
        Math m1 = (x, y) -> x + y;

        //数组
        Math[] m2 = {(x, y) -> x + y};

        //强转
        Object m3 = (Math) (x, y) -> x + y;

        //通过返回类型
        Math m4 = creat();
        TypeDemo demo = new TypeDemo();

        // 当有二义性的时候，使用强转对应的接口解决
        demo.test((Math) (x, y) -> x + y);
        demo.test((Math2) (x, y) -> x + y);
    }

    public static Math creat() {
        return (x, y) -> x + y;
    }

    public void test(Math math) {

    }

    public void test(Math2 math) {

    }
}