package com.daimler.duke.document.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.daimler.duke.document.constant.Constants;

/**
 * Utility Class
 *
 * @author SANDGUP.
 *
 */
public final class CommonUtil {

    /**
     * Default private Constructor.
     */
    private CommonUtil() {
        // emtpy constructor
    }

    /**
     * Converts the unique identifier to the db unique identifier
     *
     * @param <T> - unique id type
     *
     * @param source - the source object type
     * @param uniqueIdType <code>Class<T></code> - unique id type
     * @return - the target object type
     * @throws ConversionException Conversion Exception
     */

    /**
     * Checks for value presence.
     *
     * @param value <code>String</code> - The String value.
     * @return <code>boolean</code> - Returns true if the value is null or empty
     *         other wise returns false
     */
    public static boolean isStringNullOrEmpty(String value) {
        return (null == value || Constants.EMPTY_STRING.equals(value.trim()))
                ? true
                : false;
    }

    public static boolean isStringNullOrEmptyOrNonBoolean(String value) {
        return (isStringNullOrEmpty(value) || ! (value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("false"))) ? true : false;
    }

    /**
     * Trims the String value if valid other wise returns null.
     *
     * @param value <code>String</code> - The String value.
     * @return <code>String</code> - Returns The Trimmed Value Or Null
     */
    public static String trimString(String value) {
        return (null == value || Constants.EMPTY_STRING.equals(value.trim()))
                ? null
                : value.trim();
    }

    /**
     * Checks for presence of value.
     *
     * @param object
     * @return data or null.
     */
    public static java.util.Date dateNullCheck(final Object object) {
        return (object == null ? null : (java.util.Date) object);
    }

    /**
     * Checks for presence of value.
     *
     * @param object
     * @return long or null
     */
    public static Long longNullCheck(final Object object) {
        return (object == null ? null : Long.valueOf(object.toString()));
    }

    /**
     * Checks the Collection and return false if it's not null and empty
     *
     * @param collection <code>Collection<T></code> The Collection object.
     * @return <code>boolean</code> Returns false if the list is not null &
     *         empty.
     */
    public static <T> boolean isCollectionEmpty(Collection<T> collection) {
        return (null == collection || collection.isEmpty()) ? true : false;
    }

    public static <T, V> boolean isMapEmpty(Map<T, V> map) {
        return (null == map || map.isEmpty()) ? true : false;
    }

    /**
     * Clears the Collection.
     *
     * @param collection <code>Collection<T></code> The Collection object.
     */
    public static <T> void clearCollection(Collection<T> collection) {
        if (!isCollectionEmpty(collection)) {
            collection.clear();
        }
    }

    /**
     * Checks Wheather The Object Is Null or Not.
     *
     * @param object <code>List<T></code> The list type objects.
     * @return <code>boolean</code> Returns true if the object is null.
     */
    public static boolean isObjectNull(Object object) {
        return (null == object) ? true : false;
    }

    /**
     * Checks for equality of the two objects
     *
     * @param object1 <code>Object</code> First Object
     * @param object2 <code>Object<code> Second Object
     * @return <code>boolean</code> Returns true is the Objects are not null &
     *         empty.
     */
    public static <T> boolean areObjectsEqual(Object object1, Object object2) {
        return (null == object1 || null == object2 || !object1.equals(object2))
                ? false
                : true;
    }

    /**
     * Checks for presence of value.
     *
     * @param object
     * @return string or null.
     */
    public static String stringNullCheck(final Object object) {
        return (object == null ? null : object.toString());
    }

    /**
     * Gets The Current Date.
     *
     * @return <code>Date</code> - Returns the Current Date.
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * Returns a list with one input object added to it
     *
     * @param t
     * @return
     */
    public static <T> List<T> getSingleObjectAsList(T t) {
        List<T> list = new ArrayList<T>();
        list.add(t);
        return list;
    }

    /**
     * @param strings.
     * @param seperator
     * @return
     */
    public static String getStringFromLists(List<String> strings,
                                            String seperator) {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            String string = (String) iterator.next();
            buffer.append(string);
            if (iterator.hasNext()) {
                buffer.append(seperator);
            }

        }
        return buffer.toString();

    }

    /**
     * @param longStr.
     * @return
     */
    public static boolean isValidLong(String longStr) {

        boolean isValid = false;
        if (!CommonUtil.isStringNullOrEmpty(longStr)) {
            try {
                Long.parseLong(longStr.trim());
                isValid = true;
            } catch (NumberFormatException e) {
                isValid = false;
            }
        }

        return isValid;

    }
    
    /**
     * to get Token From authorization header
     * @param authorization
     * @return
     */
    public static String getToken(String authorization) {
        String token = authorization.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();
        return token;
    }
}
