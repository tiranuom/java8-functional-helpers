package org.tix.stream.maps;

import java.util.function.Function;

/**
 * Created by tiran on 4/10/15.
 */
public final class Tuple<K, V> {
    private K key;
    private V value;

    private Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public static <K, V> Tuple<K, V> of(K k, V v) {
        return new Tuple<>(k,v);
    }

    public static <K, V>Function<K, Tuple<K, V>> withValue(V value) {
        return k -> of(k, value);
    }

    public static <K, V>Function<V, Tuple<K, V>> withKey(K key) {
        return v -> of(key, v);
    }
}
