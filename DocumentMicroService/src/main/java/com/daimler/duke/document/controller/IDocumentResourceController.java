package com.daimler.duke.document.controller;

import java.util.List;

import com.daimler.duke.document.dto.CreateDocumentRequestV1;
import com.daimler.duke.document.dto.CreateMetadataIdIndexRestResponseV1;
import com.daimler.duke.document.dto.CreateMetatadaRestResponseV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.dto.GetDocumentRestResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;

/**
 * @author SANDGUP.
 *
 */
public interface IDocumentResourceController {

    /**
     * Fetch the attachment document
     * 
     * @param encodedName
     * @param documentId
     * @return
     */
    GetDocumentRestResponseV1 getDocument(String authorization, String encodedName, String documentId);

    /**
     * 
     * create the attachment document
     * 
     * @param encodedName
     * @param document
     * @return
     */
    CreateMetatadaRestResponseV1 createDocument(String authorization,
                                              String encodedName,
                                              CreateDocumentRequestV1 document);

    /**
     * 
     * delete the attachment document
     * 
     * @param encodedName
     * @param documentId
     */
    RestResponseObject deleteDocument(String authorization, String encodedName, String documentId);

    /**
     * delete all the attachment document
     * 
     * @param encodedName
     * @param documentIds
     */
    RestResponseObject deleteAllDocuments(String authorization, String encodedName, List<String> documentIds);

    /**
     * duplicate Complete document
     * 
     * @param documentId
     * 
     * @return RestResponseObject
     */
    CreateMetatadaRestResponseV1 duplicateDocument(String authorization, String encryptedName, String documentId);

    /**
     * 
     * Creates multiple documents.
     * 
     * @param authorization
     * @param encryptedName
     * @param documents
     * @return
     */
    CreateMetadataIdIndexRestResponseV1 createMultipleDocuments(String authorization,
                                                              String encryptedName,
                                                              List<CreateDocumentRequestV1> documents);

    /**
     * Fetch multiple documents.
     * 
     * @param authorization
     * @param encryptedName
     * @param documentIds
     * @return
     */
    RestResponseObject fetchMultipleDocuments(String authorization, String encryptedName, List<String> documentIds);

    /**
     * create Multiple DocumentContent
     * 
     * @param authorization
     * @param encryptedName
     * @param documentContentList
     * @return
     */
    RestResponseObject createMultipleDocumentContent(String authorization,
                                                     String encryptedName,
                                                     List<DocumentContentV1> documentContentList);

    /**
     * delete Document Content for a given metadataId
     * 
     * @param authorization
     * @param encryptedName
     * @param metadataId
     */
    RestResponseObject deleteDocumentContent(String authorization, String encryptedName, String metadataId);

    /**
     * update Document
     * 
     * @param authorization
     * @param encryptedName
     * @param document
     * @return
     */
    RestResponseValue updateDocument(String authorization, String encryptedName, DocumentV1 document);

}
