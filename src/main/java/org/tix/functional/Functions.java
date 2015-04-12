package org.tix.functional;

import java.util.function.*;

/**
 * Created by tiran on 4/12/15.
 */
public class Functions {
    
    public static <T1, T2, R> BiFunction<T2, T1, R> swap(BiFunction<T1, T2, R> function) {
        return (t2, t1) -> function.apply(t1, t2);
    }

    public static <T1, T2> BiConsumer<T2, T1> swapConsumer(BiConsumer<T1, T2> consumer) {
        return (t2, t1) -> consumer.accept(t1, t2);
    }

    public static <T1, T2> BiPredicate<T2, T1> swapPredicate(BiPredicate<T1, T2> predicate) {
        return (t2, t1) -> predicate.test(t1, t2);
    }

    public static <T1, T2, R> Function<T1, Function<T2, R>> curried(BiFunction<T1, T2, R> function) {
        return t1 -> t2 -> function.apply(t1, t2);
    }

    public static <T1, T2> Function<T1, Consumer<T2>> curriedConsumer(BiConsumer<T1, T2> consumer) {
        return t1 -> t2 -> consumer.accept(t1, t2);
    }

    public static <T1, T2> Function<T1, Predicate<T2>> curriedPredicate(BiPredicate<T1, T2> predicate) {
        return t1 -> t2 -> predicate.test(t1, t2);
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> uncurried(Function<T1, Function<T2, R>> function) {
        return (t1, t2) -> function.apply(t1).apply(t2);
    }

    public static <T1, T2> BiConsumer<T1, T2> uncurriedConsumer(Function<T1, Consumer<T2>> function) {
        return (t1, t2) -> function.apply(t1).accept(t2);
    }

    public static <T1, T2> BiPredicate<T1, T2> uncurriedPredicate(Function<T1, Predicate<T2>> function) {
        return (t1, t2) -> function.apply(t1).test(t2);
    }
}
