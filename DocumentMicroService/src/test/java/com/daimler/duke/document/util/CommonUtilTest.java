package com.daimler.duke.document.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

@PrepareForTest({
    CommonUtil.class
})
public class CommonUtilTest {

    @Test
    public void testIsStringNullOrEmptyForNull() {
        assertTrue(CommonUtil.isStringNullOrEmpty(null));
    }
    
    @Test
    public void testIsStringNullOrEmptyForBlank() {
        assertTrue(CommonUtil.isStringNullOrEmpty(""));
    }
    
    @Test
    public void testIsStringNullOrEmptyForNotNull() {
        assertFalse(CommonUtil.isStringNullOrEmpty("NotEmpty"));
    }
    
    @Test
    public void testIsStringNullOrEmptyOrNonBooleanForBoolean() {
        assertFalse(CommonUtil.isStringNullOrEmptyOrNonBoolean("true"));
        assertFalse(CommonUtil.isStringNullOrEmptyOrNonBoolean("False"));
        assertFalse(CommonUtil.isStringNullOrEmptyOrNonBoolean("TRUE"));
    }
    
    @Test
    public void testIsStringNullOrEmptyOrNonBooleanForNonBoolean() {
        assertTrue(CommonUtil.isStringNullOrEmptyOrNonBoolean("Nottrue"));
    }
    
    @Test
    public void testisObjectNullForNull() {
        assertTrue(CommonUtil.isObjectNull(null));
    }
    
    @Test
    public void testisObjectNullForNotNull() {
        assertFalse(CommonUtil.isObjectNull(new Object()));
    }
    
    @Test
    public void testIsCollectionEmptyForEmpty() {
        assertTrue(CommonUtil.isCollectionEmpty(new ArrayList<String>()));
    }
    
    @Test
    public void testIsCollectionEmptyForNonEmpty() {
        assertFalse(CommonUtil.isCollectionEmpty(getRandomList()));
    }
    
    @Test
    public void testIsMapEmptyNonEmpty() {
        assertFalse(CommonUtil.isMapEmpty(getRandomMap()));
    }
    
    @Test
    public void testIsMapEmptyForEmpty() {
        assertTrue(CommonUtil.isMapEmpty(new HashMap<String, String>()));
    }
    
    @Test
    public void testClearCollection() {
        ArrayList<String> sampleList = getRandomList();
        CommonUtil.clearCollection(sampleList);
        assertThat("Size is Zero:",sampleList.size(),is(0));
    }
    
    @Test
    public void testClearCollectionForEmptyCollection() {
        ArrayList<String> sampleList = new ArrayList<String>();
        CommonUtil.clearCollection(sampleList);
        assertThat("Size is Zero:",sampleList.size(),is(0));
    }
    
    @Test
    public void testAreObjectsEqual() {
        assertTrue(CommonUtil.areObjectsEqual(new Integer(20), new Integer(20)));
    }
    
    @Test
    public void testAreObjectsEqualForNotEqual() {
        assertFalse(CommonUtil.areObjectsEqual(new Integer(20), new Integer(22)));
    }
    
    @Test
    public void testDateNullCheck() {        
        assertTrue(CommonUtil.dateNullCheck(new Date()) != null);
    }
    
    @Test
    public void testDateNullCheckForNull() {        
        assertTrue(CommonUtil.dateNullCheck(null) == null);
    }
    
    @Test
    public void testGetCurrentDate(){
        assertTrue(CommonUtil.getCurrentDate().equals(new Date()));
    }
    
    @Test
    public void testStringNullCheck() {        
        assertTrue(CommonUtil.stringNullCheck("") != null);
    }
    
    @Test
    public void testStringNullCheckForNull() {        
        assertTrue(CommonUtil.stringNullCheck(null) == null);
    }
    
    @Test
    public void testLongNullCheck() {        
        assertTrue(CommonUtil.longNullCheck(new Long("1234")) != null);
    }
    
    @Test
    public void testLongNullCheckForNull() {        
        assertTrue(CommonUtil.longNullCheck(null) == null);
    }
    
    @Test
    public void testGetStringFromLists() {
        assertEquals(String.class,CommonUtil.getStringFromLists(getRandomList(), ",").getClass());
    }
    
    @Test
    public void testGetStringFromListsCheckResults() {
        assertTrue(CommonUtil.getStringFromLists(getRandomList(), ",").contains("First"));
        assertTrue(CommonUtil.getStringFromLists(getRandomList(), ",").contains("Second"));
        assertFalse(CommonUtil.getStringFromLists(getRandomList(), ",").contains("Random"));
    }
    
    @Test
    public void testGetStringFromListsForList() {
        assertNotEquals(ArrayList.class,CommonUtil.getStringFromLists(getRandomList(), ",").getClass());
    }
    
    @Test
    public void testIsValidLong() {
        assertTrue(CommonUtil.isValidLong("12322"));
    }
    
    @Test
    public void testIsValidLongForNonLong() {
        assertFalse(CommonUtil.isValidLong("ABC"));
    }
    
    @Test
    public void testIsValidLongForNUll() {
        assertFalse(CommonUtil.isValidLong(null));
    }
    
    @Test
    public void testTrimStringForEmpty() {        
        assertTrue(CommonUtil.trimString("") == null);
    }
    
    @Test
    public void testTrimStringForNull() {        
        assertTrue(CommonUtil.trimString(null) == null);
    }
    
    @Test
    public void testTrimStringForNotNull() {        
        assertTrue(CommonUtil.trimString("1234 ").length() == 4);
    }
    
    @Test
    public void testTrimStringForNotNull2() {        
        assertTrue(CommonUtil.trimString("1234 ").length() != 5);
    }
    
    @Test
    public void testGetSingleObjectAsList() {        
        assertThat("List Size is 1",CommonUtil.getSingleObjectAsList("1234").size(),is(1));
    }
    
    @Test
    public void testGetSingleObjectAsListCheckValidData() {        
        assertTrue(CommonUtil.getSingleObjectAsList("1234").get(0).equals("1234"));
    }
    
    private HashMap<String, String> getRandomMap(){
        HashMap<String, String> newMap = new HashMap<String, String>();
        newMap.put("Key1", "Value1");
        newMap.put("Key2", "Value2");
        newMap.put("Key3", "Value3");
        return newMap;
    }
    
    private ArrayList<String> getRandomList(){
        ArrayList<String> newList = new ArrayList<String>();
        newList.add("First");
        newList.add("Second");
        return newList;
    }

}
