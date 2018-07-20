package com.daimler.duke.document.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.dto.CreateDocumentRequestV1;
import com.daimler.duke.document.dto.CreateMetadataIdIndexRestResponseV1;
import com.daimler.duke.document.dto.CreateMetatadaRestResponseV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.dto.GetContentRestResponseV1;
import com.daimler.duke.document.dto.GetDocumentRestResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.util.ConversionUtil;
import com.daimler.duke.document.util.RequestResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * DocumentResourceController class provides the basic CRUD operation for Document Resource implementation. Also has
 * rest endpoint for Version V1.
 * 
 * @author NAYASAR
 *
 */
@RestController
@Api(value = "Document Resource" + Constants.V1, description = "Document Resource Operation")
@RequestMapping("/documents/" + Constants.V1)
public class DocumentResourceController implements IDocumentResourceController {

    private static final Logger           LOGGER = LoggerFactory.getLogger(DocumentResourceController.class);

    /**
     * Inject the IDocumentService.
     */
    @Inject
    private IDocumentService              documentService;

    @Inject
    private IDocumentAppIdentifierService documentAppIdentifierService;

    List<CreateDocumentRequestV1>         cdR    = new ArrayList<>();

    DocumentResourceController() {
        // Default constructor
    }

    /**
     * createDocument api
     * 
     * @param DocumentV1 document
     * @return RestResponseObject
     */

    @Override
    @ApiOperation(value = "create document", response = CreateMetatadaRestResponseV1.class)
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetatadaRestResponseV1 createDocument(@RequestHeader("authorization") String authorization,
                                                       @RequestHeader("applicationName") String encryptedName,
                                                       @RequestBody final CreateDocumentRequestV1 createDocumentRequest) {

        LOGGER.info("DocumentResourceController:createDocument() started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentResourceLogController.logJsonRequest(createDocumentRequest);
        DocumentV1 document = ConversionUtil.convertFromCreateDocumentRequest(createDocumentRequest);

        final String docId = documentService.createDocument(applicationName, document);
        return RequestResponseUtil.convertToCreateMetatadaRestResponse(docId);
    }

    /**
     * deleteDocument api
     * 
     * @param documentId
     * 
     */
    @Override
    @ApiOperation(value = "delete document", response = RestResponseObject.class)
    @DeleteMapping("/{id}")
    public RestResponseObject deleteDocument(@RequestHeader("authorization") String authorization,
                                             @RequestHeader("applicationName") String encryptedName,
                                             @PathVariable("id") final String documentId) {

        LOGGER.info("DocumentResourceController:deleteDocument() started - DocumentId is " + documentId);
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        documentService.deleteDocument(applicationName, documentId);
        LOGGER.info("DocumentResourceController:deleteDocument() ended.");

        return RequestResponseUtil.convertToRestResponseObject(Constants.DOCUMENT_DELETE_SUCCESSFUL);

    }

    /**
     * getDocument api with documentId
     * 
     * @param documentId
     * 
     * @return RestResponseObject
     */

    @Override
    @ApiOperation(value = "get a document", response = GetDocumentRestResponseV1.class)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetDocumentRestResponseV1 getDocument(@RequestHeader("authorization") String authorization,
                                                 @RequestHeader("applicationName") String encryptedName,
                                                 @PathVariable("id") final String documentId) {

        LOGGER.info("DocumentResourceController:getDocument()- DocumentId is " + documentId);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        final DocumentV1 document = documentService.getDocument(applicationName, documentId);
        return RequestResponseUtil.convertToGetDocumentRestResponse(document);
    }

    /**
     * getDocumentContent api with documentContentId
     * 
     * @param documentContentId
     * @return RestResponseObject
     */
    @ApiOperation(value = "return content of a document", response = GetContentRestResponseV1.class)
    @GetMapping(value = "/{id}/content", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetContentRestResponseV1 getDocumentContent(@RequestHeader("authorization") String authorization,
                                                       @RequestHeader("applicationName") String encryptedName,
                                                       @PathVariable("id") final String documentContentId) {

        LOGGER.info("DocumentResourceController:getDocumentContent() is started - DocumentContentId is "
                + documentContentId);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        final DocumentContentV1 documentContent =
                documentService.getDocumentContent(applicationName, documentContentId);
        return RequestResponseUtil.convertToGetContentRestResponse(documentContent);
    }

    /**
     * delete list of document.
     * 
     * @param documentId
     * 
     */
    @Override
    @ApiOperation(value = "delete list of documents", response = RestResponseObject.class)
    @DeleteMapping("/delete/{ids}")
    public RestResponseObject deleteAllDocuments(@RequestHeader("authorization") String authorization,
                                                 @RequestHeader("applicationName") String encryptedName,
                                                 @PathVariable("ids") final List<String> documentIds) {

        LOGGER.info("DocumentResourceController:deleteAllDocuments() is started- List of documentIds are: "
                + documentIds);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        documentService.deleteAllDocuments(applicationName, documentIds);
        LOGGER.info("DocumentResourceController:deleteAllDocuments() is ended");

        return RequestResponseUtil.convertToRestResponseObject(Constants.SUCCESSFUL_DELETE);

    }

    /**
     * create Multiple DocumentContent.
     * 
     * @param encodedName
     * @param documentContentList
     */
    @Override
    @ApiOperation(value = "create Multiple Content", response = RestResponseObject.class)
    @PostMapping(value = "/contents", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject createMultipleDocumentContent(@RequestHeader("authorization") String authorization,
                                                            @RequestHeader("applicationName") String encryptedName,
                                                            @RequestBody List<DocumentContentV1> documentContentList) {

        LOGGER.info("DocumentResourceController:createMultipleDocumentContent() is started");

        DocumentResourceLogController.logJsonRequest(documentContentList);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        List<String> metaDataIds = documentService.createMultipleDocumentContent(applicationName, documentContentList);

        return RequestResponseUtil.convertToRestResponseObject(metaDataIds);
    }

    /**
     * duplicate Complete document
     * 
     * @param documentId
     * 
     * @return RestResponseObject
     */

    @Override
    @ApiOperation(value = "duplicate document", response = CreateMetatadaRestResponseV1.class)
    @GetMapping(value = "/duplicate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetatadaRestResponseV1 duplicateDocument(@RequestHeader("authorization") String authorization,
                                                          @RequestHeader("applicationName") String encryptedName,
                                                          @PathVariable("id") final String documentId) {

        LOGGER.info("DocumentResourceController:duplicateDocument()- DocumentId is " + documentId);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        final String document = documentService.duplicateDocument(applicationName, documentId);
        return RequestResponseUtil.convertToCreateMetatadaRestResponse(document);
    }

    /**
     * create multiple Document api
     * 
     * @param DocumentV1 document
     * @return RestResponseObject
     */

    @Override
    @ApiOperation(value = "create multiple documents", response = CreateMetadataIdIndexRestResponseV1.class)
    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetadataIdIndexRestResponseV1 createMultipleDocuments(@RequestHeader("authorization") String authorization,
                                                                       @RequestHeader("applicationName") String encryptedName,
                                                                       @RequestBody final List<CreateDocumentRequestV1> documentRequests) {

        LOGGER.info("DocumentResourceController:createMultipleDocuments() started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentResourceLogController.logJsonRequest(documentRequests);
        List<DocumentV1> documents = new ArrayList<>();
        for (CreateDocumentRequestV1 createDocumentRequest: documentRequests) {
            DocumentV1 document = ConversionUtil.convertFromCreateDocumentRequest(createDocumentRequest);
            documents.add(document);
        }

        final Map<Integer,String> docIds = documentService.createMultipleDocuments(applicationName, documents);
        return RequestResponseUtil.convertToCreateMetadataIdIndexRestResponse(docIds);
    }

    /**
     * Fetch multiple Documents api
     * 
     * @param DocumentV1 document
     * @return RestResponseObject
     */

    @Override
    @ApiOperation(value = "fetch multiple documents", response = RestResponseObject.class)
    @GetMapping(value = "/multiple/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject fetchMultipleDocuments(@RequestHeader("authorization") String authorization,
                                                     @RequestHeader("applicationName") String encryptedName,
                                                     @PathVariable("ids") final List<String> documentIds) {

        LOGGER.info("DocumentResourceController:fetchMultipleDocuments() started with documentIds: " + documentIds);
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        final List<DocumentV1> documentList = documentService.fetchMultipleDocuments(applicationName, documentIds);
        return RequestResponseUtil.convertToRestResponseObject(documentList);
    }

    /**
     * delete Document content api
     * 
     * @param documentId
     * 
     */
    @Override
    @ApiOperation(value = "delete document contents", response = RestResponseObject.class)
    @DeleteMapping("/content/{metadataId}")
    public RestResponseObject deleteDocumentContent(@RequestHeader("authorization") String authorization,
                                                    @RequestHeader("applicationName") String encryptedName,
                                                    @PathVariable("metadataId") final String metadataId) {

        LOGGER.info("DocumentResourceController:deleteDocument() started - metadataId is " + metadataId);
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        documentService.deleteDocumentContent(applicationName, metadataId);
        LOGGER.info("DocumentResourceController:deleteDocument() ended.");

        return RequestResponseUtil.convertToRestResponseObject(Constants.SUCCESSFUL_DELETE);
    }

    /**
     * update Document api
     * 
     * @param DocumentV1 document
     * @return RestResponseObject
     */

    @Override
    @ApiOperation(value = "update document", response = RestResponseValue.class)
    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseValue updateDocument(@RequestHeader("authorization") String authorization,
                                            @RequestHeader("applicationName") String encryptedName,
                                            @RequestBody final DocumentV1 document) {

        LOGGER.info("DocumentResourceController:updateDocument() started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentResourceLogController.logJsonRequest(document);
        final String docId = documentService.updateDocument(applicationName, document);
        LOGGER.info("DocumentResourceController:updateDocument() ended.");
        return RequestResponseUtil.convertToRestResponseValue(docId);
    }

    /**
     * To cancel Uploading chunk in between
     * 
     * @param authorization
     * @param metadataId
     * @return
     */
    @ApiOperation(value = "Cancel Chunking", response = RestResponseObject.class)
    @GetMapping(value = "cancel/{metadataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject cancelFileUpload(@RequestHeader("authorization") String authorization,
                                               @RequestHeader("applicationName") String encryptedName,
                                               @PathVariable("metadataId") final String metadataId) {
        LOGGER.info("DocumentResourceController:cancelChunking started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        LOGGER.info("DocumentResourceController:cancelChunking ended.");
        return RequestResponseUtil.convertToRestResponseObject(documentService.cancelFileUpload(applicationName,
                                                                                                metadataId));
    }
}
