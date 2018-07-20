package com.daimler.duke.document.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.CreateMetaDataRequestV1;
import com.daimler.duke.document.dto.CreateMetadataIdIndexRestResponseV1;
import com.daimler.duke.document.dto.CreateMetadataIdTokenRestResponseV1;
import com.daimler.duke.document.dto.CreateMetatadaRestResponseV1;
import com.daimler.duke.document.dto.CreateUploadChunkSizeResponseV1;
import com.daimler.duke.document.dto.DocumentMetaDataTokenMapV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentMetaDataWithSerialNoV1;
import com.daimler.duke.document.dto.GetMetadataRestResponseV1;
import com.daimler.duke.document.service.DocumentUploadChunkService;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.service.TokenStatusService;
import com.daimler.duke.document.util.ConversionUtil;
import com.daimler.duke.document.util.RequestResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * DocumentMetaDataResourceController class Provides the basic CRUD operation for DocumentMetaData Resource
 * implementation. Also has rest endpoint for Version V1.
 * 
 * @author NAYASAR
 *
 */
@RestController
@Api(value = "Document Metadata Resource" + Constants.V1, description = "Document Metadata Resource Operation")
@RequestMapping("/documentmetadatas/" + Constants.V1)
public class DocumentMetaDataResourceController {

    private static final Logger           LOGGER = LoggerFactory.getLogger(DocumentMetaDataResourceController.class);

    /**
     * Inject the IDocumentService.
     */
    @Inject
    private IDocumentService              documentService;

    @Inject
    private DocumentUploadChunkService    documentUploadChunkService;

    @Inject
    private IDocumentAppIdentifierService documentAppIdentifierService;

    @Inject
    private TokenStatusService            tokenStatusService;

    DocumentMetaDataResourceController() {
        // Default constructor
    }

    /**
     * create documentmetadata
     * 
     * @param documentMetaData.
     * @return
     */
    @ApiOperation(value = "Create Document metadata", response = CreateMetatadaRestResponseV1.class)
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetatadaRestResponseV1 createDocumentMetaData(@RequestHeader("authorization") String authorization,
                                                               @RequestHeader("applicationName") String encryptedName,
                                                               @RequestBody final CreateMetaDataRequestV1 createMetaDataRequest) {

        LOGGER.info("DocumentMetaDataResourceController:createDocumentMetaData started");
        DocumentResourceLogController.logJsonRequest(createMetaDataRequest);
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentMetaDataV1 documentMetaData = ConversionUtil.convertFromCreateMetaDataRequest(createMetaDataRequest);
        final String docId = documentService.createDocumentMetaData(applicationName, documentMetaData);
        return RequestResponseUtil.convertToCreateMetatadaRestResponse(docId);
    }

    /**
     * get documentmetadata.
     * 
     * @param documentId.
     * @return
     */
    @ApiOperation(value = "get metadata", response = GetMetadataRestResponseV1.class)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetMetadataRestResponseV1 getDocumentMetaData(@RequestHeader("authorization") String authorization,
                                                         @RequestHeader("applicationName") String encryptedName,
                                                         @PathVariable("id") final String documentId) {

        LOGGER.info("DocumentMetaDataResourceController:getDocumentMetaData() started- documentId is " + documentId);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        final DocumentMetaDataV1 documentMetaData = documentService.getDocumentMetaData(applicationName, documentId);
        return RequestResponseUtil.convertToGetMetadataRestResponse(documentMetaData);
    }

    /**
     * update documentmetadata.
     * 
     * @param documentMetaData
     * @return
     */
    @ApiOperation(value = "update document metadata", response = CreateMetatadaRestResponseV1.class)
    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetatadaRestResponseV1 updateDocumentMetaData(@RequestHeader("authorization") String authorization,
                                                               @RequestHeader("applicationName") String encryptedName,
                                                               @RequestBody final DocumentMetaDataV1 documentMetaData) {

        LOGGER.info("DocumentMetaDataResourceController:updateDocumentMetaData() started");
        DocumentResourceLogController.logJsonRequest(documentMetaData);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        DocumentResourceLogController.logJsonRequest(documentMetaData);
        final String docId = documentService.updateDocumentMetaData(applicationName, documentMetaData);
        return RequestResponseUtil.convertToCreateMetatadaRestResponse(docId);
    }

    /**
     * Fetch list of documentmetadata.
     * 
     * @param documentId.
     * @return
     */
    @ApiOperation(value = "return list of document metadata", response = GetMetadataRestResponseV1.class)
    @GetMapping(value = "/multiple/{ids}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GetMetadataRestResponseV1 getListDocumentMetaData(@RequestHeader("authorization") String authorization,
                                                             @RequestHeader("applicationName") String encryptedName,
                                                             @PathVariable("ids") final List<String> documentIds) {

        LOGGER.info("DocumentMetaDataResourceController:getListDocumentMetaData() started with documentIds:"
                + documentIds);

        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);
        final List<DocumentMetaDataV1> documentMetaData =
                documentService.getListDocumentMetaData(applicationName, documentIds);
        return RequestResponseUtil.convertToGetMetadataRestResponse(documentMetaData);

    }

    /**
     * create Multiple DocumentMetaData api
     * 
     * @param authorization
     * @param encryptedName
     * @param DocumentMetaDataList
     * @return
     */
    @ApiOperation(value = "Create multiple Document MetaData", response = CreateMetadataIdIndexRestResponseV1.class)
    @PostMapping(value = "/multiple", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetadataIdIndexRestResponseV1 createMultipleDocumentMetaData(@RequestHeader("authorization") String authorization,
                                                                              @RequestHeader("applicationName") String encryptedName,
                                                                              @RequestBody final List<DocumentMetaDataWithSerialNoV1> documentMetaDataList) {

        LOGGER.info("DocumentResourceController:createMultipleDocuments() started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentResourceLogController.logJsonRequest(documentMetaDataList);
        final Map<Integer,String> metadataTokenIds =
                documentService.createMultipleDocumentMetaData(applicationName, documentMetaDataList);
        return RequestResponseUtil.convertToCreateMetadataIdIndexRestResponse(metadataTokenIds);
    }

    /**
     * create Multiple DocumentMetaData Token api
     * 
     * @param authorization
     * @param encryptedName
     * @param DocumentMetaDataList
     * @return
     */
    @ApiOperation(value = "Create multiple document metadata and return tokens", response = CreateMetadataIdTokenRestResponseV1.class)
    @PostMapping(value = "/saveMetadatasToken", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetadataIdTokenRestResponseV1 createMultipleDocumentMetaDataWithToken(@RequestHeader("authorization") String authorization,
                                                                                       @RequestHeader("applicationName") String encryptedName,
                                                                                       @RequestBody final List<DocumentMetaDataWithSerialNoV1> documentMetaDataList) {

        LOGGER.info("DocumentResourceController:createMultipleDocuments() started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentResourceLogController.logJsonRequest(documentMetaDataList);
        final Map<Integer,String> docIdsMap =
                documentService.createMultipleDocumentMetaData(applicationName, documentMetaDataList);

        // saveMutipleToken with generating the JWT token per documentId.
        List<DocumentMetaDataTokenMapV1> documentTokenInfoList = tokenStatusService.createToken(docIdsMap);

        return RequestResponseUtil.convertToCreateMetadataIdTokenRestResponse(documentTokenInfoList);
    }
    
    /** 
     *To get Token to download document     
     * @param authorization
     * @param encryptedName
     * @param DocumentMetaDataList
     * @return
     */
    
    @ApiOperation(value = "To get Token to download document", response = CreateMetadataIdTokenRestResponseV1.class)
    @GetMapping(value = "/getToken/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CreateMetadataIdTokenRestResponseV1 getTokenToDownloadDocument(@RequestHeader("authorization") String authorization,
                                                                                       @RequestHeader("applicationName") String encryptedName,
                                                                                       @PathVariable("id")  final String metadataId) {

        LOGGER.info("DocumentResourceController:createMultipleDocuments() started.");
        String applicationName = documentAppIdentifierService.decryptApplicationName(encryptedName);

        DocumentResourceLogController.logJsonRequest(metadataId);
        
        // saveMutipleToken with generating the JWT token per documentId.
        DocumentMetaDataTokenMapV1 documentTokenInfo = tokenStatusService.getTokenForMetadata(metadataId);

        return RequestResponseUtil.convertToCreateMetadataIdTokenRestResponse(documentTokenInfo);
    }
    
    

    @ApiOperation(value = "set chunk size", response = CreateUploadChunkSizeResponseV1.class)
    @RequestMapping(value = "/chunkSize", method = RequestMethod.POST)
    public CreateUploadChunkSizeResponseV1 uploadChunkSize(@RequestHeader("authorization") String authorization,
                                                          @RequestHeader("applicationName") String encryptedName,
                                                          @RequestParam(value = "chunkSize", required = true) String chunkSize) {
        DbUploadFileChunkSize uploadChunk = documentUploadChunkService.saveUploadChunkSize(chunkSize);
        return RequestResponseUtil.convertToCreateUploadCunkSizeResponseV1(uploadChunk.getUploadCunkSize());

    }

}
