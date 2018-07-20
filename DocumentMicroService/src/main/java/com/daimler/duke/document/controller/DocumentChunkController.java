package com.daimler.duke.document.controller;

import javax.inject.Inject;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.dto.DocumentChunkTransferredStatus;
import com.daimler.duke.document.dto.DocumentChunkV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.GetContentRestResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.service.DocumentUploadChunkService;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.service.TokenStatusService;
import com.daimler.duke.document.util.CommonUtil;
import com.daimler.duke.document.util.RequestResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Chunking" + Constants.V1, description = "Chunking Operation")
@RequestMapping("/chunk/" + Constants.V1)
public class DocumentChunkController {

    @Inject
    private DocumentUploadChunkService documentChunkService;
    @Inject
    private IDocumentService           documentService;

    @Inject
    private TokenStatusService         tokenStatusService;

    public DocumentChunkController() {
        // Default constructor
    }

    /**
     * To save a single chunk
     * 
     * @param DocumentChunkV1.
     * @return The status of save operation
     */
    @ApiOperation(value = "Save a chunk", response = RestResponseValue.class)
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseValue saveChunck(@RequestHeader("authorization") String authorization,
                                        @RequestBody final DocumentChunkV1 documentChunkV1) {

        validateTokenWithDocumentId(CommonUtil.getToken(authorization), documentChunkV1.getDocumentMetaDataId());
        return RequestResponseUtil.convertToRestResponseValue(documentChunkService.saveChunk(documentChunkV1,
                                                                                             CommonUtil.getToken(authorization)));

    }

    /**
     * Fetch list of chunks ids that already saved.
     * 
     * @param list of chunks.
     * @return
     */

    @ApiOperation(value = "return chunk ids for a given Metadata id", response = RestResponseObject.class)
    @GetMapping(value = "/{metadataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject getTransferredChunkStatus(@RequestHeader("authorization") String authorization,
                                                        @PathVariable("metadataId") final String metadataId) {

        validateTokenWithDocumentId(CommonUtil.getToken(authorization), metadataId);
        DocumentChunkTransferredStatus response = documentChunkService.getChunkIdList(metadataId);
        return RequestResponseUtil.convertToRestResponseObject(response);

    }

    /**
     * Merge chunks .
     * 
     * @param status of merging.
     * @return
     */
    @ApiOperation(value = "Merge Chunk", response = RestResponseObject.class)
    @GetMapping(value = "merge/{metadataId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject mergeChunks(@RequestHeader("authorization") String authorization,
                                          @PathVariable("metadataId") final String metadataId) {

        validateTokenWithDocumentId(CommonUtil.getToken(authorization), metadataId);
        return RequestResponseUtil.convertToRestResponseObject(documentChunkService.mergeChunks( (metadataId)));

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
                                               @PathVariable("metadataId") final String metadataId) {

        validateTokenWithDocumentId(CommonUtil.getToken(authorization), metadataId);
        return RequestResponseUtil.convertToRestResponseObject(documentChunkService.cancelFileUpload(metadataId));
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
                                                       @PathVariable("id") final String metadataId) {
        validateTokenWithDocumentId(CommonUtil.getToken(authorization), metadataId);
        tokenStatusService.updateTokenStatusToInActive(CommonUtil.getToken(authorization));
        final DocumentContentV1 documentContent = documentService.getDocumentContent(metadataId);

        return RequestResponseUtil.convertToGetContentRestResponse(documentContent);
    }

    /**
     * validate Token With DocumentId
     * 
     * @param authorization
     * @param metadataId
     * @return
     */
    public void validateTokenWithDocumentId(String token, String metadataId) {

        DbTokenStatus tokenObj = null;
        try {
            tokenObj = tokenStatusService.findTokenStatusByToken(token);
            if (tokenObj != null && !StringUtils.isEmpty(tokenObj.getDocumentId())
                    && metadataId != null
                    && metadataId.equals(tokenObj.getDocumentId())) {
                // nothing to do.
            }
            else {
                throw new AuthorizationException(CommonErrorCodes.NOT_VALID_FOR_DOCUMENT_ID.getErrorCode(),
                                                 CommonErrorCodes.NOT_VALID_FOR_DOCUMENT_ID.getDescDe());
            }
        }
        catch (Exception e) {
            // it's Duke client request.
            throw e;
        }

    }
}
