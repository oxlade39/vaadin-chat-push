package com.github.oxlade39.rx;

import rx.util.functions.Func1;

/**
* @author dan
*/
public class ToStringFunction<T> implements Func1<T, String> {

    private ToStringFunction() {
    }

    public static <T> ToStringFunction<T> toStringF() {
        return new ToStringFunction<T>();
    }

    @Override
    public String call(T dateTime) {
        return dateTime.toString();
    }
}
