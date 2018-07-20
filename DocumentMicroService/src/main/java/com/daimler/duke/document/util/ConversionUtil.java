
package com.daimler.duke.document.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbApplicationAutherizationGroup;
import com.daimler.duke.document.db.entity.DbApplicationIdentifier;
import com.daimler.duke.document.db.entity.DbDocumentContent;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.dto.ApplicationAutherizationGroupV1;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.dto.CreateDocumentRequestV1;
import com.daimler.duke.document.dto.CreateMetaDataRequestV1;
import com.daimler.duke.document.dto.DocumentChunkV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * ConversionUtil.
 * 
 * @author NAYASAR
 *
 */
public final class ConversionUtil {
    /**
     * Logger instance
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversionUtil.class);

    private ConversionUtil() {
    }

    /**
     * @param dbDocumentMetaData.
     * @return
     */
    public static DocumentMetaDataV1 convertFromDbDocumentMetaData(DbDocumentMetaData dbDocumentMetaData) {
        DocumentMetaDataV1 documentMetaData = null;
        if (null != dbDocumentMetaData) {

            documentMetaData = new DocumentMetaDataV1();
            documentMetaData.setDocumentId(dbDocumentMetaData.getDocumentId());
            documentMetaData.setDocumentName(dbDocumentMetaData.getDocumentName());
            documentMetaData.setDocumentType(dbDocumentMetaData.getDocumentype());
            documentMetaData.setComment(dbDocumentMetaData.getComment());
            documentMetaData.setFirstName(dbDocumentMetaData.getFirstName());
            documentMetaData.setLastName(dbDocumentMetaData.getLastName());
            documentMetaData.setDepartment(dbDocumentMetaData.getDepartment());
            documentMetaData.setShortId(dbDocumentMetaData.getShortId());
            documentMetaData.setSize(dbDocumentMetaData.getSize());

        }

        return documentMetaData;

    }

    /**
     * @param documentMetaData.
     * @return
     */
    public static DbDocumentMetaData convertFromDocumentMetaData(DocumentMetaDataV1 documentMetaData) {
        DbDocumentMetaData dbDocumentMetaData = null;
        if (null != documentMetaData) {

            dbDocumentMetaData = new DbDocumentMetaData();
            dbDocumentMetaData.setCreationTime(new Date());
            dbDocumentMetaData.setUpdatedTime(new Date());

            dbDocumentMetaData.setDocumentName(documentMetaData.getDocumentName());
            dbDocumentMetaData.setDocumentype(documentMetaData.getDocumentType());
            dbDocumentMetaData.setComment(documentMetaData.getComment());
            dbDocumentMetaData.setFirstName(documentMetaData.getFirstName());
            dbDocumentMetaData.setLastName(documentMetaData.getLastName());
            dbDocumentMetaData.setDepartment(documentMetaData.getDepartment());
            dbDocumentMetaData.setShortId(documentMetaData.getShortId());
            dbDocumentMetaData.setSize(documentMetaData.getSize());
        }

        return dbDocumentMetaData;

    }

    /**
     * @param dbDocumentContent.
     * @return
     */
    public static DocumentContentV1 convertFromDbDocumentContent(DbDocumentContent dbDocumentContent) {
        DocumentContentV1 documentContent = null;
        if (null != dbDocumentContent) {
            documentContent = new DocumentContentV1();
            try {
                documentContent.setData(EncodeDecodeUtil.encodeByteArrayToBase64(dbDocumentContent.getData()));
                // documentContent.setDocumentContentId(dbDocumentContent.getDocumentContentId());
                // documentContent.setDocumentMtDtdId(dbDocumentContent.getDocumentMtDtdId());

            }
            catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
                System.out.println("convertFromDbDocumentContent:" + e.getMessage());
                throw new DocumentException(CommonErrorCodes.ERROR_IN_ENCODING_DATA_TO_BASE64.getErrorCode(),
                                            CommonErrorCodes.ERROR_IN_ENCODING_DATA_TO_BASE64.getDescDe(),
                                            e.getMessage());
            }
        }

        return documentContent;

    }

    /**
     * @param documentContent.
     * @return
     */
    public static DbDocumentContent convertFromDocumentContent(DocumentContentV1 documentContent) {
        DbDocumentContent dbDocumentContent = null;
        if (null != documentContent) {
            dbDocumentContent = new DbDocumentContent();
            try {
                dbDocumentContent.setData(EncodeDecodeUtil.decodeBase64ToByteArray(documentContent.getData()));
                // dbDocumentContent.setDocumentContentId(documentContent.getDocumentContentId());
                dbDocumentContent.setDocumentMtDtdId(documentContent.getDocumentMtDtdId());

            }
            catch (Exception e) {
                e.printStackTrace();
                LOGGER.error(e.getMessage(), e);
                System.out.println("convertFromDocumentContent:" + e.getMessage());
                throw new DocumentException(CommonErrorCodes.ERROR_IN_DECODING_BASE64_DATA.getErrorCode(),
                                            CommonErrorCodes.ERROR_IN_DECODING_BASE64_DATA.getDescDe(),
                                            e.getMessage());
            }
        }

        return dbDocumentContent;

    }

    /**
     * Convert ApplicationIdentifierV1 to DbApplicationIdentifier.
     * 
     * @param applicationIdentifier
     * @return
     */
    public static DbApplicationIdentifier convertFromApplicationIdentifier(ApplicationIdentifierV1 applicationIdentifier) {
        DbApplicationIdentifier dbApplicationIdentifier = null;
        if (null != applicationIdentifier) {

            dbApplicationIdentifier = new DbApplicationIdentifier();
            dbApplicationIdentifier.setApplicationName(applicationIdentifier.getApplicationName());
            dbApplicationIdentifier.setSecretKey(applicationIdentifier.getSecretKey());
            dbApplicationIdentifier.setSecretCode(applicationIdentifier.getSecretCode());
            dbApplicationIdentifier.setFirstName(applicationIdentifier.getFirstName());
            dbApplicationIdentifier.setLastName(applicationIdentifier.getLastName());
            dbApplicationIdentifier.setDepartment(applicationIdentifier.getDepartment());

            if (!CollectionUtils.isEmpty(applicationIdentifier.getApplicationAutherizationGrp())) {
                List<DbApplicationAutherizationGroup> dbApplicationAutherizationGrpList = new ArrayList<>();
                for (ApplicationAutherizationGroupV1 applicationAutherizationGroup: applicationIdentifier.getApplicationAutherizationGrp()) {
                    DbApplicationAutherizationGroup dbApplicationAutherizationGroup =
                            new DbApplicationAutherizationGroup();
                    dbApplicationAutherizationGroup.setAutherizationGroup(applicationAutherizationGroup.getAutherizationGroup());

                    dbApplicationAutherizationGrpList.add(dbApplicationAutherizationGroup);
                }

                dbApplicationIdentifier.setApplicationAutherizationGrp(dbApplicationAutherizationGrpList);
            }

        }
        return dbApplicationIdentifier;
    }

    /**
     * Convert DbApplicationIdentifier to ApplicationIdentifierV1.
     * 
     * @param dbApplicationIdentifier
     * @return
     */
    public static ApplicationIdentifierV1 convertFromDbApplicationIdentifier(DbApplicationIdentifier dbApplicationIdentifier) {
        ApplicationIdentifierV1 applicationIdentifier = null;
        if (null != dbApplicationIdentifier) {

            applicationIdentifier = new ApplicationIdentifierV1();
            applicationIdentifier.setApplicationId(dbApplicationIdentifier.getApplicationId());
            applicationIdentifier.setApplicationName(dbApplicationIdentifier.getApplicationName());
            applicationIdentifier.setSecretKey(dbApplicationIdentifier.getSecretKey());
            applicationIdentifier.setSecretCode(dbApplicationIdentifier.getSecretCode());
            applicationIdentifier.setFirstName(dbApplicationIdentifier.getFirstName());
            applicationIdentifier.setLastName(dbApplicationIdentifier.getLastName());
            applicationIdentifier.setDepartment(dbApplicationIdentifier.getDepartment());

            if (!CollectionUtils.isEmpty(dbApplicationIdentifier.getApplicationAutherizationGrp())) {
                List<ApplicationAutherizationGroupV1> applicationAutherizationGrpList = new ArrayList<>();
                for (DbApplicationAutherizationGroup dbApplicationAutherizationGroup: dbApplicationIdentifier.getApplicationAutherizationGrp()) {
                    ApplicationAutherizationGroupV1 applicationAutherizationGroup =
                            new ApplicationAutherizationGroupV1();
                    applicationAutherizationGroup.setAutherizationGroup(dbApplicationAutherizationGroup.getAutherizationGroup());
                    applicationAutherizationGrpList.add(applicationAutherizationGroup);
                }
                applicationIdentifier.setApplicationAutherizationGrp(applicationAutherizationGrpList);
            }

        }

        return applicationIdentifier;
    }

    public static DBObject convertDocumentChunkToDBObject(DocumentChunkV1 documentChunkV1) {
        DBObject metaData = new BasicDBObject();
        metaData.put(Constants.CHUNK_ID, Long.parseLong(documentChunkV1.getChunkId()));
        metaData.put(Constants.TOTAL_NUM_OF_CHUNKS, Long.parseLong(documentChunkV1.getTotalNumOfChunks()));
        metaData.put(Constants.DOCUMENT_META_DATA_ID, documentChunkV1.getDocumentMetaDataId());

        return metaData;

    }

    public static DocumentChunkV1 convertGridFSDBFileToDocumentChunk(GridFSDBFile gridFSDBFile) {
        DocumentChunkV1 documentChunkV1 = null;
        ByteArrayOutputStream baos = null;
        if (gridFSDBFile != null) {
            documentChunkV1 = new DocumentChunkV1();
            baos = new ByteArrayOutputStream();
            try {
                gridFSDBFile.writeTo(baos);
                documentChunkV1.setContent(EncodeDecodeUtil.encodeByteArrayToBase64(baos.toByteArray()));
                documentChunkV1.setChunkId((String) gridFSDBFile.getMetaData().get(Constants.CHUNK_ID));
                documentChunkV1.setTotalNumOfChunks((String) gridFSDBFile.getMetaData()
                                                                         .get(Constants.TOTAL_NUM_OF_CHUNKS));
                documentChunkV1.setDocumentMetaDataId((String) gridFSDBFile.getMetaData()
                                                                           .get(Constants.DOCUMENT_META_DATA_ID));
            }
            catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                throw new DocumentException(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getErrorCode(),
                                            CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getDescDe(),
                                            e.getMessage());

            }
            finally {
                try {
                    baos.close();
                }
                catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    throw new DocumentException(CommonErrorCodes.ERROR_IN_CLOSING_BYTE_STREAM.getErrorCode(),
                                                CommonErrorCodes.ERROR_IN_CLOSING_BYTE_STREAM.getDescDe(),
                                                e.getMessage());
                }
            }

        }
        return documentChunkV1;

    }

    /**
     * 
     * @param createMetaDataRequest
     * @return
     */
    public static DocumentMetaDataV1 convertFromCreateMetaDataRequest(CreateMetaDataRequestV1 createMetaDataRequest) {
        DocumentMetaDataV1 documentMetaData = null;
        if (null != createMetaDataRequest) {

            documentMetaData = new DocumentMetaDataV1();

            documentMetaData.setDocumentName(createMetaDataRequest.getDocumentName());
            documentMetaData.setDocumentType(createMetaDataRequest.getDocumentType());
            documentMetaData.setComment(createMetaDataRequest.getComment());
            documentMetaData.setFirstName(createMetaDataRequest.getFirstName());
            documentMetaData.setLastName(createMetaDataRequest.getLastName());
            documentMetaData.setDepartment(createMetaDataRequest.getDepartment());
            documentMetaData.setShortId(createMetaDataRequest.getShortId());

            documentMetaData.setSize(createMetaDataRequest.getSize());
        }

        return documentMetaData;

    }

    /**
     * 
     * @param documentMetaData
     * @return
     */
    public static CreateMetaDataRequestV1 convertFromDocumentMetaDataV1(DocumentMetaDataV1 documentMetaData) {
        CreateMetaDataRequestV1 createMetaDataRequest = null;
        if (null != documentMetaData) {

            createMetaDataRequest = new CreateMetaDataRequestV1();

            createMetaDataRequest.setDocumentName(documentMetaData.getDocumentName());
            createMetaDataRequest.setDocumentType(documentMetaData.getDocumentType());
            createMetaDataRequest.setComment(documentMetaData.getComment());
            createMetaDataRequest.setFirstName(documentMetaData.getFirstName());
            createMetaDataRequest.setLastName(documentMetaData.getLastName());
            createMetaDataRequest.setDepartment(documentMetaData.getDepartment());
            createMetaDataRequest.setShortId(documentMetaData.getShortId());
            createMetaDataRequest.setSize(documentMetaData.getSize());
        }

        return createMetaDataRequest;

    }

    /**
     * 
     * @param createMetaDataRequest
     * @return
     */
    public static DocumentV1 convertFromCreateDocumentRequest(CreateDocumentRequestV1 createMetaDataRequest) {
        DocumentMetaDataV1 documentMetaData = null;
        DocumentContentV1 documentContent = null;

        if (createMetaDataRequest != null) {
            if (null != createMetaDataRequest.getDocumentMetaData()) {
                documentMetaData = convertFromCreateMetaDataRequest(createMetaDataRequest.getDocumentMetaData());
            }
            if (null != createMetaDataRequest.getDocumentContent()
                    && !StringUtils.isEmpty(createMetaDataRequest.getDocumentContent().getData())) {
                documentContent = new DocumentContentV1();
                documentContent.setData(createMetaDataRequest.getDocumentContent().getData());
            }
        }

        DocumentV1 document = new DocumentV1();
        document.setDocumentMetaData(documentMetaData);
        document.setDocumentContent(documentContent);

        return document;

    }
}
