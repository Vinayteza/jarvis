
package com.daimler.duke.document.constant;

/**
 * @author SANDGUP..
 *
 */
public interface Constants {

    /**
     * APP_PROPERTIES
     */
    String  APP_PROPERTIES          = "application.properties";
    /**
     * EMPTY_STRING
     */
    String  EMPTY_STRING            = "";

    /**
     * SEPERATOR String
     */
    String  SEPERATOR               = "|";

    /**
     * Constant for version v1.
     */
    String  V1                      = "V1";

    /**
     * Constant for VERSION_NOT_SUPPORTED MESSAGE.
     */
    String  VERSION_NOT_SUPPORTED   = "Version is not supported Or Request without a version number";

    /**
     * Constant for VERSION_NOT_SUPPORTED MESSAGE.
     */
    String  VERSION_NOT_IN_REQUEST  = "Request without a version number";

    /**
     * Constant for SECRET MESSAGE.
     */
    String  SECRET                  = "secret123";

    /**
     * Constant for AUTHORIZATION_GROUP MESSAGE.
     */
    String  AUTHORIZATION_GROUP     = "authoGroup123";

    /**
     * Constant for AUTHENTICATION_SCHEME MESSAGE.
     */
    String  AUTHENTICATION_SCHEME   = "Bearer";

    /**
     * Constant for AUTHORIZATION MESSAGE.
     */
    String  AUTHORIZATION           = "Authorization";

    /**
     * Constant for APPLICATION_NAME MESSAGE.
     */
    String  APPLICATION_NAME        = "ApplicationName";

    /**
     * Constant for FILENAME MESSAGE.
     */
    String  FILENAME                = "filename";

    String  STR_PASS_PHRASE         = "atADSdrowssAPGnitpircNE";

    String  STR_SECRET_KEY_ARGUMENT = "PBEWithMD5AndDES";

    String  UTF8                    = "UTF8";

    String  ROLE                    = "role";

    String  CONTENT_TYPE            = "Content-type";

    String  META_DATA               = "metadata";
    String  DOT                     = ".";
    String  CHUNK_ID                = "chunkId";
    String  TOTAL_NUM_OF_CHUNKS     = "totalnumofchunks";
    String  DOCUMENT_META_DATA_ID   = "documentMetaDataId";

    String  SUCCESSFUL_DELETE       = "Document content has been deleted";
    String  DOCUMENT_DELETE_SUCCESSFUL       = "Document has been deleted";


    Integer APPLICATION_CODE        = 2;

    Integer EXPIRE_DATE             = 14;
    String  IS_NUMERIC_REGX         = "[0-9]+";
    String  FETCH_KEY              = "fetchNumber";
    String  FETCH_VALUE              = "12345678";
}
