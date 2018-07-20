package com.daimler.duke.document.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.CreateUploadChunkSizeResponseV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.service.DocumentUploadChunkService;
import com.daimler.duke.document.util.RequestResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Health Check", description = "Health Check Operation")
@RequestMapping("/")
public class ApplicationRunStatusController {

    @Autowired
    private DbDocumentMetaDataRepository dbDocumentMetaDataRepository;

    @Inject
    private DocumentUploadChunkService   documentService;

    private static final Logger          LOGGER = LoggerFactory.getLogger(ApplicationRunStatusController.class);
    
    @ApiOperation(value = "Application Up/Down", response = RestResponseObject.class)
    @GetMapping(value = "/healthbeat", produces = MediaType.APPLICATION_JSON_VALUE)
    public RestResponseObject checkDatabaseRunningStatus() {

        LOGGER.info("ApplicationRunningStatusController:checkDatabaseRunningStatus() is started");
        dbDocumentMetaDataRepository.findOne("2334455");
        return RequestResponseUtil.convertToRestResponseObject("Document service is running fine");
    }

    @ApiOperation(value = "Get Chunk Size", response = CreateUploadChunkSizeResponseV1.class)
    @RequestMapping(value = "/chunkSize", method = RequestMethod.GET)
    public CreateUploadChunkSizeResponseV1 getUploadChunkSize() {
        DbUploadFileChunkSize uploadChunk = documentService.getUploadChunkSize();
            return RequestResponseUtil.convertToCreateUploadCunkSizeResponseV1(uploadChunk.getUploadCunkSize());
    }

}
