package cn.mirrorming;

import java.util.function.*;

/**
 * 方法引用
 *
 * @author: mirrorming
 **/
class Dog {
    private String name = "神狗";
    private int food = 100;

    public Dog() {
    }

    public Dog(String name) {
        this.name = name;
    }

    public static void bark(Dog dog) {
        System.out.println(dog + "在叫");
    }

    /**
     * JDK 会默认把当前实例传入非静态方法，参数名为this
     *
     * @param num
     * @return
     */
    public int eat(Dog this, int num) {
        System.out.println("吃了" + num + "斤狗粮");
        return this.food - num;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

public class MethodRefrenceDemo {
    public static void main(String[] args) {
        Consumer<String> consumer = s -> System.out.println(s);
        //方法引用
        Consumer<String> methodRefrenceConsumer = System.out::println;
        methodRefrenceConsumer.accept("方法引用");

        //静态方法的引用
        Consumer<Dog> dogConsumer = Dog::bark;
        dogConsumer.accept(new Dog());

        //非静态方法，使用对象实例引用
        Dog dog = new Dog();
        Function<Integer, Integer> function = dog::eat;
        System.out.println("function - 还剩下" + function.apply(2) + "斤");

        UnaryOperator<Integer> unaryOperator = dog::eat;
        System.out.println("unaryOperator - 还剩下" + unaryOperator.apply(33) + "斤");

        IntUnaryOperator intUnaryOperator = dog::eat;
        System.out.println("intUnaryOperator - 还剩下" + intUnaryOperator.applyAsInt(33) + "斤");

        //使用类名来方法引用
        BiFunction<Dog, Integer, Integer> biFunction = Dog::eat;
        System.out.println(biFunction.apply(new Dog(), 11));

        //构造方法引用
        Supplier<Dog> supplier = Dog::new;
        System.out.println("创建了新实例：" + supplier.get());

        //构造方法引用
        Function<String, Dog> dogFunction = Dog::new;
        System.out.println("创建了新实例：" + dogFunction.apply("啸天"));
    }
}