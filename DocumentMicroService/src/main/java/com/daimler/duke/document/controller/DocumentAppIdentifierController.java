package com.daimler.duke.document.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.constant.Constants;
import com.daimler.duke.document.dto.ApplicationAutherizationGroupV1;
import com.daimler.duke.document.dto.ApplicationIdentifierV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;
import com.daimler.duke.document.service.IDocumentAppIdentifierService;
import com.daimler.duke.document.util.RequestResponseUtil;

/**
 * DocumentAppIdentifierController class provides the basic CRUD operation for DocumentAppIdentifier Resource
 * implementation. Also has rest endpoint for Version V1.
 * 
 * @author NAYASAR
 *
 */
@RestController
//@Api(value = "Document Application Identifier Resource" + Constants.V1, description = "Document Application Identifier Resource Operation")
@RequestMapping("/documentappidentifier/" + Constants.V1)
public class DocumentAppIdentifierController {

    private static final Logger           LOGGER = LoggerFactory.getLogger(DocumentAppIdentifierController.class);

    /**
     * Inject the IDocumentAppIdentifierService.
     */
    @Inject
    private IDocumentAppIdentifierService documentAppIdentifierService;

    DocumentAppIdentifierController() {
        // Default constructor
    }

    /**
     * create applicationIdentifier
     * 
     * @param applicationIdentifier.
     * @return
     */
    //@ApiOperation(value = "Add autherized app", response = RestResponseObject.class)
    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject createDocumentApplicationIdentifier(@RequestBody final ApplicationIdentifierV1 applicationIdentifier) {

        LOGGER.info("DocumentAppIdentifierController:createDocumentApplicationIdentifier() is started");
        DocumentResourceLogController.logJsonRequest(applicationIdentifier);
        final String appId = documentAppIdentifierService.createDocumentApplicationIdentifier(applicationIdentifier);
        return RequestResponseUtil.convertToRestResponseObject(appId);
    }

    /**
     * return applicationIdentifier
     * 
     * @param applicationId.
     * @return
     */
    //@ApiOperation(value = "get app name for a given app id", response = RestResponseObject.class)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject getDocumentApplicationIdentifier(@PathVariable("id") final String applicationId) {

        LOGGER.info("DocumentAppIdentifierController:getDocumentApplicationIdentifier() is started with applicationId "
                + applicationId);

        final ApplicationIdentifierV1 applicationIdentifierV1 =
                documentAppIdentifierService.getDocumentApplicationIdentifier(applicationId);
        return RequestResponseUtil.convertToRestResponseObject(applicationIdentifierV1);
    }

    /**
     * delete applicationIdentifier api
     * 
     * @param applicationId
     * 
     */
    //@ApiOperation(value = "delete authrization of an app", response = RestResponseObject.class)
    @DeleteMapping("/{id}")
    public void deleteDocumentApplicationIdentifier(@PathVariable("id") final String applicationId) {

        LOGGER.info("DocumentAppIdentifierController:deleteDocumentApplicationIdentifier() is started with applicationId "
                + applicationId);
        documentAppIdentifierService.deleteDocumentApplicationIdentifier(applicationId);
        LOGGER.info("DocumentAppIdentifierController:deleteDocumentApplicationIdentifier() is ended");

    }

    /**
     * update applicationIdentifier
     * 
     * @param applicationIdentifier.
     * @return
     */
    //@ApiOperation(value = "Update autherized app", response = RestResponseObject.class)
    @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject updateDocumentApplicationIdentifier(@RequestBody final ApplicationIdentifierV1 applicationIdentifier) {

        LOGGER.info("DocumentAppIdentifierController:updateDocumentApplicationIdentifier() is started");

        DocumentResourceLogController.logJsonRequest(applicationIdentifier);
        final String appId = documentAppIdentifierService.updateDocumentApplicationIdentifier(applicationIdentifier);
        return RequestResponseUtil.convertToRestResponseObject(appId);
    }

    /**
     * return get SecreteKey for a given Application name.
     * 
     * @param applicationId.
     * @return
     */
    //@ApiOperation(value = "get secreteKey for an application", response = RestResponseValue.class)
    @GetMapping(value = "/secreteKey/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseValue getSecreteKeyByApplicationName(@PathVariable("applicationName") final String applicationName) {

        LOGGER.info("DocumentAppIdentifierController:getSecreteKeyByApplicationName() is started with applicationName: "
                + applicationName);

        final String secretKey = documentAppIdentifierService.getSecreteKeyByApplicationName(applicationName);
        return RequestResponseUtil.convertToRestResponseValue(secretKey);
    }

    /**
     * return get list of Authorized Groups for given Application name.
     * 
     * @param applicationId.
     * @return
     */
    //@ApiOperation(value = "get List of autherized group for an application", response = RestResponseObject.class)
    @GetMapping(value = "/authorizedGroups/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject getListOfAuthorizedGroups(@PathVariable("applicationName") final String applicationName) {

        LOGGER.info("DocumentAppIdentifierController:getListOfAuthorizedGroups() is started with applicationName is "
                + applicationName);

        final List<ApplicationAutherizationGroupV1> listOfAuthorizedGroups =
                documentAppIdentifierService.getListOfAuthorizedGroups(applicationName);
        return RequestResponseUtil.convertToRestResponseObject(listOfAuthorizedGroups);
    }

    /**
     * return applicationIdentifier for a given Application name.
     * 
     * @param applicationId.
     * @return
     */
    //@ApiOperation(value = "get applicationIdentifier for an application", response = RestResponseObject.class)
    @GetMapping(value = "/details/{applicationName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject getApplicationIdentifierByApplicationName(@PathVariable("applicationName") final String applicationName) {

        LOGGER.info("DocumentAppIdentifierController:getApplicationIdentifierByApplicationName() is started with applicationName: "
                + applicationName);

        final ApplicationIdentifierV1 applicationIdentifier =
                documentAppIdentifierService.findByApplicationName(applicationName);
        return RequestResponseUtil.convertToRestResponseObject(applicationIdentifier);
    }

}
