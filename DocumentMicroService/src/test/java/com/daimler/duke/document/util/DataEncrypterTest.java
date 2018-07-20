package com.daimler.duke.document.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.daimler.duke.document.exception.EncryptionException;

/**
 * ApplicationEncrypterTest class For encryption and decryption of class
 * 
 * @author RMAHAKU
 *
 */
public class DataEncrypterTest {

    /**
     * Decrypt with Valid Encrypted Data
     */
    @Test
    public void encryptTest() {

        String phraseString = null;
        String dukeApplication = null;
        try {
            phraseString = "DukeApplication";
            dukeApplication = "Duke";
            getEncryptorObject(phraseString);
            DataEncrypter dataEncrypter = getEncryptorObject(phraseString);
            String encryptApplName = dataEncrypter.encrypt(dukeApplication);
            assertNotNull(encryptApplName);
            String decodedApplName = dataEncrypter.decrypt(encryptApplName);
            assertEquals(decodedApplName, dukeApplication);
        }
        catch (EncryptionException e) {
            assertFalse(true);
        }

    }

    /**
     * Decrypt With Modified Encrypt aplication name
     */
    @Test
    public void decryptWithModifiedEncyptedData() {

        String phraseString = null;
        String dukeApplication = null;
        try {
            phraseString = "DukeApplication";
            dukeApplication = "Duke";
            DataEncrypter dataEncrypter = getEncryptorObject(phraseString);
            String encryptApplName = dataEncrypter.encrypt(dukeApplication);
            assertNotNull(encryptApplName);
            String decryptApplName = dataEncrypter.decrypt(encryptApplName + "Modified");
            assertEquals(decryptApplName, dukeApplication);
        }
        catch (EncryptionException e) {
            assertTrue(true);
        }
    }

    /**
     * Decrypt With Modified(Wrong) SecretKey
     */
    @Test
    public void decryptWithWrongSecretKey() {

        String phraseString = null;
        String dukeApplication = null;
        try {
            System.out.println("ApplicationEncrypterTest-->decryptWithWrongSecretKey() started");
            phraseString = "DukeApplication";
            dukeApplication = "Duke";
            DataEncrypter applicationEncrypter = getEncryptorObject(phraseString);
            String encryptApplName = applicationEncrypter.encrypt(dukeApplication);
            assertNotNull(encryptApplName);
            DataEncrypter applicationEncrypterModified = getEncryptorObject(phraseString + "Modified");
            String decryptApplName = applicationEncrypterModified.decrypt(encryptApplName);
            assertEquals(decryptApplName, dukeApplication);

        }
        catch (EncryptionException e) {
            assertTrue(true);
        }
    }

    /**
     * pass phraseString to get DataEncrypter object
     * 
     * @param phraseString
     * @return
     * @throws EncryptionFault
     */
    private DataEncrypter getEncryptorObject(String phraseString) throws EncryptionException {
        DataEncrypter dataEncrypter = new DataEncrypter(phraseString);
        return dataEncrypter;
    }
}