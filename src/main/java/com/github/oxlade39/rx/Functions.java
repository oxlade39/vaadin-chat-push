package com.github.oxlade39.rx;

import rx.util.functions.Func1;

/**
 * @author dan
 */
public class Functions {
    private Functions(){}

    public static <T, V> Func1<T, Boolean> instanceOf(final Class<V> instance) {
        return new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                return instance.isAssignableFrom(t.getClass());
            }
        };
    }

    public static <T, V> Func1<T, V> castTo(final Class<V> to) {
        return new Func1<T, V>() {
            @Override
            public V call(T t) {
                return to.cast(t);
            }
        };
    }
}
