package com.daimler.duke.document.util;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;

import com.daimler.duke.document.constant.Constants;

public class EncodeDecodeUtilTest {

    @Test
    public void testEncodeByteArrayToBase64() {
        byte[] input = "str123".getBytes();
        String base64 = EncodeDecodeUtil.encodeByteArrayToBase64(input);
        assertNotNull(base64);
    }

    @Test
    public void testEncodeByteArrayToBase64IfNull() {
        String base64 = EncodeDecodeUtil.encodeByteArrayToBase64(null);
        assertNotNull(base64);
        assertThat(base64, equalTo(Constants.EMPTY_STRING));
    }

    @Test
    public void testEncodeStringToBase64() {
        String input = "str123";
        String base64 = EncodeDecodeUtil.encodeStringToBase64(input);
        assertNotNull(base64);
    }

    @Test
    public void testEncodeStringToBase64IfNull() {
        String base64 = EncodeDecodeUtil.encodeStringToBase64(null);
        assertNull(base64);
    }

    @Test
    public void testDecodeBase64ToString() {
        String input = "str123";
        String base64 = EncodeDecodeUtil.decodeBase64ToString(input);
        assertNotNull(base64);
    }

    @Test
    public void testDecodeBase64ToStringIfNull() {
        String base64 = EncodeDecodeUtil.decodeBase64ToString(null);
        assertNull(base64);
    }

    @Test
    public void testDecodeBase64ToByteArray() {
        String input = "str123";
        byte[] base64 = EncodeDecodeUtil.decodeBase64ToByteArray(input);
        assertNotNull(base64);
    }

    @Test
    public void testDecodeBase64ToByteArrayIfNull() {
        byte[] base64 = EncodeDecodeUtil.decodeBase64ToByteArray(null);
        assertNull(base64);
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        final Constructor<?>[] constructors = EncodeDecodeUtil.class.getDeclaredConstructors();
        for (Constructor<?> constructor: constructors) {
            constructor.setAccessible(true);
            EncodeDecodeUtil util = (EncodeDecodeUtil) constructor.newInstance();
            assertNotNull(util);
            Assert.assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
    }

}
