package com.daimler.duke.document.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.dto.CreateContentRestResponseV1;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.service.IDocumentService;
import com.daimler.duke.document.service.TokenStatusService;
import com.daimler.duke.document.util.RequestResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * DocumentContentResourceController class provides the basic CRUD operation for Document Resource implementation. Also
 * has rest endpoint for Version V1.
 * 
 * @author NAYASAR
 *
 */
@RestController
@Api(value = "Document Content for 3rd party Resource"
        + Constants.V1, description = "Document Content for 3rd party Operation")
@RequestMapping("/documentContent/" + Constants.V1)
public class DocumentContentResourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentContentResourceController.class);

    @Inject
    private TokenStatusService  tokenStatusService;

    @Inject
    private IDocumentService    documentService;

    /**
     * create Document Content.
     * 
     * @param authorization
     * @param documentContent
     */
    @ApiOperation(value = "create Document Content", response = CreateContentRestResponseV1.class)
    @PostMapping(value = "/content", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CreateContentRestResponseV1 createDocumentContent(@RequestHeader("authorization") String authorization,
                                                             @RequestBody DocumentContentV1 documentContent) {

        LOGGER.info("DocumentContentResourceController:createDocumentContent() is started");
        DocumentResourceLogController.logJsonRequest(documentContent);
        String token = authorization.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();

        validateTokenWithDocumentId(authorization, documentContent);
        String metaDataId = documentService.createDocumentContent(documentContent);

        // update the token status to inactive.
        tokenStatusService.updateTokenStatusToInActive(token);

        LOGGER.info("DocumentContentResourceController:createDocumentContent() ended.");
        return RequestResponseUtil.convertToCreateContentRestResponseV1(metaDataId);
    }

    /**
     * delete Document content api
     * 
     * @param documentId
     * 
     */
    /*
     * @ApiOperation(value = "delete document contents", response = RestResponseObject.class)
     * @DeleteMapping("/{metadataId}") public final RestResponseObject
     * deleteDocumentContentForThirdParty(@RequestHeader("authorization") String authorization,
     * @PathVariable("metadataId") final String metadataId) {
     * LOGGER.info("DocumentContentResourceController:deleteDocumentContentForThirdParty() started - metadataId is " +
     * metadataId); documentService.deleteDocumentContentForThirdParty(metadataId);
     * LOGGER.info("DocumentContentResourceController:deleteDocumentContentForThirdParty() ended."); return
     * RequestResponseUtil.convertToRestResponseObject(Constants.SUCCESSFUL_DELETE); }
     */

    /**
     * validate Token With DocumentId
     * 
     * @param authorization
     * @return
     */
    public void validateTokenWithDocumentId(String authorization, DocumentContentV1 documentContent) {
        String token = authorization.substring(Constants.AUTHENTICATION_SCHEME.length()).trim();
        if (documentContent != null) {
            DbTokenStatus tokenObj = null;
            try {
                tokenObj = tokenStatusService.findTokenStatusByToken(token);
                if (tokenObj != null && !StringUtils.isEmpty(tokenObj.getDocumentId())
                        && documentContent.getDocumentMtDtdId() != null
                        && documentContent.getDocumentMtDtdId().equals(tokenObj.getDocumentId())) {
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

}
