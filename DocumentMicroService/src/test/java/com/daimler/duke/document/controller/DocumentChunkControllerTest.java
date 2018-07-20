package com.daimler.duke.document.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.daimler.duke.document.db.entity.DbTokenStatus;
import com.daimler.duke.document.db.entity.DbUploadFileChunkSize;
import com.daimler.duke.document.dto.DocumentChunkTransferredStatus;
import com.daimler.duke.document.dto.DocumentChunkV1;
import com.daimler.duke.document.dto.RestResponseObject;
import com.daimler.duke.document.dto.RestResponseValue;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.service.DocumentUploadChunkService;
import com.daimler.duke.document.service.TokenStatusService;

/**
 * Document Chunk Controller Test
 * 
 * @author kshebin
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentChunkController.class
})
public class DocumentChunkControllerTest {

    private DocumentChunkController    documentChunkController;
    private DocumentUploadChunkService documentChunkService;
    private TokenStatusService         tokenStatusService;

    @Before
    public void init() throws Exception {
        documentChunkController = new DocumentChunkController();
        tokenStatusService = PowerMockito.mock(TokenStatusService.class);
        documentChunkService = PowerMockito.mock(DocumentUploadChunkService.class);

        Whitebox.setInternalState(documentChunkController, "documentChunkService", documentChunkService);
        Whitebox.setInternalState(documentChunkController, "tokenStatusService", tokenStatusService);
    }

    @Test
    public void testGetTransferredChunkStatus() {

        DocumentChunkTransferredStatus documentChunkTransferredStatus = this.getDummyStatus();

        PowerMockito.when(documentChunkService.getChunkIdList(Mockito.any(String.class)))
                    .thenReturn(documentChunkTransferredStatus);
        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                    .thenReturn(dbTokenStatus);

        RestResponseObject response =
                documentChunkController.getTransferredChunkStatus("tokenfor5a223f3e5953c7136439beb5",
                                                                  "5a223f3e5953c7136439beb5");

        assertNotNull(response);
        assertNotNull(response.getResult());
        assertEquals(response.getStatusMessage(), "successfull");
        assertEquals(response.getStatusCode(), 200);
        assertEquals( ((Long) ((DocumentChunkTransferredStatus) response.getResult()).getChunkIds().get(0)).longValue(),
                      5L);
        assertEquals( ((Long) ((DocumentChunkTransferredStatus) response.getResult()).getChunkIds().get(1)).longValue(),
                      7L);
        assertEquals( ((Long) ((DocumentChunkTransferredStatus) response.getResult()).getChunkIds().get(2)).longValue(),
                      6L);
        assertEquals( ((Long) ((DocumentChunkTransferredStatus) response.getResult()).getChunkIds().get(3)).longValue(),
                      9L);
        response.getStatusMessage();
    }

    @Test
    public void testSaveChunkWithInvalidNullToken() {
        DocumentChunkV1 documentChunkV1 = getDummyDocumentChunkV1();
        PowerMockito.when(documentChunkService.saveChunk(Mockito.any(DocumentChunkV1.class), Mockito.any(String.class)))
                    .thenReturn(true);

        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class))).thenReturn(null);

        try {
            RestResponseValue response =
                    documentChunkController.saveChunck("tokenfor5a223f3e5953c7136439beb5", documentChunkV1);
            assertTrue(false);
        }
        catch (Exception e) {
            assertTrue(true);

        }
    }

    @Test
    public void testSaveChunkWithInvalidToken() {
        DocumentChunkV1 documentChunkV1 = getDummyDocumentChunkV1();
        PowerMockito.when(documentChunkService.saveChunk(Mockito.any(DocumentChunkV1.class), Mockito.any(String.class)))
                    .thenReturn(true);

        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                    .thenThrow(new ValidationException());

        try {
            RestResponseValue response =
                    documentChunkController.saveChunck("tokenfor5a223f3e5953c7136439beb5", documentChunkV1);
            assertTrue(false);
        }
        catch (Exception e) {
            assertTrue(true);

        }
    }

    @Test
    public void testSaveChunk() {
        DocumentChunkV1 documentChunkV1 = getDummyDocumentChunkV1();
        PowerMockito.when(documentChunkService.saveChunk(Mockito.any(DocumentChunkV1.class), Mockito.any(String.class)))
                    .thenReturn(true);

        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                    .thenReturn(dbTokenStatus);

        RestResponseValue response =
                documentChunkController.saveChunck("tokenfor5a223f3e5953c7136439beb5", documentChunkV1);
        assertNotNull(response);
        assertNotNull(response.getValue());
        assertEquals(response.getStatusMessage(), "successfull");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(true, response.getValue());

    }

    @Test
    public void testMergeChunk() {

        PowerMockito.when(documentChunkService.mergeChunks(Mockito.any(String.class))).thenReturn(true);
        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                    .thenReturn(dbTokenStatus);
        RestResponseObject response =
                documentChunkController.mergeChunks("tokenfor5a223f3e5953c7136439beb5", "5a223f3e5953c7136439beb5");
        assertNotNull(response);
        assertNotNull(response.getResult());
        assertEquals(response.getStatusMessage(), "successfull");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(true, response.getResult());

    }

    @Test
    public void testCancelFileUpload() {

        PowerMockito.when(documentChunkService.cancelFileUpload(Mockito.any(String.class))).thenReturn(true);

        DbTokenStatus dbTokenStatus = new DbTokenStatus();
        dbTokenStatus.setDocumentId("5a223f3e5953c7136439beb5");
        PowerMockito.when(tokenStatusService.findTokenStatusByToken(Mockito.any(String.class)))
                    .thenReturn(dbTokenStatus);

        RestResponseObject response = documentChunkController.cancelFileUpload("tokenfor5a223f3e5953c7136439beb5",
                                                                               "5a223f3e5953c7136439beb5");
        assertNotNull(response);
        assertNotNull(response.getResult());
        assertEquals(response.getStatusMessage(), "successfull");
        assertEquals(response.getStatusCode(), 200);
        assertEquals(true, response.getResult());

    }

    private DocumentChunkV1 getDummyDocumentChunkV1() {
        DocumentChunkV1 documentChunkV1 = new DocumentChunkV1();

        documentChunkV1.setChunkId("01");

        documentChunkV1.setContent("String");
        documentChunkV1.setDocumentMetaDataId("5a223f3e5953c7136439beb5");
        documentChunkV1.setTotalNumOfChunks("08");
        return documentChunkV1;

    }

    private DocumentChunkTransferredStatus getDummyStatus() {

        DocumentChunkTransferredStatus documentChunkTransferredStatus = new DocumentChunkTransferredStatus();
        documentChunkTransferredStatus.setDocumentMetaDataId("5a223f3e5953c7136439beb5");
        documentChunkTransferredStatus.addChunkId(5L);
        documentChunkTransferredStatus.addChunkId(7L);
        documentChunkTransferredStatus.addChunkId(6L);
        documentChunkTransferredStatus.addChunkId(9L);
        return documentChunkTransferredStatus;

    }

    private DbUploadFileChunkSize createDocumentMataData(String chunkSize) {
        DbUploadFileChunkSize document = new DbUploadFileChunkSize();
        document.setUploadCunkSize(chunkSize);
        return document;
    }
}
