package com.daimler.duke.document.interceptor;

import java.util.ArrayList;
import java.util.List;

import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.dto.DocumentChunkV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.dto.UniqueId;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.util.CommonUtil;
import com.daimler.duke.document.util.EncodeDecodeUtil;
import com.daimler.duke.document.util.OperationType;

/**
 * Request Validator for DocumentMetaData object.
 * 
 * @author NAYASAR
 *
 */
public class RequestValidator {

    /**
     * validate DocumentMetaDataV1 Request
     * 
     * @param documentMetaData.
     * @param operationType
     * @return
     */
    public static List<String> validateDocumentMetaDataRequest(DocumentMetaDataV1 documentMetaData,
                                                               OperationType operationType) {
        List<String> errorDetail = new ArrayList<>();
        if (CommonUtil.isObjectNull(documentMetaData)) {
            errorDetail.add(CommonErrorCodes.INVALID_DOCUMRNT_MATADATA.getDescDe());
            return errorDetail;
        }
        if (OperationType.CREATE.equals(operationType)) {
            if (!CommonUtil.isStringNullOrEmpty(documentMetaData.getDocumentId())) {
                errorDetail.add(CommonErrorCodes.DOCUMENT_ID_MUST_BE_NULL.getDescDe());
            }
        }
        if (OperationType.UPDATE.equals(operationType)) {
            if (CommonUtil.isStringNullOrEmpty(documentMetaData.getDocumentId())) {
                errorDetail.add(CommonErrorCodes.DOCUMENT_ID_IS_NULL.getDescDe());
            }
        }

        if (CommonUtil.isStringNullOrEmpty(documentMetaData.getDocumentType())) {
            errorDetail.add(CommonErrorCodes.DOCUMENT_TYPE_IS_REQUIRED.getDescDe());
        }
        if (CommonUtil.isStringNullOrEmpty(documentMetaData.getDocumentName())) {
            errorDetail.add(CommonErrorCodes.DOCUMENT_NAME_IS_REQUIRED.getDescDe());
        }

        return errorDetail;

    }

    /**
     * validate the Document Request data.
     * 
     * @param document.
     * @param operationType
     * @return
     */
    public static List<String> validateDocumentRequest(DocumentV1 document, OperationType operationType) {
        List<String> errorDetail = new ArrayList<>();
        if (OperationType.CREATE.equals(operationType) || OperationType.UPDATE.equals(operationType)) {
            if (CommonUtil.isObjectNull(document.getDocumentMetaData())) {
                errorDetail.add(CommonErrorCodes.INVALID_DOCUMRNT_MATADATA.getDescDe());
            }

            if (!CommonUtil.isObjectNull(document.getDocumentMetaData())) {
                if (CommonUtil.isStringNullOrEmpty(document.getDocumentMetaData().getDocumentType())) {
                    errorDetail.add(CommonErrorCodes.DOCUMENT_TYPE_IS_REQUIRED.getDescDe());
                }
                if (CommonUtil.isStringNullOrEmpty(document.getDocumentMetaData().getDocumentName())) {
                    errorDetail.add(CommonErrorCodes.DOCUMENT_NAME_IS_REQUIRED.getDescDe());
                }
            }
            if (CommonUtil.isObjectNull(document.getDocumentContent())) {
                errorDetail.add(CommonErrorCodes.INVALID_DOCUMENT_CONTENT.getDescDe());
                return errorDetail;
            }
            if (CommonUtil.isStringNullOrEmpty(document.getDocumentContent().getData())) {
                errorDetail.add(CommonErrorCodes.INVALID_DOCUMENT_CONTENT.getDescDe());

            }
        }

        return errorDetail;
    }

    /**
     * @param uniqueId.
     * @return
     */
    public static boolean isUniqueIdCorrect(UniqueId uniqueId) {
        boolean validate = true;
        if (CommonUtil.isObjectNull(uniqueId) || CommonUtil.isStringNullOrEmpty(uniqueId.getExtension())
                || CommonUtil.isStringNullOrEmpty(uniqueId.getRoot())) {
            validate = false;
        }
        return validate;
    }

    public static List<String> validateApplicationIdentifierRequest(ApplicationIdentifierV1 applicationIdentifier,
                                                                    OperationType operationType) {
        List<String> errorDetail = new ArrayList<>();
        if (CommonUtil.isObjectNull(applicationIdentifier)) {
            errorDetail.add(CommonErrorCodes.INVALID_APPLICATION_IDENTIFIER.getDescDe());
            return errorDetail;
        }
        if (OperationType.CREATE.equals(operationType)) {
            if (!CommonUtil.isStringNullOrEmpty(applicationIdentifier.getApplicationId())) {
                errorDetail.add(CommonErrorCodes.APPLICATION_ID_MUST_BE_NULL.getDescDe());
            }
        }
        if (OperationType.UPDATE.equals(operationType)) {
            if (CommonUtil.isStringNullOrEmpty(applicationIdentifier.getApplicationId())) {
                errorDetail.add(CommonErrorCodes.APPLICATION_ID_IS_NULL.getDescDe());
            }
        }

        if (CommonUtil.isStringNullOrEmpty(applicationIdentifier.getApplicationName())) {
            errorDetail.add(CommonErrorCodes.APPLICATION_NAME_IS_REQUIRED.getDescDe());
        }
        if (CommonUtil.isStringNullOrEmpty(applicationIdentifier.getSecretKey())) {
            errorDetail.add(CommonErrorCodes.SECRETS_KEY_IS_REQUIRED.getDescDe());
        }

        if (CommonUtil.isStringNullOrEmpty(applicationIdentifier.getSecretCode())) {
            errorDetail.add(CommonErrorCodes.SECRETS_CODE_IS_REQUIRED.getDescDe());
        }

        return errorDetail;

    }

    public static List<String> validateDocumentChunk(DocumentChunkV1 documentChunkV1, DbUploadFileChunkSize dbUploadFileChunkSize) {
        List<String> errorDetail = new ArrayList<>();

        if (CommonUtil.isStringNullOrEmpty(documentChunkV1.getChunkId())) {
            errorDetail.add(CommonErrorCodes.CHUNK_ID_CANNOT_BE_NULL.getDescDe());
        }
        else if (!CommonUtil.isValidLong(documentChunkV1.getChunkId())) {
            errorDetail.add(CommonErrorCodes.CHUNK_ID_SHOULD_BE_A_VALID_LONG.getDescDe());
        }

        if (CommonUtil.isStringNullOrEmpty(documentChunkV1.getDocumentMetaDataId())) {
            errorDetail.add(CommonErrorCodes.DOCUMENT_META_DATA_ID_CANNOT_BE_NULL.getDescDe());
        }

        if (CommonUtil.isStringNullOrEmpty(documentChunkV1.getTotalNumOfChunks())) {
            errorDetail.add(CommonErrorCodes.TOTAL_NUM_OF_CHUNKS_CANNOT_BE_NULL.getDescDe());
        }
        else if (!CommonUtil.isValidLong(documentChunkV1.getTotalNumOfChunks())) {
            errorDetail.add(CommonErrorCodes.TOTAL_NUM_OF_CHUNKS_SHOULD_BE_A_VALID_LONG.getDescDe());
        }

        if (CommonUtil.isStringNullOrEmpty(documentChunkV1.getContent())) {
            errorDetail.add(CommonErrorCodes.DATA_CANNOT_BE_NULL.getDescDe());
        }
        else {
            try {
                byte[] data =
                        EncodeDecodeUtil.decodeBase64ToByteArray(documentChunkV1.getContent());

                if (dbUploadFileChunkSize == null) {
                    errorDetail.add(CommonErrorCodes.UPLOAD_CHUNK_SIZE_NOT_CONFIGURED.getDescDe());
                } else {
                    
                    
                    if (data.length != Integer.parseInt(dbUploadFileChunkSize.getUploadCunkSize())
                            && Long.parseLong(documentChunkV1.getTotalNumOfChunks()) != Long.parseLong(documentChunkV1.getChunkId())) {
                        errorDetail.add(CommonErrorCodes.CHUNK_SIZE_SHOULD_BE_CONFIGURED_ONE.getDescDe()
                                + dbUploadFileChunkSize.getUploadCunkSize());
                    }
                }
            }
            catch (Exception e) {
                errorDetail.add(CommonErrorCodes.DATA_IS_NOT_VALID_BASE64.getDescDe());
            }
        }

        return errorDetail;
    }

    public static List<String> validateDocumentContentRequest(DocumentContentV1 documentContentV1,
                                                              OperationType operationType) {
        List<String> errorDetail = new ArrayList<>();
        if (OperationType.CREATE.equals(operationType)) {
            if (CommonUtil.isObjectNull(documentContentV1)) {
                errorDetail.add(CommonErrorCodes.INVALID_DOCUMENT_CONTENT.getDescDe());
            }

            if (CommonUtil.isObjectNull(documentContentV1.getDocumentMtDtdId())) {
                errorDetail.add(CommonErrorCodes.DOCUMENT_ID_REQUIRED_FOR_DOCUMENT_CONTENT.getDescDe());
                return errorDetail;
            }
            if (CommonUtil.isStringNullOrEmpty(documentContentV1.getData())) {
                errorDetail.add(CommonErrorCodes.INVALID_DOCUMENT_CONTENT.getDescDe());

            }
        }

        return errorDetail;
    }

    public static List<String> validateTokenStatusRequest(DbTokenStatus dbTokenStatus, OperationType operationType) {
        List<String> errorDetail = new ArrayList<>();
        if (CommonUtil.isObjectNull(dbTokenStatus)) {
            errorDetail.add(CommonErrorCodes.INVALID_TOKEN_STATUS.getDescDe());
            return errorDetail;
        }
        if (OperationType.CREATE.equals(operationType)) {
            if (!CommonUtil.isStringNullOrEmpty(dbTokenStatus.getTokenStatusId())) {
                errorDetail.add(CommonErrorCodes.TOKEN_STATUS_ID_MUST_BE_NULL.getDescDe());
            }
        }
        if (OperationType.UPDATE.equals(operationType)) {
            if (CommonUtil.isStringNullOrEmpty(dbTokenStatus.getTokenStatusId())) {
                errorDetail.add(CommonErrorCodes.TOKEN_STATUS_ID_IS_NULL.getDescDe());
            }
        }
        return errorDetail;

    }

}
