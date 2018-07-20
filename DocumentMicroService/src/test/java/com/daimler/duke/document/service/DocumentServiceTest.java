package com.daimler.duke.document.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.daimler.duke.document.db.entity.DbApplicationIdentifier;
import com.daimler.duke.document.db.entity.DbDocumentMetaData;
import com.daimler.duke.document.dto.DocumentContentV1;
import com.daimler.duke.document.dto.DocumentMetaDataV1;
import com.daimler.duke.document.dto.DocumentMetaDataWithSerialNoV1;
import com.daimler.duke.document.dto.DocumentV1;
import com.daimler.duke.document.exception.AuthorizationException;
import com.daimler.duke.document.exception.CommonErrorCodes;
import com.daimler.duke.document.exception.DocumentException;
import com.daimler.duke.document.exception.MongoConnectException;
import com.daimler.duke.document.exception.ValidationException;
import com.daimler.duke.document.repository.DbApplicationIdentifierRepository;
import com.daimler.duke.document.repository.DbDocumentMetaDataRepository;
import com.daimler.duke.document.util.MockObjectTest;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
                  DocumentService.class, GridFSDBFile.class, GridFSFile.class
})
public class DocumentServiceTest {

    private DbDocumentMetaDataRepository      dbDocumentMetaDataRepository;

    private DbApplicationIdentifierRepository dbApplicationIdentifierRepository;

    private GridFsTemplate                    gridFsTemplate;

    private GridFSFile                        gridFSFile;

    private DocumentService                   documentService;

    @Before
    public void init() throws Exception {

        dbDocumentMetaDataRepository = PowerMockito.mock(DbDocumentMetaDataRepository.class);
        dbApplicationIdentifierRepository = PowerMockito.mock(DbApplicationIdentifierRepository.class);
        gridFsTemplate = PowerMockito.mock(GridFsTemplate.class);
        gridFSFile = PowerMockito.mock(GridFSFile.class);

        documentService = new DocumentService();
        Whitebox.setInternalState(documentService, "dbDocumentMetaDataRepository", dbDocumentMetaDataRepository);
        Whitebox.setInternalState(documentService, "gridFsTemplate", gridFsTemplate);
        Whitebox.setInternalState(documentService,
                                  "dbApplicationIdentifierRepository",
                                  dbApplicationIdentifierRepository);

        DbApplicationIdentifier dbApplicationIdentifier = MockObjectTest.createDbApplicationIdentifier();
        PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                    .thenReturn(dbApplicationIdentifier);

    }

    @Test
    public void testDuplicateDocument() {

    }

    @Test
    public void testGetDocumentMetaData() {
        String documentId = "5a223f3e5953c7136439beb5";
        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        DocumentMetaDataV1 documentMetaDataV1 = documentService.getDocumentMetaData("Duke", "5a223f3e5953c7136439beb5");

        // Assertions here.
        Assert.assertNotNull(documentMetaDataV1);
        assertThat("ApplicationId", documentMetaDataV1.getDocumentId(), equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testGetDocumentMetaDataIsNotExist() {

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(null);
            // method under test
            documentService.getDocumentMetaData("Duke", "5a223f3e5953c7136439beb5");
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testGetDocumentMetaDataErrorPath() {

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentService.getDocumentMetaData("Duke", "5a223f3e5953c7136439beb5");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testApplicationIdIsNotInMetadata() {

        try {
            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData("78678979", "TerstName", "pdf", 1l);
            dbDocumentMetaData.setApplicationId(null);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData);
            // method under test
            documentService.getDocumentMetaData("Duke", "5a223f3e5953c7136439beb5");
            Assert.fail();
        }
        catch (AuthorizationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo("You don't have access to this document"));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(104));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("You don't have access to this document"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    ////
    @Test
    public void testCreateDocumentMetaData() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentMetaDataV1 documentMetaData = MockObjectTest.createDocumentMataData(null, "TerstName", "pdf", 1l);
        documentMetaData.setDocumentId(null);

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        String savedDocumentId = documentService.createDocumentMetaData("Duke", documentMetaData);

        // Assertions here.
        Assert.assertNotNull(savedDocumentId);
        assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testCreateDocumentMetaDataErrorPath() {

        DocumentMetaDataV1 documentMetaData = MockObjectTest.createDocumentMataData(null, "TerstName", "pdf", 1l);

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentService.createDocumentMetaData("Duke", documentMetaData);
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testCreateDocumentMetaDataWithoutAppIdentifier() {

        PowerMockito.when(dbApplicationIdentifierRepository.findByApplicationName(Mockito.any(String.class)))
                    .thenReturn(null);
        DocumentMetaDataV1 documentMetaData = MockObjectTest.createDocumentMataData(null, "TerstName", "pdf", 1l);

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentService.createDocumentMetaData("Duke", documentMetaData);
            Assert.fail();
        }
        catch (AuthorizationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo("Duke Application is not allowed"));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(105));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Duke Application is not allowed"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    ////

    @Test
    public void testCreateDocument() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentMetaData().setDocumentId(null);

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                    .thenReturn(dbDocumentMetaData);

        PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                    .thenReturn(gridFSFile);

        PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        String savedDocumentId = documentService.createDocument("Duke", document);

        // Assertions here.
        Assert.assertNotNull(savedDocumentId);
        assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testUpdateDocument() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentMetaData().setDocumentId(documentId);

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.save(Mockito.any(DbDocumentMetaData.class)))
                    .thenReturn(dbDocumentMetaData);

        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                    .thenReturn(gridFSFile);

        PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

        // method under test
        String savedDocumentId = documentService.updateDocument("Duke", document);

        // Assertions here.
        Assert.assertNotNull(savedDocumentId);
        assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testUpdateDocumentMetaData() {

        DocumentMetaDataV1 documentMetaData =
                MockObjectTest.createDocumentMataData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        PowerMockito.when(dbDocumentMetaDataRepository.exists(Mockito.any(String.class))).thenReturn(true);

        PowerMockito.when(dbDocumentMetaDataRepository.save(Mockito.any(DbDocumentMetaData.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        String savedDocumentId = documentService.updateDocumentMetaData("Duke", documentMetaData);

        // Assertions here.
        Assert.assertNotNull(savedDocumentId);
        assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testUpdateDocumentMetaDataIfIdNotExist() {

        try {
            DocumentMetaDataV1 documentMetaData =
                    MockObjectTest.createDocumentMataData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);

            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(null);

            // method under test
            String savedDocumentId = documentService.updateDocumentMetaData("Duke", documentMetaData);

            // Assertions here.
            Assert.assertNotNull(savedDocumentId);
            assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

    @Test
    public void testUpdateDocumentMetaDataIfException() {

        try {
            DocumentMetaDataV1 documentMetaData =
                    MockObjectTest.createDocumentMataData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);

            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            // method under test
            String savedDocumentId = documentService.updateDocumentMetaData("Duke", documentMetaData);

            // Assertions here.
            Assert.assertNotNull(savedDocumentId);
            assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    /////

    @Test
    public void testDeleteDocument() throws Exception {

        // PowerMockito.doNothing().when(dbApplicationIdentifierRepository, "delete", Mockito.any(String.class));

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);
        GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

        PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
        PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

        PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

        // method under test
        documentService.deleteDocument("Duke", "65656565656565");
    }

    @Test
    public void testDeleteDocumentErrorPath() {
        try {

            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData);

            GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

            PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
            PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

            PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

            Mockito.doThrow(new RuntimeException("Can not find object"))
                   .doNothing()
                   .when(dbDocumentMetaDataRepository)
                   .delete(Mockito.any(String.class));

            // method under test
            documentService.deleteDocument("Duke", "65656565656565");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Can not find object"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testGetDocumentContent() throws Exception {

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

        PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
        PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

        PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        DocumentContentV1 documentContent = documentService.getDocumentContent("Duke", "65656565656565");
        Assert.assertNotNull(documentContent);
        Assert.assertNotNull(documentContent.getData());
    }

    @Test
    public void testGetDocumentContentExceptioWriteToGridFSDBFile() throws Exception {
        try {
            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData);

            GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

            PowerMockito.when(gridFSFile.writeTo(Mockito.any(ByteArrayOutputStream.class)))
                        .thenThrow(new IOException("Something went wrong"));

            PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

            // method under test
            documentService.getDocumentContent("Duke", "65656565656565");

        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(102));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.ERROR_IN_WRITING_INTO_BYTE_STREAM.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("Something went wrong"));

        }
    }

    @Test
    public void testGetDocumentContentIfNull() throws Exception {

        try {
            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData);

            PowerMockito.when(gridFsTemplate.find(Mockito.any(Query.class))).thenReturn(null);

            // method under test
            documentService.getDocumentContent("Duke", "65656565656565");
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(101));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.DOCUMENT_CONTENT_DOES_NOT_EXIST.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testGetDocumentContentIfException() throws Exception {

        try {
            PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentService.getDocumentContent("Duke", "65656565656565");
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testGetDocument() {
        String documentId = "5a223f3e5953c7136439beb5";
        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

        PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
        PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

        PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

        // method under test
        DocumentV1 document = documentService.getDocument("Duke", "5a223f3e5953c7136439beb5");

        // Assertions here.
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.getDocumentMetaData());
        Assert.assertNotNull(document.getDocumentContent());
        assertThat("DocumentId", document.getDocumentMetaData().getDocumentId(), equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testGetDocumenIsNotExist() {

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(null);
            // method under test
            documentService.getDocument("Duke", "5a223f3e5953c7136439beb5");
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    //
    @Test
    public void testGetListDocumentMetaData() {
        String documentId = "5a223f3e5953c7136439beb5";
        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        List<String> documentIds = new ArrayList();
        documentIds.add(documentId);
        // method under test
        List<DocumentMetaDataV1> documentMetaDataList = documentService.getListDocumentMetaData("Duke", documentIds);

        // Assertions here.
        Assert.assertNotNull(documentMetaDataList);
        assertThat("ApplicationId", documentMetaDataList.get(0).getDocumentId(), equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testGetListDocumentMetaDataaIsNotExist() {

        try {
            String documentId = "5a223f3e5953c7136439beb5";
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class))).thenReturn(null);

            List<String> documentIds = new ArrayList();
            documentIds.add(documentId);
            // method under test
            documentService.getListDocumentMetaData("Duke", documentIds);
            Assert.fail();
        }
        catch (DocumentException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(100));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo(CommonErrorCodes.COMMON001.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testGetListDocumentMetaDataIfException() {

        try {
            String documentId = "5a223f3e5953c7136439beb5";
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            List<String> documentIds = new ArrayList();
            documentIds.add(documentId);
            // method under test
            documentService.getListDocumentMetaData("Duke", documentIds);
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    //
    @Test
    public void testCreateMultipleDocumentContent() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
        documentContent.setDocumentMtDtdId(documentId);

        PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                    .thenReturn(gridFSFile);

        PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

        DbDocumentMetaData dbDocumentMetaData = new DbDocumentMetaData();
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        List<DocumentContentV1> documentContentList = new ArrayList();
        documentContentList.add(documentContent);

        // method under test
        List<String> savedDocumentIds = documentService.createMultipleDocumentContent("Duke", documentContentList);

        // Assertions here.
        Assert.assertNotNull(savedDocumentIds);
        assertThat("DocumentId", savedDocumentIds.get(0), equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testCreateMultipleDocumentContentIfException() {
        try {
            String documentId = "5a223f3e5953c7136439beb5";
            DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
            documentContent.setDocumentMtDtdId(documentId);

            PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

            PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(null);

            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData)
                        .thenReturn(null);

            List<DocumentContentV1> documentContentList = new ArrayList();
            documentContentList.add(documentContent);

            // method under test
            List<String> savedDocumentIds = documentService.createMultipleDocumentContent("Duke", documentContentList);

            // Assertions here.
            Assert.assertNotNull(savedDocumentIds);
            assertThat("DocumentId", savedDocumentIds.get(0), equalTo("5a223f3e5953c7136439beb5"));
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }

    }

    @Test
    public void testCreateMultipleDocumentContentIfNullInput() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
        // document.getDocumentMetaData().setDocumentId(null);

        PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                    .thenReturn(gridFSFile);

        PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

        List<DocumentContentV1> documentContentList = new ArrayList();

        // method under test
        List<String> savedDocumentIds = documentService.createMultipleDocumentContent("Duke", documentContentList);

        // Assertions here.
        Assert.assertNull(savedDocumentIds);

    }

    @Test
    public void testDeleteAllDocuments() throws Exception {

        // PowerMockito.doNothing().when(dbApplicationIdentifierRepository, "delete", Mockito.any(String.class));
        String documentId = "5a223f3e5953c7136439beb5";
        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

        PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
        PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

        PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

        List<String> ids = new ArrayList<String>();
        ids.add(documentId);
        // method under test
        documentService.deleteAllDocuments("Duke", ids);
    }

    @Test
    public void testDeleteAllDocumentsIdIdNull() throws Exception {

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        documentService.deleteAllDocuments("Duke", null);
    }

    @Test
    public void testDeleteAllDocumentErrorPath() {
        try {

            String documentId = "5a223f3e5953c7136439beb5";
            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData("5a223f3e5953c7136439beb5", "TerstName", "pdf", 1l);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData);

            GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

            PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
            PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

            PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

            Mockito.doThrow(new RuntimeException("Can not find object"))
                   .doNothing()
                   .when(dbDocumentMetaDataRepository)
                   .delete(Mockito.any(String.class));

            List<String> ids = new ArrayList<String>();
            ids.add(documentId);
            // method under test
            documentService.deleteAllDocuments("Duke", ids);
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    //
    @Test
    public void testCreateDocumentContent() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
        documentContent.setDocumentMtDtdId(documentId);

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                    .thenReturn(gridFSFile);

        PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

        // method under test
        String savedDocumentId = documentService.createDocumentContent(documentContent);

        // Assertions here.
        Assert.assertNotNull(savedDocumentId);
        assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testCreateDocumentContentIfException() {
        try {
            String documentId = "5a223f3e5953c7136439beb5";
            DocumentContentV1 documentContent = MockObjectTest.createDocumentContent();
            documentContent.setDocumentMtDtdId(documentId);

            DbDocumentMetaData dbDocumentMetaData =
                    MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
            PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                        .thenReturn(dbDocumentMetaData);

            PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));

            PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

            List<DocumentContentV1> documentContentList = new ArrayList();
            documentContentList.add(documentContent);

            // method under test
            String savedDocumentId = documentService.createDocumentContent(documentContentList.get(0));

            // Assertions here.
            Assert.assertNotNull(savedDocumentId);
            assertThat("DocumentId", savedDocumentId, equalTo("5a223f3e5953c7136439beb5"));
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }

    }

    ///

    @Test
    public void testCreateMultipleDocumentMetaData() {
        String documentId = "5a223f3e5953c7136439beb5";
        List<DocumentMetaDataWithSerialNoV1> documentMetaDataList = new ArrayList<>();
        documentMetaDataList.add(MockObjectTest.createDocumentMetaDataWithSerialNo(null, "TerstName", "pdf", 1l));

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                    .thenReturn(dbDocumentMetaData);

        // method under test
        Map<Integer,String> documentIdsMap =
                documentService.createMultipleDocumentMetaData("Duke", documentMetaDataList);

        // Assertions here.
        Assert.assertNotNull(documentIdsMap);
        assertThat("DocumentId", documentIdsMap.get(0), equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testCreateMultipleDocumentMetaDataErrorPath() {

        List<DocumentMetaDataWithSerialNoV1> documentMetaDataList = new ArrayList<>();
        documentMetaDataList.add(MockObjectTest.createDocumentMetaDataWithSerialNo(null, "TerstName", "pdf", 1l));

        try {
            PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                        .thenThrow(new RuntimeException("Something went wrong"));
            // method under test
            documentService.createMultipleDocumentMetaData("Duke", documentMetaDataList);
            Assert.fail();
        }
        catch (MongoConnectException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(800));
            assertThat(ex.getErrorContainer().getErrorDetails(), equalTo("Something went wrong"));
            assertThat(ex.getErrorContainer().getMessage(), equalTo(MongoConnectException.MONOGO_CONNECT_ERROR_MSG));

        }
    }

    @Test
    public void testValidateSerlNoMultipleDocumentMetaData() {

        List<DocumentMetaDataWithSerialNoV1> documentMetaDataList = new ArrayList<>();
        documentMetaDataList.add(new DocumentMetaDataWithSerialNoV1());

        try {
            // method under test
            documentService.createMultipleDocumentMetaData("Duke", documentMetaDataList);
            Assert.fail();
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.SERIAL_NO_CAN_NOT_NULL.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.SERIAL_NO_CAN_NOT_NULL.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testValidateDuplicateNoMultipleDocumentMetaData() {

        List<DocumentMetaDataWithSerialNoV1> documentMetaDataList = new ArrayList<>();
        documentMetaDataList.add(MockObjectTest.createDocumentMetaDataWithSerialNo(null, "TerstName", "pdf", 1l));
        documentMetaDataList.add(MockObjectTest.createDocumentMetaDataWithSerialNo(null, "TerstName", "pdf", 1l));

        try {
            // method under test
            documentService.createMultipleDocumentMetaData("Duke", documentMetaDataList);
            Assert.fail();
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.SERIAL_NO_CAN_NOT_DUPLICATE.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.SERIAL_NO_CAN_NOT_DUPLICATE.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testMultipleDocumentMetaDataEmptyList() {

        List<DocumentMetaDataWithSerialNoV1> documentMetaDataList = new ArrayList<>();

        try {
            // method under test
            documentService.createMultipleDocumentMetaData("Duke", documentMetaDataList);
            Assert.fail();
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.DOCUMENTS_CAN_NOT_BE_NULL.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.DOCUMENTS_CAN_NOT_BE_NULL.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }
    }

    @Test
    public void testFetchMultipleDocuments() {
        String documentId = "5a223f3e5953c7136439beb5";
        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        GridFSDBFile gridFSFile = PowerMockito.mock(GridFSDBFile.class);

        PowerMockito.when(gridFSFile.getId()).thenReturn("5a223f3e5953c7136439beb5");
        PowerMockito.when(gridFSFile.getFilename()).thenReturn("765");

        PowerMockito.when(gridFsTemplate.findOne(Mockito.any(Query.class))).thenReturn(gridFSFile);

        List<String> documentIds = new ArrayList<>();
        documentIds.add(documentId);
        documentIds.add(documentId);

        // method under test
        List<DocumentV1> document = documentService.fetchMultipleDocuments("Duke", documentIds);

        // Assertions here.
        Assert.assertNotNull(document);
        Assert.assertNotNull(document.get(0).getDocumentMetaData());
        Assert.assertNotNull(document.get(0).getDocumentContent());
        assertThat("DocumentId",
                   document.get(0).getDocumentMetaData().getDocumentId(),
                   equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testFetchMultipleDocumentsIfEmptyIds() {
        try {
            List<String> documentIds = new ArrayList<>();

            // method under test
            documentService.fetchMultipleDocuments("Duke", documentIds);
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.DOCUMENT_IDS_CAN_NOT_BE_NULL.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.DOCUMENT_IDS_CAN_NOT_BE_NULL.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

    @Test
    public void testCreateMultipleDocuments() {
        String documentId = "5a223f3e5953c7136439beb5";
        DocumentV1 document = MockObjectTest.createDocument();
        document.getDocumentMetaData().setDocumentId(null);

        DbDocumentMetaData dbDocumentMetaData =
                MockObjectTest.createDbDocumentMetaData(documentId, "TerstName", "pdf", 1l);
        PowerMockito.when(dbDocumentMetaDataRepository.insert(Mockito.any(DbDocumentMetaData.class)))
                    .thenReturn(dbDocumentMetaData);

        PowerMockito.when(gridFsTemplate.store(Mockito.any(InputStream.class), Mockito.any(String.class)))
                    .thenReturn(gridFSFile);

        PowerMockito.when(gridFSFile.getId()).thenReturn(documentId);

        PowerMockito.when(dbDocumentMetaDataRepository.findOne(Mockito.any(String.class)))
                    .thenReturn(dbDocumentMetaData);

        List<DocumentV1> documents = new ArrayList<>();
        documents.add(document);

        // method under test
        Map<Integer,String> savedDocumentId = documentService.createMultipleDocuments("Duke", documents);

        // Assertions here.
        Assert.assertNotNull(savedDocumentId);
        assertThat("DocumentId", savedDocumentId.get(0), equalTo("5a223f3e5953c7136439beb5"));

    }

    @Test
    public void testCreateMultipleDocumentsIfEmptyIds() {
        try {
            // method under test
            documentService.createMultipleDocuments("Duke", new ArrayList<>());
        }
        catch (ValidationException ex) {
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getMessage(), equalTo(CommonErrorCodes.DOCUMENTS_CAN_NOT_BE_NULL.getDescDe()));
            Assert.assertNotNull(ex.getErrorContainer());
            assertThat(ex.getErrorContainer().getErrorCode(), equalTo(400));
            assertThat(ex.getErrorContainer().getErrorDetails(),
                       equalTo(CommonErrorCodes.DOCUMENTS_CAN_NOT_BE_NULL.getDescDe()));
            assertThat(ex.getErrorContainer().getMessage(), equalTo("failed"));

        }

    }

}
