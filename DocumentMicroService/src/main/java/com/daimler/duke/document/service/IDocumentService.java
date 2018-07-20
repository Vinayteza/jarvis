package com.daimler.duke.document.service;

import java.util.List;
import java.util.Map;

import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentMetaDataWithSerialNoV1;
import com.daimler.duke.document.dto.DocumentV1;

/**
 * This IDocumentService interface have all the CRUD operations definition.
 * 
 * @author NAYASAR
 *
 */
public interface IDocumentService {

    /**
     * Loads the DocumentMetaData from database.
     * 
     * @param applicationName
     * @param documentId
     * @return
     */
    DocumentMetaDataV1 getDocumentMetaData(String applicationName, String documentId);

    /**
     * Fetch list of documents from database.
     * 
     * @param applicationName
     * @param documentIds
     * @return
     */
    List<DocumentMetaDataV1> getListDocumentMetaData(String applicationName, List<String> documentIds);

    /**
     * Loads the Document with DocumentMetaData and DocumentContent.
     * 
     * @param applicationName
     * @param documentId
     * @return
     */

    DocumentV1 getDocument(String applicationName, String documentId);

    /**
     * Loads the DocumentContent from database.
     * 
     * @param applicationName
     * @param documentContentId
     * @return
     */

    DocumentContentV1 getDocumentContent(String applicationName, String documentContentId);

    /**
     * create Document with DocumentMetaData and DocumentContent
     * 
     * @param applicationName
     * @param document
     * @return
     */
    String createDocument(String applicationName, DocumentV1 document);

    /**
     * Delete the document from database.
     * 
     * @param applicationName
     * @param documentId
     */
    void deleteDocument(String applicationName, String documentId);

    /**
     * Delete list of documents
     * 
     * @param applicationName
     * @param documentIds
     */
    void deleteAllDocuments(String applicationName, List<String> documentIds);

    /**
     * creates the DocumentMetaData and save into database.
     * 
     * @param applicationName
     * @param documentMetaData.
     * @return
     */
    String createDocumentMetaData(String applicationName, DocumentMetaDataV1 documentMetaData);

    /**
     * Update the DocumentMetaData.
     * 
     * @param applicationName
     * @param documentMetaData
     */

    String updateDocumentMetaData(String applicationName, DocumentMetaDataV1 documentMetaData);

    /**
     * create Multiple DocumentContent.
     * 
     * @param applicationName
     * @param documentContentList
     */
    String createDocumentContent(DocumentContentV1 documentContent);

    String duplicateDocument(String applicationName, String documentId);

    /**
     * Creates multiple documents.
     * 
     * @param applicationName
     * @param documents
     * @return
     */
    Map<Integer,String> createMultipleDocuments(String applicationName, List<DocumentV1> documents);

    /**
     * Fetch multiple documents.
     * 
     * @param applicationName
     * @param documentIds
     * @return
     */
    List<DocumentV1> fetchMultipleDocuments(String applicationName, List<String> documentIds);

    /**
     * create Multiple DocumentMetaData.
     * 
     * @param applicationName
     * @param documentMetaDataList
     * @return
     */
    Map<Integer,String> createMultipleDocumentMetaData(String applicationName,
                                                       List<DocumentMetaDataWithSerialNoV1> documentMetaDataList);

    /**
     * create Multiple DocumentContent
     * 
     * @param applicationName
     * @param documentContentList
     * @return
     */
    List<String> createMultipleDocumentContent(String applicationName, List<DocumentContentV1> documentContentList);

    /**
     * 
     * Delete the content from database only.
     * 
     * @param applicationName
     * @param metadataId
     * 
     */
    void deleteDocumentContent(final String applicationName, final String metadataId);

    /**
     * Delete the content from database only. It doesn't check the applicationName.
     * 
     * @param metadataId
     */
    void deleteDocumentContentForThirdParty(final String metadataId);

    /**
     * update Document for both metadata & content
     * 
     * @param applicationName
     * @param document
     * @return
     */
    String updateDocument(final String applicationName, final DocumentV1 document);

    /**
     * to delete all chunks against the given metadata Id
     * @param applicationName
     * @param metadataId
     * @return
     */
    boolean cancelFileUpload(String applicationName, String metadataId);
    /**
     * Loads the DocumentContent from database.
     * 
     * @param documentContentId
     * @return
     */

    DocumentContentV1 getDocumentContent(String documentId);

}
