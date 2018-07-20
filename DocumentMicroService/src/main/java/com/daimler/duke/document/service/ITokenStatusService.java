package com.daimler.duke.document.service;

import java.util.List;
import java.util.Map;

import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.dto.DocumentMetaDataTokenMapV1;

public interface ITokenStatusService {

    String createTokenStatus(DbTokenStatus tokenStatus);

    DbTokenStatus findTokenStatus(String tokenId);

    String updateTokenStatus(DbTokenStatus tokenStatus);

    List<DocumentMetaDataTokenMapV1> createToken(Map<Integer,String> docIdsMap);

    DbTokenStatus findTokenStatusByToken(String token);

    String updateTokenStatusToInActive(String token);

    /**
     * To get a token for downloading a document content
     * @param metaDataId
     * @return
     */
    DocumentMetaDataTokenMapV1 getTokenForMetadata(String metaDataId);

    String updateTokenStatusToInActiveBydocumentId(String metadataId);

}
