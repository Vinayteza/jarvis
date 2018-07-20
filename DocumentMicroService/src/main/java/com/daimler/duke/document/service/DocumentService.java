package com.daimler.duke.document.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbApplicationIdentifier;
import com.daimler.duke.document.db.entity.DbDocumentContent;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentMetaDataWithSerialNoV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.interceptor.RequestValidator;
import com.daimler.duke.document.repository.DbApplicationIdentifierRepository;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.util.CommonUtil;
import com.daimler.duke.document.util.ConversionUtil;
import com.daimler.duke.document.util.OperationType;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * This DocumentAppIdentifierService class have all the CRUD operations.
 * 
 * @author SANDGUP.
 *
 */
@RequestScoped
@Component
// @ExceptionI
public class DocumentService implements IDocumentService {

    private static final Logger               LOGGER = LoggerFactory.getLogger(DocumentService.class);
    @Autowired
    private DbDocumentMetaDataRepository      dbDocumentMetaDataRepository;

    @Autowired
    private DbApplicationIdentifierRepository dbApplicationIdentifierRepository;

    @Autowired
    private GridFsTemplate                    gridFsTemplate;

    @Autowired
    private DocumentUploadChunkService        documentUploadChunkService;

    /**
     * Loads the DocumentMetaData from database.
     * 
     * @param applicationName
     * @param documentId
     * @return
     */
    @Override
    public DocumentMetaDataV1 getDocumentMetaData(final String applicationName, final String documentId) {

        DbDocumentMetaData dbDocumentMetaData = null;
        try {

            DbApplicationIdentifier dbApplicationIdentifier =
                    dbApplicationIdentifierRepository.findByApplicationName(applicationName);

            dbDocumentMetaData = verifyDocumentMetaData(documentId);

            if (dbApplicationIdentifier != null && !StringUtils.isEmpty(dbDocumentMetaData.getApplicationId())
                    && !StringUtils.isEmpty(dbApplicationIdentifier.getApplicationId())
                    && dbDocumentMetaData.getApplicationId().equals(dbApplicationIdentifier.getApplicationId())) {
                // do nothing.
            }
            else {
                throw new AuthorizationException(CommonErrorCodes.NO_ACCESS_TO_THIS_DOCUMENT.getErrorCode(),
                                                 CommonErrorCodes.NO_ACCESS_TO_THIS_DOCUMENT.getDescDe());
            }
        }

        catch (DocumentException e) {
            throw e;
        }
        catch (AuthorizationException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }

        DocumentMetaDataV1 documentMetaData = ConversionUtil.convertFromDbDocumentMetaData(dbDocumentMetaData);
        return documentMetaData;
    }

    /**
     * Loads the Document with DocumentMetaData and DocumentContent.
     * 
     * @param applicationName
     * @param documentId
     * @return
     */
    @Override
    public DocumentV1 getDocument(final String applicationName, final String documentId) {
        DocumentMetaDataV1 documentMetaData = getDocumentMetaData(applicationName, documentId);
        DocumentContentV1 documentContent = getDocumentContent(applicationName, documentId);
        DocumentV1 document = new DocumentV1(documentMetaData, documentContent);
        return document;
    }

    /**
     * Loads the DocumentContent from database.
     * 
     * @param applicationName
     * @param documentContentId
     * @return
     */
    @Override
    public DocumentContentV1 getDocumentContent(final String applicationName, final String documentId) {

        GridFSDBFile gridFSFile = getGridFSFile(documentId);
        if (gridFSFile == null) {
            throw new DocumentException(CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getErrorCode(),
                                        CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getDescDe());
        }
        DbDocumentContent dbDocumentContent = convertFileToDbDocumentContent(gridFSFile);
        DocumentContentV1 documentContent = ConversionUtil.convertFromDbDocumentContent(dbDocumentContent);
        if (documentContent == null) {

            throw new DocumentException(CommonErrorCodes.DOCUMENT_NOT_UPLOADED_COMPLETELY.getErrorCode(),
                                        CommonErrorCodes.DOCUMENT_NOT_UPLOADED_COMPLETELY.getDescDe());
        }

        documentContent.setDocumentMtDtdId(documentId);
        return documentContent;
    }

    /**
     * Loads the DocumentContent from database.
     * 
     * @param documentContentId
     * @return
     */
    @Override
    public DocumentContentV1 getDocumentContent(final String documentId) {

        GridFSDBFile gridFSFile = getGridFSFile(documentId);
        if (gridFSFile == null) {
            throw new DocumentException(CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getErrorCode(),
                                        CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getDescDe());
        }
        DbDocumentContent dbDocumentContent = convertFileToDbDocumentContent(gridFSFile);
        DocumentContentV1 documentContent = ConversionUtil.convertFromDbDocumentContent(dbDocumentContent);
        if (documentContent == null) {

            throw new DocumentException(CommonErrorCodes.DOCUMENT_NOT_UPLOADED_COMPLETELY.getErrorCode(),
                                        CommonErrorCodes.DOCUMENT_NOT_UPLOADED_COMPLETELY.getDescDe());
        }

        documentContent.setDocumentMtDtdId(documentId);
        return documentContent;
    }

    /**
     * converts File To DbDocumentContent collection.
     * 
     * @param findOne
     * @return
     */
    private DbDocumentContent convertFileToDbDocumentContent(final GridFSDBFile findOne) {
        DbDocumentContent dbDocumentContent = null;
        ByteArrayOutputStream baos = null;
        if (findOne != null) {
            dbDocumentContent = new DbDocumentContent();
            baos = new ByteArrayOutputStream();
            try {
                findOne.writeTo(baos);
                dbDocumentContent.setData(baos.toByteArray());
                dbDocumentContent.setDocumentContentId(findOne.getId().toString());
                dbDocumentContent.setDocumentMtDtdId(findOne.getFilename());
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
        return dbDocumentContent;
    }

    /**
     * Finds GridFSDBFile for given fileName from database.
     * 
     * @param fileNames
     * @return
     */
    private GridFSDBFile getGridFSFile(final String fileName) {
        GridFSDBFile file = null;
        try {
            file = gridFsTemplate.findOne(new Query().addCriteria(Criteria.where(Constants.FILENAME).in(fileName)));
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }

        return file;
    }

    /**
     * duplicate Document with DocumentMetaData and DocumentContent
     * 
     * @param applicationName
     * @param document
     * @return
     */
    @Override
    public String duplicateDocument(final String applicationName, final String documentId) {

        DocumentV1 document = this.getDocument(applicationName, documentId);

        document.getDocumentMetaData().setDocumentId(null);

        String newDocumentId = this.createDocument(applicationName, document);

        return newDocumentId;

    }

    /**
     * create Document with DocumentMetaData and DocumentContent
     * 
     * @param applicationName
     * @param document
     * @return
     */
    @Override
    public String createDocument(final String applicationName, final DocumentV1 document) {
        validateDocumentRequest(document, OperationType.CREATE);

        String documentId = document.getDocumentMetaData().getDocumentId();
        documentId = createDocumentMetaData(applicationName, document.getDocumentMetaData());

        DbDocumentContent dbDocumentContentToSave =
                ConversionUtil.convertFromDocumentContent(document.getDocumentContent());
        dbDocumentContentToSave.setDocumentMtDtdId(documentId);

        createDocumentContent(dbDocumentContentToSave);
        return documentId;
    }

    private void validateDocumentRequest(DocumentV1 document, OperationType operationType) throws DocumentException {
        List<String> validateDocumentRequest = RequestValidator.validateDocumentRequest(document, operationType);
        String stringFromLists = CommonUtil.getStringFromLists(validateDocumentRequest, Constants.SEPERATOR);

        if (!CommonUtil.isStringNullOrEmpty(stringFromLists)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), stringFromLists);

        }
    }

    private void validateDocumentMetaDataRequest(DocumentMetaDataV1 documentMetaData, OperationType operationType)
            throws ValidationException {
        List<String> validateDocumentRequest =
                RequestValidator.validateDocumentMetaDataRequest(documentMetaData, operationType);
        String stringFromLists = CommonUtil.getStringFromLists(validateDocumentRequest, Constants.SEPERATOR);

        if (!CommonUtil.isStringNullOrEmpty(stringFromLists)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), stringFromLists);

        }
    }

    private void validateDocumentContentRequest(DocumentContentV1 documentContent, OperationType operationType)
            throws DocumentException {
        List<String> validateDocumentRequest =
                RequestValidator.validateDocumentContentRequest(documentContent, operationType);
        String stringFromLists = CommonUtil.getStringFromLists(validateDocumentRequest, Constants.SEPERATOR);

        if (!CommonUtil.isStringNullOrEmpty(stringFromLists)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), stringFromLists);

        }
    }

    /**
     * Delete the document with metadata and content from database.
     * 
     * @param applicationName.
     * @param documentId
     */
    @Override
    public void deleteDocument(final String applicationName, final String documentId) {
        try {

            // Check the documentMetaData exists or not before delete content.
            getDocumentMetaData(applicationName, documentId);

            // delete metadata first
            dbDocumentMetaDataRepository.delete(documentId);

            // delete content
            gridFsTemplate.delete(new Query().addCriteria(Criteria.where(Constants.FILENAME).is(documentId)));
        }
        catch (DocumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;

        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
    }

    /**
     * creates the DocumentMetaData and save into database.
     * 
     * @param applicationName
     * @param documentMetaData.
     * @return
     */
    @Override
    public String createDocumentMetaData(final String applicationName, final DocumentMetaDataV1 documentMetaData) {
        validateDocumentMetaDataRequest(documentMetaData, OperationType.CREATE);
        // DocumentMetaData metaData = null;
        DbDocumentMetaData dbDocumentMetaDataToSave = ConversionUtil.convertFromDocumentMetaData(documentMetaData);
        try {

            DbApplicationIdentifier dbApplicationIdentifier =
                    dbApplicationIdentifierRepository.findByApplicationName(applicationName);
            if (dbApplicationIdentifier != null) {
                dbDocumentMetaDataToSave.setApplicationId(dbApplicationIdentifier.getApplicationId());
                dbDocumentMetaDataToSave = dbDocumentMetaDataRepository.insert(dbDocumentMetaDataToSave);
            }
            else {
                throw new AuthorizationException(CommonErrorCodes.NOT_ALLOWED_TO_THIS_APPLICATION.getErrorCode(),
                                                 applicationName
                                                         + CommonErrorCodes.NOT_ALLOWED_TO_THIS_APPLICATION.getDescDe());
            }
        }
        catch (AuthorizationException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
        // metaData =
        // ConversionUtil.convertFromDbDocumentMetaData(dbDocumentMetaDataToSave);
        return dbDocumentMetaDataToSave.getDocumentId();
    }

    /**
     * Update the DocumentMetaData.
     * 
     * @param applicationName
     * @param documentMetaData
     */
    @Override
    public String updateDocumentMetaData(final String applicationName, final DocumentMetaDataV1 documentMetaData) {

        validateDocumentMetaDataRequest(documentMetaData, OperationType.UPDATE);
        DbDocumentMetaData dbDocumentMetaDataToUpdate = ConversionUtil.convertFromDocumentMetaData(documentMetaData);

        try {

            getDocumentMetaData(applicationName, documentMetaData.getDocumentId());

            DbApplicationIdentifier dbApplicationIdentifier =
                    dbApplicationIdentifierRepository.findByApplicationName(applicationName);
            if (dbApplicationIdentifier != null) {
                dbDocumentMetaDataToUpdate.setApplicationId(dbApplicationIdentifier.getApplicationId());
            }
            dbDocumentMetaDataToUpdate.setDocumentId(documentMetaData.getDocumentId());
            dbDocumentMetaDataToUpdate.setUpdatedTime(new Date());
            dbDocumentMetaDataToUpdate = dbDocumentMetaDataRepository.save(dbDocumentMetaDataToUpdate);
        }
        catch (DocumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        return dbDocumentMetaDataToUpdate.getDocumentId();
    }

    // @Override
    /**
     * creates the document Content.
     * 
     * @param dbDocumentContent
     * @return
     */
    public String createDocumentContent(final DbDocumentContent dbDocumentContent) {

        InputStream myInputStream = new ByteArrayInputStream(dbDocumentContent.getData());
        Object documentContentId = null;
        try {

            verifyDocumentMetaData(dbDocumentContent.getDocumentMtDtdId());

            // Check the content exists or not before delete content.
            GridFSDBFile gridFSFile = getGridFSFile(dbDocumentContent.getDocumentMtDtdId());
            if (gridFSFile != null) {
                throw new DocumentException(CommonErrorCodes.DOCUMENT_CONTENT_ALREADY_EXIST.getErrorCode(),
                                            CommonErrorCodes.DOCUMENT_CONTENT_ALREADY_EXIST.getDescDe());
            }

            documentContentId = gridFsTemplate.store(myInputStream, dbDocumentContent.getDocumentMtDtdId()).getId();
            dbDocumentContent.setDocumentContentId(documentContentId.toString());

        }
        catch (DocumentException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
        finally {
            try {
                myInputStream.close();
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new DocumentException(CommonErrorCodes.ERROR_IN_CLOSING_BYTE_STREAM.getErrorCode(),
                                            CommonErrorCodes.ERROR_IN_CLOSING_BYTE_STREAM.getDescDe(),
                                            e.getMessage());
            }
        }

        return documentContentId.toString();
    }

    /**
     * Fetch list of documents from database.
     * 
     * @param applicationName
     * @param documentIds
     * @return
     */
    @Override
    public List<DocumentMetaDataV1> getListDocumentMetaData(final String applicationName, List<String> documentIds) {

        List<DocumentMetaDataV1> documentMetaDataV1List = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(documentIds)) {
                for (String documentId: documentIds) {
                    DocumentMetaDataV1 documentMetaDataV1 = getDocumentMetaData(applicationName, documentId);
                    if (documentMetaDataV1 != null) {
                        documentMetaDataV1List.add(documentMetaDataV1);
                    }
                }
            }
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }

        return documentMetaDataV1List;
    }

    /**
     * Delete list of documents
     * 
     * @param applicationName
     * @param documentIds
     */
    @Override
    public void deleteAllDocuments(final String applicationName, List<String> documentIds) {
        try {
            if (!CollectionUtils.isEmpty(documentIds)) {
                for (String documentId: documentIds) {
                    deleteDocument(applicationName, documentId);
                }
            }
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }

    }

    /**
     * create DocumentContent.
     * 
     * @param documentContentList
     */
    @Override
    public String createDocumentContent(DocumentContentV1 documentContent) {
        validateDocumentContentRequest(documentContent, OperationType.CREATE);
        String documentContentId = null;
        try {
            if (documentContent != null) {
                DbDocumentContent dbDocumentContentToSave = ConversionUtil.convertFromDocumentContent(documentContent);
                documentContentId = createDocumentContent(dbDocumentContentToSave);
            }
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }

        return documentContentId;

    }

    /**
     * create Multiple DocumentContent.
     * 
     * @param applicationName
     * @param documentContentList
     */
    @Override
    public Map<Integer,String> createMultipleDocuments(String applicationName, List<DocumentV1> documents) {

        Map<Integer,String> documentIds = null;
        int index = 0;
        if (!CollectionUtils.isEmpty(documents)) {
            documentIds = new HashMap<>();
            for (DocumentV1 document: documents) {
                String documentContentId = createDocument(applicationName, document);
                documentIds.put(index++, documentContentId);
            }
        }
        else {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                          CommonErrorCodes.DOCUMENTS_CAN_NOT_BE_NULL.getDescDe());
        }
        return documentIds;

    }

    /**
     * Fetch multiple documents.
     * 
     * @param applicationName
     * @param documentIds
     * @return
     */
    @Override
    public List<DocumentV1> fetchMultipleDocuments(String applicationName, List<String> documentIds) {
        // TODO Auto-generated method stub
        ArrayList<DocumentV1> documentList = null;
        if (!CollectionUtils.isEmpty(documentIds)) {
            documentList = new ArrayList<DocumentV1>();
            for (String documentId: documentIds) {
                DocumentV1 Document = getDocument(applicationName, documentId);
                documentList.add(Document);
            }
        }
        else {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                          CommonErrorCodes.DOCUMENT_IDS_CAN_NOT_BE_NULL.getDescDe());
        }

        return documentList;
    }

    /**
     * create Multiple DocumentMetaData.
     * 
     * @param applicationName
     * @param documentContentList
     */
    @Override
    public Map<Integer,String> createMultipleDocumentMetaData(String applicationName,
                                                              List<DocumentMetaDataWithSerialNoV1> documentMetaDataList) {

        // Validate DocumentMetaDataWithSequence request
        validateDocumentMetaDataRequest(documentMetaDataList);

        Map<Integer,String> documentIds = null;
        if (!CollectionUtils.isEmpty(documentMetaDataList)) {
            documentIds = new HashMap<>();
            for (DocumentMetaDataWithSerialNoV1 docMetaDataWithSerialNo: documentMetaDataList) {

                DocumentMetaDataV1 documentMetaData =
                        ConversionUtil.convertFromCreateMetaDataRequest(docMetaDataWithSerialNo.getDocumentMetaData());
                String documentContentId = createDocumentMetaData(applicationName, documentMetaData);
                documentIds.put(docMetaDataWithSerialNo.getSerialNo(), documentContentId);
            }
        }
        else {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                          CommonErrorCodes.DOCUMENTS_CAN_NOT_BE_NULL.getDescDe());
        }
        return documentIds;

    }

    private void validateDocumentMetaDataRequest(List<DocumentMetaDataWithSerialNoV1> documentMetaDataList) {
        List<Integer> serialNoList = new ArrayList<>();
        for (DocumentMetaDataWithSerialNoV1 docMetaDataWithSerialNo: documentMetaDataList) {

            // Validate serial number
            if (docMetaDataWithSerialNo.getSerialNo() == null) {
                throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                              CommonErrorCodes.SERIAL_NO_CAN_NOT_NULL.getDescDe());
            }

            if (serialNoList.contains(docMetaDataWithSerialNo.getSerialNo())) {
                throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                              CommonErrorCodes.SERIAL_NO_CAN_NOT_DUPLICATE.getDescDe());
            }
            serialNoList.add(docMetaDataWithSerialNo.getSerialNo());

            // Validate documentMetaData
            if (docMetaDataWithSerialNo.getDocumentMetaData() == null) {
                throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                              CommonErrorCodes.INVALID_DOCUMRNT_MATADATA.getDescDe());
            }
            DocumentMetaDataV1 documentMetaData =
                    ConversionUtil.convertFromCreateMetaDataRequest(docMetaDataWithSerialNo.getDocumentMetaData());
            validateDocumentMetaDataRequest(documentMetaData, OperationType.CREATE);
        }
    }

    /**
     * create Multiple DocumentContent.
     * 
     * @param applicationName
     * @param documentContentList
     */
    @Override
    public List<String> createMultipleDocumentContent(String applicationName,
                                                      List<DocumentContentV1> documentContentList) {

        List<String> documentContentIds = null;
        try {
            if (!CollectionUtils.isEmpty(documentContentList)) {
                documentContentIds = new ArrayList<>();
                for (DocumentContentV1 documentContent: documentContentList) {
                    validateDocumentContentRequest(documentContent, OperationType.CREATE);
                    DbDocumentContent dbDocumentContentToSave =
                            ConversionUtil.convertFromDocumentContent(documentContent);
                    dbDocumentContentToSave.setDocumentMtDtdId(documentContent.getDocumentMtDtdId());
                    String documentContentId = createDocumentContent(dbDocumentContentToSave);
                    documentContentIds.add(documentContentId);
                }
            }
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }

        return documentContentIds;

    }

    /**
     * Delete the content from database only.
     * 
     * @param applicationName.
     * @param documentId
     */
    @Override
    public void deleteDocumentContent(final String applicationName, final String metadataId) {
        try {

            // Check the documentMetaData exists or not before delete.
            getDocumentMetaData(applicationName, metadataId);

            // Check the content exists or not before delete content.
            GridFSDBFile gridFSFile = getGridFSFile(metadataId);
            if (gridFSFile == null) {
                throw new DocumentException(CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getErrorCode(),
                                            CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getDescDe());
            }
            gridFsTemplate.delete(new Query().addCriteria(Criteria.where(Constants.FILENAME).is(metadataId)));
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
    }

    /**
     * Delete the content from database only.
     * 
     * @param applicationName.
     * @param documentId
     */
    @Override
    public void deleteDocumentContentForThirdParty(final String metadataId) {
        try {

            // Check the documentMetaData exists or not before delete.
            verifyDocumentMetaData(metadataId);

            // Check the content exists or not before delete content.
            GridFSDBFile gridFSFile = getGridFSFile(metadataId);
            if (gridFSFile == null) {
                throw new DocumentException(CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getErrorCode(),
                                            CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getDescDe());
            }
            gridFsTemplate.delete(new Query().addCriteria(Criteria.where(Constants.FILENAME).is(metadataId)));
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }

    }

    /**
     * update Document with DocumentMetaData and DocumentContent
     * 
     * @param applicationName
     * @param document
     * @return
     */
    @Override
    public String updateDocument(final String applicationName, final DocumentV1 document) {
        validateDocumentRequest(document, OperationType.UPDATE);
        String documentId = document.getDocumentMetaData().getDocumentId();

        updateDocumentMetaData(applicationName, document.getDocumentMetaData());
        updateDocumentContent(document.getDocumentContent(), documentId);

        return documentId;
    }

    /**
     * update the document Content.
     * 
     * @param dbDocumentContent
     * @return
     */
    private String updateDocumentContent(final DocumentContentV1 documentContent, String documentId) {

        DbDocumentContent dbDocumentContentToSave = ConversionUtil.convertFromDocumentContent(documentContent);
        dbDocumentContentToSave.setDocumentMtDtdId(documentId);
        InputStream myInputStream = new ByteArrayInputStream(dbDocumentContentToSave.getData());
        Object documentContentId = null;
        try {

            verifyDocumentMetaData(dbDocumentContentToSave.getDocumentMtDtdId());

            // delete before updating the document content- Since there is no update operation for file.
            gridFsTemplate.delete(new Query().addCriteria(Criteria.where(Constants.FILENAME)
                                                                  .in(dbDocumentContentToSave.getDocumentMtDtdId())));

            documentContentId =
                    gridFsTemplate.store(myInputStream, dbDocumentContentToSave.getDocumentMtDtdId()).getId();
            dbDocumentContentToSave.setDocumentContentId(documentContentId.toString());
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
        finally {
            try {
                myInputStream.close();
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new DocumentException(CommonErrorCodes.ERROR_IN_CLOSING_BYTE_STREAM.getErrorCode(),
                                            CommonErrorCodes.ERROR_IN_CLOSING_BYTE_STREAM.getDescDe(),
                                            e.getMessage());
            }
        }

        return documentContentId.toString();
    }

    /**
     * Verify the document MetaData exist in database.
     * 
     * @param metadataId
     * @return
     */
    private DbDocumentMetaData verifyDocumentMetaData(String metadataId) {
        DbDocumentMetaData dbDocumentMetaData = dbDocumentMetaDataRepository.findOne(metadataId);

        if (dbDocumentMetaData == null) {
            throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                        CommonErrorCodes.COMMON001.getDescDe());
        }

        return dbDocumentMetaData;
    }

    @Override
    public boolean cancelFileUpload(String applicationName, String metadataId) {
        DbApplicationIdentifier dbApplicationIdentifier =
                dbApplicationIdentifierRepository.findByApplicationName(applicationName);

        if (dbApplicationIdentifier == null) {
            throw new AuthorizationException(CommonErrorCodes.NO_ACCESS_TO_THIS_DOCUMENT.getErrorCode(),
                                             CommonErrorCodes.NO_ACCESS_TO_THIS_DOCUMENT.getDescDe());
        }
        return documentUploadChunkService.cancelFileUpload(metadataId);

    }

}
