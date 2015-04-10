package org.tix.stream.maps;

import java.util.Comparator;
import java.util.Map;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by tiran on 4/10/15.
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
        return kvTuple -> biConsumer.accept(kvTuple.getKey(), kvTuple.getValue());
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
        return entry -> consumer.accept(entry.getKey());
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
        return entry -> consumer.accept(entry.getValue());
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
        return entry -> biFunction.apply(entry.getKey(), entry.getValue());
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
     * @return Tuple to R function
     */
    public static <K, V, R> Function<Tuple<K, V>, Tuple<R, V>> keys(Function<? super K, ? extends R> function) {
        return kvTuple -> Tuple.of(function.apply(kvTuple.getKey()), kvTuple.getValue());
    }

    /**
     * Returns a transformer function which transforms Value mapping function to Tuple mapping Function.
     *
     * <code>
     *     streamOfTupleStringString.map(values(value -> "value :" + value);
     * </code>
     *
     * @param function Function which is usd to transform value to R
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to R function
     */
    public static <K, V, R> Function<Tuple<K, V>, Tuple<K, R>> values(Function<? super V, ? extends R> function) {
        return kvTuple -> Tuple.of(kvTuple.getKey(), function.apply(kvTuple.getValue()));
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
        return Tuple.of(entry.getValue(), entry.getKey());
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
        return entry -> biPredicate.test(entry.getKey(), entry.getValue());
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
        return entry -> predicate.test(entry.getKey());
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
        return entry -> predicate.test(entry.getValue());
    }

    /**
     * Returns a transformer function which transforms function of value to Stream<R>. useful in flatMap function.
     *
     * <code>
     *     streamOfTupleIntInt.flatMap(withValue(value -> Stream.of(value));
     * </code>
     *
     * @param function function produces R stream using values
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to Stream Tuple function.
     */
    public static <K, V, R> Function<Tuple<K, V>, Stream<Tuple<K, R>>> withValue(Function<V, Stream<R>> function) {
        return entry -> function.apply(entry.getValue()).map(Tuple.withKey(entry.getKey()));
    }

    /**
     * Returns a transformer function which transforms function of key to Stream<R>. useful in flatMap function.
     *
     * <code>
     *     streamOfTupleIntInt.flatMap(withKey(key -> Stream.of(key));
     * </code>
     *
     * @param function function produces R stream using keys
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return Tuple to Stream Tuple function.
     */
    public static <K, V, R> Function<Tuple<K, V>, Stream<Tuple<R, V>>> withKey(Function<K, Stream<R>> function) {
        return entry -> function.apply(entry.getKey()).map(Tuple.withValue(entry.getValue()));
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
        return Collectors.toMap(Tuple::getKey, Tuple::getValue);
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
        return (o1, o2) -> comparator.compare(o1.getKey(), o2.getKey());
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
        return (o1, o2) -> comparator.compare(o1.getValue(), o2.getValue());
    }
}
