package cn.mirrorming;


import java.text.DecimalFormat;
import java.util.function.Function;

/**
 * @author: mirrorming
 **/

class MyMoney {
    private final int money;

    public MyMoney(int money) {
        this.money = money;
    }

    public void printMoney(Function<Integer, String> moneyFormat) {
        System.out.println("我的钱：" + moneyFormat.apply(money));
    }
}

public class MoneyDemo {
    public static void main(String[] args) {
        MyMoney myMoney = new MyMoney(123456789);
        myMoney.printMoney(i -> new DecimalFormat("#,###").format(i));


        /**
         * 函数接口链式操作
         */
        Function<Integer, String> function = i -> new DecimalFormat("#,###").format(i);
        myMoney.printMoney(function.andThen(s -> "RMB（" + s + "）"));
    }
}