package org.tix.stream.maps;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.testng.Assert.*;
import static org.tix.stream.maps.Maps.*;

/**
 * Created by tiran on 4/10/15.
 */
public class MapsTest {

    private HashMap<Integer, String> hashMap;

    @BeforeMethod
    public void setUp() throws Exception {
        hashMap = new HashMap<Integer, String>() {{
            put(1, "first");
            put(2, "second");
            put(3, "third");
            put(4, "fourth");
            put(5, "fifth");
        }};
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @Test
    public void testToEntryComparator() throws Exception {
        ArrayList<String> strings = new ArrayList<>();
        hashMap.entrySet().stream().map(Maps::toTuple)
                .forEach(toEntry((a, b) -> strings.add(a + ":" + b)));

        assertTrue(strings.contains("1:first"));
        assertTrue(strings.contains("2:second"));
        assertTrue(strings.contains("3:third"));
        assertTrue(strings.contains("4:fourth"));
        assertTrue(strings.contains("5:fifth"));
    }

    @Test
    public void testToKey() throws Exception {
        ArrayList<Integer> keys = new ArrayList<>();

        hashMap.entrySet().stream().map(Maps::toTuple)
                .forEach(toKey(keys::add));

        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertTrue(keys.contains(3));
        assertTrue(keys.contains(4));
        assertTrue(keys.contains(5));
    }

    @Test
    public void testToValue() throws Exception {
        ArrayList<String> values = new ArrayList<>();

        hashMap.entrySet().stream().map(Maps::toTuple)
                .forEach(toValue(values::add));

        assertTrue(values.contains("first"));
        assertTrue(values.contains("second"));
        assertTrue(values.contains("third"));
        assertTrue(values.contains("fourth"));
        assertTrue(values.contains("fifth"));
    }

    @Test
    public void testEntries() throws Exception {
        List<String> strings = hashMap.entrySet().stream().map(Maps::toTuple)
                .map(entries((k, v) -> k + ":" + v))
                .collect(toList());

        assertTrue(strings.contains("1:first"));
        assertTrue(strings.contains("2:second"));
        assertTrue(strings.contains("3:third"));
        assertTrue(strings.contains("4:fourth"));
        assertTrue(strings.contains("5:fifth"));
    }

    @Test
    public void testKeys() throws Exception {
        Map<Integer, String> results = hashMap.entrySet().stream().map(Maps::toTuple)
                .map(keys(i -> i * 2))
                .collect(toMap());

        assertNull(results.get(1));
        assertEquals("first", results.get(2));
        assertNull(results.get(3));
        assertEquals("second", results.get(4));
        assertNull(results.get(5));
        assertEquals("third", results.get(6));
        assertNull(results.get(7));
        assertEquals("fourth", results.get(8));
        assertNull(results.get(9));
        assertEquals("fifth", results.get(10));
    }

    @Test
    public void testValues() throws Exception {
        Map<Integer, Integer> results = hashMap.entrySet().stream().map(Maps::toTuple)
                .map(values(String::length))
                .collect(toMap());

        assertTrue("first".length() == results.get(1));
        assertTrue("second".length() == results.get(2));
        assertTrue("third".length() == results.get(3));
        assertTrue("fourth".length() == results.get(4));
        assertTrue("fifth".length() == results.get(5));
    }

    @Test
    public void testSwap() throws Exception {
        Map<String, Integer> result = hashMap.entrySet().stream().map(Maps::toTuple)
                .map(Maps::swap)
                .collect(toMap());

        assertTrue(1 == result.get("first"));
        assertTrue(2 == result.get("second"));
        assertTrue(3 == result.get("third"));
        assertTrue(4 == result.get("fourth"));
        assertTrue(5 == result.get("fifth"));
    }

    @Test
    public void testIsEntry() throws Exception {
        Map<Integer, String> result = hashMap.entrySet().stream().map(Maps::toTuple)
                .filter(isEntry((i, s) -> i < 4 && s.length() == 6))
                .collect(toMap());

        assertEquals(1, result.size());
        assertEquals("second", result.get(2));
    }

    @Test
    public void testIsKey() throws Exception {
        Map<Integer, String> results = hashMap.entrySet().stream().map(Maps::toTuple)
                .filter(isKey(k -> k % 2 == 0))
                .collect(toMap());

        assertEquals(2, results.size());
        assertTrue(results.values().contains("second"));
        assertTrue(results.values().contains("fourth"));
    }

    @Test
    public void testIsValue() throws Exception {
        Map<Integer, String> results = hashMap.entrySet().stream().map(Maps::toTuple)
                .filter(isValue(v -> v.length() == 6))
                .collect(toMap());

        assertEquals(2, results.size());
        assertTrue(results.values().contains("second"));
        assertTrue(results.values().contains("fourth"));
    }

    @Test
    public void testWithValue() throws Exception {
        //TODO flatmap value application while keeping the keys
    }

    @Test
    public void testWithKey() throws Exception {
        //TODO flatmap key application while keeping the value
    }

    @Test
    public void testByKey() throws Exception {
        List<String> list = hashMap.entrySet().stream().map(Maps::toTuple)
                .sorted(byKey(Comparator.<Integer>reverseOrder()))
                .map(Tuple::getSecond).collect(toList());
        assertEquals("fifth", list.get(0));
        assertEquals("fourth", list.get(1));
        assertEquals("third", list.get(2));
        assertEquals("second", list.get(3));
        assertEquals("first", list.get(4));

    }

    @Test
    public void testByValue() throws Exception {
        List<Integer> list = hashMap.entrySet().stream().map(Maps::toTuple)
                .sorted(byValue(comparing(String::length)))
                .map(Tuple::getFirst).collect(toList());

        assertTrue(1 == list.get(0));
        assertTrue(3 == list.get(1));
        assertTrue(5 == list.get(2));
        assertTrue(2 == list.get(3));
        assertTrue(4 == list.get(4));
    }
}