package cn.mirrorming.webflux.utils;

import cn.mirrorming.webflux.exception.CheckException;

import java.util.stream.Stream;

/**
 * @author: mirrorming
 * @create: 2019-06-30 21:38
 **/

public class CheckUtil {

    private final static String[] INVALID_NAMES = {"admin", "mirror", "mirrorming"};

    /**
     * 校验名字，不成功抛出校验异常
     */
    public static void checkName(String value) {
        Stream.of(INVALID_NAMES)
                .filter(name -> name.equalsIgnoreCase(value))
                .findAny().ifPresent(name -> {
            throw new CheckException("name", value);
        });
    }
}