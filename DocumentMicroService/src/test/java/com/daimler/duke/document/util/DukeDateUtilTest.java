package com.daimler.duke.document.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DukeDateUtilTest {
    
    public static final String DDMMYYYYHHMMSS_DOTDELIM = "dd.MM.yyyy.HH:mm:ss";

    @Test
    public void testToDateStringNotNull() {
       String date = DukeDateUtil.toDateString(new Date(), DDMMYYYYHHMMSS_DOTDELIM);
       assertNotNull(date);
    }
    
    @Test
    public void testToDateStringNotBlank() {
       String date = DukeDateUtil.toDateString(new Date(), DDMMYYYYHHMMSS_DOTDELIM);
       assertNotEquals("", date);
    }
    
    /*@Test
    public void testToDateTestNotNull() {
        Date date = DukeDateUtil.toDate("", DDMMYYYYHHMMSS_DOTDELIM);
        assertNotEquals("", date);
    }
*/
}
