package org.tix.stream.maps;

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
     * Transforms <code>java.util.Map.Entry</code> to <code>org.tix.MapEntry</code>
     *
     * Should be used as,
     * <code>
     *     anonymousMap.entrySet().stream.map(Maps::transform)
     * </code>
     *
     * @param entry stream element to be transformed
     * @param <K> key type of the map entry
     * @param <V> value type of map entry
     * @return MapEntry instance
     */
    public static <K, V> MapEntry<K,V> transform(Map.Entry<K, V> entry) {
        return MapEntry.of(entry.getKey(), entry.getValue());
    }

    /**
     * Returns a transformer function which transforms a ByConsumer of key and value to Consumer of MapEntry.
     *
     * <code>
     *     streamOfMapEntry.peak(consumeEntries((key, value) -> System.out.println(key + ":" + value)));
     * </code>
     *
     * @param biConsumer consumer to consume key and value
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry consumer
     */
    public static <K,V> Consumer<MapEntry<K,V>> consumeEntries(BiConsumer<? super K, ? super V> biConsumer) {
        return kvMapEntry -> biConsumer.accept(kvMapEntry.getKey(), kvMapEntry.getValue());
    }

    /**
     * Returns a transformer function which transforms a Consumer of key to Consumer of MapEntry,
     * while preserving the value.
     *
     * <code>
     *     streamOfMapEntry.peak(consumeKeys(key -> System.out.println(key)))
     * </code>
     * @param consumer Key consumer function
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry consumer
     */
    public static <K,V> Consumer<MapEntry<K,V>> consumeKeys(Consumer<? super K> consumer) {
        return entry -> consumer.accept(entry.getKey());
    }

    /**
     * Returns a transformer function which transforms a Consumer of value to Consumer of MapEntry,
     * while preserving the key.
     *
     * <code>
     *     streamOfMapEntry.peak(consumeValues(value -> System.out.println(value));
     * </code>
     *
     * @param consumer Value consumer function
     * @param <K> Key type
     * @param <V> Value function
     * @return MapEntry consumer
     */
    public static <K,V> Consumer<MapEntry<K,V>> consumeValues(Consumer<? super V> consumer) {
        return entry -> consumer.accept(entry.getValue());
    }

    /**
     * Returns a transformer function which transforms a BiFunction of key and value to R, to Function of MapEntry to T,
     *
     * <code>
     *     streamOfMapEntryStringString.map(entries((key, value) -> key + ":" + value);
     * </code>
     *
     * @param biFunction Function which is used to transform key and value to R.
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return MapEntry to R function
     */
    public static <K, V, R> Function<MapEntry<K, V>, R> entries(BiFunction<? super K, ? super V, ? extends R> biFunction) {
        return entry -> biFunction.apply(entry.getKey(), entry.getValue());
    }

    /**
     * Returns a transformer function which transforms Key mapping function to MapEntry mapping function.
     *
     * <code>
     *     streamOfMapEntryStringString.map(keys(key -> "key : " + key);
     * </code>
     *
     * @param function Function which is used to transform key to R
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return MapEntry to R function
     */
    public static <K, V, R> Function<MapEntry<K, V>, MapEntry<R, V>> keys(Function<? super K, ? extends R> function) {
        return kvMapEntry -> MapEntry.of(function.apply(kvMapEntry.getKey()), kvMapEntry.getValue());
    }

    /**
     * Returns a transformer function which transforms Value mapping function to MapEntry mapping Function.
     *
     * <code>
     *     streamOfMapEntryStringString.map(values(value -> "value :" + value);
     * </code>
     *
     * @param function Function which is usd to transform value to R
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return MapEntry to R function
     */
    public static <K, V, R> Function<MapEntry<K, V>, MapEntry<K, R>> values(Function<? super V, ? extends R> function) {
        return kvMapEntry -> MapEntry.of(kvMapEntry.getKey(), function.apply(kvMapEntry.getValue()));
    }

    /**
     * Swaps the key and value of map entry.
     *
     * <code>
     *     streamOfMapEntryStringInt.map(Maps::swap);
     * </code>
     *
     * @param entry entry to be swapped.
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry to R function.
     */
    public static <K, V> MapEntry<V, K> swap(MapEntry<K, V> entry) {
        return MapEntry.of(entry.getValue(), entry.getKey());
    }

    /**
     * Returns a transformer function which transforms a ByPredicate of key and value to a Predicate of MapEntry.
     *
     * <code>
     *     streamOfMapEntryIntInt.filter(predicateEntries((key, value) -> key + value > 10);
     * </code>
     *
     * @param biPredicate predicate function which consumes key and value seperately
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry predicate.
     */
    public static <K, V> Predicate<MapEntry<K, V>> predicateEntries(BiPredicate<? super K, ? super V> biPredicate) {
        return entry -> biPredicate.test(entry.getKey(), entry.getValue());
    }

    /**
     * Returns a transformer function which transforms key Predicate to a MapEntry Predicate.
     *
     * <code>
     *     streamOfMapEntryIntInt.filter(predicateKeys(key -> key > 10);
     * </code>
     *
     * @param predicate Key predicate
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry Predicate
     */
    public static <K, V> Predicate<MapEntry<K, V>> predicateKeys(Predicate<? super K> predicate) {
        return entry -> predicate.test(entry.getKey());
    }

    /**
     * Returns a transformer function which transforms value Predicate to a MapEntry Predicate.
     *
     * <code>
     *     streamOfMapEntryIntInt.filter(predicateValues(value -> value > 10);
     * </code>
     *
     * @param predicate Value predicate
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry Predicate
     */
    public static <K, V> Predicate<MapEntry<K, V>> predicateValues(Predicate<? super V> predicate) {
        return entry -> predicate.test(entry.getValue());
    }

    /**
     * Returns a transformer function which transforms function of value to Stream<R>. useful in flatMap function.
     *
     * <code>
     *     streamOfMapEntryIntInt.flatMap(valuesStream(value -> Stream.of(value));
     * </code>
     *
     * @param function function produces R stream using values
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return MapEntry to Stream MapEntry function.
     */
    public static <K, V, R> Function<MapEntry<K, V>, Stream<MapEntry<K, R>>> valuesStream(Function<V, Stream<R>> function) {
        return entry -> function.apply(entry.getValue()).map(MapEntry.withKey(entry.getKey()));
    }

    /**
     * Returns a transformer function which transforms function of key to Stream<R>. useful in flatMap function.
     *
     * <code>
     *     streamOfMapEntryIntInt.flatMap(keyStream(key -> Stream.of(key));
     * </code>
     *
     * @param function function produces R stream using keys
     * @param <K> Key type
     * @param <V> Value type
     * @param <R> Result type
     * @return MapEntry to Stream MapEntry function.
     */
    public static <K, V, R> Function<MapEntry<K, V>, Stream<MapEntry<R, V>>> keysStream(Function<K, Stream<R>> function) {
        return entry -> function.apply(entry.getKey()).map(MapEntry.withValue(entry.getValue()));
    }

    /**
     * Returns a collector which is collects MapEntries to a map.
     *
     * <code>
     *     streamOfMapEntryIntInt.collect(Maps.toMap());
     * </code>
     *
     * @param <K> Key type
     * @param <V> Value type
     * @return MapEntry to java.util.Map collector
     */
    public static <K, V> Collector<MapEntry<K, V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(MapEntry::getKey, MapEntry::getValue);
    }
}
