package org.tix.stream.maps;

import java.util.Comparator;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class Maps {

    /**
     * Transforms <code>java.util.Map.Entry</code> to <code>org.tix.Tuple</code>
     *
     * Should be used as,
     * <code>
     *     anonymousMap.entrySet().stream.map(Maps::toTuple)
     * </code>
     *
     * @param entry stream element to be transformed
     * @param <K> key type of the map entry
     * @param <V> value type of map entry
     * @return Tuple instance
     */
    public static <K, V> Tuple<K,V> toTuple(Map.Entry<K, V> entry) {
        return Tuple.of(entry.getKey(), entry.getValue());
    }

    /**
     * Returns a transformer function which transforms a ByConsumer of key and value to Consumer of Tuple.
     *
     * <code>
     *     streamOfTuple.peak(toEntry((key, value) -> System.out.println(key + ":" + value)));
     * </code>
     *
     * @param biConsumer consumer to consume key and value
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple consumer
     */
    public static <K,V> Consumer<Tuple<K,V>> toEntry(BiConsumer<? super K, ? super V> biConsumer) {
        return kvTuple -> biConsumer.accept(kvTuple.getFirst(), kvTuple.getSecond());
    }

    /**
     * Returns a transformer function which transforms a Consumer of key to Consumer of Tuple,
     * while preserving the value.
     *
     * <code>
     *     streamOfTuple.peak(toKey(key -> System.out.println(key)))
     * </code>
     * @param consumer Key consumer function
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple consumer
     */
    public static <K,V> Consumer<Tuple<K,V>> toKey(Consumer<? super K> consumer) {
        return entry -> consumer.accept(entry.getFirst());
    }

    /**
     * Returns a transformer function which transforms a Consumer of value to Consumer of Tuple,
     * while preserving the key.
     *
     * <code>
     *     streamOfTuple.peak(toValue(value -> System.out.println(value));
     * </code>
     *
     * @param consumer Value consumer function
     * @param <K> Key type
     * @param <V> Value function
     * @return Tuple consumer
     */
    public static <K,V> Consumer<Tuple<K,V>> toValue(Consumer<? super V> consumer) {
        return entry -> consumer.accept(entry.getSecond());
    }

    /**
     * Returns a transformer function which transforms a BiFunction of key and value to R, to Function of Tuple to T,
     *
     * <code>
     *     streamOfTupleStringString.map(entries((key, value) -> key + ":" + value);
     * </code>
     *
     * @param biFunction Function which is used to transform key and value to R.
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to R function
     */
    public static <K, V, R> Function<Tuple<K, V>, R> entries(BiFunction<? super K, ? super V, ? extends R> biFunction) {
        return entry -> biFunction.apply(entry.getFirst(), entry.getSecond());
    }

    /**
     * Returns a transformer function which transforms Key mapping function to Tuple mapping function.
     *
     * <code>
     *     streamOfTupleStringString.map(keys(key -> "key : " + key);
     * </code>
     *
     * @param function Function which is used to transform key to R
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to Tuple function
     */
    public static <K, V, R> Function<Tuple<K, V>, Tuple<R, V>> keys(Function<? super K, ? extends R> function) {
        return kvTuple -> Tuple.of(function.apply(kvTuple.getFirst()), kvTuple.getSecond());
    }

    /**
     * Returns a transformer function which transforms Key mapping function to Tuple mapping function.
     * Optimized for UnaryOperator, by modifying the existing Tuple rather than creating new Tuple.
     *
     * <code>
     *     streamOfTupleStringString.map(keysUnary(key -> "key : " + key);
     * </code>
     *
     * @param operator UnaryOperator which is used to transform key;
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple to Tuple function.
     */
    public static <K, V> Function<Tuple<K, V>, Tuple<K, V>> keysUnary(UnaryOperator<K> operator) {
        return kvTuple -> {
            kvTuple.setFirst(operator.apply(kvTuple.getFirst()));
            return kvTuple;
        };
    }

    /**
     * Returns a transformer function which transforms Value mapping function to Tuple mapping Function.
     *
     * <code>
     *     streamOfTupleStringString.map(values(value -> "value :" + value);
     * </code>
     *
     * @param function Function which is used to transform value to R
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to Tuple function
     */
    public static <K, V, R> Function<Tuple<K, V>, Tuple<K, R>> values(Function<? super V, ? extends R> function) {
        return kvTuple -> Tuple.of(kvTuple.getFirst(), function.apply(kvTuple.getSecond()));
    }

    /**
     * Returns a transformer function which transforms Value mapping function to Tuple mapping function.
     * Optimized for UnaryOperator, by modifying the existing Tuple rather than creating new Tuple.
     *
     * <code>
     *     streamOfTupleStringString.map(valuesUnary(value -> "value :" + value);
     * </code>
     *
     * @param operator UnaryOperator which is used to transform value;
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple to Tuple function.
     */
    public static <K, V> Function<Tuple<K, V>, Tuple<K, V>> valuesUnary(UnaryOperator<V> operator) {
        return kvTuple -> {
            kvTuple.setSecond(operator.apply(kvTuple.getSecond()));
            return kvTuple;
        };
    }

    /**
     * Swaps the key and value of map entry.
     *
     * <code>
     *     streamOfTupleStringInt.map(Maps::swap);
     * </code>
     *
     * @param entry entry to be swapped.
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple to R function.
     */
    public static <K, V> Tuple<V, K> swap(Tuple<K, V> entry) {
        return Tuple.of(entry.getSecond(), entry.getFirst());
    }

    /**
     * Returns a transformer function which transforms a ByPredicate of key and value to a Predicate of Tuple.
     *
     * <code>
     *     streamOfTupleIntInt.filter(isEntry((key, value) -> key + value > 10);
     * </code>
     *
     * @param biPredicate predicate function which consumes key and value seperately
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple predicate.
     */
    public static <K, V> Predicate<Tuple<K, V>> isEntry(BiPredicate<? super K, ? super V> biPredicate) {
        return entry -> biPredicate.test(entry.getFirst(), entry.getSecond());
    }

    /**
     * Returns a transformer function which transforms key Predicate to a Tuple Predicate.
     *
     * <code>
     *     streamOfTupleIntInt.filter(isKey(key -> key > 10);
     * </code>
     *
     * @param predicate Key predicate
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple Predicate
     */
    public static <K, V> Predicate<Tuple<K, V>> isKey(Predicate<? super K> predicate) {
        return entry -> predicate.test(entry.getFirst());
    }

    /**
     * Returns a transformer function which transforms value Predicate to a Tuple Predicate.
     *
     * <code>
     *     streamOfTupleIntInt.filter(isValue(value -> value > 10);
     * </code>
     *
     * @param predicate Value predicate
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple Predicate
     */
    public static <K, V> Predicate<Tuple<K, V>> isValue(Predicate<? super V> predicate) {
        return entry -> predicate.test(entry.getSecond());
    }

    /**
     * Returns a transformer function which transforms function of value to Stream<R>. useful in flatMap function.
     *
     * <code>
     *     streamOfTupleIntInt.flatMap(withSecond(value -> Stream.of(value));
     * </code>
     *
     * @param function function produces R stream using values
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to Stream Tuple function.
     */
    public static <K, V, R> Function<Tuple<K, V>, Stream<Tuple<K, R>>> withValue(Function<V, Stream<R>> function) {
        return entry -> function.apply(entry.getSecond()).map(Tuple.withFirst(entry.getFirst()));
    }

    /**
     * Returns a transformer function which transforms function of key to Stream<R>. useful in flatMap function.
     *
     * <code>
     *     streamOfTupleIntInt.flatMap(withFirst(key -> Stream.of(key));
     * </code>
     *
     * @param function function produces R stream using keys
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to Stream Tuple function.
     */
    public static <K, V, R> Function<Tuple<K, V>, Stream<Tuple<R, V>>> withKey(Function<K, Stream<R>> function) {
        return entry -> function.apply(entry.getFirst()).map(Tuple.withSecond(entry.getSecond()));
    }

    /**
     * Returns a collector which is collects Tuples to a map.
     *
     * <code>
     *     streamOfTupleIntInt.collect(Maps.toMap());
     * </code>
     *
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple to java.util.Map collector
     */
    public static <K, V> Collector<Tuple<K, V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(Tuple::getFirst, Tuple::getSecond);
    }

    /**
     * Returns a transformer which transforms key comparator to tuple comparator.
     *
     * <code>
     *     streamOfTupleIntInt.sorted(byKey((a, b) -> a < b);
     * </code>
     *
     * @param comparator Key comparator
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple comparator
     */
    public static <K, V> Comparator<Tuple<K, V>> byKey(Comparator<K> comparator) {
        return (o1, o2) -> comparator.compare(o1.getFirst(), o2.getFirst());
    }

    /**
     * Returns a transformer which transforms value comparator to tuple comparator.
     *
     * <code>
     *     streamOfTupleIntInt.sorted(byValue((a, b) -> a < b);
     * </code>
     *
     * @param comparator Value comparator
     * @param <K> Key type
     * @param <V> Value type
     * @return Tuple comparator
     */
    public static <K, V> Comparator<Tuple<K, V>> byValue(Comparator<V> comparator) {
        return (o1, o2) -> comparator.compare(o1.getSecond(), o2.getSecond());
    }
}
