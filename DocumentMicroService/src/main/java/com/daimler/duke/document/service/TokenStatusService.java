package com.daimler.duke.document.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.dto.DocumentMetaDataTokenMapV1;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.interceptor.RequestValidator;
import com.daimler.duke.document.jwt.service.IJwtAuthService;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.repository.DbTokenStatusRepository;
import com.daimler.duke.document.util.CommonUtil;
import com.daimler.duke.document.util.OperationType;

@RequestScoped
@Component
public class TokenStatusService implements ITokenStatusService {

    private static final Logger     LOGGER = LoggerFactory.getLogger(TokenStatusService.class);

    @Autowired
    private DbTokenStatusRepository dbTokenStatusRepository;

    @Autowired
    private IJwtAuthService         iJwtAuthUtilService;
    @Autowired
    private DbDocumentMetaDataRepository dbDocumentMetaDataRepository;
    @Override
    public String createTokenStatus(DbTokenStatus tokenStatus) {
        validateTokenStatusRequest(tokenStatus, OperationType.CREATE);

        DbTokenStatus tokenStatusToSave = null;
        try {
            tokenStatusToSave = dbTokenStatusRepository.insert(tokenStatus);
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
        return tokenStatusToSave.getTokenStatusId();
    }

    
    @Override
    public DbTokenStatus findTokenStatus(String tokenId) {

        DbTokenStatus dbTokenStatus = null;
        try {
            dbTokenStatus = dbTokenStatusRepository.findOne(tokenId);
            if (dbTokenStatus == null) {
                throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                            CommonErrorCodes.COMMON001.getDescDe());
            }
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
        return dbTokenStatus;
    }

    @Override
    public String updateTokenStatusToInActiveBydocumentId(String metadataId) {
      
        DbTokenStatus tokenStatus = dbTokenStatusRepository.findByDocumentId(metadataId);
        if (tokenStatus == null) {
            throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                        CommonErrorCodes.COMMON001.getDescDe());
        }
        tokenStatus.setActive("F");
        String tokenStatusId = updateTokenStatus(tokenStatus);
        return tokenStatusId;
        
        
    } 
    
    @Override
    public DbTokenStatus findTokenStatusByToken(String token) {

        DbTokenStatus dbTokenStatus = null;
        try {
            dbTokenStatus = dbTokenStatusRepository.findByTokenValue(token);
            if (dbTokenStatus == null) {
                throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                            CommonErrorCodes.COMMON001.getDescDe());
            }
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());
        }
        return dbTokenStatus;
    }

    @Override
    public String updateTokenStatus(DbTokenStatus tokenStatus) {
        validateTokenStatusRequest(tokenStatus, OperationType.UPDATE);

        DbTokenStatus tokenStatusToSave = null;
        try {
            boolean exists = dbTokenStatusRepository.exists(tokenStatus.getTokenStatusId());
            if (!exists) {
                throw new DocumentException(CommonErrorCodes.COMMON001.getErrorCode(),
                                            CommonErrorCodes.COMMON001.getDescDe());

            }
            tokenStatusToSave = dbTokenStatusRepository.save(tokenStatus);
        }
        catch (DocumentException e) {
            throw e;
        }
        catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new MongoConnectException(MongoConnectException.MONOGO_CONNECT_ERROR_MSG, e.getMessage());

        }
        return tokenStatusToSave.getTokenStatusId();
    }

    @Override
    public String updateTokenStatusToInActive(String token) {
        DbTokenStatus tokenStatus = findTokenStatusByToken(token);
        tokenStatus.setActive("F");
        String tokenStatusId = updateTokenStatus(tokenStatus);
        return tokenStatusId;
    }
    
    /* (non-Javadoc)
     * @see com.daimler.duke.document.service.ITokenStatusService#getTokenForMetadata(java.lang.String)
     */
    @Override
    public DocumentMetaDataTokenMapV1 getTokenForMetadata(String metaDataId) {
        
        DbDocumentMetaData documentMetaData =
                dbDocumentMetaDataRepository.findOne(metaDataId);
        //validating is Meta data exists for the given id 
        if (documentMetaData == null) {    
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(),
                                          CommonErrorCodes.COMMON001.getDescDe());
        }
        // generate the jwt token per element
        final String token = iJwtAuthUtilService.issueJwt();
        // Save into the TokenStatus collection.
        saveTokenStatus(metaDataId, token);
        // prepare DocumentTokenInfoV1 object for send bask the response object.
        DocumentMetaDataTokenMapV1 documentTokenInfo =
                createDocumentTokenInfo(metaDataId, 0, token);

        return documentTokenInfo;

    } 
    
    @Override
    public List<DocumentMetaDataTokenMapV1> createToken(Map<Integer,String> docIdsMap) {

        LOGGER.info("saveMutipleToken is started with docIdsMap values: " + docIdsMap);

        List<DocumentMetaDataTokenMapV1> documentTokenInfoList = null;
        if (docIdsMap != null && !docIdsMap.isEmpty()) {

            documentTokenInfoList = new ArrayList<DocumentMetaDataTokenMapV1>();
            Set<Entry<Integer,String>> docIdsEntry = docIdsMap.entrySet();
            for (Entry<Integer,String> docIdEntry: docIdsEntry) {

                // generate the jwt token per element
                final String token = iJwtAuthUtilService.issueJwt();

                // Save into the TokenStatus collection.
                saveTokenStatus(docIdEntry, token);

                // prepare DocumentTokenInfoV1 object for send bask the response object.
                DocumentMetaDataTokenMapV1 documentTokenInfo = createDocumentTokenInfo(docIdEntry, token);
                documentTokenInfoList.add(documentTokenInfo);
            }
        }

        return documentTokenInfoList;

    }
    private void saveTokenStatus(final String metadataId, final String token) {
        DbTokenStatus tokenStatus = new DbTokenStatus();
        tokenStatus.setActive("T");
        tokenStatus.setTokenValue(token);
        tokenStatus.setDocumentId(metadataId);
        tokenStatus.setCreateTime(new Date());
        createTokenStatus(tokenStatus);
    }
    private void saveTokenStatus(final Entry<Integer,String> docIdEntry, final String token) {
        DbTokenStatus tokenStatus = new DbTokenStatus();
        tokenStatus.setActive("T");
        tokenStatus.setTokenValue(token);
        tokenStatus.setDocumentId(docIdEntry.getValue());
        tokenStatus.setCreateTime(new Date());
        createTokenStatus(tokenStatus);
    }

    private DocumentMetaDataTokenMapV1 createDocumentTokenInfo(String metadataId,int index,
                                                               final String token) {
          DocumentMetaDataTokenMapV1 documentTokenInfo = new DocumentMetaDataTokenMapV1();
          documentTokenInfo.setIndex(index);
          documentTokenInfo.setDocumentId(metadataId);
          documentTokenInfo.setToken(token);
          return documentTokenInfo;
      }
    private DocumentMetaDataTokenMapV1 createDocumentTokenInfo(final Entry<Integer,String> docIdEntry,
                                                             final String token) {
        DocumentMetaDataTokenMapV1 documentTokenInfo = new DocumentMetaDataTokenMapV1();
        documentTokenInfo.setIndex(docIdEntry.getKey());
        documentTokenInfo.setDocumentId(docIdEntry.getValue());
        documentTokenInfo.setToken(token);
        return documentTokenInfo;
    }

    private void validateTokenStatusRequest(DbTokenStatus dbTokenStatus, OperationType operationType)
            throws ValidationException {
        List<String> validateTokenStatusRequest =
                RequestValidator.validateTokenStatusRequest(dbTokenStatus, operationType);
        String stringFromLists = CommonUtil.getStringFromLists(validateTokenStatusRequest, Constants.SEPERATOR);

        if (!CommonUtil.isStringNullOrEmpty(stringFromLists)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST.value(), stringFromLists);

        }
    }

}
