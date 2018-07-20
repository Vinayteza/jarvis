package com.daimler.duke.document.util;

import java.util.Base64;

import com.daimler.duke.document.constant.Constants;

/**
 * @author SANDGUP.
 *
 */
public final class EncodeDecodeUtil {
    private EncodeDecodeUtil() {
    }

    /**
     * @param input.
     * @return
     * @throws Exception
     */
    public static String encodeByteArrayToBase64(final byte[] input) {
        if (input != null && input.length > 0) {
            return Base64.getEncoder().encodeToString(input);
        }
        return Constants.EMPTY_STRING;
    }

    /**
     * @param input.
     * @return
     * @throws Exception
     */
    public static String encodeStringToBase64(final String input) {
        if (input != null && !Constants.EMPTY_STRING.equals(input)) {
            return Base64.getEncoder().encodeToString(input.getBytes());
        }
        return input;
    }

    /**
     * @param input.
     * @return
     * @throws Exception
     */
    public static String decodeBase64ToString(final String input) {
        if (input != null && !Constants.EMPTY_STRING.equals(input)) {
            return new String(Base64.getDecoder().decode(input));
        }
        return input;
    }

    /**
     * @param input.
     * @return
     * @throws Exception
     */
    public static byte[] decodeBase64ToByteArray(final String input) {
        if (input != null && !Constants.EMPTY_STRING.equals(input)) {
            return Base64.getDecoder().decode(input);
        }
        return null;
    }

}
