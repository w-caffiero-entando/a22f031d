package org.entando.entando.web.common.model;

import org.junit.Test;

import java.util.function.BiFunction;

import static org.junit.Assert.*;

public class RestNamedIdTest {

    public static final String TEST_ID = "306dadea";
    public static final String TEST_ID2 = "code=306dadea";
    public static final String TEST_ID3 = "code=306dadea=96f6=48c7==";
    public static final String TEST_ID4 = "code==306dadea=96f6=48c7==";
    public static final String TEST_ID5 = "=code=306dadea=96f6=48c7==";
    public static final String TEST_ID6 = "==code=306dadea=96f6=48c7==";
    public static final String TEST_ID7 = "code=";
    public static final String TEST_ID8 = "";
    public static final String TEST_ID9 = "=";

    @Test
    public void testSimpleCostruction() {
        RestNamedId res = RestNamedId.from(TEST_ID);
        assertEquals("", res.name);
        assertEquals(TEST_ID, res.value);
        assertFalse(res.hasName());
        assertTrue(res.hasName(""));

        res = RestNamedId.from(TEST_ID2);
        assertEquals("code", res.name);
        assertEquals("306dadea", res.value);
        assertTrue(res.hasName());
        assertTrue(res.hasName("code"));

        res = RestNamedId.from(TEST_ID3);
        assertEquals("code", res.name);
        assertEquals("306dadea=96f6=48c7==", res.value);

        res = RestNamedId.from(TEST_ID4);
        assertEquals("code", res.name);
        assertEquals("=306dadea=96f6=48c7==", res.value);
    }

    @Test
    public void testCostructionWithOf() {
        BiFunction<String, String, Boolean> test = (name, value) -> {
            RestNamedId res = RestNamedId.of(name, value);
            assertEquals((name == null) ? "" : name, res.name);
            assertEquals(value, res.value);
            return true;
        };

        test.apply("code", "306dadea");
        test.apply("", "306dadea=");
        test.apply("", "=306dad=ea");
        test.apply("", "=306dad=ea");
        test.apply(null, "=306dad=ea");
    }

    @Test
    public void testCostructionSpecials() {
        RestNamedId res = RestNamedId.from(TEST_ID8);
        assertEquals("", res.name);
        assertEquals("", res.value);
        assertFalse(res.hasName());
        assertTrue(res.hasName(""));

        res = RestNamedId.from(TEST_ID9);
        assertEquals("", res.name);
        assertEquals("", res.value);

        res = RestNamedId.from(TEST_ID5);
        assertEquals("", res.name);
        assertEquals("code=306dadea=96f6=48c7==", res.value);

        res = RestNamedId.from(TEST_ID6);
        assertEquals("", res.name);
        assertEquals("=code=306dadea=96f6=48c7==", res.value);

        res = RestNamedId.from(TEST_ID7);
        assertEquals("code", res.name);
        assertEquals("", res.value);

        res = RestNamedId.from(null);
        assertEquals("", res.name);
        assertEquals("", res.value);
    }

    @Test
    public void testValueExtraction() {
        assertEquals("306dadea", RestNamedId.from(TEST_ID).getValidValue(RestNamedId.NO_NAME).orElse(null));
        assertEquals("306dadea", RestNamedId.from(TEST_ID2).getValidValue("code").orElse(null));
        assertNull(RestNamedId.from(TEST_ID2).getValidValue("a-wrong-name").orElse(null));
        assertNull(RestNamedId.from(TEST_ID2).getValidValue(null).orElse(null));
        assertNull(RestNamedId.from(null).getValidValue(null).orElse(null));
    }

    @Test
    public void testToString() {
        assertEquals("306dadea", RestNamedId.from(TEST_ID).toString());
        assertEquals("code=306dadea", RestNamedId.from(TEST_ID2).toString());
        assertEquals("", RestNamedId.from(TEST_ID8).toString());
        assertEquals("=", RestNamedId.from(TEST_ID9).toString());
        assertEquals("", RestNamedId.of("","").toString());
        assertEquals("306dadea", RestNamedId.of("","306dadea").toString());
        assertEquals("==", RestNamedId.of("","=").toString());
    }
}