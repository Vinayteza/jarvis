package com.daimler.duke.document.exception;

/**
 * 
 * @author RMAHAKU
 *
 */
public enum CommonErrorCodes {

    COMMON001(100, "Object Not Found", "Object Not Found"),
    DOCUMENT_CONTENT_DOES_NOT_EXIST(101, "Document content does not exist", "Document content does not exist"),
    ERROR_IN_WRITING_INTO_BYTE_STREAM(102, "Error in writing into byte stream", "Error in writing into byte stream"),
    ERROR_IN_CLOSING_BYTE_STREAM(103, "Error in closing byte stream", "Error in closing byte stream"),
    NO_ACCESS_TO_THIS_DOCUMENT(104, "You don't have access to this document", "You don't have access to this document"),
    NOT_ALLOWED_TO_THIS_APPLICATION(105, " Application is not allowed", " Application is not allowed"),
    SECRET_NULL_OR_EMPTY(106, "SecretCode not Null/Empty", "SecretCode not Null/Empty"),
    ENCRYPTED_NAME_NOT_VALID(107, "Encrypted Application Name is not valid", "Encrypted Application Name is not valid"),
    NOT_VALID_AUTH_GROUP(108, "Not Valid Authorization Group for this application ",
            "Not Valid Authorization Group for this application "),
    INVALID_DOCUMRNT_MATADATA(109, "Invalid documentmetadata", "Invalid documentmetadata"),
    DOCUMENT_ID_MUST_BE_NULL(110, "Document id must be null", "Document id must be null"),
    DOCUMENT_ID_IS_NULL(111, "Document id is null", "Document id is null"),
    DOCUMENT_TYPE_IS_REQUIRED(112, "Document type is required", "Document type is required"),
    DOCUMENT_NAME_IS_REQUIRED(113, "Document name is required", "Document name is required"),
    INVALID_DOCUMENT_CONTENT(114, "Invalid document content", "Invalid document content"),
    SECRETS_CODE_IS_REQUIRED(115, "SecretsCode is required", "SecretsCode is required"),
    SECRETS_KEY_IS_REQUIRED(116, "SecretsKey is required", "SecretsKey is required"),
    APPLICATION_NAME_IS_REQUIRED(117, "Application name is required", "Application name is required"),
    APPLICATION_ID_IS_NULL(118, "Application id is null", "Application id is null"),
    APPLICATION_ID_MUST_BE_NULL(119, "Application id must be null", "Application id must be null"),
    INVALID_APPLICATION_IDENTIFIER(120, "Invalid applicationIdentifier", "Invalid applicationIdentifier"),
    ERROR_IN_DECODING_BASE64_DATA(121, "Error in decoding base64 data", "Error in decoding base64 data"),
    ERROR_IN_ENCODING_DATA_TO_BASE64(122, "Error in encoding data to base64 ", "Error in encoding data to base64 "),
    INVALID_TOKEN(123, "Invalid Token ", "Invalid Token"),
    DOCUMENTS_CAN_NOT_BE_NULL(124, "Documents can not be null ", "Documents can not be null"),

    DOCUMENT_IDS_CAN_NOT_BE_NULL(125, "DocumentId's can not be null ", "DocumentId's can not be null"),

    CHUNK_ID_CANNOT_BE_NULL(126, "ChunkId cannot be null", "Chunk id cannot be null"),

    DOCUMENT_META_DATA_ID_CANNOT_BE_NULL(127, "documentMetadataId cannot be null ",
            "Document metadata id cannot be null"),

    TOTAL_NUM_OF_CHUNKS_CANNOT_BE_NULL(128, "totalNumOfChunks cannot be null", "totalNumOfChunks cannot be null"),
    DATA_CANNOT_BE_NULL(129, "data cannot be null", "data cannot be null"),
    DATA_IS_NOT_VALID_BASE64(130, "data is not a valid base64 String ", "data is not a valid base64 String "),
    DOCUMENT_META_DATA_ID_IS_INVALID(131, "documentMetadataId is not valid ", "documentMetadataId is not valid"),
    CHUNK_ALREADY_SAVED(132, "This chunk already saved ", "This chunk already saved"),
    CHUNK_ID_SHOULD_BE_A_VALID_LONG(133, "ChunkId should be a valid long", "ChunkId should be a valid long"),
    TOTAL_NUM_OF_CHUNKS_SHOULD_BE_A_VALID_LONG(134, "totalNumOfChunks should be a valid long",
            "totalNumOfChunks should be a valid long"),

    INVALID_TOKEN_STATUS(135, "Invalid tokenStatus", "Invalid tokenStatus"),
    TOKEN_STATUS_ID_MUST_BE_NULL(136, "tokenStatus id must be null", "tokenStatus id must be null"),
    TOKEN_STATUS_ID_IS_NULL(137, "tokenStatus id is null", "tokenStatus id is null"),
    NOT_VALID_FOR_DOCUMENT_ID(138, "Not valid token for given document id", "Not valid token for given document id"),
    DOCUMENT_ID_REQUIRED_FOR_DOCUMENT_CONTENT(139, "Document id is required for document content",
            "Document id is required for document content"),
    TOKEN_USED(140, "Token is already used ", "Token is already used "),
    ALREADY_UPLOADED_ALL_CHUNKS(141, "Already uploaded all chunks for this meta data ",
            "Already uploaded all chunks for this meta data "),

    DOCUMENT_NOT_UPLOADED_COMPLETELY(142, "Document not uploaded or Document uploaded partially",
            "Document not uploaded or Document uploaded partially"),
    NOT_UPLOADED_ALL_CHUNKS(143, "Not uploaded all chunks", "Not uploaded all chunks"),
    CHUNK_SIZE_CANNOT_BE_NULL_OR_NON_NUMERIC(153, "Chunk size is null or non-numberic",
            "Chunk size is null or non-numberic"),
    CHUNK_SIZE_CANNOT_BE_ZERO(154, "Chunk size zero can not be set, Please set right number.",
            "Chunk size zero can not be set, Please set right number."),
    CHUNK_SIZ_DOCUEMNT_NOT_EXIST(155, "Chunk document does not exist, Please set chunk size first.",
            "Chunk document does not exist, Please set chunk size first."),
    UPLOAD_CHUNK_SIZE_NOT_CONFIGURED(144, "Upload chunk size is not configured ",
            "Upload chunk size is not configured "),
    UPLOAD_DOCUMENT_FOR_THIS_META_DATA_ALREADY_COMPLETED(145, "upload document for this meta data already completed",
            "upload document for this meta data already completed"),
    TOTAL_NUM_OF_CHUNKS_SHOULD_BE_SAME(146, "totalNumOfChunks should be same for all chunks",
            "totalNumOfChunks should be same for all chunks"),
    CHUNK_SIZE_SHOULD_BE_CONFIGURED_ONE(147, "Chunk size should be ", "Chunk size should be "),
    DOCUMENT_CONTENT_ALREADY_EXIST(148, "Document content already exist for given metadata",
            "Document content already exist for given metadata"),
    SERIAL_NO_CAN_NOT_NULL(149, "SerialNo can not be null", "SerialNo can not be null"),
    SERIAL_NO_CAN_NOT_DUPLICATE(150, "SerialNo can not be duplicate", "SerialNo can not be duplicate"),
    TOKEN_EXPIRED(151, "Token is already Expired ", "Token is already Expired "),
    DOCUMENT_META_DATA_NOT_FOUND(152, "documentMetadata not found", "documentMetadata not found"),;
    /**
     * 
     */
    private int    errorCode;
    /**
     * 
     */
    private String descEn;
    /**
     * 
     */
    private String descDe;

    CommonErrorCodes(final int inputErrorCode, final String inputDescEn, final String inputDescDe) {
        this.errorCode = inputErrorCode;
        this.descEn = inputDescEn;
        this.descDe = inputDescDe;
    }

    /**
     * 
     * @return
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 
     * @return
     */

    public String getDescEn() {
        return descEn;
    }

    /**
     * 
     * @return
     */
    public String getDescDe() {
        return descDe;
    }

}
