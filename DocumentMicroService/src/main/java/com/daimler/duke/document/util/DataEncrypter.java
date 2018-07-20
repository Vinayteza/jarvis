package com.daimler.duke.document.util;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.exception.EncryptionException;

/**
 * DataEncrypter class used for Encrypt & Decrypt the Application. Encryption followed by Encoding with Respect to
 * phraseString, Decryption and followed by Decoding with Respect to phraseString
 * 
 * @author RMAHAKU
 *
 */
public class DataEncrypter {

    /**
     * logger instance.
     */
    private static final Logger LOGGER         = LoggerFactory.getLogger(DataEncrypter.class);
    private Cipher              ecipher;
    private Cipher              dcipher;
    private byte[]              salt           = {
                                                   (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
                                                   (byte) 0x35, (byte) 0xE3, (byte) 0x03
    };
    private int                 iterationCount = 19;

    /**
     * Constructs ApplicationEncrypter object with specified phraseString value.
     *
     * @param phraseString
     * @throws EncryptionException
     * 
     */
    public DataEncrypter(String phraseString) throws EncryptionException {
        try {
            String passPhrase = Constants.STR_PASS_PHRASE + phraseString;
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(Constants.STR_SECRET_KEY_ARGUMENT).generateSecret(keySpec);
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        }
        catch (java.security.InvalidAlgorithmParameterException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in DataEncrypter() method: " + e);
        }
        catch (java.security.spec.InvalidKeySpecException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in DataEncrypter() method: " + e);

        }
        catch (javax.crypto.NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in DataEncrypter() method: " + e);
        }
        catch (java.security.NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in DataEncrypter() method: " + e);
        }
        catch (java.security.InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in DataEncrypter() method: " + e);
        }
    }

    /**
     * Encrypt the given aplicationName.
     *
     * @param aplicationName
     * @return
     * @throws EncryptionException
     */
    public String encrypt(String aplicationName) throws EncryptionException {
        try {
            byte[] utf8 = aplicationName.getBytes(Constants.UTF8);

            // Encrypt
            byte[] encrytApplication = ecipher.doFinal(utf8);

            // Encode
            return new sun.misc.BASE64Encoder().encode(encrytApplication);
        }
        catch (javax.crypto.BadPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in encrypt() method: " + e);
        }
        catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in encrypt() method: " + e);
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in encrypt() method: " + e);

        }
    }

    /**
     * Decrypt the given encrypted application.
     *
     * @param encryptedApplication
     * @return
     * @throws EncryptionException
     */
    public String decrypt(String encryptedApplication) throws EncryptionException {
        try {
            // Decode base64 to get bytes
            byte[] decodedApplication = new sun.misc.BASE64Decoder().decodeBuffer(encryptedApplication);

            // Decrypt
            byte[] utf8 = dcipher.doFinal(decodedApplication);

            // Decode using utf-8
            return new String(utf8, Constants.UTF8);

        }
        catch (javax.crypto.BadPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in decrypt() method: " + e);
        }
        catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in decrypt() method: " + e);
        }
        catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in decrypt() method: " + e);
        }
        catch (java.io.IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new EncryptionException("Exception occured in decrypt() method: " + e);
        }
    }

}
