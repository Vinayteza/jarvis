package com.daimler.duke.document.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbDocumentContent;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.DocumentChunkTransferredStatus;
import com.daimler.duke.document.dto.DocumentChunkV1;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.interceptor.RequestValidator;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.util.CommonUtil;
import com.daimler.duke.document.util.ConversionUtil;
import com.daimler.duke.document.util.EncodeDecodeUtil;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * @author kshebin
 *
 */
@RequestScoped
@Component
public class DocumentUploadChunkService implements IDocumentUploadChunkService {

  /**
   * Logger instance
   */
  private static final Logger          LOGGER = LoggerFactory.getLogger(ConversionUtil.class);
  @Autowired
  private GridFsTemplate               gridFsTemplate;
  @Autowired
  private DbDocumentMetaDataRepository dbDocumentMetaDataRepository;

  @Autowired(required = true)
  private MongoTemplate                mongoTemplate;
  @Autowired
  private DocumentService              documentService;

  @Autowired
  private TokenStatusService           tokenStatusService;

  /*
   * It sets chunk size on server with client application will bulk file upload. (non-Javadoc)
   * @see com.daimler.duke.document.service.IDocumentUploadChunkService#
   * saveUploadChunkSize(java.lang.String)
   */
  @Override
  public DbUploadFileChunkSize saveUploadChunkSize(String chunkSize) {
    boolean eval = isNumericRegex(chunkSize);
    if (!eval) {
      throw new DocumentException(CommonErrorCodes.CHUNK_SIZE_CANNOT_BE_NULL_OR_NON_NUMERIC.getErrorCode(),
                                  CommonErrorCodes.CHUNK_SIZE_CANNOT_BE_NULL_OR_NON_NUMERIC.getDescDe());
    }
    if (Integer.valueOf(chunkSize) == 0) {
      throw new DocumentException(CommonErrorCodes.CHUNK_SIZE_CANNOT_BE_ZERO.getErrorCode(),
                                  CommonErrorCodes.CHUNK_SIZE_CANNOT_BE_ZERO.getDescDe());
    }
    DbUploadFileChunkSize document =
        mongoTemplate.findOne(new Query(Criteria.where(Constants.FETCH_KEY)
                                                .is(Constants.FETCH_VALUE)),
                              DbUploadFileChunkSize.class);
    if (document != null) {
      Update update = new Update();
      update.set("uploadCunkSize", chunkSize);
      mongoTemplate.updateFirst(new Query(Criteria.where(Constants.FETCH_KEY)
                                                  .is(Constants.FETCH_VALUE)),
                                update,
                                DbUploadFileChunkSize.class);
      document.setUploadCunkSize(chunkSize);
      return document;
    } else {
      DbUploadFileChunkSize newDocument = new DbUploadFileChunkSize();
      newDocument.setUploadCunkSize(chunkSize);
      mongoTemplate.save(newDocument);
      return newDocument;
    }

  }

  /*
   * It gets chunk size from the server where it is already set. Constants.FETCH_KEY is just a key
   * in custom entity. Constants.FETCH_VALUE is just a value for key. (non-Javadoc)
   * @see com.daimler.duke.document.service.IDocumentUploadChunkService# getUploadChunkSize()
   */
  @Override
  public DbUploadFileChunkSize getUploadChunkSize() {
    DbUploadFileChunkSize document =
        mongoTemplate.findOne(new Query(Criteria.where(Constants.FETCH_KEY)
                                                .is(Constants.FETCH_VALUE)),
                              DbUploadFileChunkSize.class);
    if (document != null)
      return document;
    else
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.CHUNK_SIZ_DOCUEMNT_NOT_EXIST.getDescDe());
  }

  /*
   * (non-Javadoc)
   * @see com.daimler.duke.document.service.IDocumentUploadChunkService#
   * getChunkIdList(java.lang.String)
   */
  @Override
  public DocumentChunkTransferredStatus getChunkIdList(String metaDataId) {
    DocumentChunkTransferredStatus documentChunkTransferredStatus =
        new DocumentChunkTransferredStatus();

    List<GridFSDBFile> gridFsdbFiles =
        gridFsTemplate.find(new Query(Criteria.where(Constants.META_DATA + Constants.DOT
            + Constants.DOCUMENT_META_DATA_ID).is(metaDataId)));

    documentChunkTransferredStatus.setDocumentMetaDataId(metaDataId);
    for (GridFSDBFile gridFSDBFile : gridFsdbFiles) {
      documentChunkTransferredStatus.addChunkId((Long) gridFSDBFile.getMetaData()
                                                                   .get(Constants.CHUNK_ID));
    }

    return documentChunkTransferredStatus;
  }

  /*
   * (non-Javadoc)
   * @see com.daimler.duke.document.service.IDocumentUploadChunkService#saveChunk(
   * com.daimler.duke.document.dto.DocumentChunkV1)
   */
  @Override
  public boolean saveChunk(DocumentChunkV1 documentChunkV1, String token) {
    validateChunkRequest(documentChunkV1);
    byte[] data = EncodeDecodeUtil.decodeBase64ToByteArray(documentChunkV1.getContent());
    DBObject metaData = ConversionUtil.convertDocumentChunkToDBObject(documentChunkV1);
    InputStream dataInputStream = new ByteArrayInputStream(data);
    Object doc = gridFsTemplate.store(dataInputStream,
                                      documentChunkV1.getDocumentMetaDataId() + ":"
                                          + Long.parseLong(documentChunkV1.getChunkId()),
                                      metaData);

    if (doc != null) {

      List<GridFSDBFile> gridFsdbFiles =
          gridFsTemplate.find(new Query(Criteria.where(Constants.META_DATA + Constants.DOT
              + Constants.DOCUMENT_META_DATA_ID).is(documentChunkV1.getDocumentMetaDataId())));
      if (!gridFsdbFiles.isEmpty()) {
        Long totalChunks =
            (Long) gridFsdbFiles.get(0).getMetaData().get(Constants.TOTAL_NUM_OF_CHUNKS);
        if (totalChunks.equals(new Long(gridFsdbFiles.size()))) {
          // If all chunks are uploaded ,then merge chunks
          boolean isComplete = mergeChunks(documentChunkV1.getDocumentMetaDataId());
          if (isComplete) {
            // update the token status to inactive.
            tokenStatusService.updateTokenStatusToInActive(token);
          }

        }
      }
    }
    return doc != null;

  }

  /*
   * (non-Javadoc)
   * @see com.daimler.duke.document.service.IDocumentUploadChunkService#mergeChunks
   * (java.lang.String)
   */
  @Override
  public boolean mergeChunks(String metaDataId) {

    DbDocumentMetaData documentMetaData = dbDocumentMetaDataRepository.findOne(metaDataId);
    // validating is Meta data exists for the given id
    if (documentMetaData == null) {
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.DOCUMENT_META_DATA_NOT_FOUND.getDescDe());
    }
    // validating if the file already merged or not

    GridFSDBFile file =
        gridFsTemplate.findOne(new Query().addCriteria(Criteria.where(Constants.FILENAME)
                                                               .in(documentMetaData.getDocumentId())));

    if (file != null) {

      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.UPLOAD_DOCUMENT_FOR_THIS_META_DATA_ALREADY_COMPLETED.getDescDe());
    }

    Query query = new Query();
    query.addCriteria(Criteria.where(Constants.META_DATA + Constants.DOT
        + Constants.DOCUMENT_META_DATA_ID).is(metaDataId));

    List<GridFSDBFile> gridFsdbFiles = new ArrayList<GridFSDBFile>(gridFsTemplate.find(query));
    // To ensure uploaded all chunks
    if (gridFsdbFiles == null || gridFsdbFiles.isEmpty()) {

      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.NOT_UPLOADED_ALL_CHUNKS.getDescDe());
    }

    Long totalNumsOfChunks =
        (Long) gridFsdbFiles.get(0).getMetaData().get(Constants.TOTAL_NUM_OF_CHUNKS);
    if (totalNumsOfChunks.intValue() != gridFsdbFiles.size()) {
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.NOT_UPLOADED_ALL_CHUNKS.getDescDe());
    }

    Comparator<GridFSDBFile> comparator = new Comparator<GridFSDBFile>() {
      @Override
      public int compare(GridFSDBFile left, GridFSDBFile right) {

        Long val = ((Long) left.getMetaData().get(Constants.CHUNK_ID))
            - ((Long) right.getMetaData().get(Constants.CHUNK_ID));

        return val.intValue();
      }
    };

    Collections.sort(gridFsdbFiles, comparator);

    byte[] data;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      for (GridFSDBFile gridFSDBFile : gridFsdbFiles) {

        if (gridFSDBFile != null) {

          gridFSDBFile.writeTo(baos);

        }

      }

      data = baos.toByteArray();

      DbDocumentContent dbDocumentContent = new DbDocumentContent();
      dbDocumentContent.setData(data);
      dbDocumentContent.setDocumentMtDtdId(metaDataId);

      documentService.createDocumentContent(dbDocumentContent);

      dbDocumentMetaDataRepository.save(documentMetaData);

      gridFsTemplate.delete(query);

      return true;

    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
      throw new DocumentException(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getErrorCode(),
                                  CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getDescDe(),
                                  e.getMessage());

    } finally {
      try {
        baos.close();
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);

      }
    }

  }

  /**
   * To validate Chunk Save request
   * 
   * @param documentChunkV1
   * @throws DocumentException
   */
  private void validateChunkRequest(DocumentChunkV1 documentChunkV1) throws DocumentException {
    long start = System.currentTimeMillis();
    System.out.println("validateChunkRequest method started at :" + start);
    // Validate the input data
    List<String> validateDocumentRequest =
        RequestValidator.validateDocumentChunk(documentChunkV1, getUploadChunkSize());

    String errorListAsString =
        CommonUtil.getStringFromLists(validateDocumentRequest, Constants.SEPERATOR);

    if (!CommonUtil.isStringNullOrEmpty(errorListAsString)) {
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(), errorListAsString);

    }

    // validating is Meta data exists for the given id
    DbDocumentMetaData documentMetaData =
        dbDocumentMetaDataRepository.findOne(documentChunkV1.getDocumentMetaDataId());
    if (documentMetaData == null) {
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.DOCUMENT_META_DATA_NOT_FOUND.getDescDe());
    }

    // validating if the file already merged or not
    GridFSDBFile file =
        gridFsTemplate.findOne(new Query().addCriteria(Criteria.where(Constants.FILENAME)
                                                               .in(documentChunkV1.getDocumentMetaDataId())));
    if (file != null) {

      throw new DocumentException(CommonErrorCodes.UPLOAD_DOCUMENT_FOR_THIS_META_DATA_ALREADY_COMPLETED.getErrorCode(),
                                  CommonErrorCodes.UPLOAD_DOCUMENT_FOR_THIS_META_DATA_ALREADY_COMPLETED.getDescDe());
    }

    // Validating if any chunks already saved with same chunk id
    List<GridFSDBFile> gridFSFile =
        gridFsTemplate.find(new Query().addCriteria(Criteria.where(Constants.FILENAME)
                                                            .is(documentChunkV1.getUniqueChunkFileName())));

    if (!CollectionUtils.isEmpty(gridFSFile)) {
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.CHUNK_ALREADY_SAVED.getDescDe());
    }

    List<GridFSDBFile> gridFsdbFiles =
        gridFsTemplate.find(new Query(Criteria.where(Constants.META_DATA + Constants.DOT
            + Constants.DOCUMENT_META_DATA_ID).is(documentChunkV1.getDocumentMetaDataId())));

    if (!gridFsdbFiles.isEmpty()
        && gridFsdbFiles.get(0).getMetaData().get(Constants.TOTAL_NUM_OF_CHUNKS) != null) {
      Long totalChunks =
          (Long) gridFsdbFiles.get(0).getMetaData().get(Constants.TOTAL_NUM_OF_CHUNKS);
      // To ensure all chunks have same total number of chunks
      if (!totalChunks.equals(Long.parseLong(documentChunkV1.getTotalNumOfChunks()))) {
        throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                      CommonErrorCodes.TOTAL_NUM_OF_CHUNKS_SHOULD_BE_SAME.getDescDe());
      }

      if (totalChunks.equals(new Long(gridFsdbFiles.size()))) {
        throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                      CommonErrorCodes.ALREADY_UPLOADED_ALL_CHUNKS.getDescDe());
      }

    }
    long end = System.currentTimeMillis();

    System.out.println(",validateChunkRequest() method ended at : " + end);

    System.out.println(",validateChunkRequest() Total time taken : " + (end - start));

  }

  private boolean isNumericRegex(String str) {
    if (str == null)
      return false;
    return str.matches(Constants.IS_NUMERIC_REGX);
  }

  @Override
  public boolean cancelFileUpload(String metaDataId) {

    // validating is Meta data exists for the given id
    DbDocumentMetaData documentMetaData = dbDocumentMetaDataRepository.findOne(metaDataId);
    if (documentMetaData == null) {
      throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                    CommonErrorCodes.DOCUMENT_META_DATA_NOT_FOUND.getDescDe());
    }

    List<GridFSDBFile> gridFsdbFiles =
        gridFsTemplate.find(new Query(Criteria.where(Constants.META_DATA + Constants.DOT
            + Constants.DOCUMENT_META_DATA_ID).is(metaDataId)));
    if (!gridFsdbFiles.isEmpty()) {

      Query query = new Query();
      query.addCriteria(Criteria.where(Constants.META_DATA + Constants.DOT
          + Constants.DOCUMENT_META_DATA_ID).is(metaDataId));

      gridFsTemplate.delete(query);
    }

    // delete metadata first
    dbDocumentMetaDataRepository.delete(metaDataId);

    GridFSDBFile file =
        gridFsTemplate.findOne(new Query().addCriteria(Criteria.where(Constants.FILENAME)
                                                               .in(metaDataId)));
    if (file != null) {
      // delete content
      gridFsTemplate.delete(new Query().addCriteria(Criteria.where(Constants.FILENAME)
                                                            .is(metaDataId)));
    }

    tokenStatusService.updateTokenStatusToInActiveBydocumentId(metaDataId);

    return true;
  }

}
