package org.tix.stream.maps;

import java.util.function.Function;

/**
 * Created by tiran on 4/10/15.
 */
public final class MapEntry<K, V> {
    private K key;
    private V value;

    private MapEntry(K key, V value) {
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

    public static <K, V> MapEntry<K, V> of(K k, V v) {
        return new MapEntry<>(k,v);
    }

    public static <K, V>Function<K, MapEntry<K, V>> withValue(V value) {
        return k -> of(k, value);
    }

    public static <K, V>Function<V, MapEntry<K, V>> withKey(K key) {
        return v -> of(key, v);
    }
}
