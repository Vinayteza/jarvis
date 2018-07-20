package com.daimler.duke.document.constant;

/**
 * encoding of binary data.
 *
 * @author chreiche
 *
 */
public enum BinaryDataEncoding {

    /**
     * BASE64 encoding.
     */
    BASE_64("BASE64"); //$NON-NLS-1$
    /*
     * plain text.
     *//*
        * TXT("TXT"); //$NON-NLS-1$
        */
    /**
     * encoding value.
     */
    private final String value;

    /**
     *
     * @param val value.
     */
    BinaryDataEncoding(final String val) {
        value = val;
    }

    /**
     * Gets the encoding.
     *
     * @return <code>String</code> - encoding
     */
    public String value() {
        return value;
    }

    /**
     * Extracts the encoding object from a given value.
     *
     * @param val <code>String</code> - encoding value
     * @return <code>BinaryDataEncoding</code> - BinaryDataEncoding object
     */
    public static BinaryDataEncoding fromValue(final String val) {
        for (final BinaryDataEncoding bd : BinaryDataEncoding.values()) {
            if (val.equals(bd.value)) {
                return bd;
            }

        }
        throw new IllegalArgumentException(val);
    }

}
