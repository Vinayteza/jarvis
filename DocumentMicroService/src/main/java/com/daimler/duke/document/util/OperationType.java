package com.daimler.duke.document.util;

/**
 * The Enum OperationTypeWS.
 */
public enum OperationType {

    /**
     * delete operation type.
     */
    DELETE("DELETE"), //$NON-NLS-1$
    /**
     * update operation type.
     */
    UPDATE("UPDATE"), //$NON-NLS-1$
    /**
     * update operation type.
     */
    READ("READ"), //$NON-NLS-1$
    /**
     * create operation type.
     */
    CREATE("CREATE"); //$NON-NLS-1$

    /**
     * String value of the enum.
     */
    private final String value;

    /**
     * Constructor of the enum.
     * 
     * @param val <code>String</code> - test status string
     */
    OperationType(final String val) {
        value = val;
    }

    /**
     * Gets the OperationType status.
     * 
     * @return <code>String</code> - OperationType status
     */
    public String value() {
        return value;
    }

    /**
     * Extracts the OperationType from the given value.
     * 
     * @param value <code>String</code> - operation type string
     * @return <code>OperationType</code> - OperationType object
     */
    public static OperationType fromValue(final String value) {
        for (OperationType bd : OperationType.values()) {
            if (bd.value.equals(value)) {
                return bd;
            }
        }
        throw new IllegalArgumentException(value);
    }

}
