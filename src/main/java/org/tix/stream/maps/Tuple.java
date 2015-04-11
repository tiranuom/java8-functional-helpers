package org.tix.stream.maps;

import java.util.function.Function;

/**
 * Created by tiran on 4/10/15.
 */
public final class Tuple<T, U> {
    private T first;
    private U second;

    private Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }

    public static <T, U> Tuple<T, U> of(T k, U v) {
        return new Tuple<>(k,v);
    }

    public static <T, U>Function<T, Tuple<T, U>> withSecond(U second) {
        return first -> of(first, second);
    }

    public static <T, U>Function<U, Tuple<T, U>> withFirst(T first) {
        return second -> of(first, second);
    }
}
